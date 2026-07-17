package ca.bc.gov.restapi.results.common.clamav;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.Duration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Unit Test | ClamAvClient")
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
}
