# API Interaction & TanStack Query

## API Client Architecture

The API client is generated from the backend's OpenAPI specification:

- **`src/services/OpenApi/**`** — Auto-generated service classes (DO NOT MODIFY). Regenerate after backend changes.
- **`src/services/API.ts`** — Hand-maintained wrapper. Configures and re-exports generated services for easy access throughout the app.

**Example API.ts structure:**

```typescript
// src/services/API.ts - hand-maintained wrapper
import { OpeningsService } from './OpenApi/services/OpeningsService';  // Auto-generated
import { ActivitiesService } from './OpenApi/services/ActivitiesService';  // Auto-generated

export class API {
  static OpeningsEndpointService = new OpeningsService();
  static ActivitiesEndpointService = new ActivitiesService();
}
```

To regenerate the API client after backend changes:

```bash
cd frontend
npm run generate:openapi
```

**This command is run by the developer after the backend is implemented and confirmed working.** The generated files in `src/services/OpenApi/**` are replaced, while `src/services/API.ts` (the wrapper) remains unchanged unless new services need to be registered.

---

## Query Key Convention

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

---

## Using TanStack Query

```typescript
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';

import API from '@/services/API';
import type { Opening } from '@/types/Opening';

const OpeningsList: React.FC = () => {
  const queryClient = useQueryClient();

  // Fetch data
  const { data, isLoading, error } = useQuery({
    queryKey: ['openings'],
    queryFn: () => API.OpeningsEndpointService.getOpenings(),
  });

  // Mutate data
  const updateMutation = useMutation({
    mutationFn: (opening: Opening) =>
      API.OpeningsEndpointService.updateOpening(opening.id, opening),
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

---

## Best Practices

### Default Caching Behavior

TanStack Query caches query results by default. Data is considered fresh until the `staleTime` expires. When you navigate back to a cached query, the stale data displays immediately without refetching.

### Explicit Refetch Flags

When you need to bypass the cache or refetch immediately, explicitly set refetch options:

```typescript
const { refetch } = useQuery({
  queryKey: ['openings'],
  queryFn: () => API.OpeningsEndpointService.getOpenings(),
});

// Manually trigger a refetch
refetch();

// Or refetch with options
refetch({ cancelRefetch: false });
```

### Prefer Optimistic Updates

Update the cache immediately before the mutation completes for instant UI feedback. If the mutation fails, invalidate the query to refetch from the server. This provides the best user experience.

### Use Direct Cache Updates When Possible

If you know exactly what the new data should be, use `setQueryData()` instead of `invalidateQueries()` to avoid an extra refetch.

### Fall Back to Invalidation

Use `invalidateQueries()` when the mutation result is unpredictable or complex, or when you need the server's canonical state.

### Error Handling

Provide user feedback for failed requests. If using optimistic updates, explain to the user that their change is being retried.

### Loading States

Show spinners/skeletons while fetching to indicate async operations.

### Stale Time

Set appropriate stale times to balance freshness and API load.

---

## Services Folder Rules

The `/src/services/` folder contains API client code:

- **`OpenApi/`** — Auto-generated service classes from the backend's OpenAPI spec (DO NOT MODIFY)
- **`API.ts`** — Hand-maintained wrapper that configures and re-exports generated services

### Rules

- **Never modify auto-generated files** in `OpenApi/` folder
- **Only modify `API.ts`** to register new services in the API wrapper class when the backend adds new controllers
- **Regenerate after backend changes:** Run `npm run generate:openapi`

### Example: Adding a New Service

If the backend adds a new controller, the OpenAPI generator creates a new service file in `OpenApi/`. Register it in `API.ts`:

```typescript
// src/services/API.ts
import { OpeningsService } from './OpenApi/services/OpeningsService';  // Auto-generated
import { ActivitiesService } from './OpenApi/services/ActivitiesService';  // Auto-generated

export class API {
  static OpeningsEndpointService = new OpeningsService();
  static ActivitiesEndpointService = new ActivitiesService();
}
```

Then use it in components:

```typescript
const { data } = useQuery({
  queryKey: ['activities'],
  queryFn: () => API.ActivitiesEndpointService.getActivities(),
});
```
