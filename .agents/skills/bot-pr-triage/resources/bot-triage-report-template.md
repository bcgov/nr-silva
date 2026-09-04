### 🤖 Bot PR Triage Report

| PR # | Title | Author | Risk Tier | Local / CI Status | Recommendation |
| :---: | :--- | :---: | :---: | :---: | :--- |
| #{{PR_NUMBER}} | `{{PR_TITLE}}` | {{AUTHOR}} | {{TIER_BADGE}} | {{STATUS}} | {{RECOMMENDATION}} |

#### 📖 Risk Tier Legend & Policy
| Tier | SemVer / Scope | Criteria | Verification & Merge Policy |
| :---: | :--- | :--- | :--- |
| 🟢 **Tier 1** | **Patch Bump** (`x.y.Z`) or lockfile | Transitive or bugfix only; zero breaking changes | Safe to merge straight from CI |
| 🟡 **Tier 2** | **Minor Bump** (`x.Y.z`) | New backward-compatible features or direct dependencies | Local verification required (`tsc`, unit tests, build) |
| 🔴 **Tier 3** | **Major Bump** (`X.y.z`) | Breaking API changes, major architectural shifts, or overrides | Developer review required; safe to merge only if proven isolated / verified |

#### 🚂 Batch Verification Summary (if applicable)
- **Batch Branch:** `test/bot-batch-verify`
- **Candidate PRs included:** {{PR_LIST}}
- **Frontend Verification:** `cd frontend && npm run prebuild && npm run test:unit && npm run build` -> {{FRONTEND_STATUS}}
- **Backend Verification:** `cd backend && ./mvnw test-compile && ./mvnw test` -> {{BACKEND_STATUS}}
- **Combined Verdict:** {{BATCH_VERDICT}}
