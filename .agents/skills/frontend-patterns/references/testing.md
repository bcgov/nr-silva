# Testing Guide

## When to Write Tests

For medium to large features implemented section-by-section:
- **Write tests at logical completion points**, not after every small implementation step
- If the user requests implementation in stages (e.g., "implement the form first, then the validation"), hold off on tests until that stage is complete
- Once a feature section is complete and functional, write tests for it
- **Final test push:** Before requesting user review, ensure all sections have corresponding tests

---

## Coverage Requirements

- **Minimum coverage:** 85%
- **Target coverage:** >90% on new code
- **Focus:** Meaningful behavioral tests, not line coverage

---

## Test File Structure

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

---

## Writing Unit Tests

Use **Jest** and **React Testing Library** for component tests:

```typescript
// src/__test__/components/Avatar.test.tsx
import { render, screen } from '@testing-library/react';

import Avatar from '@/components/Avatar';

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

---

## Testing Hooks

```typescript
import { renderHook, act } from '@testing-library/react';
import { useOpeningState } from '@/hooks/useOpeningState';

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

---

## Testing API Queries

```typescript
import { renderHook, waitFor } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { useOpeningsQuery } from '@/hooks/useOpeningsQuery';

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

---

## Avoiding Meaningless Tests

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

## Key Testing Principles

1. **Test behavioral expectations**, not implementation details
2. **Verify user-facing outcomes** (rendered text, state changes, error messages)
3. **Avoid testing framework internals** (React internals, component lifecycle)
4. **Use semantic queries** (getByRole, getByLabelText) instead of getByTestId when possible
5. **Test accessibility** by using queries that real users would rely on
