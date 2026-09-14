# Silva SonarCloud Remediation Patterns

This guide provides concrete, approved code recipes for remediating the most common SonarCloud rules in the **Silva** codebase.

---

## 1. `java:S2095`: Resources Should Be Closed (BLOCKER BUG)

### Problem
SonarCloud flags any unclosed resource (`Socket`, `InputStream`, `OutputStream`, `Connection`, `Statement`) as a **Blocker Bug**. In Silva, this downgrades the project Reliability Rating from **A** to **E**.

### Generic Scenario: Sockets, Streams, and Client Connections
When establishing network sockets, stream readers, or I/O resources where connection setup might throw:

#### ❌ Non-Compliant Pattern
```java
private Socket openSocket() throws IOException {
  Socket socket = new Socket();
  socket.connect(new InetSocketAddress(host, port), timeout);
  return socket;
}
```
*If `socket.connect()` times out or throws an `IOException`, the newly allocated `socket` descriptor is never closed.*

#### ✅ Compliant Pattern (Try-Catch with Safe Cleanup)
```java
private Socket openSocket() throws IOException {
  Socket socket = new Socket();
  try {
    socket.connect(new InetSocketAddress(host, port), timeout);
    return socket;
  } catch (Exception e) {
    try {
      socket.close();
    } catch (IOException closeEx) {
      log.warn("Failed to close socket after connect failure", closeEx);
    }
    throw e;
  }
}
```
Or use try-with-resources whenever the resource lifecycle is localized to a method:
```java
try (Socket socket = openSocket();
     OutputStream out = socket.getOutputStream();
     InputStream in = socket.getInputStream()) {
  // perform I/O operations
}
```

---

## 2. `java:S2699` & `typescript:S2699`: Tests Should Include Assertions (BLOCKER CODE SMELL)

### Problem
Tests that simply execute code or mock verification without an assertion library call do not verify outcomes and can result in false positives.

### A. Backend (JUnit 5)

#### ❌ Non-Compliant Pattern
```java
@Test
void testProcessOpeningSuccess() {
  openingService.processOpening(sampleDto);
  // Missing assertion!
}
```

#### ✅ Compliant Pattern
Use `assertNotNull`, `assertEquals`, or explicitly assert no exceptions are thrown:
```java
@Test
void testProcessOpeningSuccess() {
  Assertions.assertDoesNotThrow(() -> openingService.processOpening(sampleDto));
}

// Or asserting state / results:
@Test
void testProcessOpeningSuccess() {
  OpeningResultDto result = openingService.processOpening(sampleDto);
  Assertions.assertNotNull(result);
  Assertions.assertEquals("COMPLETED", result.getStatus());
}
```

### B. Frontend (Jest / Vitest / Playwright)

#### ❌ Non-Compliant Pattern (Playwright E2E)
```typescript
test('should navigate to dashboard', async ({ page }) => {
  await page.goto('/dashboard');
  await page.click('#submit-button');
  // Missing assertion!
});
```

#### ✅ Compliant Pattern
```typescript
test('should navigate to dashboard', async ({ page }) => {
  await page.goto('/dashboard');
  await page.click('#submit-button');
  await expect(page.locator('h1')).toHaveText('Dashboard Overview');
});
```

---

## 3. `java:S5778`: One Assertion per `assertThrows` Lambda (CODE SMELL)

### Problem
In JUnit 5, putting arrangement, mock setup, or auxiliary methods inside the `assertThrows()` lambda is dangerous: if an auxiliary mock or setup statement throws an unexpected exception, the test passes erroneously. SonarCloud flags any lambda that contains more than a single method invocation.

#### ❌ Non-Compliant Pattern
```java
@Test
void testGetOpeningDetailsNotFound() {
  Assertions.assertThrows(NotFoundException.class, () -> {
    when(openingRepository.findById(123L)).thenReturn(Optional.empty()); // Arrange inside lambda!
    openingService.getOpeningDetails(123L);
  });
}
```

#### ✅ Compliant Pattern
Move all arrangement, stubbing, and parameter setup **outside** the lambda. Keep only the single method invocation being tested inside:
```java
@Test
void testGetOpeningDetailsNotFound() {
  // Arrange outside
  when(openingRepository.findById(123L)).thenReturn(Optional.empty());

  // Act & Assert single call
  Assertions.assertThrows(NotFoundException.class, () -> openingService.getOpeningDetails(123L));
}
```

---

## 4. `typescript:S3358`: Avoid Nested Ternary Operators (CODE SMELL)

### Problem
Nested ternaries in React JSX make conditional rendering difficult to read and debug, contributing to high cognitive complexity.

#### ❌ Non-Compliant Pattern
```tsx
return (
  <div className="status-container">
    {isLoading ? (
      <Loading />
    ) : isError ? (
      <ErrorMessage error={error} />
    ) : data && data.length > 0 ? (
      <DataList items={data} />
    ) : (
      <EmptyState />
    )}
  </div>
);
```

#### ✅ Compliant Pattern: Early Returns or Helper Render Functions
```tsx
const renderBody = () => {
  if (isLoading) return <Loading />;
  if (isError) return <ErrorMessage error={error} />;
  if (!data || data.length === 0) return <EmptyState />;
  return <DataList items={data} />;
};

return (
  <div className="status-container">
    {renderBody()}
  </div>
);
```

---

## 5. `typescript:S3776` & `java:S3776`: Reduce Cognitive Complexity (CRITICAL CODE SMELL)

### Problem
Functions with high cognitive complexity (threshold $> 15$) contain deeply nested conditionals, loops, and branching logic (common in complex validation, spatial processing, and multi-step UI views).

#### ❌ Non-Compliant Pattern
```typescript
function processFeatures(features: Feature[], filterType: string) {
  const results = [];
  for (const f of features) {
    if (f.properties) {
      if (filterType === 'ALL' || f.properties.type === filterType) {
        if (f.geometry && f.geometry.coordinates) {
          // deep nesting...
        }
      }
    }
  }
  return results;
}
```

#### ✅ Compliant Pattern: Guard Clauses & Pure Helpers
```typescript
function isValidFeature(f: Feature, filterType: string): boolean {
  if (!f.properties || !f.geometry?.coordinates) return false;
  return filterType === 'ALL' || f.properties.type === filterType;
}

function processFeatures(features: Feature[], filterType: string) {
  return features.filter((f) => isValidFeature(f, filterType)).map(transformFeature);
}
```
