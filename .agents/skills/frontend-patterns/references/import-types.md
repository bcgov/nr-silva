# Import Organization & TypeScript Types

## Import Hierarchy

Imports must follow a strict hierarchy with **blank lines between groups**:

1. **External packages** (React, third-party libraries)
2. **Project-level imports** (using `@/` alias)
3. **Folder-level imports** (relative paths within the same feature)
4. **Styles** (always last)

### Example

```typescript
// External packages
import React, { useState } from 'react';
import { Location } from '@carbon/icons-react';
import { useQuery } from '@tanstack/react-query';

// Project-level imports (use @/ alias)
import OpeningsMap from '@/components/OpeningsMap';
import useBreakpoint from '@/hooks/UseBreakpoint';
import API from '@/services/API';
import type { Opening } from '@/types/Opening';

// Folder-level imports (same component)
import { useOpeningState } from './useOpeningState';
import { constants } from './constants';

// Styles (always last)
import './styles.scss';
```

---

## Import Path Rules

- **Prefer `@/` alias** over relative paths (e.g., `@/components/Card` not `../../components/Card`)
- **Exception in tests:** Relative paths may be necessary if `@/` doesn't resolve (e.g., in Jest configurations)
- **Never mix styles:** Don't import SCSS from multiple levels (only from current component/screen)
- **Remove unused imports:** Always clean up unused imports before committing

---

## Type Definitions

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

---

## Never Use `any`

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

### If Type Is Genuinely Unknown

Use `unknown`:

```typescript
function handleUnknown(value: unknown) {
  if (typeof value === 'string') {
    return value.toUpperCase();
  }
  throw new Error('Expected string');
}
```

---

## Component Props Types

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

## Key Patterns

- **Use `type` keyword** for type-only imports: `import type { Opening } from '@/types/Opening'`
- **Use `interface`** for component props and shared types
- **Use `type`** for union types, tuples, and type aliases
- **Never mix types and values** in the same import statement without using `type` keyword
