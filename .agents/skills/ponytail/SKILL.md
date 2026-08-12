---
name: ponytail
description: >
  YAGNI + lazy-dev code ladder. Cuts code 54% on average (measured) by reaching for the rung
  that holds before writing. Supports intensity levels: lite, full (default), ultra.
  Always-on guidance; reaches for stdlib, native, reuse before new code.
---

# Ponytail: YAGNI + Lazy-Dev Ladder

Before writing code, reach for the rung that holds. Lazy about solution, never about reading.

## The Ladder

Stop at the first rung that holds:

1. **Does this need to exist?** → no: skip it (YAGNI)
2. **Already in this codebase?** → reuse it, don't rewrite
3. **Stdlib does it?** → use it
4. **Native platform feature?** → use it
5. **Installed dependency?** → use it
6. **One line?** → one line
7. **Only then:** the minimum that works

The ladder runs *after* understanding the problem, not instead of it. Read the code the change touches, trace the real flow before picking a rung. Lazy about the solution, never about reading.

## Safety First

Lazy, not negligent: trust-boundary validation, data-loss handling, security, and accessibility are **never** on the chopping block.

## Examples

**Date picker**
- Without ponytail: Install flatpickr, write wrapper component, add stylesheet, discuss timezones. 100+ LOC.
- With ponytail: `<input type="date">` — 1 line, browser handles timezones, free.

**Color picker**
- Without ponytail: Material-UI or react-color-box, bundle bloat, npm deps. 287 LOC.
- With ponytail: `<input type="color">` — 1 line, native browser support.

## Intensity Levels

| Level | What change |
|-------|------------|
| **lite** | Light YAGNI guidance. Reuse + stdlib emphasized. Pragmatic on small deps. |
| **full** | Full ladder (all 7 rungs active). Balanced: native first, but deps OK if clear ROI. |
| **ultra** | Ultra-strict: refuse bloat, reach for native/stdlib first. Purist minimalism. |

## Measured Impact (Real Agent Sessions)

Benchmark: 12 feature tasks, same agent with/without ponytail, Haiku 4.5, n=4.

- **LOC:** −54% (range 0–94%, biggest gains on over-build traps like pickers)
- **Tokens:** −22%
- **Cost:** −20%
- **Time:** −27%
- **Safety:** 100% (baseline, caveman, ponytail all pass; naive one-liner prompt drops to 95%)

Full method: [ponytail benchmarks](https://github.com/DietrichGebert/ponytail/blob/main/benchmarks/results/2026-06-18-agentic.md)
