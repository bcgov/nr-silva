---
name: frontend-patterns
type: skill
description: Frontend coding patterns for React, TypeScript, styling, API integration, and testing in Silva
trigger: ["frontend patterns", "React patterns", "styling guide", "component structure"]
---

# Frontend Patterns Skill

Reference guide for **frontend coding conventions** in Silva (React + TypeScript + Vite). Use this skill when implementing frontend features.

---

## Quick Navigation

**Choose your reference based on task type:**

### 1. **CSS/Styling/Layout/Theme Tasks**
Load: `styling.md`
- SCSS hierarchy & scoping rules
- Design tokens & CSS variables
- No inline styles, no `!important`
- Carbon component style overrides
- Default component classes

**Use when task involves:** "Fix CSS layout", "adjust theme", "responsive bug", "styling issue", "color/padding/margin"

---

### 2. **React/Component/Screen/Hooks Tasks**
Load: `components.md`
- Screen convention (folder structure, naming)
- Component patterns (simple vs. composite)
- Barrel export pattern
- TypeScript component props
- Responsive Grid/Subgrid usage
- Carbon component usage basics

**Use when task involves:** "New component", "new screen", "fix component", "hook logic", "component structure", "Grid layout", "responsive design"

---

### 3. **API/Data Fetching/Query/Service Tasks**
Load: `api-integration.md`
- OpenAPI client architecture (auto-generated vs. wrapper)
- Query key conventions
- TanStack Query (useQuery, useMutation)
- Cache management & best practices
- Services folder rules (OpenApi/ + API.ts)

**Use when task involves:** "Add API call", "fetch data", "query optimization", "cache", "mutation", "new endpoint integration", "TanStack Query"

---

### 4. **Testing/Coverage/Test Structure Tasks**
Load: `testing.md`
- Test file structure (mirrored `src/__test__/`)
- Jest + React Testing Library setup
- Unit tests (components, hooks, API queries)
- Coverage requirements (85% minimum, 90% target)
- Meaningful assertions (behavior, not implementation)

**Use when task involves:** "Write test", "test component", "test hook", "coverage", "Jest", "React Testing Library", "unit test"

---

### 5. **Import Organization/TypeScript Types Tasks**
Load: `import-types.md`
- Import hierarchy (packages → `@/` → folder → styles)
- Import path rules (`@/` alias preference, relative paths in tests)
- TypeScript type definitions & interfaces
- Never use `any` (use `unknown` instead)
- Component props typing

**Use when task involves:** "Import issue", "organize imports", "TypeScript error", "type definition", "`any` type", "interface", "prop types"

---

## Task Classification Decision Tree

**Apply this tree to classify the task before loading references:**

```
1. Does task involve CSS/layout/theme/styling/colors/spacing?
   YES → Load styling.md
   NO  → Go to 2

2. Does task involve React/component/screen/hooks/rendering/state?
   YES → Load components.md
   NO  → Go to 3

3. Does task involve API/fetch/query/cache/mutation/TanStack Query?
   YES → Load api-integration.md
   NO  → Go to 4

4. Does task involve testing/Jest/test structure/coverage?
   YES → Load testing.md
   NO  → Go to 5

5. Does task involve imports/TypeScript types/interfaces?
   YES → Load import-types.md
   NO  → Use main frontend/AGENTS.md for general guidance

Multi-concern tasks:
- Identify PRIMARY concern (what the task is mostly about)
- Load that reference first
- Mention in response: "Also loaded [secondary reference] for [reason]"
```

---

## Reference Scope Boundaries

### `styling.md` — What's IN / What's OUT

**IN:**
- SCSS hierarchy & scoping patterns
- Design tokens & CSS variables from `@bcgov-nr/nr-theme`
- No inline styles rule
- Avoiding `!important`
- Carbon component style overrides
- Default component styles (`.default-grid`, `.default-zebra-table`, etc.)
- Reusable styles in `default-components.scss`

**OUT:**
- Component structure (see `components.md`)
- React component usage (see `components.md`)
- Grid/Subgrid layout logic (see `components.md`)

**Cross-reference note:** "For React/Grid structure, see components.md. For Carbon component setup, see components.md."

---

### `components.md` — What's IN / What's OUT

**IN:**
- Screen convention (folder structure, naming, example)
- Component patterns (Pattern A simple, Pattern B composite)
- Barrel export pattern
- Component structure guidelines
- Responsive design with Grid/Subgrid
- Grid breakpoints & responsive principles
- TypeScript component props interfaces
- Carbon component imports & usage
- Design token imports for components

**OUT:**
- CSS styling details (see `styling.md`)
- API/data fetching (see `api-integration.md`)
- Test writing (see `testing.md`)

**Cross-reference note:** "For CSS styling, see styling.md. For API integration, see api-integration.md."

---

### `api-integration.md` — What's IN / What's OUT

**IN:**
- API client architecture (OpenAPI auto-generated vs. API.ts wrapper)
- Query key conventions (structure matching endpoints)
- TanStack Query usage (useQuery, useMutation, cache)
- Best practices (optimistic updates, stale time, error handling, loading states)
- Services folder rules (never edit OpenApi/, only modify API.ts)
- Regenerating API client (`npm run generate:openapi`)

**OUT:**
- Component usage of queries (see `components.md` for usage in JSX)
- Styling queries/loading states (see `styling.md`)
- Testing queries (see `testing.md`)

**Cross-reference note:** "For testing API queries, see testing.md. For using queries in components, see components.md."

---

### `testing.md` — What's IN / What's OUT

**IN:**
- Test file structure (mirrored `src/__test__/`)
- Jest + React Testing Library setup
- Writing unit tests (components, hooks, API queries)
- Coverage requirements (85% minimum, 90% target)
- Avoiding meaningless tests
- Testing behavioral expectations, not implementation details

**OUT:**
- Component structure (see `components.md`)
- API setup (see `api-integration.md`)
- Styling (see `styling.md`)

**Cross-reference note:** "For component structure, see components.md. For API integration, see api-integration.md."

---

### `import-types.md` — What's IN / What's OUT

**IN:**
- Import hierarchy (packages → `@/` → folder → styles)
- Import path rules (`@/` alias, relative paths in tests)
- Type definitions & interfaces (general patterns)
- Never use `any` (use `unknown` instead)
- Component props typing (interfaces)

**OUT:**
- Component-specific structure (see `components.md` for component patterns)
- API types/DTOs (see `api-integration.md`)
- Test types (see `testing.md`)

**Cross-reference note:** "For component-specific typing, see components.md. For API types, see api-integration.md."

---

## Fallback: When Unsure

If the task straddles categories:
1. Classify PRIMARY concern using decision tree above
2. Load that reference
3. Mention in response which reference you loaded and why
4. Note: "If you need [secondary concern] patterns, load [reference name]"

---

## Resources

- Main entry: `frontend/AGENTS.md`
- IBM Carbon Design System: https://carbondesignsystem.com/
- TanStack Query Docs: https://tanstack.com/query/latest
- React Testing Library: https://testing-library.com/react
- TypeScript Handbook: https://www.typescriptlang.org/docs/
- Vite Docs: https://vitejs.dev/
