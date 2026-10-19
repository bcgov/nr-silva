# Backend Sonar Compliance

Consult the project's live [Java profile](https://sonarcloud.io/api/qualityprofiles/search?organization=bcgov-sonarcloud&project=nr-silva-backend&language=java) and [assigned quality gate](https://sonarcloud.io/api/qualitygates/get_by_project?organization=bcgov-sonarcloud&project=nr-silva-backend) before Sonar compliance work. This checklist is selective; the live settings define the complete rules and thresholds.

## Java code

- **S1192:** Extract repeated literals flagged by Sonar into meaningful constants.
- **S3358:** Replace nested ternaries with clear conditionals.
- **S112 / S1130:** Declare specific checked exceptions that callers must handle; remove unnecessary declarations.
- **S2095:** Close owned resources with try-with-resources.
- **S125:** Remove commented-out code.
- **S3252:** Access inherited static members through their declaring class.
- **S8688:** Specify a `ZoneId` or `Clock` for time-based `.now()` calls.
- **S2259 / S3655:** Handle nullable values before dereferencing; check presence or use safe alternatives before accessing `Optional` contents.
- **S2755:** Configure XML parsers to prevent XXE attacks.

## Tests

- **S2699:** Verify observable outcomes with meaningful assertions.
- **S5853:** Chain consecutive AssertJ assertions on the same subject.
- **S5976:** Use parameterized tests for similar cases without weakening case-specific assertions.
- **S5778:** Prepare fixtures, serialize data, and calculate arguments before the exception assertion. Keep only the operation expected to throw inside its lambda.
- **S8714:** Use `assertThrows` or `assertThatThrownBy` instead of manual try/catch with `fail`. Preserve assertions about exception types, causes, messages, and statuses.
- **S1130:** Remove checked exceptions from test method declarations when the test no longer throws them.
- Preserve existing regression scenarios and assertion strength when resolving Sonar findings.

```java
byte[] bytes = json.getBytes(StandardCharsets.UTF_8);

assertThatThrownBy(() -> service.processSpatialFile("test.geojson", bytes))
    .isInstanceOf(ResponseStatusException.class);
```

## Compliance checks

- Apply these checks to moved or renamed code as well as newly written code.
- Resolve findings without adding suppressions, widening analysis exclusions, lowering quality thresholds, or weakening tests.
- Local checks do not establish Sonar compliance; report live findings and Quality Gate status separately when analysis is available.

See the [Sonar remediation recipes](../../sonar-audit/resources/remediation-patterns.md) for detailed examples and the [sonar-audit skill](../../sonar-audit/SKILL.md) for live analysis.
