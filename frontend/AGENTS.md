# Frontend Agent Guidelines

## Overview

The Silva frontend is a **React + TypeScript** application built with **Vite**, using **IBM Carbon Design System** components for a consistent, accessible UI. This document defines coding conventions, patterns, and workflows for implementing frontend features.

**Tech Stack:**
- React with TypeScript
- Vite (build tool)
- SCSS for styling
- IBM Carbon components
- TanStack Query (React Query) for data fetching
- Playwright for E2E testing
- Jest for unit testing

**Deployment:** Docker container on OpenShift via GitHub Actions

---

## Screen Convention

Screens represent full-page views. They are located in `src/screens/` with a consistent structure.

### Folder Structure

```
src/screens/ScreenName/
├── index.tsx           # Main screen component (required)
├── styles.scss         # Screen-specific styles (required)
├── constants.ts(x)     # Constants/configuration (optional)
├── utils.ts(x)         # Utility functions (optional)
├── definitions.ts      # Type definitions (optional)
└── {Component}.tsx     # Supporting components (optional)
```

### Naming Convention

- Folder name: **PascalCase** (e.g., `Dashboard`, `CreateOpening`, `Openings`)
- Export default screen component from `index.tsx`

### Example Screen: `Dashboard`

```typescript
// src/screens/Dashboard/index.tsx
import React from 'react';

import PageTitle from '@/components/PageTitle';
import Card from '@/components/Card';

import './styles.scss';

const Dashboard: React.FC = () => {
  return (
    <div className="dashboard-container">
      <PageTitle title="Dashboard" />
      {/* Screen content */}
    </div>
  );
};

export default Dashboard;
```

### Screen Styling

```scss
// src/screens/Dashboard/styles.scss
@use '@bcgov-nr/nr-theme/design-tokens/variables.scss' as vars;
@use '@bcgov-nr/nr-theme/design-tokens/colors.scss' as colors;
@use '@carbon/type';

.dashboard-container {
  width: 100%;
  padding: 2rem;

  .dashboard-title {
    @include type.type-style('heading-02');
    margin-bottom: 1rem;
  }

  .dashboard-section {
    background: var(--#{vars.$bcgov-prefix}-layer-02);
  }
}
```

### Key Patterns

- Use `.default-grid` class for consistent 2rem padding and 2.5rem row gaps
- Screens typically use Carbon `Grid` and `Column` components for layout
- Import styles at the **bottom** of the file
- Component logic kept in `index.tsx`; complex logic extracted to `utils.ts`
- Constants and enums in `constants.ts`
- Type definitions in `definitions.ts` if screen-specific

---

## Component Convention

Components are reusable UI elements located in `src/components/`. Two patterns exist:

### Pattern A: Simple Components (Single File)

For small, self-contained components:

```
src/components/Avatar/
├── index.tsx       # Component implementation
└── styles.scss     # Component styles
```

**Example:**

```typescript
// src/components/Avatar/index.tsx
import React from 'react';

import './styles.scss';

interface AvatarProps {
  initials: string;
  size?: 'sm' | 'md' | 'lg';
}

/**
 * Avatar component that displays user's initials
 * @param initials - User initials (e.g., "JD")
 * @param size - Avatar size (default: 'md')
 */
const Avatar: React.FC<AvatarProps> = ({ initials, size = 'md' }) => {
  return (
    <div className={`avatar avatar-${size}`}>
      {initials}
    </div>
  );
};

export default Avatar;
```

### Pattern B: Composite Components (Folder with Sub-Components)

For complex components with multiple related sub-components:

```
src/components/OpeningDetails/
├── index.tsx                    # Barrel export file
├── OpeningOverview/
│   ├── index.tsx
│   └── styles.scss
├── OpeningSummary/
│   ├── index.tsx
│   └── styles.scss
├── OpeningActivities/
│   ├── index.tsx
│   └── styles.scss
└── styles.scss                  # Parent component styles (if needed)
```

**Barrel Export Pattern:**

```typescript
// src/components/OpeningDetails/index.tsx
export { default as OpeningOverview } from './OpeningOverview';
export { default as OpeningSummary } from './OpeningSummary';
export { default as OpeningActivities } from './OpeningActivities';
```

**Usage:**

```typescript
import {
  OpeningOverview,
  OpeningSummary,
  OpeningActivities,
} from '@/components/OpeningDetails';

const MyScreen = () => (
  <>
    <OpeningOverview id={1} />
    <OpeningSummary id={1} />
    <OpeningActivities id={1} />
  </>
);
```

### Component Structure Guidelines

- Keep sub-components in their own folders with `index.tsx` and `styles.scss`
- Parent `index.tsx` is a **barrel export file** only (re-exports sub-components)
- Each sub-component is self-contained and independently styled
- Use this pattern when components naturally group together or are heavily interdependent

---

## Responsive Design

**Always implement with responsiveness in mind.** Silva is used across different devices and screen sizes, so all components and screens must adapt gracefully.

### Using Grid & Subgrid

Use **Carbon's `Grid` and `Column` components** or **CSS Grid/Subgrid** for responsive layouts:

```typescript
// src/screens/Dashboard/index.tsx
import { Grid, Column } from '@carbon/react';

const Dashboard: React.FC = () => (
  <Grid className="dashboard-container">
    <Column lg={16} md={8} sm={4} className="full-width-col">
      {/* Full-width header content */}
    </Column>
    <Column lg={8} md={4} sm={4}>
      {/* Left column - 50% on desktop, 100% on mobile */}
    </Column>
    <Column lg={8} md={4} sm={4}>
      {/* Right column - 50% on desktop, 100% on mobile */}
    </Column>
  </Grid>
);
```

### Grid Breakpoints

Carbon uses responsive column ratios:
- **`lg`**: Large screens (1440px+) — 16-column grid
- **`md`**: Medium screens (768px-1439px) — 8-column grid
- **`sm`**: Small screens (320px-767px) — 4-column grid

### Subgrid for Nested Layouts

Use subgrid when nesting grid layouts:

```scss
// src/components/OpeningCard/styles.scss
.opening-card {
  display: grid;
  grid-template-columns: subgrid;
  gap: 1rem;

  .card-header {
    grid-column: 1 / -1;  // Span full width
  }

  .card-body {
    grid-column: 1 / -1;
  }
}
```

### Key Responsive Principles

- **Mobile-first:** Design for small screens first, then enhance for larger screens
- **Flexible components:** Avoid hardcoded widths; use percentages and grid layouts
- **Touch-friendly:** Ensure interactive elements have adequate padding/size for touch
- **Test across breakpoints:** Verify layout at `sm` (320px), `md` (768px), and `lg` (1440px)

### Common Responsive Classes

The project provides pre-made responsive utilities:
- `.default-grid` — Main content grid with responsive padding and gaps
- `.full-width-col` — Column that stretches beyond grid padding when needed
- `.subgrid-full-width-col` — Sub-grid column spanning full width

---

## Styling Rules

### No Inline Styles

❌ **Never use inline styles:**
```typescript
// DON'T DO THIS
<div style={{ color: 'blue', fontSize: '16px' }}>Text</div>
```

✅ **Use SCSS classes instead:**
```typescript
<div className="my-component__text">Text</div>
```

### SCSS Hierarchy (Prevent Style Leaking)

Use nested class hierarchies to prevent naming collisions. Always scope styles under a root container class.

```scss
.component-container {
  width: 100%;
  padding: 1rem;

  .title-class {
    font-size: 2rem;
    color: var(--#{vars.$bcgov-prefix}-text-01);

    .icon {
      color: var(--#{vars.$bcgov-prefix}-brand-blue);
      margin-left: 0.5rem;
    }
  }

  .content-area {
    margin-top: 1rem;

    .item {
      padding: 0.5rem;

      &:hover {
        background: var(--#{vars.$bcgov-prefix}-layer-hover-01);
      }
    }
  }
}
```

**Benefits:**
- Styles are scoped to the component
- Avoids unintended style conflicts with other components
- Clear parent-child relationships in CSS

### Reusable Styles: `default-components.scss`

If a style is **reusable across multiple components**, add it to `src/styles/default-components.scss` with a **`default-`** prefix.

**Check existing styles first** — they may already exist:

```scss
// Existing reusable styles in default-components.scss
.default-grid           // Main content grid
.default-zebra-table    // Alternating row colors
.default-pagination     // Pagination component
.default-tab-list       // Tab styling
.default-accordion      // Accordion styling
.silva-toast            // Toast notification positioning
```

**Adding a new reusable style:**

```scss
// src/styles/default-components.scss
.default-input-wrapper {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;

  label {
    font-weight: 600;
    color: var(--#{vars.$bcgov-prefix}-text-01);
  }

  input {
    padding: 0.5rem;
    border: 1px solid var(--#{vars.$bcgov-prefix}-border-01);
    border-radius: 2px;
  }
}
```

**Use in components:**

```scss
// src/components/MyComponent/styles.scss
.my-component {
  @extend .default-input-wrapper;

  // Custom additions
  .special-input {
    border-color: colors.$bcgov-brand-blue;
  }
}
```

### Design Tokens & CSS Variables

Always use design tokens from `@bcgov-nr/nr-theme` instead of hardcoded colors or values.

**Imports in SCSS:**

```scss
@use '@bcgov-nr/nr-theme/design-tokens/variables.scss' as vars;
@use '@bcgov-nr/nr-theme/design-tokens/colors.scss' as colors;
@use '@carbon/type';
```

**Usage:**

```scss
.my-component {
  background: var(--#{vars.$bcgov-prefix}-layer-02);
  color: var(--#{vars.$bcgov-prefix}-text-01);
  padding: vars.$spacing-md;
  font-family: type.$body-01-font-family;
}
```

### Avoiding `!important`

Never use `!important` unless there is literally no other way.

❌ **Avoid:**
```scss
.button {
  color: blue !important;  // BAD
}
```

✅ **Use specificity instead:**
```scss
.button-container .button {
  color: blue;  // More specific, no !important needed
}
```

**Only Exception:** When overriding third-party Carbon component styles where specificity is genuinely impossible:

```scss
// Only if absolutely necessary
.my-component :global(.bx-btn) {
  background: var(--#{vars.$bcgov-prefix}-layer-02) !important;
}
```

### Carbon Component Style Overrides

Never override Carbon styles using class selectors like `.bx-button`. Instead, use CSS variables and prefix-aware class selectors.

❌ **Don't do this:**
```scss
.bx-button {
  background: blue;  // Affects all buttons globally
}
```

✅ **Do this:**
```scss
.my-component {
  // Use prefix-aware selectors for overrides
  .#{vars.$bcgov-prefix}--btn__primary {
    background: var(--#{vars.$bcgov-prefix}-layer-02);
  }
}
```

---

## Import Organization

Imports must follow a strict hierarchy with **blank lines between groups**:

1. **External packages** (React, third-party libraries)
2. **Project-level imports** (using `@/` alias)
3. **Folder-level imports** (relative paths within the same feature)
4. **Styles** (always last)

Below is an example, you should not include the comments.
```typescript
// External packages
import React, { useState } from 'react';
import { Location } from '@carbon/icons-react';
import { useQuery } from '@tanstack/react-query';

// Project-level imports (use @/ alias)
import OpeningsMap from '@/components/OpeningsMap';
import useBreakpoint from '@/hooks/UseBreakpoint';
import { getOpenings } from '@/services/API';
import type { Opening } from '@/types/Opening';

// Folder-level imports (same component)
import { useOpeningState } from './useOpeningState';
import { constants } from './constants';

// Styles (always last)
import './styles.scss';
```

### Import Path Rules

- **Prefer `@/` alias** over relative paths (e.g., `@/components/Card` not `../../components/Card`)
- **Exception in tests:** Relative paths may be necessary if `@/` doesn't resolve (e.g., in Jest configurations)
- **Never mix styles:** Don't import SCSS from multiple levels (only from current component/screen)
- **Remove unused imports:** Always clean up unused imports before committing

---

## TypeScript & Types

### Type Definitions

All components must have proper TypeScript types. Use the `/types` folder for shared type definitions.

```typescript
// src/types/Opening.ts
export interface Opening {
  id: string;
  name: string;
  status: 'active' | 'closed' | 'pending';
  area: number;
  location: {
    latitude: number;
    longitude: number;
  };
}

export interface OpeningFilters {
  status?: Opening['status'];
  minArea?: number;
}
```

**Use in components:**

```typescript
import type { Opening } from '@/types/Opening';

interface OpeningCardProps {
  opening: Opening;
  onSelect?: (opening: Opening) => void;
}

const OpeningCard: React.FC<OpeningCardProps> = ({ opening, onSelect }) => {
  return <div>{opening.name}</div>;
};
```

### Never Use `any`

❌ **Don't use `any` type:**
```typescript
const data: any = apiResponse;  // BAD
function process(param: any) {  // BAD
  return param.value;
}
```

✅ **Use proper types:**
```typescript
import type { ApiResponse } from '@/types/api';

const data: ApiResponse = apiResponse;
function process(param: OpeningData) {
  return param.value;
}
```

**If a type is genuinely unknown, use `unknown`:**
```typescript
function handleUnknown(value: unknown) {
  if (typeof value === 'string') {
    return value.toUpperCase();
  }
  throw new Error('Expected string');
}
```

### Component Props Types

Always define props interfaces:

```typescript
interface MyComponentProps {
  title: string;
  count?: number;
  onClose: () => void;
  items: Array<{ id: string; label: string }>;
}

const MyComponent: React.FC<MyComponentProps> = ({
  title,
  count = 0,
  onClose,
  items,
}) => {
  // Component body
};
```

---

## Carbon Components

Components are built using **IBM Carbon Design System**.

### BCGov Theme Integration

Silva uses **Carbon customized with the BCGov `nr-theme`** for consistent branding and design tokens. The theming is configured through:

- `src/styles/theme.scss` — BCGov theme variables and design tokens (colors, spacing, typography)
- `src/styles/components-overrides.scss` — Custom Carbon component style overrides to match BCGov design

**Do not modify these files directly unless updating the overall design system.** They are the single source of truth for theming. Individual components should use design tokens and CSS variables defined in these files, not hardcoded values.

### Checking Component Version

Check `package.json` for the installed Carbon version:

```bash
grep '@carbon' package.json
```

This tells you which Carbon components and features are available.

### Using Carbon Components

Import from `@carbon/react`:

```typescript
import {
  Button,
  Grid,
  Column,
  TextInput,
  Select,
  SelectItem,
} from '@carbon/react';

const MyForm = () => (
  <Grid>
    <Column lg={8} md={6} sm={4}>
      <TextInput labelText="Name" placeholder="Enter name" />
    </Column>
    <Column lg={8} md={6} sm={4}>
      <Button>Submit</Button>
    </Column>
  </Grid>
);
```

### Styling Carbon Components

Use CSS variables, not class selectors:

```scss
// src/components/MyComponent/styles.scss
.my-component {
  // Set Carbon component variables
  --cds-text-01: var(--#{vars.$bcgov-prefix}-text-01);
  --cds-interactive-01: var(--#{vars.$bcgov-prefix}-brand-blue);

  // Use prefix-aware selectors for overrides
  .#{vars.$bcgov-prefix}--label {
    font-weight: 600;
  }
}
```

**Reference Design Tokens:**
- Use `@bcgov-nr/nr-theme` tokens for spacing, colors, typography
- Check `src/styles/default-components.scss` for Carbon overrides already defined
- Refer to Carbon docs for available CSS variables

---

## API Interaction & TanStack Query

Use **TanStack Query** (React Query) for all API data fetching, caching, and state management.

### API Client

The API client is **auto-generated** from the backend's OpenAPI specification:

```typescript
// src/services/API.ts - DO NOT MODIFY (auto-generated)
export class API {
  static getOpenings(filters?: OpeningFilters) {
    return fetch('/api/openings', { /* ... */ });
  }

  static updateOpening(id: string, data: Partial<Opening>) {
    return fetch(`/api/openings/${id}`, { /* ... */ });
  }
}
```

To regenerate the API client after backend changes:

```bash
cd frontend
npm run generate:openapi
```

**This command is run by the developer after the backend is implemented and confirmed working.**

### Query Key Convention

Query keys must match the API endpoint structure:

| Endpoint | Query Key |
|----------|-----------|
| `GET /posts` | `['posts']` |
| `GET /posts/1` | `['posts', '1']` |
| `GET /posts?author=1` | `['posts', { author: 1 }]` |
| `GET /posts/2/comments` | `['posts', '2', 'comments']` |
| `GET /openings` | `['openings']` |
| `GET /openings/123` | `['openings', '123']` |
| `GET /openings/123/activities` | `['openings', '123', 'activities']` |

### Using TanStack Query

```typescript
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';

import { API } from '@/services/API';
import type { Opening } from '@/types/Opening';

const OpeningsList: React.FC = () => {
  const queryClient = useQueryClient();

  // Fetch data
  const { data, isLoading, error } = useQuery({
    queryKey: ['openings'],
    queryFn: () => API.getOpenings(),
  });

  // Mutate data
  const updateMutation = useMutation({
    mutationFn: (opening: Opening) =>
      API.updateOpening(opening.id, opening),
    onSuccess: (updatedOpening) => {
      // Invalidate cache
      queryClient.invalidateQueries({ queryKey: ['openings'] });
      queryClient.setQueryData(['openings', updatedOpening.id], updatedOpening);
    },
  });

  if (isLoading) return <div>Loading...</div>;
  if (error) return <div>Error: {error.message}</div>;

  return (
    <div>
      {data?.map((opening) => (
        <div key={opening.id}>{opening.name}</div>
      ))}
    </div>
  );
};
```

### Best Practices

- **Default caching behavior:** TanStack Query caches query results by default. Data is considered fresh until the `staleTime` expires. When you navigate back to a cached query, the stale data displays immediately without refetching.
- **Explicit refetch flags:** When you need to bypass the cache or refetch immediately, explicitly set refetch options:
  ```typescript
  const { refetch } = useQuery({
    queryKey: ['openings'],
    queryFn: () => API.getOpenings(),
  });

  // Manually trigger a refetch
  refetch();

  // Or refetch with options
  refetch({ cancelRefetch: false });
  ```
- **Prefer optimistic updates:** Update the cache immediately before the mutation completes for instant UI feedback. If the mutation fails, invalidate the query to refetch from the server. This provides the best user experience.
- **Use direct cache updates when possible:** If you know exactly what the new data should be, use `setQueryData()` instead of `invalidateQueries()` to avoid an extra refetch.
- **Fall back to invalidation:** Use `invalidateQueries()` when the mutation result is unpredictable or complex, or when you need the server's canonical state.
- **Error handling:** Provide user feedback for failed requests. If using optimistic updates, explain to the user that their change is being retried.
- **Loading states:** Show spinners/skeletons while fetching to indicate async operations.
- **Stale time:** Set appropriate stale times to balance freshness and API load.

---

## Services Folder

The `/src/services/` folder is **auto-generated** from the backend's OpenAPI specification.

### Rules

- **Never modify auto-generated files** (e.g., individual service files)
- **Only modify `API.ts`** to register new services in the API constructor if needed
- **Regenerate after backend changes:** Run `npm run generate:openapi`

### Example: Adding a New Service

If the backend adds a new controller, the OpenAPI generator creates a new service file. You may need to add it to the API constructor:

```typescript
// src/services/API.ts
import { OpeningsService } from './OpeningsService';  // Auto-generated
import { ActivitiesService } from './ActivitiesService';  // Auto-generated

export class API {
  static openings = new OpeningsService();
  static activities = new ActivitiesService();
}
```

Then use it in components:

```typescript
const { data } = useQuery({
  queryKey: ['activities'],
  queryFn: () => API.activities.getActivities(),
});
```

---

## Testing

Only **unit tests** are written by agents. Integration and E2E tests are handled separately.

### When to Write Tests

For medium to large features implemented section-by-section:
- **Write tests at logical completion points**, not after every small implementation step
- If the user requests implementation in stages (e.g., "implement the form first, then the validation"), hold off on tests until that stage is complete
- Once a feature section is complete and functional, write tests for it
- **Final test push:** Before requesting user review, ensure all sections have corresponding tests

### Coverage Requirements

- **Minimum coverage:** 85%
- **Target coverage:** >90% on new code
- **Focus:** Meaningful behavioral tests, not line coverage

### Test File Structure

Tests are organized in a mirrored structure under `src/__test__/` that matches the source layout:

```
src/
├── components/MyComponent/
│   ├── index.tsx
│   └── styles.scss
├── __test__/
│   ├── components/
│   │   └── MyComponent.test.tsx
│   ├── utils/
│   │   └── helpers.test.ts
│   ├── screens/
│   │   └── Dashboard.test.tsx
│   └── ...
```

Place test files in `src/__test__/` following the directory structure of the code being tested.

### Writing Unit Tests

Use **Jest** and **React Testing Library** for component tests:

```typescript
// src/components/Avatar/__tests__/Avatar.test.tsx
import { render, screen } from '@testing-library/react';

import Avatar from '../index';

describe('Avatar', () => {
  it('renders user initials', () => {
    render(<Avatar initials="JD" />);
    expect(screen.getByText('JD')).toBeInTheDocument();
  });

  it('applies size class correctly', () => {
    const { container } = render(<Avatar initials="JD" size="lg" />);
    expect(container.firstChild).toHaveClass('avatar-lg');
  });

  it('uses default size when not provided', () => {
    const { container } = render(<Avatar initials="JD" />);
    expect(container.firstChild).toHaveClass('avatar-md');
  });
});
```

### Testing Hooks

```typescript
import { renderHook, act } from '@testing-library/react';
import { useOpeningState } from '../useOpeningState';

describe('useOpeningState', () => {
  it('initializes with default state', () => {
    const { result } = renderHook(() => useOpeningState());
    expect(result.current.isOpen).toBe(false);
  });

  it('toggles open state', () => {
    const { result } = renderHook(() => useOpeningState());
    act(() => result.current.toggle());
    expect(result.current.isOpen).toBe(true);
  });
});
```

### Testing API Queries

```typescript
import { renderHook, waitFor } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { useOpeningsQuery } from '../useOpeningsQuery';

const createWrapper = () => {
  const queryClient = new QueryClient();
  return ({ children }) => (
    <QueryClientProvider client={queryClient}>
      {children}
    </QueryClientProvider>
  );
};

describe('useOpeningsQuery', () => {
  it('fetches openings successfully', async () => {
    const { result } = renderHook(() => useOpeningsQuery(), {
      wrapper: createWrapper(),
    });

    await waitFor(() => {
      expect(result.current.isSuccess).toBe(true);
    });

    expect(result.current.data).toHaveLength(2);
  });
});
```

### Avoiding Meaningless Tests

❌ **Don't test just to hit coverage:**
```typescript
// BAD: Testing implementation details
it('calls setState', () => {
  const setState = jest.fn();
  // ...
});
```

✅ **Test behavior:**
```typescript
// GOOD: Testing user-facing behavior
it('displays a message when opening is created', () => {
  render(<CreateOpeningForm />);
  fireEvent.click(screen.getByRole('button', { name: /submit/i }));
  expect(screen.getByText(/opening created/i)).toBeInTheDocument();
});
```

---

---

## Quick Reference Checklist

Use this when implementing a new feature:

- [ ] **Screen/Component:** Folder structure correct (PascalCase, index.tsx, styles.scss)
- [ ] **Styling:** No inline styles, use SCSS with hierarchy, no `!important`
- [ ] **Imports:** Organized hierarchy (packages → `@/` → folder → styles)
- [ ] **Types:** All props/state properly typed, no `any`
- [ ] **Carbon:** Using Carbon components, CSS variables for overrides
- [ ] **Responsive:** Uses Grid/Subgrid, tested at sm/md/lg breakpoints
- [ ] **API:** Using TanStack Query with correct query keys, API client up-to-date
- [ ] **Tests:** Unit tests written (at logical completion points), ≥85% coverage, meaningful assertions
- [ ] **Cleanup:** No unused imports, linter passing, code formatted

---

## Resources

- **IBM Carbon Design System:** https://carbondesignsystem.com/
- **TanStack Query Docs:** https://tanstack.com/query/latest
- **React Testing Library:** https://testing-library.com/react
- **TypeScript Handbook:** https://www.typescriptlang.org/docs/
- **Vite Docs:** https://vitejs.dev/

---

**Questions or need clarification? Check the main [AGENTS.md](../AGENTS.md) for full-stack context, or see [CONTRIBUTING.md](../CONTRIBUTING.md) for general guidelines.**
