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

  private static final String NOT_FOUND_SUFFIX = ":NOT_FOUND";
  private static final String STATUS_ACT = "ACT";
  private static final String CLIENT_00010003 = "00010003";

  private record SeedCutBlock(
      Long cbSkey,
      String forestFileId,
      String cuttingPermitId,
      String cutBlockId,
      String timberMark,
      String status,
      String licenseeClientNumber) {}

  private static final String SEED_DATA_CSV =
      """
      10000000,TFL01,,01,47/12K,HRS,00012797
      10000001,TFL01,,02,47/6AK,AP,00032699
      10000002,TFL01,,03,47/12U,DD,00045503
      10000003,TFL01,104,04,44/270,EE,00146886
      10000004,TFL01,105,05,K1AEEE,HB,00149237
      10000005,TFL01,106,06,EZ7210,HD,00012797
      10000006,TFL01,107,07,77623,HS,00032699
      10000007,TFL01,108,08,A1687,HX,00045503
      10000008,TFL01,109,09,AY5001,ID,00146886
      10000009,TFL01,110,10,AU6001,LC,00149237
      10000010,TFL02,,11,FA9145,PD,00012797
      10000011,TFL02,,12,39/27Y,PE,00032699
      10000012,TFL02,,13,ER4055,PL,00045503
      10000013,TFL02,114,14,47/12K,PP,00146886
      10000014,TFL02,115,15,47/6AK,S,00149237
      10000015,TFL02,116,16,47/12U,SP,00012797
      10000016,TFL02,117,17,44/270,HRS,00032699
      10000017,TFL02,118,18,K1AEEE,AP,00045503
      10000018,TFL02,119,19,EZ7210,DD,00146886
      10000019,TFL02,120,20,77623,EE,00149237
      10000020,TFL03,,21,A1687,HB,00012797
      10000021,TFL03,,22,AY5001,HD,00032699
      10000022,TFL03,,23,AU6001,HS,00045503
      10000023,TFL03,124,24,FA9145,HX,00146886
      10000024,TFL03,125,25,39/27Y,ID,00149237
      10000025,TFL03,126,26,ER4055,LC,00012797
      10000026,TFL03,127,27,47/12K,PD,00032699
      10000027,TFL03,128,28,47/6AK,PE,00045503
      10000028,TFL03,129,29,47/12U,PL,00146886
      10000029,TFL03,130,30,44/270,PP,00149237
      10000030,TFL04,,31,K1AEEE,S,00012797
      10000031,TFL04,,32,EZ7210,SP,00032699
      10000032,TFL04,,33,77623,HRS,00045503
      10000033,TFL04,134,34,A1687,AP,00146886
      10000034,TFL04,135,35,AY5001,DD,00149237
      10000035,TFL04,136,36,AU6001,EE,00012797
      10000036,TFL04,137,37,FA9145,HB,00032699
      10000037,TFL04,138,38,39/27Y,HD,00045503
      10000038,TFL04,139,39,ER4055,HS,00146886
      10000039,TFL04,140,40,47/12K,HX,00149237
      10000040,TFL05,,41,47/6AK,ID,00012797
      10000041,TFL05,,42,47/12U,LC,00032699
      10000042,TFL05,,43,44/270,PD,00045503
      10000043,TFL05,144,44,K1AEEE,PE,00146886
      10000044,TFL05,145,45,EZ7210,PL,00149237
      10000045,TFL05,146,46,77623,PP,00012797
      10000046,TFL05,147,47,A1687,S,00032699
      10000047,TFL05,148,48,AY5001,SP,00045503
      10000048,TFL05,149,49,AU6001,HRS,00146886
      10000049,TFL05,150,50,FA9145,AP,00149237
      """;

  private static final List<SeedCutBlock> SYNTHETIC_CUT_BLOCKS =
      List.of(
          new SeedCutBlock(1001L, "TFL001", "CP1", "CB001", "47/12K", STATUS_ACT, CLIENT_00010003),
          new SeedCutBlock(1002L, "TFL002", null, "CB002", "47/12M", STATUS_ACT, CLIENT_00010003),
          new SeedCutBlock(1003L, "TFL003", "CP03", "CB003", "47/12N", STATUS_ACT, "99999999"));

  private final Map<String, SeedCutBlock> seedDataMap = new ConcurrentHashMap<>();
  private final Map<String, ExternalCutBlockDetails> fixtures = new ConcurrentHashMap<>();
  private final Map<String, ExternalTenureSummary> summaryFixtures = new ConcurrentHashMap<>();

  /** Initializes MockTenureAdapter with standard baseline fixtures. */
  public MockTenureAdapter() {
    loadDefaultFixtures();
  }

  private static List<SeedCutBlock> loadSeedCutBlocks() {
    return SEED_DATA_CSV.lines()
        .filter(line -> !line.isBlank())
        .map(line -> line.split(",", -1))
        .map(
            parts ->
                new SeedCutBlock(
                    Long.parseLong(parts[0]),
                    parts[1],
                    parts[2].isEmpty() ? null : parts[2],
                    parts[3],
                    parts[4],
                    parts[5],
                    parts[6]))
        .toList();
  }

  private void loadDefaultFixtures() {
    // 1. Seed data from V999.3.0__seed_data.sql
    for (SeedCutBlock seed : loadSeedCutBlocks()) {
      seedDataMap.put(
          buildKey(seed.forestFileId(), seed.cuttingPermitId(), seed.cutBlockId()), seed);
    }

    // 2. Synthetic baseline fixtures used by unit and contract integration tests
    for (SeedCutBlock synthetic : SYNTHETIC_CUT_BLOCKS) {
      seedDataMap.put(
          buildKey(
              synthetic.forestFileId(),
              synthetic.cuttingPermitId(),
              synthetic.cutBlockId()),
          synthetic);
    }

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
    String key = buildKey(forestFileId, cuttingPermitId, cutBlockId);
    summaryFixtures.remove(key + NOT_FOUND_SUFFIX);
    summaryFixtures.put(key, summary);
  }

  /**
   * Registers a tenure summary fixture with an explicit raw key.
   *
   * @param key raw fixture key
   * @param summary summary details to return
   */
  public void registerSummaryFixture(String key, ExternalTenureSummary summary) {
    summaryFixtures.remove(key + NOT_FOUND_SUFFIX);
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
        buildKey(forestFileId, cuttingPermitId, cutBlockId) + NOT_FOUND_SUFFIX,
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

    // 1. Check if an explicit summary fixture was registered
    ExternalTenureSummary registered = summaryFixtures.get(key);
    if (registered != null) {
      return Optional.of(registered);
    }

    // 2. Check if explicitly marked as not found
    if (summaryFixtures.containsKey(key + NOT_FOUND_SUFFIX)) {
      return Optional.empty();
    }

    ExternalCutBlockDetails detailsMatch = fixtures.get(key);
    if (detailsMatch != null && !detailsMatch.exists()) {
      return Optional.empty();
    }

    // 3. Check baseline seed data from V999.3.0__seed_data.sql or synthetic baselines
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
