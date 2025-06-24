package ca.bc.gov.restapi.results.security;

import ca.bc.gov.restapi.results.common.security.IdentityProvider;
import ca.bc.gov.restapi.results.common.security.UserInfo;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Unit Test | User Information")
class UserInfoTest {

  @Test
  @DisplayName("Create user info")
  void createUserInfo() {
    UserInfo userInfo =
        new UserInfo(
            "123456789@idir",
            "Bilbo",
            "Baggings",
            "bilbo.baggings@gov.bc.ca",
            "Baggings, Bilbo LWRS:EX",
            "BAGGINGS",
            null,
            IdentityProvider.IDIR,
            Set.of(),
            "abcdef123456789");

    Assertions.assertNotNull(userInfo);
    Assertions.assertEquals("Bilbo", userInfo.firstName());
    Assertions.assertEquals("Baggings", userInfo.lastName());
    Assertions.assertEquals("bilbo.baggings@gov.bc.ca", userInfo.email());
    Assertions.assertEquals("Baggings, Bilbo LWRS:EX", userInfo.displayName());
    Assertions.assertEquals("BAGGINGS", userInfo.idirUsername());
    Assertions.assertNull(userInfo.businessName());
    Assertions.assertEquals(IdentityProvider.IDIR, userInfo.identityProvider());
    Assertions.assertTrue(userInfo.roles().isEmpty());
  }

  @Test
  @DisplayName("Create invalid null user")
  void createInvalidNullUser() {
    // Id not null
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          new UserInfo(
              null,
              "Bilbo",
              "Baggings",
              "bilbo.baggings@gov.bc.ca",
              "Baggings, Bilbo LWRS:EX",
              "BAGGINGS",
              null,
              IdentityProvider.IDIR,
              Set.of(),
              "abcdef123456789");
        });

    // E-mail not null
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          new UserInfo(
              "123456789@idir",
              "Bilbo",
              "Baggings",
              null,
              "Baggings, Bilbo LWRS:EX",
              "BAGGINGS",
              null,
              IdentityProvider.IDIR,
              Set.of(),
              "abcdef123456789");
        });

    // Display name not null
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          new UserInfo(
              "123456789@idir",
              "Bilbo",
              "Baggings",
              "bilbo.baggings@gov.bc.ca",
              null,
              "BAGGINGS",
              null,
              IdentityProvider.IDIR,
              Set.of(),
              "abcdef123456789");
        });

    // Identity provider not null
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          new UserInfo(
              "123456789@idir",
              "Bilbo",
              "Baggings",
              "bilbo.baggings@gov.bc.ca",
              "Baggings, Bilbo LWRS:EX",
              "BAGGINGS",
              null,
              null,
              Set.of(),
              "abcdef123456789");
        });

    // Roles not null
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          new UserInfo(
              "123456789@idir",
              "Bilbo",
              "Baggings",
              "bilbo.baggings@gov.bc.ca",
              "Baggings, Bilbo LWRS:EX",
              "BAGGINGS",
              null,
              IdentityProvider.IDIR,
              null,
              "abcdef123456789");
        });

    // Token not null
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          new UserInfo(
              "123456789@idir",
              "Bilbo",
              "Baggings",
              "bilbo.baggings@gov.bc.ca",
              "Baggings, Bilbo LWRS:EX",
              "BAGGINGS",
              null,
              IdentityProvider.IDIR,
              Set.of(),
              null);
        });
  }
}
