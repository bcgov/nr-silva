### 🤖 Bot PR Triage Report

| PR # | Title | Author | Risk Tier | Local / CI Status | Recommendation |
| :---: | :--- | :---: | :---: | :---: | :--- |
| #{{PR_NUMBER}} | `{{PR_TITLE}}` | {{AUTHOR}} | {{TIER_BADGE}} | {{STATUS}} | {{RECOMMENDATION}} |

#### 🚂 Batch Verification Summary (if applicable)
- **Batch Branch:** `test/bot-batch-verify`
- **Candidate PRs included:** {{PR_LIST}}
- **Frontend Verification:** `cd frontend && npm run prebuild && npm run test:unit && npm run build` -> {{FRONTEND_STATUS}}
- **Backend Verification:** `cd backend && ./mvnw test-compile && ./mvnw test` -> {{BACKEND_STATUS}}
- **Combined Verdict:** {{BATCH_VERDICT}}
