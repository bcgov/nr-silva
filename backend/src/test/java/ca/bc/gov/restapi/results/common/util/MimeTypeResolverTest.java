package ca.bc.gov.restapi.results.common.util;

import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Unit Test | MimeTypeResolver")
class MimeTypeResolverTest {

    @ParameterizedTest(name = "Resolve \"{0}\" should return \"{1}\"")
    @MethodSource("knownMimeTypes")
    @DisplayName("Resolve known MIME types")
    void shouldResolveKnownMimeTypes(String code, String expectedMimeType) {
        String actual = MimeTypeResolver.resolve(code);
        Assertions.assertEquals(expectedMimeType, actual);
    }

    @ParameterizedTest(name = "Resolve unknown or null code \"{0}\" should fallback to octet-stream")
    @MethodSource("unknownMimeTypes")
    @DisplayName("Resolve unknown MIME types")
    void shouldReturnOctetStreamForUnknown(String code) {
        String actual = MimeTypeResolver.resolve(code);
        Assertions.assertEquals("application/octet-stream", actual);
    }

    private static Stream<Arguments> knownMimeTypes() {
        return Stream.of(
                Arguments.of("PDF", "application/pdf"),
                Arguments.of("pdf", "application/pdf"),
                Arguments.of("JPG", "image/jpeg"),
                Arguments.of("xls", "application/vnd.ms-excel"),
                Arguments.of("TXT", "text/plain"),
                Arguments.of("Gif", "image/gif"),
                Arguments.of("Zip", "application/zip")
        );
    }

    private static Stream<Arguments> unknownMimeTypes() {
        return Stream.of(
                Arguments.of((String) null),
                Arguments.of(""),
                Arguments.of("foobar"),
                Arguments.of("exe")
        );
    }
}