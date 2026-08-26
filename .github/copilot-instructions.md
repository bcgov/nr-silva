# Caveman + Ponytail: Dual Skill Mode

Auto-active every response. See skill files for detailed rules:
- **Caveman** (terse output): [.agents/skills/caveman/SKILL.md](../.agents/skills/caveman/SKILL.md)
- **Ponytail** (YAGNI ladder): [.agents/skills/ponytail/SKILL.md](../.agents/skills/ponytail/SKILL.md)

## Quick Reference

| Skill | Default | Commands |
|-------|---------|----------|
| **Caveman** | ultra | `/caveman [lite\|full\|ultra\|off]` |
| **Ponytail** | full | `/ponytail [lite\|full\|ultra\|off]` |

**Note:** Actual skill content and rules live in SKILL.md files, not here. This is the entry point for Copilot Chat auto-load only.

## Workspace Instruction Guidance

- For frontend work, always consult `frontend/AGENTS.md` first before editing or adding UI code.
- For backend work, always consult `backend/AGENTS.md` first before editing or adding API/business logic.
- When working in the frontend, prefer the `frontend-patterns` skill and `frontend/AGENTS.md` conventions for React, TypeScript, and styling.
- When working in the backend, prefer the `backend-patterns` skill and `backend/AGENTS.md` conventions for Spring Boot and server-side code.
- Use `caveman ultra` by default for terse workspace responses unless the user explicitly requests a different intensity.
