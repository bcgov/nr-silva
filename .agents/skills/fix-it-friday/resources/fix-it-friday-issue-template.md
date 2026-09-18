## 🛠️ Fix-it Friday Report — {{DATE}}

### 1. 🤖 Automated Dependency & Bot PR Triage
| PR # | Title | Author | Risk Tier | Local / CI Status | Recommendation |
| :---: | :--- | :---: | :---: | :---: | :--- |
{{BOT_PR_TABLE_ROWS}}

#### 📖 Risk Tier Legend & Policy
| Tier | SemVer / Scope | Criteria | Verification & Merge Policy |
| :---: | :--- | :--- | :--- |
| 🟢 **Tier 1** | **Patch Bump** (`x.y.Z`) or lockfile | Transitive or bugfix only; zero breaking changes | Safe to merge straight from CI |
| 🟡 **Tier 2** | **Minor Bump** (`x.Y.z`) | New backward-compatible features or direct dependencies | Local verification required (`tsc`, unit tests, build) |
| 🔴 **Tier 3** | **Major Bump** (`X.y.z`) | Breaking API changes, major architectural shifts, or overrides | Developer review required; safe to merge only if proven isolated / verified |

**Batch Verification Verdict:**
{{BATCH_TEST_VERDICT}}

---

### 2. 🛡️ Security & Vulnerabilities Audit
- **Frontend (`npm audit`):** {{FRONTEND_AUDIT_SUMMARY}}
- **Backend:** {{BACKEND_AUDIT_SUMMARY}}

---

### 3. 📊 SonarCloud Quality Gates & Health
| Project | Gate Status | Line Coverage (≥ 80%) | Duplication (≤ 3.0%) | Blockers / Criticals | Dashboard |
| :--- | :---: | :---: | :---: | :---: | :---: |
| **`nr-silva-backend`** | {{BACKEND_SONAR_GATE}} | {{BACKEND_COVERAGE}} | {{BACKEND_DUPLICATION}} | {{BACKEND_BLOCKERS}} | [View](https://sonarcloud.io/summary/new_code?id=nr-silva-backend) |
| **`nr-silva-frontend`** | {{FRONTEND_SONAR_GATE}} | {{FRONTEND_COVERAGE}} | {{FRONTEND_DUPLICATION}} | {{FRONTEND_BLOCKERS}} | [View](https://sonarcloud.io/summary/new_code?id=nr-silva-frontend) |

---

### 4. 🧹 Repository Hygiene & Tech Debt
- [ ] **Lint / Build Configuration:** {{LINT_SUMMARY}}
- [ ] **Outdated Packages Spotlight:** {{OUTDATED_PACKAGES_SUMMARY}}
- [ ] **Overrides & Resolutions Hygiene:** {{OVERRIDES_SUMMARY}}
- [ ] **Stale Branches:** {{STALE_BRANCHES_SUMMARY}}

---

### 5. 📋 Today's Action Items
{{ACTION_ITEMS_CHECKLIST}}

