### 🤖 Bot PR Triage Report

| PR # | Title | Author | Risk Tier | Local / CI Status | Recommendation |
| :---: | :--- | :---: | :---: | :---: | :--- |
| #{{PR_NUMBER}} | `{{PR_TITLE}}` | {{AUTHOR}} | {{TIER_BADGE}} | {{STATUS}} | {{RECOMMENDATION}} |

<details>
<summary><strong>📖 Risk Tier Legend & Merge Policy</strong></summary>

- 🟢 **Tier 1 (Patch / Lockfile only):** Patch-level bump (`x.y.Z`) or transitive dependency lockfile change. Safe to merge once CI passes.
- 🟡 **Tier 2 (Minor / DevTool):** Minor bump (`x.Y.z`), transitive override, or devDependency tool bump. Requires local verification (`tsc`, unit tests, build).
- 🔴 **Tier 3 (Major / Runtime Breaking):** Major SemVer bump (`X.y.z`) to direct runtime dependencies or breaking CVE fix. Requires manual developer review.
</details>

#### 🚂 Batch Verification Summary (if applicable)
- **Batch Branch:** `test/bot-batch-verify`
- **Candidate PRs included:** {{PR_LIST}}
- **Frontend Verification:** `cd frontend && npm run prebuild && npm run test:unit && npm run build` -> {{FRONTEND_STATUS}}
- **Backend Verification:** `cd backend && ./mvnw test-compile && ./mvnw test` -> {{BACKEND_STATUS}}
- **Combined Verdict:** {{BATCH_VERDICT}}
