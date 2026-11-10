package ca.bc.gov.restapi.results.common.provider.mock;

import ca.bc.gov.restapi.results.common.dto.ExternalCutBlockDetails;
import ca.bc.gov.restapi.results.common.dto.ExternalTenureSummary;
import ca.bc.gov.restapi.results.common.provider.TenureProvider;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

/**
 * Offline mock adapter implementing {@link TenureProvider} for local development, unit tests, and
 * simulated contract testing.
 *
 * <p>Preloaded with baseline fixtures matching the PostgreSQL dev seed data ({@code
 * V999.3.0__seed_data.sql}) covering 50 cut blocks across tenures {@code TFL01} through {@code
 * TFL05} and standard ministry test clients ({@code 00012797}, {@code 00032699}, {@code 00045503},
 * {@code 00146886}, {@code 00149237}), alongside synthetic test fixtures ({@code TFL001}–{@code
 * TFL003}).
 *
 * <p>Unregistered tenures strictly return {@link ExternalCutBlockDetails#notFound()} for cut block
 * verification and {@link Optional#empty()} for tenure summaries.
 */
@Component("mockTenureAdapter")
public class MockTenureAdapter implements TenureProvider {

  private record SeedCutBlock(
      Long cbSkey,
      String forestFileId,
      String cuttingPermitId,
      String cutBlockId,
      String timberMark,
      String status,
      String licenseeClientNumber) {}

  private static final List<SeedCutBlock> SEED_CUT_BLOCKS =
      List.of(
          // --- Sourced directly from V999.3.0__seed_data.sql ---
          new SeedCutBlock(10000000L, "TFL01", null, "01", "47/12K", "HRS", "00012797"),
          new SeedCutBlock(10000001L, "TFL01", null, "02", "47/6AK", "AP", "00032699"),
          new SeedCutBlock(10000002L, "TFL01", null, "03", "47/12U", "DD", "00045503"),
          new SeedCutBlock(10000003L, "TFL01", "104", "04", "44/270", "EE", "00146886"),
          new SeedCutBlock(10000004L, "TFL01", "105", "05", "K1AEEE", "HB", "00149237"),
          new SeedCutBlock(10000005L, "TFL01", "106", "06", "EZ7210", "HD", "00012797"),
          new SeedCutBlock(10000006L, "TFL01", "107", "07", "77623", "HS", "00032699"),
          new SeedCutBlock(10000007L, "TFL01", "108", "08", "A1687", "HX", "00045503"),
          new SeedCutBlock(10000008L, "TFL01", "109", "09", "AY5001", "ID", "00146886"),
          new SeedCutBlock(10000009L, "TFL01", "110", "10", "AU6001", "LC", "00149237"),
          new SeedCutBlock(10000010L, "TFL02", null, "11", "FA9145", "PD", "00012797"),
          new SeedCutBlock(10000011L, "TFL02", null, "12", "39/27Y", "PE", "00032699"),
          new SeedCutBlock(10000012L, "TFL02", null, "13", "ER4055", "PL", "00045503"),
          new SeedCutBlock(10000013L, "TFL02", "114", "14", "47/12K", "PP", "00146886"),
          new SeedCutBlock(10000014L, "TFL02", "115", "15", "47/6AK", "S", "00149237"),
          new SeedCutBlock(10000015L, "TFL02", "116", "16", "47/12U", "SP", "00012797"),
          new SeedCutBlock(10000016L, "TFL02", "117", "17", "44/270", "HRS", "00032699"),
          new SeedCutBlock(10000017L, "TFL02", "118", "18", "K1AEEE", "AP", "00045503"),
          new SeedCutBlock(10000018L, "TFL02", "119", "19", "EZ7210", "DD", "00146886"),
          new SeedCutBlock(10000019L, "TFL02", "120", "20", "77623", "EE", "00149237"),
          new SeedCutBlock(10000020L, "TFL03", null, "21", "A1687", "HB", "00012797"),
          new SeedCutBlock(10000021L, "TFL03", null, "22", "AY5001", "HD", "00032699"),
          new SeedCutBlock(10000022L, "TFL03", null, "23", "AU6001", "HS", "00045503"),
          new SeedCutBlock(10000023L, "TFL03", "124", "24", "FA9145", "HX", "00146886"),
          new SeedCutBlock(10000024L, "TFL03", "125", "25", "39/27Y", "ID", "00149237"),
          new SeedCutBlock(10000025L, "TFL03", "126", "26", "ER4055", "LC", "00012797"),
          new SeedCutBlock(10000026L, "TFL03", "127", "27", "47/12K", "PD", "00032699"),
          new SeedCutBlock(10000027L, "TFL03", "128", "28", "47/6AK", "PE", "00045503"),
          new SeedCutBlock(10000028L, "TFL03", "129", "29", "47/12U", "PL", "00146886"),
          new SeedCutBlock(10000029L, "TFL03", "130", "30", "44/270", "PP", "00149237"),
          new SeedCutBlock(10000030L, "TFL04", null, "31", "K1AEEE", "S", "00012797"),
          new SeedCutBlock(10000031L, "TFL04", null, "32", "EZ7210", "SP", "00032699"),
          new SeedCutBlock(10000032L, "TFL04", null, "33", "77623", "HRS", "00045503"),
          new SeedCutBlock(10000033L, "TFL04", "134", "34", "A1687", "AP", "00146886"),
          new SeedCutBlock(10000034L, "TFL04", "135", "35", "AY5001", "DD", "00149237"),
          new SeedCutBlock(10000035L, "TFL04", "136", "36", "AU6001", "EE", "00012797"),
          new SeedCutBlock(10000036L, "TFL04", "137", "37", "FA9145", "HB", "00032699"),
          new SeedCutBlock(10000037L, "TFL04", "138", "38", "39/27Y", "HD", "00045503"),
          new SeedCutBlock(10000038L, "TFL04", "139", "39", "ER4055", "HS", "00146886"),
          new SeedCutBlock(10000039L, "TFL04", "140", "40", "47/12K", "HX", "00149237"),
          new SeedCutBlock(10000040L, "TFL05", null, "41", "47/6AK", "ID", "00012797"),
          new SeedCutBlock(10000041L, "TFL05", null, "42", "47/12U", "LC", "00032699"),
          new SeedCutBlock(10000042L, "TFL05", null, "43", "44/270", "PD", "00045503"),
          new SeedCutBlock(10000043L, "TFL05", "144", "44", "K1AEEE", "PE", "00146886"),
          new SeedCutBlock(10000044L, "TFL05", "145", "45", "EZ7210", "PL", "00149237"),
          new SeedCutBlock(10000045L, "TFL05", "146", "46", "77623", "PP", "00012797"),
          new SeedCutBlock(10000046L, "TFL05", "147", "47", "A1687", "S", "00032699"),
          new SeedCutBlock(10000047L, "TFL05", "148", "48", "AY5001", "SP", "00045503"),
          new SeedCutBlock(10000048L, "TFL05", "149", "49", "AU6001", "HRS", "00146886"),
          new SeedCutBlock(10000049L, "TFL05", "150", "50", "FA9145", "AP", "00149237"));

  private final Map<String, SeedCutBlock> seedDataMap = new ConcurrentHashMap<>();
  private final Map<String, ExternalCutBlockDetails> fixtures = new ConcurrentHashMap<>();
  private final Map<String, ExternalTenureSummary> summaryFixtures = new ConcurrentHashMap<>();

  /** Initializes MockTenureAdapter with standard baseline fixtures. */
  public MockTenureAdapter() {
    loadDefaultFixtures();
  }

  private void loadDefaultFixtures() {
    // 1. Seed data from V999.3.0__seed_data.sql
    for (SeedCutBlock seed : SEED_CUT_BLOCKS) {
      seedDataMap.put(
          buildKey(seed.forestFileId(), seed.cuttingPermitId(), seed.cutBlockId()), seed);
    }

    // 2. Synthetic baseline fixtures used by unit and contract integration tests
    registerFixture(
        "TFL001",
        "CP1",
        "CB001",
        ExternalCutBlockDetails.of(1001L, true, "47/12K", "ACT"));
    registerSummaryFixture(
        "TFL001",
        "CP1",
        "CB001",
        new ExternalTenureSummary("TFL001", "CP1", "CB001", "47/12K", "00010003", "ACT", 1001L));

    registerFixture(
        "TFL002",
        null,
        "CB002",
        ExternalCutBlockDetails.of(1002L, true, "47/12M", "ACT"));
    registerSummaryFixture(
        "TFL002",
        null,
        "CB002",
        new ExternalTenureSummary("TFL002", null, "CB002", "47/12M", "00010003", "ACT", 1002L));

    // Valid cut block, but client not a licensee
    registerFixture(
        "TFL003",
        "CP03",
        "CB003",
        ExternalCutBlockDetails.of(1003L, false, "47/12N", "ACT"));
    registerSummaryFixture(
        "TFL003",
        "CP03",
        "CB003",
        new ExternalTenureSummary("TFL003", "CP03", "CB003", "47/12N", "99999999", "ACT", 1003L));

    // Explicit non-existent cut block fixture
    registerFixture("TFL999", null, "CB999", ExternalCutBlockDetails.notFound());
    registerSummaryNotFound("TFL999", null, "CB999");
  }

  /**
   * Builds a normalized fixture lookup key from tenure components.
   *
   * @param forestFileId tenure file ID
   * @param cuttingPermitId cutting permit identifier (may be null)
   * @param cutBlockId cut block identifier
   * @return normalized fixture key
   */
  public static String buildKey(
      String forestFileId, String cuttingPermitId, String cutBlockId) {
    return normalize(forestFileId) + ":" + normalize(cuttingPermitId) + ":" + normalize(cutBlockId);
  }

  /**
   * Builds a normalized fixture lookup key including client number.
   *
   * @param forestFileId tenure file ID
   * @param cuttingPermitId cutting permit identifier (may be null)
   * @param cutBlockId cut block identifier
   * @param clientNumber client number
   * @return normalized client-specific fixture key
   */
  public static String buildKey(
      String forestFileId, String cuttingPermitId, String cutBlockId, String clientNumber) {
    return buildKey(forestFileId, cuttingPermitId, cutBlockId) + ":" + normalize(clientNumber);
  }

  private static String normalize(String value) {
    return value != null ? value.trim().toUpperCase(Locale.ROOT) : "";
  }

  private static boolean matchesClient(String candidate, String target) {
    if (candidate == null || target == null) {
      return false;
    }
    String c = candidate.trim();
    String t = target.trim();
    if (c.equalsIgnoreCase(t)) {
      return true;
    }
    String strippedC = c.replaceFirst("^0+", "");
    String strippedT = t.replaceFirst("^0+", "");
    return !strippedC.isEmpty() && strippedC.equalsIgnoreCase(strippedT);
  }

  /**
   * Registers a cut block verification fixture by tenure identifiers.
   *
   * @param forestFileId tenure file ID
   * @param cuttingPermitId cutting permit identifier (may be null)
   * @param cutBlockId cut block identifier
   * @param details verified details to return
   */
  public void registerFixture(
      String forestFileId,
      String cuttingPermitId,
      String cutBlockId,
      ExternalCutBlockDetails details) {
    fixtures.put(buildKey(forestFileId, cuttingPermitId, cutBlockId), details);
  }

  /**
   * Registers a cut block verification fixture for a specific client number.
   *
   * @param forestFileId tenure file ID
   * @param cuttingPermitId cutting permit identifier (may be null)
   * @param cutBlockId cut block identifier
   * @param clientNumber client number
   * @param details verified details to return
   */
  public void registerFixture(
      String forestFileId,
      String cuttingPermitId,
      String cutBlockId,
      String clientNumber,
      ExternalCutBlockDetails details) {
    fixtures.put(buildKey(forestFileId, cuttingPermitId, cutBlockId, clientNumber), details);
  }

  /**
   * Registers a cut block verification fixture with an explicit raw key.
   *
   * @param key raw fixture key
   * @param details verified details to return
   */
  public void registerFixture(String key, ExternalCutBlockDetails details) {
    fixtures.put(key, details);
  }

  /**
   * Registers a not-found cut block verification fixture.
   *
   * @param forestFileId tenure file ID
   * @param cuttingPermitId cutting permit identifier (may be null)
   * @param cutBlockId cut block identifier
   */
  public void registerNotFound(
      String forestFileId, String cuttingPermitId, String cutBlockId) {
    registerFixture(forestFileId, cuttingPermitId, cutBlockId, ExternalCutBlockDetails.notFound());
  }

  /**
   * Registers a tenure summary fixture by tenure identifiers.
   *
   * @param forestFileId tenure file ID
   * @param cuttingPermitId cutting permit identifier (may be null)
   * @param cutBlockId cut block identifier
   * @param summary summary details to return
   */
  public void registerSummaryFixture(
      String forestFileId,
      String cuttingPermitId,
      String cutBlockId,
      ExternalTenureSummary summary) {
    summaryFixtures.put(buildKey(forestFileId, cuttingPermitId, cutBlockId), summary);
  }

  /**
   * Registers a tenure summary fixture with an explicit raw key.
   *
   * @param key raw fixture key
   * @param summary summary details to return
   */
  public void registerSummaryFixture(String key, ExternalTenureSummary summary) {
    summaryFixtures.put(key, summary);
  }

  /**
   * Registers that no summary should be found for a given tenure.
   *
   * @param forestFileId tenure file ID
   * @param cuttingPermitId cutting permit identifier (may be null)
   * @param cutBlockId cut block identifier
   */
  public void registerSummaryNotFound(
      String forestFileId, String cuttingPermitId, String cutBlockId) {
    summaryFixtures.remove(buildKey(forestFileId, cuttingPermitId, cutBlockId));
    summaryFixtures.put(
        buildKey(forestFileId, cuttingPermitId, cutBlockId) + ":NOT_FOUND",
        new ExternalTenureSummary(null, null, null, null, null, null, null));
  }

  /** Clears all registered fixtures and resets to empty state. */
  public void clearFixtures() {
    fixtures.clear();
    summaryFixtures.clear();
    seedDataMap.clear();
  }

  /** Resets all fixtures back to standard baseline. */
  public void reset() {
    clearFixtures();
    loadDefaultFixtures();
  }

  @Override
  public ExternalCutBlockDetails verifyCutBlock(
      String forestFileId,
      String cuttingPermitId,
      String cutBlockId,
      String clientNumber) {

    // 1. Try client-specific custom fixture first
    String clientKey = buildKey(forestFileId, cuttingPermitId, cutBlockId, clientNumber);
    ExternalCutBlockDetails clientMatch = fixtures.get(clientKey);
    if (clientMatch != null) {
      return clientMatch;
    }

    // 2. Try general tenure custom fixture
    String generalKey = buildKey(forestFileId, cuttingPermitId, cutBlockId);
    ExternalCutBlockDetails generalMatch = fixtures.get(generalKey);
    if (generalMatch != null) {
      return generalMatch;
    }

    // 3. Try baseline seed data from V999.3.0__seed_data.sql
    SeedCutBlock seed = seedDataMap.get(generalKey);
    if (seed != null) {
      boolean isLicensee = matchesClient(clientNumber, seed.licenseeClientNumber());
      return ExternalCutBlockDetails.of(
          seed.cbSkey(), isLicensee, seed.timberMark(), seed.status());
    }

    // 4. If not found in fixtures or seed data, return not found
    return ExternalCutBlockDetails.notFound();
  }

  @Override
  public Optional<ExternalTenureSummary> getTenureSummary(
      String forestFileId, String cuttingPermitId, String cutBlockId) {

    String key = buildKey(forestFileId, cuttingPermitId, cutBlockId);

    // Check if explicitly marked as not found
    if (summaryFixtures.containsKey(key + ":NOT_FOUND")) {
      return Optional.empty();
    }

    ExternalCutBlockDetails detailsMatch = fixtures.get(key);
    if (detailsMatch != null && !detailsMatch.exists()) {
      return Optional.empty();
    }

    ExternalTenureSummary registered = summaryFixtures.get(key);
    if (registered != null) {
      return Optional.of(registered);
    }

    // Check baseline seed data from V999.3.0__seed_data.sql
    SeedCutBlock seed = seedDataMap.get(key);
    if (seed != null) {
      return Optional.of(
          new ExternalTenureSummary(
              seed.forestFileId(),
              seed.cuttingPermitId(),
              seed.cutBlockId(),
              seed.timberMark(),
              seed.licenseeClientNumber(),
              seed.status(),
              seed.cbSkey()));
    }

    return Optional.empty();
  }
}
