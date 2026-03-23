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
import { CodeDescriptionDto } from "@/types/OpenApiTypes";
```

You can now use `CodeDescriptionDto` for type-safe API responses or variables in your code.

---

**Tip:**  
Re-run `npm run generate:openapi` whenever your backend API changes to keep your types up to date.

## Deployment Model Configuration

The frontend supports multiple backend database configurations via the `VITE_DEPLOYMENT_MODEL` environment variable.

### Available Models

- **hybrid** (default): Indicates that the backend is using Oracle database as the primary datasource.
- **postgres**: Indicates that the backend is using PostgreSQL database as the primary datasource.

### Configuration

Set the deployment model in your `.env` file:

```
VITE_DEPLOYMENT_MODEL=hybrid
# or
VITE_DEPLOYMENT_MODEL=postgres
```

### Debug Component

A debug component (`DeploymentModelIndicator`) displays the current deployment model. This component:
- Shows "Hybrid Model" or "Postgres Model"
- Is **only visible in non-production environments** (dev, test, PR deployments)
- Is **hidden in production**

To integrate this component into the app layout, import it from `src/components/DeploymentModelIndicator`.

---
