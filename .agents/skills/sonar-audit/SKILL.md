---
name: sonar-audit
description: >
  Audit, query, and remediate SonarCloud Quality Gate failures, blocker bugs, test code smells,
  and technical debt across nr-silva-backend and nr-silva-frontend without requiring tokens.
  USE FOR: "sonar audit", "check quality gate", "sonar status", "sonar PR check", "sonar blockers",
  "remediate sonar rule", and pre-merge quality gate verification.
---

# SonarCloud Audit & Remediation Skill

This skill guides developers and AI agents through auditing, querying, and remediating SonarCloud Quality Gate failures, blocker bugs, and code smells across `nr-silva-backend` and `nr-silva-frontend`.

It is backed by a zero-dependency, zero-token Node.js CLI (`.agents/skills/sonar-audit/scripts/sonar-api.mjs`) that queries SonarCloud's public REST APIs anonymously without requiring `SONAR_TOKEN` or Docker containers.

---

## 1. Quick Reference & CLI Commands

Run the CLI directly:
```bash
node .agents/skills/sonar-audit/scripts/sonar-api.mjs [options]
```

### Common Commands:

| Task | Command | Description |
| :--- | :--- | :--- |
| **Check Quality Gates on `main`** | `node .agents/skills/sonar-audit/scripts/sonar-api.mjs --status` | Live Pass/Fail status for backend & frontend |
| **Verify Pull Request Quality Gate** | `node .agents/skills/sonar-audit/scripts/sonar-api.mjs --pr <PR_NUMBER>` | Verify whether a PR passed SonarCloud & list introduced issues |
| **Audit Blockers & Criticals** | `node .agents/skills/sonar-audit/scripts/sonar-api.mjs --blockers` | Query all unresolved Blocker & Critical issues with exact file:line |
| **Audit by Rule ID** | `node .agents/skills/sonar-audit/scripts/sonar-api.mjs --rule <RULE_ID>` | Query specific rule (e.g. `S5778`, `java:S2095`, `typescript:S3358`) |
| **Filter by Project** | `node .agents/skills/sonar-audit/scripts/sonar-api.mjs --project frontend --status` | Target `backend` or `frontend` exclusively |
| **Structured JSON for Agents** | `node .agents/skills/sonar-audit/scripts/sonar-api.mjs --status --json` | Emit raw JSON for programmatic parsing |

---

## 2. SonarCloud Projects in Silva

- **Backend Project:** `nr-silva-backend` ([SonarCloud Dashboard](https://sonarcloud.io/summary/new_code?id=nr-silva-backend))
- **Frontend Project:** `nr-silva-frontend` ([SonarCloud Dashboard](https://sonarcloud.io/summary/new_code?id=nr-silva-frontend))
- **CI Workflow:** `.github/workflows/analysis.yml` triggers SonarCloud scans on PRs and merges to `main`.

### Quality Gate Thresholds:
- **Reliability Rating:** $\le 1$ (Grade A) — *Any unresolved bug fails this gate.*
- **Security Rating:** $\le 1$ (Grade A)
- **Maintainability Rating:** $\le 1$ (Grade A)
- **Line Coverage:** $\ge 80.0\%$
- **Duplicated Lines Density:** $\le 3.0\%$
- **Security Hotspots Reviewed:** $100.0\%$

---

## 3. Autonomous Remediation Workflow

When an agent is tasked with fixing SonarCloud issues or preparing a PR for merge, follow this systematic workflow:

```
1. Audit Live Status / Target Issue
   • Run: node .agents/skills/sonar-audit/scripts/sonar-api.mjs --status
   • Run: node .agents/skills/sonar-audit/scripts/sonar-api.mjs --rule <RULE_ID> (or --blockers)
        │
2. Locate Source & Root Cause
   • Identify exact file path and line number from CLI output
   • Inspect corresponding source file
        │
3. Apply Remediation Recipe
   • Consult resources/remediation-patterns.md for Silva-specific patterns
   • Apply fix following backend/AGENTS.md or frontend/AGENTS.md guidelines
        │
4. Verify Locally
   • Backend: cd backend && ./mvnw test checkstyle:checkstyle
   • Frontend: cd frontend && npm run test:unit && npm run prebuild
        │
5. Push & Confirm PR Gate
   • Open / update PR
   • Check gate in CI: node .agents/skills/sonar-audit/scripts/sonar-api.mjs --pr <PR_NUMBER>
```

---

## 4. Remediation Priority Framework

When triaging SonarCloud findings, prioritize issues using this 4-tier hierarchy:

### Tier 1: Blocker Bugs & Gate Breakers (P0 — Immediate)
*Issues that automatically fail the Reliability Rating (Grade A threshold $\le 1$):*
- **Resource Leaks (`java:S2095`):** Unclosed `Socket`, `InputStream`, `OutputStream`, or database connections. Must use try-with-resources or null-safe finally blocks.
- **Null Safety & Exceptions:** Unhandled `NullPointerException` risks or uncaught checked exceptions in critical flows.

### Tier 2: Test Integrity & False Pass Prevention (P1 — High)
*Issues that compromise the reliability of automated test suites:*
- **Missing Assertions (`java:S2699`, `typescript:S2699`):** Tests executing logic without verifying outcomes via assertion libraries.
- **Assertion Isolation (`java:S5778`):** `assertThrows` lambdas containing mock arrangement or auxiliary setup statements rather than isolating the single throwing call.

### Tier 3: Quality Gate Thresholds (P1 — High)
*Metrics that directly cause the Quality Gate status to become `ERROR`:*
- **Line Coverage ($\ge 80.0\%$):** Ensure all new feature logic and branches are covered by unit/integration tests.
- **Duplicated Lines ($\le 3.0\%$):** Consolidate copy-pasted UI components, utilities, or repeated boilerplate into shared abstractions.
- **Exclusion Hygiene:** Ensure machine-generated code (e.g. generated OpenAPI clients/DTOs) is properly excluded in CI analysis configurations.

### Tier 4: Maintainability & Cognitive Hygiene (P2 — Tech Debt)
*Code health improvements that reduce long-term maintenance burden:*
- **Cognitive Complexity (`java:S3776`, `typescript:S3776`):** Functions exceeding threshold ($> 15$) due to deep nesting or complex branching.
- **Nested Ternaries (`typescript:S3358`):** Chained conditionals in React JSX that should be refactored to guard clauses or helper renderers.
- **Commented-Out Code (`java:S125`):** Dead code preserved in comments that should be removed (retrievable via git history).
- **String Literal Duplication (`java:S1192`):** Repeated string literals that should be extracted into shared constants.

---

## 5. Code Recipes & Pattern Guide

For concrete, copy-pasteable before-and-after examples for Silva rules, refer to:
👉 [remediation-patterns.md](resources/remediation-patterns.md)
