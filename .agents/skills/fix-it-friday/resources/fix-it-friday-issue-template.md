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

### 3. 🧹 Repository Hygiene & Tech Debt
- [ ] **Lint / Build Configuration:** {{LINT_SUMMARY}}
- [ ] **Outdated Packages Spotlight:** {{OUTDATED_PACKAGES_SUMMARY}}
- [ ] **Stale Branches:** {{STALE_BRANCHES_SUMMARY}}

---

### 4. 📋 Today's Action Items
{{ACTION_ITEMS_CHECKLIST}}
