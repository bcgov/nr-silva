---
name: repo-audit
description: >
  Audit the repository for security vulnerabilities, outdated dependencies, code hygiene issues, and technical debt.
  USE FOR: running security scans (npm audit), finding outdated packages, checking linter/checkstyle configuration anomalies,
  identifying stale branches, and generating actionable maintenance reports or tech debt tickets.
  DO NOT USE FOR: reviewing individual PR diffs (use bot-pr-triage or pr-comment-resolver instead).
---

# Repository Health & Tech Debt Audit Skill

This skill guides the agent through performing a routine health, security, and maintenance audit of the Silva codebase.

---

## 1. Audit Checklist & Commands

### A. Security Vulnerabilities
1. **Frontend:**
   ```bash
   cd frontend
   npm audit --audit-level=moderate
   ```
   - Note any High or Critical vulnerabilities immediately.
   - For Moderate issues, check if an automated `npm audit fix` is available or if it's in `devDependencies` vs production runtime.

2. **Backend:**
   - Review `pom.xml` for known deprecated versions or check against current Spring Boot security advisories.
   - Run dependency analysis:
     ```bash
     cd backend
     ./mvnw dependency:analyze
     ```

---

### B. Outdated Dependencies
1. **Frontend:**
   ```bash
   cd frontend
   npm outdated --depth=0
   ```
   Focus on:
   - Core UI libraries (`@carbon/react`, `@carbon/icons-react`)
   - Core Framework & Build tools (`vite`, `react`, `react-router-dom`)
   - Heavy utility libraries (`ol`, `proj4`, `luxon`)

2. **Backend:**
   Use Maven's version plugin to identify outdated libraries and Spring Boot BOM properties:
   ```bash
   cd backend
   ./mvnw versions:display-dependency-updates
   ./mvnw versions:display-property-updates
   ```
   Focus on:
   - Spring Boot parent / starter updates
   - AWS SDK (`software.amazon.awssdk`)
   - Database drivers (PostgreSQL, Oracle JDBC) and Flyway

---

### C. Code Hygiene & Configuration Anomalies
1. **Frontend Linting & Tooling Configuration:**
   - Verify `.eslintrc.json` syntax (ensure no trailing commas invalidating JSON).
   - Check ESLint 9 compatibility (e.g. presence of `eslint.config.js` or `ESLINT_USE_FLAT_CONFIG=false`).
   - Check TypeScript build health:
     ```bash
     cd frontend
     npm run prebuild
     ```
2. **Backend Style & Build:**
   ```bash
   cd backend
   ./mvnw checkstyle:checkstyle
   ```

---

### D. Repository & Branch Hygiene
1. **Stale Remote Branches (>30 days old):**
   ```bash
   git for-each-ref --sort=-committerdate refs/remotes/origin --format='%(committerdate:short) %(authorname) %(refname:short)' | grep -v 'origin/main' | head -n 10
   ```
   Identify branches that have already merged or are abandoned to recommend cleanup.

2. **Test Suite Baseline:**
   - **Frontend:**
     ```bash
     cd frontend
     npm run test:unit
     ```
     Verify all 800+ unit tests continue to pass cleanly.
   - **Backend Dual-Database Tests (matches `analysis.yml` workflow):**
     Silva runs integration tests twice to validate both primary databases:
     ```bash
     cd backend
     # 1. Oracle Primary DB Run:
     ./mvnw clean test -Dserver.primary-db=oracle --no-transfer-progress checkstyle:checkstyle -P all-tests

     # 2. Postgres Primary DB Run:
     ./mvnw clean test -Dflyway-environment=dev -Dserver.primary-db=postgres --no-transfer-progress checkstyle:checkstyle -P all-tests
     ```

---

## 2. Generating the Audit Report

Populate the findings into [repo-audit-template.md](file://resources/repo-audit-template.md):
1. Categorize vulnerabilities by severity.
2. Highlight high-priority outdated packages.
3. List 2–4 concrete, actionable remediation tasks with estimated difficulty (Low/Medium/High).
