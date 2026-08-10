---
name: pr-comment-resolver
description: >
  Skill for resolving GitHub pull request review comments.
  USE FOR: fetching PR review comments, applying changes, committing them separately, pushing them, and replying to the review comments with the 7-digit commit hashes.
  DO NOT USE FOR: non-git/non-PR operations, general code-only tasks that do not involve pull request reviews.
---

# GitHub PR Comment Resolver — Skill

This skill outlines the step-by-step workflow for fetching, analyzing, addressing, committing, and replying to pull request review comments on GitHub.

---

## Workflow Steps

### 1. Identify the Target Pull Request
* Determine the current git branch name:
  ```bash
  git branch --show-current
  ```
* Call the MCP tool `list_pull_requests` on `github-mcp-server` (or query the GitHub API) to locate the open pull request that corresponds to the active branch. Note down the `pullNumber`.

### 2. Retrieve Review Comments
**Option A: Using GitHub CLI (Recommended)**
```bash
# Get all pull request review comments
gh api repos/bcgov/nr-silva/pulls/<PR_NUMBER>/comments

# Parse with Python for readability
gh api repos/bcgov/nr-silva/pulls/<PR_NUMBER>/comments 2>&1 | python3 -c "
import json, sys
for c in json.load(sys.stdin):
    print(f'ID: {c[\"id\"]} | Path: {c[\"path\"]} | Line: {c[\"line\"]} | Body: {c[\"body\"][:80]}...')
"
```

**Option B: Using MCP Tools**
* Call the `pull_request_read` tool with `method="get_review_comments"`, specifying the `owner`, `repo`, and `pullNumber`.

**For each comment, extract:**
  * The `commentId` (the numeric ID, e.g., `3678193517`).
  * The file `path` and target `line` numbers.
  * The comment `body` detailing the requested change.
  * The `is_resolved` flag — only address unresolved comments (`is_resolved: false`).

### 3. Validate Comments Retrieved
**Before proceeding, confirm:**
```bash
# Count total unresolved comments
gh api repos/bcgov/nr-silva/pulls/<PR_NUMBER>/comments | \
  python3 -c "import json, sys; comments = json.load(sys.stdin); print(f'Total: {len(comments)} comment(s)')"
```
* If count is **0**, the PR may not have review comments yet, or they may already be resolved.
* If count is **> 0**, proceed with validation and addressing each comment.

### 4. Validate Each Comment
For each comment:
1. **Read Context**: Examine the referenced code, the full method/component, and surrounding logic.
2. **Assess Validity**: Determine if the comment is:
   - **Valid & necessary**: Code genuinely has the issue described.
   - **Invalid/misunderstood**: Reviewer misunderstood the code, context, or project conventions.
   - **Already addressed**: Prior commits or other code changes already resolved it.
   - **Against convention**: Conflicts with project standards or documented patterns.
   - **Duplicate**: Same concern already raised in another comment.
3. **Decision**:
   - If **valid**: Proceed to Step 4 (Address the Comment).
   - If **invalid/unnecessary**: Reply with a brief explanation of why the comment is not applicable, then move to next comment.

### 5. Address Each Valid Comment Sequentially
For each validated comment:
1. **Analyze and Modify**: Read the referenced code block and edit the file to address the feedback using your code editing tools.
2. **Local Validation**: Run the relevant local tests or verification commands to confirm the fix is correct and functional.
3. **Commit**: Stage and commit the fix separately with a descriptive commit message:
   ```bash
   git add <modified_file_path>
   git commit -m "fix(<scope>): <short description of fix>"
   ```
4. **Push**: Push the commit to the remote branch:
   ```bash
   git push
   ```
5. **Get Commit Hash**: Extract the 7-digit commit SHA:
   ```bash
   git rev-parse --short HEAD
   ```
6. **Post Reply** (two options):

   **Option A: GitHub CLI (Recommended)**
   ```bash
   gh api repos/bcgov/nr-silva/pulls/<PR_NUMBER>/comments/<COMMENT_ID>/replies \
     -f body="Fixed in commit <7-digit-hash>"
   ```

   **Option B: MCP Tool**
   Call the `add_reply_to_pull_request_comment` tool with:
   * `owner`, `repo`, `pullNumber`
   * `commentId`: the numeric ID of the review comment
   * `body`: `"Fixed in commit <7-digit-hash>."`

### 6. Final Verification
* Verify the codebase is clean and regression-free:
  * **Check number of changed files**: Count the files modified across all commits.
  * **If ≤2 files changed**: Run targeted tests for the affected modules only.
  * **If >2 files changed**: Run the full test suite to ensure no regressions.

---

## Project-Specific Verification (nr-silva)

When working on the **nr-silva** repository, verify your changes using the following local commands:

## Backend test commands
- For backend with Oracle as primary db: `./mvnw -s ~/.m2/settings.xml clean install -Dserver.primary-db=oracle --no-transfer-progress -P all-tests`
- For backend with Postgres as primary db: `./mvnw -s ~/.m2/settings.xml clean install -Dserver.primary-db=postgres --no-transfer-progress -P all-tests`
Below we use `./mvnw test` for short.

### For Limited Changes (1-2 files)
Identify the affected test classes and run targeted tests:
```bash
cd backend
./mvnw test -Dtest=AffectedServiceTest
./mvnw test -Dtest=AffectedIntegrationTest
```

### For Multiple Changes (3+ files)
Run the complete backend test suite:
```bash
cd backend
./mvnw clean test
```

Alternatively, to verify compilation only (faster):
```bash
cd backend
./mvnw clean compile
```

---

## Troubleshooting

### Problem: No comments found when calling the API
**Common causes:**
1. **Wrong PR number**: Verify the PR number from GitHub or use `gh pr view` to confirm the current PR
   ```bash
   gh pr view  # Shows current PR number and branch
   ```
2. **Comments already resolved**: Check GitHub UI to see if all Copilot/reviewer comments are marked as resolved
3. **API permissions**: Ensure GitHub CLI is authenticated:
   ```bash
   gh auth status
   ```

### Problem: Forgot to fetch comments initially
**Solution:** Always run the fetch and validation step (Step 2-3) FIRST, before analyzing any code:
```bash
# Quick check if PR has any review comments
gh api repos/bcgov/nr-silva/pulls/1380/comments | python3 -c "import json, sys; print(len(json.load(sys.stdin)), 'comment(s)')"
```
This prevents missed reviews and wasted time analyzing code without knowing what needs fixing.

### Problem: Getting 404 on API call
**Typical issue:** Using wrong repository path. Verify with:
```bash
gh repo view  # Shows owner/repo path
```
Then use the correct format: `repos/{owner}/{repo}/pulls/{number}/comments`
