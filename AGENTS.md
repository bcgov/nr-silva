# Silva Agent Guidelines

## Overview

**Silva** is a modernized version of RESULTS, a forestry management system serving the BC forestry industry. It replaces a legacy system with a modern tech stack while maintaining domain-specific functionality for forest operations, tenures, openings, and related forestry workflows.

### Key Characteristics

- **Domain:** BC forestry industry (forestry terminology is domain-specific)
- **Purpose:** Manage forest operations, land tenure, opening data, stocking standards, and forestry activities
- **Scope:** Full-stack application with separate backend and frontend concerns
- **Legacy Context:** Modernization of RESULTS; some domain concepts carry forward

## Technology Stack

### Backend
- **Language:** Java
- **Framework:** Spring Boot
- **Build Tool:** Maven
- **API:** REST endpoints (OpenAPI/Swagger-documented)
- **Location:** `/backend`

### Frontend
- **Language:** TypeScript
- **Framework:** React (with Vite)
- **Build Tool:** npm/Node.js
- **UI Components:** IBM Carbon Design System
- **Styling:** SCSS with theme design tokens
- **Location:** `/frontend`

### Infrastructure & Deployment
- **Container Platform:** OpenShift (Red Hat container orchestration)
- **CI/CD:** GitHub Actions
- **Deployment Automation:** Scripts in `.github/actions`
- **Configuration:** YAML-based OpenShift deployment files

## Repository Structure

```
nr-silva/
├── backend/                  # Java Spring Boot application
│   ├── src/main/java/       # Backend source code
│   ├── src/test/            # Backend tests
│   ├── pom.xml              # Maven configuration
│   ├── Dockerfile           # Container image definition
│   ├── openshift.deploy.yml # OpenShift deployment config
│   └── AGENTS.md            # Backend-specific guidelines
├── frontend/                 # React + Vite application
│   ├── src/                 # Frontend source code
│   ├── public/              # Static assets
│   ├── package.json         # npm dependencies
│   ├── vite.config.ts       # Vite build configuration
│   ├── Dockerfile           # Container image definition
│   ├── openshift.deploy.yml # OpenShift deployment config
│   └── AGENTS.md            # Frontend-specific guidelines
├── common/                   # Shared OpenShift configs
│   ├── openshift.database.yml
│   ├── openshift.init.yml
│   └── openshift.sysdig.yml
├── nr-silva-etl/            # ETL pipeline for data migration
├── docs/                     # Documentation and API collections
├── .github/
│   └── actions/             # Reusable GitHub Actions workflows
├── README.md                # Main project documentation
├── CONTRIBUTING.md          # Contribution guidelines
└── AGENTS.md                # This file
```

## Getting Started

1. **Backend work:** Read [`backend/AGENTS.md`](backend/AGENTS.md) for Java/Spring Boot conventions
2. **Frontend work:** Read [`frontend/AGENTS.md`](frontend/AGENTS.md) for React/TypeScript conventions

---

## ⚠️ Critical: Never Assume

**This project has specific patterns and conventions. Do NOT make assumptions based on general knowledge or common practices elsewhere.**

### Rules for Agents

1. **Light exploration for technical patterns** — You CAN explore to verify:
   - Folder structure (list directories)
   - Naming conventions (check existing files)
   - Testing patterns (see how tests are organized)
   - Configuration file locations (check for example configs)
   - This takes seconds and prevents hours of wasted work

2. **Ask about domain/business details when uncertain** — Do NOT assume:
   - User/feature specifications
   - Data relationships and models
   - Business logic or workflows
   - Domain-specific terminology or concepts
   - Requirements or acceptance criteria
   - If you're making an assumption about any of these, ask the user first

3. **When in doubt, ask** — If exploring would take too long or it's beyond technical patterns:
   - Ask the user directly
   - Better to ask than waste time on wrong assumptions

### Examples

**Technical patterns — explore to verify:**

❌ **WRONG:** "I assume tests are colocated in `__tests__/` folders next to components (that's common in React projects)"
✅ **RIGHT:** Check `src/` structure first, verify actual location, then proceed

❌ **WRONG:** "I'll use the standard Carbon theme setup"
✅ **RIGHT:** Read `src/styles/theme.scss` to understand the actual BCGov integration

**Domain/business details — ask when uncertain:**

❌ **WRONG:** "I assume Openings can have multiple Tenures because that sounds logical"
✅ **RIGHT:** Ask the user about the data relationship before implementing

❌ **WRONG:** "I'll create a workflow where users first validate data, then submit, based on common practice"
✅ **RIGHT:** Ask the user what the actual business process requires

---

## Key Concepts for Agents

### Domain Knowledge
- **Silva:** The application name (forest/woodland-related)
- **RESULTS:** Legacy system being modernized
- **BC Forestry:** All domain terminology is specific to British Columbia forestry practices
- **Tenures:** Land use rights/licenses in forestry context
- **Openings:** Areas of forest where harvesting or silviculture operations occur
- **Stocking Standards:** Requirements for forest regeneration after harvesting

### Full-Stack Development Workflow

When implementing a feature that spans backend and frontend:

1. **Backend Implementation First**
   - Agent implements the backend feature (see `backend/AGENTS.md` for conventions)
   - API endpoint created or modified
   - Tests written and passing

2. **Wait for User Confirmation**
   - Agent pauses and requests user (developer) confirmation that the backend feature works correctly
   - User tests the endpoint(s) manually or via tests

3. **OpenAPI Regeneration (User Action)**
   - **User (developer) manually runs:** `npm run generate:openapi` in the frontend directory
   - This regenerates the TypeScript API client from the backend's OpenAPI specification
   - Only necessary if new endpoints created, or endpoints/DTOs were modified or deleted
   - Agent does NOT run this command; it's explicitly a developer action

4. **Frontend Implementation**
   - Agent implements the frontend feature (see `frontend/AGENTS.md` for conventions)
   - Uses the regenerated API client
   - Tests written and passing

## Common Tasks

### Adding a New Feature
- See `backend/AGENTS.md` → `frontend/AGENTS.md` → test workflow
- Document domain context if introducing new forestry concepts

### Fixing a Bug
- Identify which layer (backend/frontend) needs the fix
- Apply the fix following the appropriate AGENTS.md guidelines
- Add regression test

## Deployment Pipeline

Silva is deployed via GitHub Actions to OpenShift. The pipeline:
1. Builds Docker images for backend and frontend
2. Runs automated tests
3. Pushes images to container registry
4. Applies Kubernetes/OpenShift manifests
5. Triggers health checks and validation

See `.github/actions` for specific workflow definitions.

## Testing Strategy

- **Backend:** Unit tests, integration tests, API contract tests
- **Frontend:** Unit tests (Jest), component tests, end-to-end tests (Playwright)
- **Coverage Target:** Minimum 85%, target >90% on new code
- Both layers use meaningful tests focused on behavior, not line coverage

## Code Quality & Standards

- **Version Control:** Git with GitHub
- **Code Review:** Pull requests required for all changes
- **Linting:** ESLint (frontend), Checkstyle (backend)
- **Formatting:** Prettier (frontend), Google Java style guide (auto-applied on save in VSCode via eclipse-java-google-style.xml; agents can write in this style to begin with)
- **Security:** CVE scanning, secret detection via GitHub
- **Documentation:** Comments for complex logic, architecture diagrams for major changes

## API & Service Integration

- Backend exposes REST API with OpenAPI documentation
- Frontend consumes API via auto-generated TypeScript client (from OpenAPI spec)
- API client regenerated when backend endpoints change (user action: `npm run generate:openapi`)
- Use TanStack Query (React Query) on frontend for data fetching and caching

## Performance & Scalability

- Spring Boot backend designed for horizontal scaling
- React frontend optimized for performance (code splitting, lazy loading)
- Database with PostGIS for spatial queries (e.g., map-based operations)
- CDN and caching strategies configured via OpenShift

## Support & Documentation

- **Issues & Questions:** See GitHub Issues
- **Architecture Documentation:** Review docs/ folder and ADRs
- **API Documentation:** Swagger/OpenAPI endpoint exposed by backend

---

**For specific coding conventions and patterns, see:**
- Backend: [`backend/AGENTS.md`](backend/AGENTS.md)
- Frontend: [`frontend/AGENTS.md`](frontend/AGENTS.md)
