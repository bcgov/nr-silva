package ca.bc.gov.restapi.results.postgres.configuration;

import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * This class grabs the configuration from the environment with allowed users to trigger the job.
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties("ca.bc.gov.nrs.results")
public class DashboardUserManagerConfig {

  private String[] dashboardJobUsers;

  /**
   * Gets the users in a list format.
   *
   * @return The user list or an empty list.
   */
  public List<String> getUserList() {
    if (Objects.isNull(dashboardJobUsers)) {
      return List.of();
    }
    return List.of(dashboardJobUsers);
  }
}
