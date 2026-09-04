---
name: bot-pr-triage
description: >
  Triage, review, and verify automated pull requests opened by Renovate, Dependabot, or other bot accounts.
  USE FOR: fetching open bot PRs, classifying risk tiers (SemVer), running local builds and unit tests,
  batch-verifying multiple bot PRs together on a staging branch, and recommending merge or hold actions.
  DO NOT USE FOR: reviewing human-authored feature PRs or non-dependency PR reviews (use pr-comment-resolver instead).
---

# Bot PR Triage & Verification Skill

This skill guides the agent through fetching, assessing risk, locally testing, and triaging automated dependency pull requests (Renovate, Dependabot).

---

## 1. GitHub Tooling Check: MCP First, CLI Fallback

Always check which GitHub interface is available before taking action:

### Option A: GitHub MCP Server (Preferred in IDE)
Check if `github-mcp-server-nr-silva` (or an active `github-mcp-server`) is registered.
Use the MCP tools:
- `search_pull_requests(query="repo:bcgov/nr-silva is:pr is:open author:app/renovate")`
- `list_pull_requests(owner="bcgov", repo="nr-silva", state="open")`
- `pull_request_read(owner="bcgov", repo="nr-silva", pullNumber=..., method="get")`
- `merge_pull_request(owner="bcgov", repo="nr-silva", pullNumber=..., method="squash")`

### Option B: GitHub CLI (`gh`) (Fallback)
If MCP is unavailable or errors, execute via shell:
```bash
gh pr list --repo bcgov/nr-silva --state open --json number,title,author,headRefName,statusCheckRollup,url
```

---

## 2. Risk Assessment Matrix

Classify each open bot PR into one of three risk tiers based on SemVer and diff scope:

| Tier | SemVer / Scope | Criteria | Verification Required |
| :---: | :--- | :--- | :--- |
| **🟢 Tier 1<br>(Zero / Low Risk)** | **Patch Bump**<br>(e.g. `6.15.2` → `6.16.0`)<br>**Lockfile only** | - Only `package-lock.json` or patch version in `package.json`<br>- Transitive dependencies only<br>- CI checks green on GitHub | Quick local typecheck (`npm run prebuild`). Safe to merge straight from CI if all green. |
| **🟡 Tier 2<br>(Medium Risk)** | **Minor Bump**<br>(e.g. `1.114.0` → `1.115.0`) | - Direct dependency in `package.json` or `pom.xml`<br>- Added APIs, new features<br>- No documented breaking changes | Full local verification: typecheck, unit tests, and production build. |
| **🔴 Tier 3<br>(High Risk)** | **Major Bump**<br>(e.g. `v5.0.0` → `v6.0.0`)<br>or **Breaking CVE Fix** | - Major SemVer change<br>- Deprecations, architectural shifts<br>- Failing CI checks | Do NOT auto-merge. Flag for developer review with link to breaking change changelog. |

---

## 3. Smart Triage Heuristics (Pre-Test Analysis)

Before running heavy test suites, apply these 4 intelligent triage heuristics:

### Heuristic 1: Dead Code / Usage Pre-Check
- **Check:** Does the codebase actually import or configure this package?
  ```bash
  git grep "<package-name>" frontend/src/ frontend/vite.config.ts backend/src/
  ```
- **Rule:** If 0 code references exist and the library is obsolete for the architecture (e.g. Webpack's `sass-loader` in a Vite project, since Vite handles SCSS natively via `sass`):
  - **Verdict:** Do NOT merge upgrades for dead code.
  - **Action:** Recommend **closing the PR** and running `npm uninstall <package-name>` to eliminate maintenance overhead.

### Heuristic 2: Inspect `dependencies` vs. `overrides`
- **Check:** Inspect the diff in `package.json`. Did Renovate bump a top-level package in `"dependencies"` / `"devDependencies"`, or merely a transitive entry in `"overrides"` / `"resolutions"`?
- **Rule:** A PR titled *"Major update to X to v8"* that only alters an entry inside `"overrides"` while the app remains on `^7.0.0` does NOT introduce breaking API changes to application code. Downgrade its risk tier if unit tests and build pass cleanly.

### Heuristic 3: Targeted Subsystem Verification
- **Check:** If a bumped devDependency is tied to a specific script in `package.json` (e.g. `nyc` in `postcoverage`):
- **Rule:** Directly execute that exact script (e.g. `npm run postcoverage`) in addition to unit tests to verify that CLI arguments, report formats, and engine requirements work as expected.

### Heuristic 4: Correlate Active CVEs to Candidate PRs
- **Check:** Run `npm audit` on `main` to identify open CVEs.
- **Rule:** Match open CVE packages (e.g. `qs`) directly to open Renovate PRs. Highlight in the triage report that merging the candidate PR resolves the active vulnerability.

---

## 4. Verification Workflows

### Mode A: Single PR Verification
When asked to test a specific bot branch (e.g. `renovate/npm-qs-vulnerability`):
1. Checkout the branch:
   ```bash
   git checkout <branch-name>
   ```
2. **If Frontend:**
   ```bash
   cd frontend
   npm ci
   npm run prebuild    # Verifies TypeScript types
   npm run test:unit   # Runs Vitest unit tests
   npm run build       # Verifies Vite production bundling
   ```
3. **If Backend:**
   - **Quick / Patch check:**
     ```bash
     cd backend
     ./mvnw test-compile checkstyle:checkstyle
     ./mvnw test
     ```
   - **Full Integration verification (matches `analysis.yml` dual-DB workflow):**
     Silva runs integration tests twice to validate both primary databases. When verifying significant backend dependency updates, run both:
     ```bash
     cd backend
     # 1. Oracle Primary DB Run:
     ./mvnw clean test -Dserver.primary-db=oracle --no-transfer-progress checkstyle:checkstyle -P all-tests

     # 2. Postgres Primary DB Run:
     ./mvnw clean test -Dflyway-environment=dev -Dserver.primary-db=postgres --no-transfer-progress checkstyle:checkstyle -P all-tests
     ```
4. Note: If frontend lint fails due to legacy `.eslintrc.json`, remember that CI ignores lint with `|| true` on `main`. Do not fail the dependency bump for pre-existing ESLint 9 config issues if types, tests, and build pass cleanly.

---

### Mode B: Batch Verification ("The Dependency Train")
When multiple bot PRs modify `package-lock.json` or `pom.xml`, testing them individually leads to merge conflicts. Verify them together:

1. **Create a temporary batch branch off `origin/main`:**
   ```bash
   git checkout -b test/bot-batch-verify origin/main
   ```
2. **Merge candidate Tier 1 and Tier 2 bot branches:**
   ```bash
   git merge origin/<bot-branch-1> --no-edit
   git merge origin/<bot-branch-2> --no-edit
   # If package-lock.json has merge conflicts, run `npm install` to regenerate cleanly:
   npm install
   git add package-lock.json && git commit -m "chore: resolve lockfile convergence"
   ```
3. **Run the test suite on the combined state:**
   ```bash
   npm run prebuild && npm run test:unit && npm run build
   ```
4. **Report Results:**
   If all pass, all tested PRs are mutually compatible and can be merged sequentially (or rebased cleanly via Renovate's rebase checkbox).
5. **Clean up:**
   ```bash
   git checkout main
   git branch -D test/bot-batch-verify
   ```

---

## 5. Reporting Output

Format the triage findings using [bot-triage-report-template.md](file://resources/bot-triage-report-template.md):
- List PR number, title, author, tier badge (`🟢 Tier 1`, `🟡 Tier 2`, `🔴 Tier 3`).
- Include CI and local test status.
- State a clear, actionable recommendation:
  - `✅ Approve & Merge`
  - `⏳ Needs developer review (Breaking changes in vX)`
  - `⚠️ Blocked by failing CI / merge conflicts`
