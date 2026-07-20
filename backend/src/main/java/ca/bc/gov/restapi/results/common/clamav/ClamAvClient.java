package ca.bc.gov.restapi.results.common.clamav;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Low-level ClamAV client. Communicates with {@code clamd} over raw TCP using the native {@code
 * INSTREAM} protocol (z-prefix, null-terminated). Never throws — any {@link IOException} is folded
 * into a {@link ClamAvVerdict} with {@link ClamAvVerdict.Status#ERROR}.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ClamAvClient {

  private static final int CHUNK_SIZE = 8192;
  private static final byte[] CMD_INSTREAM = "zINSTREAM\0".getBytes(StandardCharsets.UTF_8);
  private static final byte[] CMD_PING = "zPING\0".getBytes(StandardCharsets.UTF_8);

  private final ClamAvProperties properties;

  /**
   * Scan {@code data} via clamd INSTREAM.
   *
   * @param data raw file bytes
   * @param filename used only for logging
   * @return scan verdict — never throws
   */
  public ClamAvVerdict scan(byte[] data, String filename) {
    try (Socket socket = openSocket()) {
      socket.setSoTimeout((int) properties.getReadTimeout().toMillis());

      try (OutputStream out = socket.getOutputStream();
          InputStream in = socket.getInputStream()) {

        out.write(CMD_INSTREAM);

        // Stream data in 8 KB chunks with 4-byte big-endian length prefix
        int offset = 0;
        while (offset < data.length) {
          int length = Math.min(CHUNK_SIZE, data.length - offset);
          writeChunkLength(out, length);
          out.write(data, offset, length);
          offset += length;
        }
        writeChunkLength(out, 0); // zero-length terminator ends the stream
        out.flush();

        return parseReply(readReply(in));
      }
    } catch (IOException e) {
      log.debug("ClamAV socket error scanning '{}': {}", filename, e.getMessage());
      return ClamAvVerdict.error(e.getMessage());
    }
  }

  /**
   * Send a {@code PING} to clamd.
   *
   * @return {@code true} if clamd replied {@code PONG}
   */
  public boolean ping() {
    try (Socket socket = openSocket()) {
      socket.setSoTimeout((int) properties.getReadTimeout().toMillis());
      try (OutputStream out = socket.getOutputStream();
          InputStream in = socket.getInputStream()) {
        out.write(CMD_PING);
        out.flush();
        return readReply(in).contains("PONG");
      }
    } catch (IOException e) {
      log.debug("ClamAV PING failed: {}", e.getMessage());
      return false;
    }
  }

  /** Configured clamd host. */
  public String host() {
    return properties.getHost();
  }

  /** Configured clamd port. */
  public int port() {
    return properties.getPort();
  }

  // --- internal helpers ---

  private Socket openSocket() throws IOException {
    Socket socket = new Socket();
    socket.connect(
        new InetSocketAddress(properties.getHost(), properties.getPort()),
        (int) properties.getConnectTimeout().toMillis());
    return socket;
  }

  private static void writeChunkLength(OutputStream out, int length) throws IOException {
    out.write((length >>> 24) & 0xFF);
    out.write((length >>> 16) & 0xFF);
    out.write((length >>> 8) & 0xFF);
    out.write(length & 0xFF);
  }

  private static String readReply(InputStream in) throws IOException {
    // clamd closes the connection after the reply; readAllBytes() returns when EOF is reached.
    // Strip null bytes from the z-protocol terminator before parsing.
    return new String(in.readAllBytes(), StandardCharsets.UTF_8).replace("\0", "").trim();
  }

  /**
   * Parse a clamd reply string into a {@link ClamAvVerdict}. Package-private for unit testing.
   *
   * <ul>
   *   <li>{@code stream: OK} → {@link ClamAvVerdict.Status#CLEAN}
   *   <li>{@code stream: <sig> FOUND} → {@link ClamAvVerdict.Status#INFECTED}
   *   <li>anything else → {@link ClamAvVerdict.Status#ERROR}
   * </ul>
   */
  static ClamAvVerdict parseReply(String reply) {
    if (reply.endsWith(" OK")) {
      return ClamAvVerdict.clean();
    }
    if (reply.endsWith(" FOUND")) {
      int colonIdx = reply.indexOf(':');
      String body = (colonIdx >= 0 ? reply.substring(colonIdx + 1) : reply).trim();
      String sig = body.substring(0, body.lastIndexOf(" FOUND")).trim();
      return ClamAvVerdict.infected(sig, reply);
    }
    return ClamAvVerdict.error(reply.isEmpty() ? "unexpected empty reply from clamd" : reply);
  }
}
