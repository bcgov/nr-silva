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
* Call the `pull_request_read` tool with `method="get_review_comments"`, specifying the `owner`, `repo`, and `pullNumber`.
* Identify all unresolved comment threads (`is_resolved: false`). For each thread, extract:
  * The `commentId` (obtained from the comment object or the suffix of `html_url` e.g., `#discussion_r3599506357` -> `3599506357`).
  * The file `path` and target `line` numbers.
  * The comment `body` detailing the requested change.

### 3. Validate Each Comment
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

### 4. Address Each Valid Comment Sequentially
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
5. **Get Commit Hash**: Extract the 7-digit commit SHA (found in the commit command output or via `git rev-parse --short HEAD`).
6. **Post Reply**: Call the MCP tool `add_reply_to_pull_request_comment` with the arguments:
   * `owner`, `repo`, `pullNumber`
   * `commentId`: the numeric ID of the review comment
   * `body`: `"Fixed in commit <7-digit-hash>."`

### 5. Final Verification
* Verify the codebase is clean and regression-free:
  * **Check number of changed files**: Count the files modified across all commits.
  * **If ≤2 files changed**: Run targeted tests for the affected modules only.
  * **If >2 files changed**: Run the full test suite to ensure no regressions.

---

## Project-Specific Verification (nr-silva)

When working on the **nr-silva** repository, verify your changes using the following local commands:

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
