package ca.bc.gov.restapi.results.common.configuration;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.flywaydb.core.api.ResourceProvider;
import org.flywaydb.core.api.resource.LoadableResource;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * A Flyway {@link ResourceProvider} that uses Spring's {@code ApplicationContext.getResources()}
 * instead of Flyway's {@code ClassPathScanner}.
 *
 * <p>In GraalVM native image, Flyway's default {@code ClassPathScanner} fails with
 * "unsupported protocol: resource" when trying to enumerate migration directories. Spring Boot
 * 3.x's AOT processor, combined with {@link FlywayRuntimeHints}, pre-computes a resource index
 * at build time. At runtime, {@code ApplicationContext.getResources(pattern)} uses this index
 * instead of scanning the classpath, making it work correctly in native image.
 */
@Slf4j
@RequiredArgsConstructor
public class NativeImageResourceProvider implements ResourceProvider {

  private final ApplicationContext context;
  private final List<String> locations;

  /**
   * Looks up a single migration resource by name across all configured locations.
   *
   * @param name the filename to look up (e.g. "V1__init.sql")
   * @return a {@link LoadableResource} if found, or {@code null}
   */
  @Override
  public LoadableResource getResource(String name) {
    for (String location : locations) {
      try {
        Resource resource = context.getResource(location + "/" + name);
        if (resource.exists()) {
          return wrap(resource);
        }
      } catch (Exception e) {
        log.debug("Error accessing resource '{}' in location '{}'; treating as not found", name, location, e);
      }
    }
    return null;
  }

  /**
   * Returns all migration resources matching the given prefix and suffixes across all configured
   * locations. Flyway calls this with e.g. prefix="V", suffixes=[".sql"] to discover versioned
   * migrations.
   *
   * @param prefix   migration file prefix (e.g. "V", "R", "U")
   * @param suffixes migration file suffixes (e.g. [".sql"])
   * @return all matching resources
   */
  @Override
  public Collection<LoadableResource> getResources(String prefix, String[] suffixes) {
    List<LoadableResource> result = new ArrayList<>();
    for (String location : locations) {
      for (String suffix : suffixes) {
        String pattern = location + "/" + prefix + "*" + suffix;
        try {
          Resource[] resources = context.getResources(pattern);
          for (Resource resource : resources) {
            if (resource.exists()) {
              result.add(wrap(resource));
            }
          }
        } catch (IOException e) {
          log.debug("Could not scan '{}' for prefix='{}' suffix='{}'", location, prefix, suffix);
        }
      }
    }
    return result;
  }

  private static LoadableResource wrap(final Resource resource) {
    return new LoadableResource() {

      @Override
      public String getAbsolutePath() {
        try {
          return resource.getURL().toString();
        } catch (IOException e) {
          return resource.getDescription();
        }
      }

      @Override
      public String getAbsolutePathOnDisk() {
        try {
          URI uri = resource.getURI();
          return uri.toString();
        } catch (IOException e) {
          return getAbsolutePath();
        }
      }

      @Override
      public String getFilename() {
        return resource.getFilename();
      }

      /**
       * Returns the path relative to the classpath root (e.g. "db/migration/V1__init.sql").
       * Flyway uses this for version parsing and duplicate detection.
       * Handles the "resource:/db/migration/V1__init.sql" URL format from GraalVM native.
       */
      @Override
      public String getRelativePath() {
        try {
          String url = resource.getURL().toString();
          int idx = url.indexOf("db/migration");
          if (idx >= 0) {
            return url.substring(idx);
          }
        } catch (IOException e) {
          // fallback to filename
        }
        return resource.getFilename();
      }

      @Override
      public Reader read() {
        try {
          return new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
          throw new RuntimeException(
              "Cannot read migration resource: " + resource.getDescription(), e);
        }
      }
    };
  }
}
