## 🛠️ Fix-it Friday Report — {{DATE}}

### 1. 🤖 Automated Dependency & Bot PR Triage
| PR # | Title | Author | Risk Tier | Local / CI Status | Recommendation |
| :---: | :--- | :---: | :---: | :---: | :--- |
{{BOT_PR_TABLE_ROWS}}

<details>
<summary><strong>📖 Risk Tier Legend & Merge Policy</strong></summary>

- 🟢 **Tier 1 (Patch / Lockfile only):** Patch-level bump (`x.y.Z`) or transitive dependency update in `package-lock.json`. Zero breaking changes expected. Safe to merge once CI passes.
- 🟡 **Tier 2 (Minor / Non-runtime DevTool):** Minor bump (`x.Y.z`), transitive override, or devDependency tool bump. Requires local verification (`tsc`, unit tests, build).
- 🔴 **Tier 3 (Major / Runtime Breaking):** Major SemVer bump (`X.y.z`) to direct runtime dependencies or breaking CVE fix. Do not auto-merge; requires changelog review, migration check, and explicit developer sign-off.
</details>

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
