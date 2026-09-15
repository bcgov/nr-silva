---
name: legacy-results-exploration
description: Locate and trace legacy RESULTS screen behaviour into its Java and Oracle implementation. Use for RESULTS screen numbers, actions, JSPs, forms, stored procedures, or legacy business-rule investigations; not for ordinary Silva implementation work.
---

# Legacy RESULTS Exploration

Use this skill to discover the implementation behind a legacy RESULTS screen quickly and report only source-supported conclusions.

## Prerequisites

Resolve both local legacy repositories before searching:

1. Use `<workspace>/nr-results` and `<workspace>/nr-mof-db` when both exist.
2. If either is absent, use explicit paths supplied by the developer. A supplied parent directory is valid when it contains both repositories.
3. If no usable explicit path was supplied, stop and ask the developer to provide the path to each missing repository, or one parent path containing both.
4. Verify the RESULTS web source and `nr-mof-db/scripts/THE` exist. Do not scan arbitrary filesystem locations or assume the repositories are checked in to the Silva project.

These local repositories are reference material. Do not modify them or add them to version control.

## Resource locator map

Start with these known locations instead of broadly searching the repositories:

| Resource | Location |
| --- | --- |
| Struts route and action/form mapping | `nr-results/source/ear/src/main/webapp/WEB-INF/struts-config.xml` |
| Screen JSPs | `nr-results/source/ear/src/main/webapp/` |
| Actions | `nr-results/source/ear/src/main/java/ca/bc/gov/mof/results/web/action/` |
| Forms | `nr-results/source/ear/src/main/java/ca/bc/gov/mof/results/web/form/` |
| Data beans | `nr-results/source/ear/src/main/java/ca/bc/gov/mof/results/data/beans/` |
| Package definitions | `nr-results/source/ear/src/main/java/ca/bc/gov/mof/results/data/pkgdefinitions/` |
| JDBC adaptors | `nr-results/source/ear/src/main/java/ca/bc/gov/mof/results/data/adaptors/` |
| Bean-to-package mapping | `nr-results/source/ear/src/main/webapp/WEB-INF/packagemap.xml` |
| Oracle package specifications | `nr-mof-db/scripts/THE/PACKAGES/` |
| Oracle package bodies | `nr-mof-db/scripts/THE/PACKAGE_BODIES/` |
| Standalone procedures and functions | `nr-mof-db/scripts/THE/PROCEDURES/`, `nr-mof-db/scripts/THE/FUNCTIONS/` |
| Dependent SQL objects | `nr-mof-db/scripts/THE/TABLES/`, `VIEWS/`, `TRIGGERS/`, `TYPES/` |

A name referenced by SQL (e.g. in a `FROM`/`JOIN` clause) is not necessarily a base table. Check `nr-mof-db/scripts/THE/VIEWS/` for a matching `R__*_<NAME>.sql` file before concluding the name has no definition in the repo — several legacy "tables" (e.g. `BEC_CODE_TABLE`, `BEC_SITE_SERIES`) are actually views over differently-named base tables (e.g. `BIOGEOCLIMATIC_CATALOGUE`, `SITE_SERIES_CATALOGUE`). Read the view's `SELECT`/`FROM` to find the real underlying table(s) before reporting columns or DDL.

## Exploration flow

Trace the actual path in this order:

`screen/JSP -> Struts action -> form and bean -> packagemap entry -> Java package definition -> Oracle routine -> referenced database objects`

1. Given a screen number, first search `struts-config.xml` and the JSP directory for `results<screen>` plus likely Java names such as `Results<screen>`.
2. From the action, identify the invoked operation and the bean it submits or retrieves. Read only the relevant action methods, form fields, and JSP controls.
3. Search `packagemap.xml` by the fully qualified bean class. Treat its `package-class` and `procedure` values as the source of truth for the Java-to-Oracle call.
4. Read that Java package definition to confirm the package name and parameter/routine descriptor.
5. Locate the Oracle package specification in `PACKAGES` and its implementation in `PACKAGE_BODIES`. If the mapping resolves to a standalone routine, search `PROCEDURES` or `FUNCTIONS` instead.
6. For a dispatcher such as `MAINLINE`, trace the action value to the internal procedure it invokes. Then inspect only the referenced tables, views, triggers, types, or helper routines necessary to support the requested conclusion.
7. If a referenced object has no file under `TABLES/`, search `VIEWS/` for it before concluding it is undocumented. If it is a view, follow its `FROM`/`JOIN` clauses to the real base table(s) and read those definitions.

Use `rg` against these locations first and narrow reads with line ranges. Never infer an Oracle package or procedure solely from a screen number, filename, or convention.

## Worked example: Results 305 tenure deletion

This is an example of the tracing method, not a substitute for re-reading current source for a new investigation.

1. `Results305MultiTenureAction.handleDelete` sets the submitted bean's `P_ACTION` to `REMOVE`.
2. The `Results305MultiTenureBean` entry in `WEB-INF/packagemap.xml` resolves to package definition `Results305MultiTenure` and procedure `MAINLINE`.
3. The Java definition identifies package `RESULTS_305_MULTI_TENURE`; its `MAINLINE` descriptor carries `P_ACTION`.
4. Locate `RESULTS_305_MULTI_TENURE` in the Oracle package specification and body. Its `MAINLINE` dispatches `P_ACTION = 'REMOVE'` to `REMOVE`.
5. Read `REMOVE` for the deletion rules and its changes to `CUT_BLOCK_OPEN_ADMIN`; only then state what RESULTS does when a tenure is removed.

The important lesson: the form or action alone establishes the UI operation, but the package map, package definition, and Oracle body establish the database behaviour.

## Report format

For each investigation, report:

- the screen or entry point and complete traced call chain;
- exact relevant paths, including the resolved Oracle package and routine;
- referenced database objects that affect the requested behaviour; and
- concise business-rule findings, each tied to the code or SQL that supports it.

Clearly distinguish verified behaviour from an inference. If the trace is incomplete, report what is unresolved rather than filling gaps with assumptions.
