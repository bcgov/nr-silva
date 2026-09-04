---
name: fix-it-friday
description: >
  Orchestrate the weekly "Fix-it Friday" workflow: triage bot PRs (Renovate/Dependabot), audit the repository
  for security and tech debt, and publish a comprehensive weekly Fix-it Friday GitHub Issue.
  USE FOR: "Run Fix-it Friday", "Fix it Friday report", "Friday maintenance check", weekly dependency triage,
  and automating weekly GitHub health check issues.
---

# Fix-it Friday Skill

This skill orchestrates the complete weekly **Fix-it Friday** maintenance routine for Silva. It unifies bot PR triage and repository health auditing, and creates a consolidated GitHub issue for the engineering team.

---

## Workflow Overview

```
1. Tooling Check (GitHub MCP vs. CLI)
       │
2. Triage Open Bot PRs (bot-pr-triage)
       │
3. Audit Repository Health (repo-audit)
       │
4. Avoid Duplicate Issue (search existing issues)
       │
5. Publish Weekly GitHub Issue via MCP or gh CLI
```

---

## Step-by-Step Instructions

### Step 1: Tooling Check (GitHub MCP vs. CLI)
Determine if the GitHub MCP tool is available in your active session:
- Look for `github-mcp-server-nr-silva` (or `github-mcp-server`) in available tools (`call_mcp_tool`).
- If MCP is present, use MCP calls for searching, reading, and creating issues.
- If MCP is absent or fails, execute commands via `gh` CLI in the shell.

---

### Step 2: Triage Open Bot PRs
1. **Fetch open bot PRs:**
   - **MCP:** Call `search_pull_requests(query="repo:bcgov/nr-silva is:pr is:open author:app/renovate author:app/dependabot")` or `list_pull_requests(owner="bcgov", repo="nr-silva", state="open")`.
   - **CLI:**
     ```bash
     gh pr list --repo bcgov/nr-silva --state open --json number,title,author,headRefName,statusCheckRollup,url
     ```
2. **Classify by Risk Tier:**
   - 🟢 **Tier 1 (Patch / Lockfile only):** Safe to merge if CI passes.
   - 🟡 **Tier 2 (Minor / New features):** Run local verification.
   - 🔴 **Tier 3 (Major / Breaking):** Flag for developer review.
3. **Run Batch Verification if multiple lockfile PRs are open:**
   - Follow the batch verification process from the `bot-pr-triage` skill:
     ```bash
     git checkout -b test/bot-batch-verify origin/main
     # merge candidate PR branches
     npm install
     npm run prebuild && npm run test:unit && npm run build
     git checkout main && git branch -D test/bot-batch-verify
     ```

---

### Step 3: Audit Repository Health
Execute the checks from the `repo-audit` skill:
1. **Security Scan:**
   - Frontend: `cd frontend && npm audit --audit-level=moderate`
   - Backend: `cd backend && ./mvnw dependency:analyze`
2. **Outdated Dependencies:**
   - Frontend: `cd frontend && npm outdated --depth=0`
   - Backend: `cd backend && ./mvnw versions:display-dependency-updates && ./mvnw versions:display-property-updates`
3. **Configuration & Hygiene Check:**
   - Check `.eslintrc.json` syntax and ESLint flat config compatibility.
   - Run `npm run prebuild` (TypeScript compiler check).
   - Run `cd backend && ./mvnw checkstyle:checkstyle`
4. **Backend Dual-Database Tests (matches `analysis.yml` workflow):**
   When verifying backend changes, run integration tests against both primary databases:
   - Oracle: `./mvnw clean test -Dserver.primary-db=oracle --no-transfer-progress checkstyle:checkstyle -P all-tests`
   - Postgres: `./mvnw clean test -Dflyway-environment=dev -Dserver.primary-db=postgres --no-transfer-progress checkstyle:checkstyle -P all-tests`
5. **Stale Remote Branches:**
   ```bash
   git for-each-ref --sort=-committerdate refs/remotes/origin --format='%(committerdate:short) %(refname:short)' | grep -v 'origin/main' | head -n 5
   ```

---

### Step 4: Check for Duplicate Issue
Before creating a new issue, verify whether a Fix-it Friday issue has already been opened for today:
- **Date format:** `YYYY-MM-DD` (e.g. `2026-09-04`).
- **MCP:** Call `search_issues(query="repo:bcgov/nr-silva in:title [Fix-it Friday] YYYY-MM-DD")`.
- **CLI:**
  ```bash
  gh issue list --repo bcgov/nr-silva --search "[Fix-it Friday] $(date +%F)" --json number,title,url
  ```
- If an issue already exists: update or post a comment to that issue instead of creating a duplicate.

---

### Step 5: Format & Publish the GitHub Issue
1. Fill out [fix-it-friday-issue-template.md](file://resources/fix-it-friday-issue-template.md) with the collected findings:
   - Bot PR triage table with recommendations.
   - Security audit summary (`npm audit` counts).
   - Tech debt and code hygiene action items with checkboxes.
2. **Issue Title:**
   ```
   [Fix-it Friday] Weekly Dependency Triage & Repo Health Report — YYYY-MM-DD
   ```
3. **Issue Labels:**
   `["Tech Debt", "Reports"]`
4. **Publishing:**
   - **MCP Tool:** Call `issue_write` on `github-mcp-server-nr-silva`:
     ```json
     {
       "method": "create",
       "owner": "bcgov",
       "repo": "nr-silva",
       "title": "[Fix-it Friday] Weekly Dependency Triage & Repo Health Report — YYYY-MM-DD",
       "body": "<formatted report markdown>",
       "labels": ["Tech Debt", "Reports"]
     }
     ```
   - **CLI Fallback:**
     ```bash
     gh issue create --repo bcgov/nr-silva \
       --title "[Fix-it Friday] Weekly Dependency Triage & Repo Health Report — $(date +%F)" \
       --body "<formatted report markdown>" \
       --label "Tech Debt,Reports"
     ```

---

### Step 6: Present Summary to User
Provide the user with:
1. Link to the created (or updated) GitHub Issue.
2. Quick summary of candidate PRs ready to merge immediately.
3. High-priority action items for the Friday maintenance session.

---

### Step 7: (Optional) Create Quick-Fix Maintenance Branch
If quick fixes or cleanup tasks were identified during the audit (e.g., removing dead dependencies like `sass-loader`, fixing linter/checkstyle syntax anomalies, or applying safe security fixes):
1. **Prompt the user:** Ask if they would like a dedicated maintenance branch created to bundle today's quick fixes.
2. **Branch Creation:**
   ```bash
   git checkout -b chore/fix-it-friday-YYYY-MM-DD origin/main
   ```
3. **Apply the identified quick fixes:**
   - Uninstall dead/unused packages (e.g. `cd frontend && npm uninstall sass-loader`).
   - Fix configuration or syntax issues (e.g. removing trailing comma in `.eslintrc.json`).
   - Apply safe automated dependency fixes if verified (`npm audit fix`).
4. **Local Verification:**
   - Frontend: `npm run prebuild && npm run test:unit && npm run build`
   - Backend (if affected): `./mvnw test-compile checkstyle:checkstyle && ./mvnw test`
5. **Commit & PR Proposal:**
   - Create a clean commit:
     ```bash
     git commit -m "chore: fix-it-friday maintenance cleanup for YYYY-MM-DD"
     ```
   - Reference the Fix-it Friday issue number in the commit/PR body (e.g., `Resolves #[ISSUE_NUMBER]`) to link the fix directly to today's report.

