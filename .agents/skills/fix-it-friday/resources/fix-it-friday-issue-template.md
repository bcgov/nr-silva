## 🛠️ Fix-it Friday Report — {{DATE}}

### 1. 🤖 Automated Dependency & Bot PR Triage
| PR # | Title | Author | Risk Tier | Local / CI Status | Recommendation |
| :---: | :--- | :---: | :---: | :---: | :--- |
{{BOT_PR_TABLE_ROWS}}

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
