# Component & Screen Patterns

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

## TypeScript Component Props

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

**Reference Design Tokens:**
- Use `@bcgov-nr/nr-theme` tokens for spacing, colors, typography
- Check `src/styles/default-components.scss` for Carbon overrides already defined
- Refer to Carbon docs for available CSS variables
- For detailed styling guidance, see `styling.md`
