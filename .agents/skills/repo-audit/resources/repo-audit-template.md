### 🛡️ Repository Health & Tech Debt Audit Report

#### 1. Security Vulnerabilities
- **Frontend (`npm audit`):**
  - High / Critical: {{FRONTEND_HIGH_COUNT}}
  - Moderate / Low: {{FRONTEND_MODERATE_COUNT}}
  - Key Findings: {{FRONTEND_VULN_DETAILS}}
- **Backend:**
  - Status: {{BACKEND_SECURITY_STATUS}}

#### 2. Outdated Dependencies Spotlight
| Package | Current | Wanted | Latest | Location | Upgrade Priority |
| :--- | :---: | :---: | :---: | :--- | :---: |
| `{{PACKAGE_NAME}}` | {{CURRENT_VER}} | {{WANTED_VER}} | {{LATEST_VER}} | `frontend` | {{PRIORITY}} |

#### 3. Code Hygiene & Configuration Anomalies
- [ ] **Lint / Formatter:** {{LINT_STATUS}}
- [ ] **Checkstyle / Google Style:** {{STYLE_STATUS}}
- [ ] **Deprecations:** {{DEPRECATION_NOTES}}

#### 4. Branch & Test Suite Health
- **Total Tests:** {{TOTAL_TESTS}} passing
- **Stale Branches (>30 days old):** {{STALE_BRANCH_COUNT}}
  - {{STALE_BRANCH_LIST}}

#### 5. Recommended Remediation Backlog
1. {{REMEDY_ITEM_1}}
2. {{REMEDY_ITEM_2}}
