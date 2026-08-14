# Frontend Agent Guidelines

**Quick Start:** This is the minimal entry point. For detailed coding patterns, load the `frontend-patterns` skill (search for "frontend patterns" in Copilot Chat) which provides 5 focused reference files:
- **styling.md** — SCSS, design tokens, no !important
- **components.md** — Screen/component structure, responsive Grid/Subgrid
- **api-integration.md** — OpenAPI client, TanStack Query, cache management
- **testing.md** — Jest + React Testing Library, 85% coverage, meaningful tests
- **import-types.md** — Import hierarchy, TypeScript types, never use `any`

## Tech Stack

- React + TypeScript (Vite)
- SCSS with `@bcgov-nr/nr-theme` design tokens
- IBM Carbon Design System components
- TanStack Query (React Query) for data fetching & caching
- Jest + React Testing Library for unit tests
- Playwright for E2E tests
- Docker on OpenShift via GitHub Actions

## Folder Structure

```
src/
├── screens/             # Full-page views (PascalCase)
│   └── ScreenName/index.tsx + styles.scss
├── components/          # Reusable UI elements
│   ├── Simple/          # Single file: index.tsx + styles.scss
│   └── Composite/       # Barrel export: sub-components + styles.scss
├── services/
│   ├── OpenApi/         # Auto-generated (DO NOT MODIFY)
│   └── API.ts           # Hand-maintained wrapper (register new services here)
├── hooks/               # Custom React hooks
├── types/               # TypeScript interfaces & types
├── utils/               # Helper functions
├── styles/              # Global styles & design tokens
├── __test__/            # Tests mirroring src/ structure
└── ...
```

## Task Classification Decision Tree

**Use this to determine which reference to load:**

```
1. Does task involve CSS/layout/theme/styling/colors/spacing?
   → Load "styling.md" reference

2. Does task involve React/component/screen/hooks/rendering/state?
   → Load "components.md" reference

3. Does task involve API/fetch/query/cache/mutation/TanStack Query?
   → Load "api-integration.md" reference

4. Does task involve testing/Jest/test structure/coverage?
   → Load "testing.md" reference

5. Does task involve imports/TypeScript types/interfaces?
   → Load "import-types.md" reference
```

All reference files are part of the `frontend-patterns` skill. Search Copilot Chat for "frontend patterns" to load them.

## API Client Architecture

The API client is auto-generated from the backend's OpenAPI spec:

- **`src/services/OpenApi/**`** — Auto-generated (DO NOT MODIFY)
- **`src/services/API.ts`** — Hand-maintained wrapper; register new services here

After backend changes:
```bash
cd frontend
npm run generate:openapi  # User runs this, not the agent
```

## Coverage Requirements

- **Minimum:** 85%
- **Target:** >90% on new code
- **Focus:** Meaningful behavioral tests, not line coverage

## Key Constraints

- Never use inline styles (use SCSS)
- Never use `any` type (use proper types or `unknown`)
- Never modify `src/services/OpenApi/**` (auto-generated)
- Always import styles last
- Use `@/` alias for project imports
- Use Carbon Grid/Subgrid for responsive layouts
- Use design tokens (never hardcoded colors)
- Avoid `!important` (use specificity instead)

## Quick Checklist

- [ ] Screen/Component: PascalCase, index.tsx, styles.scss
- [ ] Styling: SCSS hierarchy, design tokens, no !important
- [ ] Imports: Organized (packages → `@/` → folder → styles)
- [ ] Types: Proper typing, no `any`
- [ ] Responsive: Grid/Subgrid, tested at sm/md/lg
- [ ] API: TanStack Query with correct query keys
- [ ] Tests: Meaningful assertions, ≥85% coverage
- [ ] Cleanup: No unused imports, linter passing

## Resources

- IBM Carbon: https://carbondesignsystem.com/
- TanStack Query: https://tanstack.com/query/latest
- React Testing Library: https://testing-library.com/react
- TypeScript: https://www.typescriptlang.org/docs/
- Vite: https://vitejs.dev/

---

**For full details on any pattern, load the `frontend-patterns` skill in Copilot Chat.**
