---
name: resolve-pr-comments
description: "Fetch and address all open review comments on a pull request"
argument-hint: "pr_number"
agent: "silva-impl"
---

Fetch and address all open review comments on pull request **`$ARGUMENTS`**.

Follow the `pr-comment-resolver` skill workflow:

1. **Retrieve Comments**: Use `pull_request_read` with `method="get_review_comments"` to find unresolved review comments (`is_resolved: false`) on PR `$ARGUMENTS`.
2. **Address Sequentially**: For each review comment:
   - Identify the file path and target lines.
   - Edit the file to address the review comment.
   - Run local unit or integration tests to verify the change.
   - Commit the change separately with Conventional Commits syntax (e.g. `fix(<scope>): <message>`).
   - Push the commit.
   - Get the 7-digit commit SHA.
   - Post a reply to the PR comment with `"Fixed in commit <7-digit-hash>."` using `add_reply_to_pull_request_comment`.
3. **Verify**: Ensure all tests pass.
