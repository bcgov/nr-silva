# Styling Rules & Design System

## No Inline Styles

❌ **Never use inline styles:**
```typescript
// DON'T DO THIS
<div style={{ color: 'blue', fontSize: '16px' }}>Text</div>
```

✅ **Use SCSS classes instead:**
```typescript
<div className="my-component__text">Text</div>
```

---

## SCSS Hierarchy (Prevent Style Leaking)

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

---

## Reusable Styles: `default-components.scss`

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

---

## Design Tokens & CSS Variables

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

---

## Avoiding `!important`

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

---

## Carbon Component Style Overrides

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

## BCGov Theme Integration

Silva uses **Carbon customized with the BCGov `nr-theme`** for consistent branding and design tokens. The theming is configured through:

- `src/styles/theme.scss` — BCGov theme variables and design tokens (colors, spacing, typography)
- `src/styles/components-overrides.scss` — Custom Carbon component style overrides to match BCGov design

**Do not modify these files directly unless updating the overall design system.** They are the single source of truth for theming. Individual components should use design tokens and CSS variables defined in these files, not hardcoded values.

---

## Styling Carbon Components

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
