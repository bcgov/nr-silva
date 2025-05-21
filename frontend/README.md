# Frontend

## OpenAPI Type Generation

This project uses [openapi-typescript](https://github.com/drwpow/openapi-typescript) to generate TypeScript types from the backend OpenAPI schema.  
This helps ensure type safety and up-to-date API contracts in your frontend code.

### Generating OpenAPI Types

**Before you start:**  
Make sure your backend server is running and serving the OpenAPI schema at `http://localhost:8080/v3/api-docs`.

#### Generate Types

Run the following command from the frontend project root:

```sh
npm run generate:openapi
```

This will:

- Fetch the OpenAPI schema from your running backend
- Generate TypeScript types
- Save them to:  
  ```
  src/types/OpenApiTypes.d.ts
  ```

### Example Usage

After generating types, you can import and use them in your code.  
For example, to use the `CodeDescriptionDto` type:

```typescript
import { components } from "@/types/OpenApiTypes";

type CodeDescriptionDto = components["schemas"]["CodeDescriptionDto"];
```

You can now use `CodeDescriptionDto` for type-safe API responses or variables in your code.

---

**Tip:**  
Re-run `npm run generate:openapi` whenever your backend API changes to keep your types up to date.
