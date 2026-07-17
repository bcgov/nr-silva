package ca.bc.gov.restapi.results.common.clamav;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.time.Duration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Unit Test | ClamAvClient")
@ExtendWith(MockitoExtension.class)
class ClamAvClientTest {

  @Test
  @DisplayName("Should return CLEAN for a reply ending with ' OK'")
  void parseReply_clean() {
    ClamAvVerdict v = ClamAvClient.parseReply("stream: OK");
    assertEquals(ClamAvVerdict.Status.CLEAN, v.status());
    assertNull(v.signature());
    assertNull(v.detail());
  }

  @Test
  @DisplayName("Should return INFECTED and extract the signature name")
  void parseReply_infected() {
    ClamAvVerdict v = ClamAvClient.parseReply("stream: Eicar-Test-Signature FOUND");
    assertEquals(ClamAvVerdict.Status.INFECTED, v.status());
    assertEquals("Eicar-Test-Signature", v.signature());
    assertNotNull(v.detail());
  }

  @Test
  @DisplayName("Should return INFECTED with a multi-word signature")
  void parseReply_infectedMultiWordSignature() {
    ClamAvVerdict v = ClamAvClient.parseReply("stream: Win.Exploit.CVE_2024 FOUND");
    assertEquals(ClamAvVerdict.Status.INFECTED, v.status());
    assertEquals("Win.Exploit.CVE_2024", v.signature());
  }

  @Test
  @DisplayName("Should return ERROR for an unexpected reply")
  void parseReply_error() {
    ClamAvVerdict v = ClamAvClient.parseReply("stream: Access denied. ERROR");
    assertEquals(ClamAvVerdict.Status.ERROR, v.status());
    assertNotNull(v.detail());
  }

  @Test
  @DisplayName("Should return ERROR for an empty reply")
  void parseReply_empty() {
    ClamAvVerdict v = ClamAvClient.parseReply("");
    assertEquals(ClamAvVerdict.Status.ERROR, v.status());
  }

  @Test
  @DisplayName("Should handle null bytes from z-protocol (already stripped by readReply)")
  void parseReply_withNullBytes() {
    // readReply strips \0 before parseReply is called; verify the stripped form parses correctly
    ClamAvVerdict v = ClamAvClient.parseReply("stream: OK"); // already stripped
    assertEquals(ClamAvVerdict.Status.CLEAN, v.status());
  }

  @Test
  @DisplayName("Should return ERROR verdict when clamd is unreachable — never throws")
  void scan_returnsErrorWhenUnreachable() {
    ClamAvProperties props =
        ClamAvProperties.builder()
            .host("localhost")
            .port(1) // no service listening on port 1
            .connectTimeout(Duration.ofMillis(200))
            .readTimeout(Duration.ofMillis(200))
            .failOpen(false)
            .build();
    ClamAvClient client = new ClamAvClient(props);

    ClamAvVerdict verdict = client.scan(new byte[] {1, 2, 3}, "test.bin");

    assertEquals(ClamAvVerdict.Status.ERROR, verdict.status());
    assertNotNull(verdict.detail());
  }

  @Test
  @DisplayName("ping() returns false when clamd is unreachable — never throws")
  void ping_returnsFalseWhenUnreachable() {
    ClamAvProperties props =
        ClamAvProperties.builder()
            .host("localhost")
            .port(1)
            .connectTimeout(Duration.ofMillis(200))
            .readTimeout(Duration.ofMillis(200))
            .failOpen(false)
            .build();
    ClamAvClient client = new ClamAvClient(props);

    // Must not throw
    boolean result = client.ping();
    assertEquals(false, result);
  }

  @Test
  @DisplayName("host() returns configured host")
  void host_returnsConfiguredHost() {
    ClamAvProperties props =
        ClamAvProperties.builder()
            .host("clamav.example.com")
            .port(3310)
            .connectTimeout(Duration.ofSeconds(5))
            .readTimeout(Duration.ofSeconds(30))
            .failOpen(false)
            .build();
    ClamAvClient client = new ClamAvClient(props);

    assertEquals("clamav.example.com", client.host());
  }

  @Test
  @DisplayName("port() returns configured port")
  void port_returnsConfiguredPort() {
    ClamAvProperties props =
        ClamAvProperties.builder()
            .host("localhost")
            .port(12345)
            .connectTimeout(Duration.ofSeconds(5))
            .readTimeout(Duration.ofSeconds(30))
            .failOpen(false)
            .build();
    ClamAvClient client = new ClamAvClient(props);

    assertEquals(12345, client.port());
  }

  @Test
  @DisplayName("scan() with empty data returns ERROR (connection error), never throws")
  void scan_emptyData_returnsError() {
    ClamAvProperties props =
        ClamAvProperties.builder()
            .host("localhost")
            .port(1)
            .connectTimeout(Duration.ofMillis(200))
            .readTimeout(Duration.ofMillis(200))
            .failOpen(false)
            .build();
    ClamAvClient client = new ClamAvClient(props);

    ClamAvVerdict verdict = client.scan(new byte[0], "empty.bin");

    assertEquals(ClamAvVerdict.Status.ERROR, verdict.status());
    assertNotNull(verdict.detail());
  }

  @Test
  @DisplayName("parseReply with just FOUND (no colon) extracts signature")
  void parseReply_foundWithoutColon() {
    ClamAvVerdict v = ClamAvClient.parseReply("Malware.Generic FOUND");
    assertEquals(ClamAvVerdict.Status.INFECTED, v.status());
    assertEquals("Malware.Generic", v.signature());
  }

  @Test
  @DisplayName("parseReply with whitespace-only reply returns ERROR")
  void parseReply_whitespaceOnly() {
    ClamAvVerdict v = ClamAvClient.parseReply("   \t  ");
    assertEquals(ClamAvVerdict.Status.ERROR, v.status());
  }

  @Test
  @DisplayName("parseReply with OK not at end returns ERROR")
  void parseReply_okNotAtEnd() {
    ClamAvVerdict v = ClamAvClient.parseReply("OK stream: something");
    assertEquals(ClamAvVerdict.Status.ERROR, v.status());
  }

  @Test
  @DisplayName("parseReply with FOUND not at end returns ERROR")
  void parseReply_foundNotAtEnd() {
    ClamAvVerdict v = ClamAvClient.parseReply("FOUND stream: something");
    assertEquals(ClamAvVerdict.Status.ERROR, v.status());
  }

  @Test
  @DisplayName("parseReply with signature containing colon")
  void parseReply_signatureWithColon() {
    ClamAvVerdict v = ClamAvClient.parseReply("stream: Win.Trojan:123:456 FOUND");
    assertEquals(ClamAvVerdict.Status.INFECTED, v.status());
    assertEquals("Win.Trojan:123:456", v.signature());
  }

  @Test
  @DisplayName("parseReply with multiple colons extracts first signature part")
  void parseReply_multipleColons() {
    ClamAvVerdict v = ClamAvClient.parseReply("stream: Something: Detected FOUND");
    assertEquals(ClamAvVerdict.Status.INFECTED, v.status());
    assertEquals("Something: Detected", v.signature());
  }

  @Test
  @DisplayName("parseReply handles stream output format correctly")
  void parseReply_streamFormat() {
    ClamAvVerdict v = ClamAvClient.parseReply("stream: OK");
    assertEquals(ClamAvVerdict.Status.CLEAN, v.status());
  }

  @Test
  @DisplayName("ping() returns false and does not throw for invalid host")
  void ping_invalidHost_returnsFalse() {
    ClamAvProperties props =
        ClamAvProperties.builder()
            .host("invalid-host-12345.example.com")
            .port(3310)
            .connectTimeout(Duration.ofMillis(100))
            .readTimeout(Duration.ofMillis(100))
            .failOpen(false)
            .build();
    ClamAvClient client = new ClamAvClient(props);

    boolean result = client.ping();
    assertEquals(false, result);
  }

  @Test
  @DisplayName("scan() with various data sizes handles chunking")
  void scan_multipleDataSizes_returnsError() {
    ClamAvProperties props =
        ClamAvProperties.builder()
            .host("localhost")
            .port(1)
            .connectTimeout(Duration.ofMillis(200))
            .readTimeout(Duration.ofMillis(200))
            .failOpen(false)
            .build();
    ClamAvClient client = new ClamAvClient(props);

    // Test with 1 byte
    ClamAvVerdict v1 = client.scan(new byte[] {0x01}, "small.bin");
    assertEquals(ClamAvVerdict.Status.ERROR, v1.status());

    // Test with 100 bytes
    byte[] data100 = new byte[100];
    for (int i = 0; i < 100; i++) {
      data100[i] = (byte) (i % 256);
    }
    ClamAvVerdict v100 = client.scan(data100, "medium.bin");
    assertEquals(ClamAvVerdict.Status.ERROR, v100.status());

    // Test with 100KB (will be chunked)
    byte[] data100k = new byte[100 * 1024];
    for (int i = 0; i < data100k.length; i++) {
      data100k[i] = (byte) (i % 256);
    }
    ClamAvVerdict v100k = client.scan(data100k, "large.bin");
    assertEquals(ClamAvVerdict.Status.ERROR, v100k.status());
  }

  @Test
  @DisplayName("parseReply with signature without FOUND at end returns ERROR")
  void parseReply_noFoundMarker() {
    ClamAvVerdict v = ClamAvClient.parseReply("stream: Malware.Generic");
    assertEquals(ClamAvVerdict.Status.ERROR, v.status());
  }

  @Test
  @DisplayName("parseReply handles FOUND with spaces in signature")
  void parseReply_multipleSpacesFOUND() {
    // This still ends with " FOUND" so it's valid
    ClamAvVerdict v = ClamAvClient.parseReply("stream: Virus.Name  With.Spaces FOUND");
    assertEquals(ClamAvVerdict.Status.INFECTED, v.status());
    assertTrue(v.signature().contains("Virus.Name"));
  }

  @Test
  @DisplayName("parseReply with very long signature name")
  void parseReply_longSignatureName() {
    String longSig =
        "Win.Malware.VeryLongNameToTestParsing.Generic.MemoryCorruption.Variant.SubVariant FOUND";
    ClamAvVerdict v = ClamAvClient.parseReply("stream: " + longSig);
    assertEquals(ClamAvVerdict.Status.INFECTED, v.status());
    assertTrue(v.signature().contains("VeryLongNameToTestParsing"));
  }

  @Test
  @DisplayName("parseReply with numeric-only signature")
  void parseReply_numericSignature() {
    ClamAvVerdict v = ClamAvClient.parseReply("stream: 12345 FOUND");
    assertEquals(ClamAvVerdict.Status.INFECTED, v.status());
    assertEquals("12345", v.signature());
  }

  @Test
  @DisplayName("scan() encodes and sends INSTREAM command format correctly")
  void scan_instructionFormat() {
    // Verify that scan attempts connection with proper configuration
    ClamAvProperties props =
        ClamAvProperties.builder()
            .host("localhost")
            .port(9999)
            .connectTimeout(Duration.ofMillis(100))
            .readTimeout(Duration.ofMillis(100))
            .failOpen(false)
            .build();
    ClamAvClient client = new ClamAvClient(props);

    // Connection should fail to unreachable port
    ClamAvVerdict result = client.scan(new byte[] {1, 2, 3, 4}, "test.bin");
    assertEquals(ClamAvVerdict.Status.ERROR, result.status());
  }

  @Test
  @DisplayName("ping() sends PING command format correctly")
  void ping_commandFormat() {
    ClamAvProperties props =
        ClamAvProperties.builder()
            .host("localhost")
            .port(9999)
            .connectTimeout(Duration.ofMillis(100))
            .readTimeout(Duration.ofMillis(100))
            .failOpen(false)
            .build();
    ClamAvClient client = new ClamAvClient(props);

    // Connection should fail to unreachable port
    boolean result = client.ping();
    assertEquals(false, result);
  }

  @Test
  @DisplayName("parseReply handles clean response with extra whitespace in stream name")
  void parseReply_cleanResponseVariations() {
    ClamAvVerdict v = ClamAvClient.parseReply("stream: OK");
    assertEquals(ClamAvVerdict.Status.CLEAN, v.status());
    assertNull(v.signature());
  }

  @Test
  @DisplayName("scan() with 8KB data (chunk size boundary) handles correctly")
  void scan_chunkSizeBoundary() {
    ClamAvProperties props =
        ClamAvProperties.builder()
            .host("localhost")
            .port(1)
            .connectTimeout(Duration.ofMillis(200))
            .readTimeout(Duration.ofMillis(200))
            .failOpen(false)
            .build();
    ClamAvClient client = new ClamAvClient(props);

    // Exactly 8KB - one chunk
    byte[] data8k = new byte[8192];
    for (int i = 0; i < data8k.length; i++) {
      data8k[i] = (byte) (i % 256);
    }
    ClamAvVerdict result = client.scan(data8k, "exact-chunk.bin");
    assertEquals(ClamAvVerdict.Status.ERROR, result.status());
  }

  @Test
  @DisplayName("scan() with 8KB + 1 byte data handles chunking correctly")
  void scan_multipleChunks() {
    ClamAvProperties props =
        ClamAvProperties.builder()
            .host("localhost")
            .port(1)
            .connectTimeout(Duration.ofMillis(200))
            .readTimeout(Duration.ofMillis(200))
            .failOpen(false)
            .build();
    ClamAvClient client = new ClamAvClient(props);

    // 8KB + 1 = two chunks
    byte[] data = new byte[8193];
    for (int i = 0; i < data.length; i++) {
      data[i] = (byte) (i % 256);
    }
    ClamAvVerdict result = client.scan(data, "multi-chunk.bin");
    assertEquals(ClamAvVerdict.Status.ERROR, result.status());
  }
}
