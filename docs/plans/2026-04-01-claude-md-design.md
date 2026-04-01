# CLAUDE.md Design Document
**Date:** 2026-04-01
**Status:** Approved

## Context
ChefApp-Server had no CLAUDE.md or developer documentation. The project is developed exclusively by Claude Code Opus 4.6 Max agents with human prompting. We needed a configuration file that enables near-fully-autonomous AI development.

## Decision
Approach 2: Ops + Vision Hybrid (~120 lines). Technical operations (commands, architecture, conventions) combined with product mission and strategic priorities. Uses `@` imports for progressive disclosure.

## Why This Approach
- Stays under the ~150 line sweet spot identified in research (Anthropic, HumanLayer)
- Includes product vision so agents can make strategic decisions, not just technical ones
- `@` imports allow deeper docs without bloating the main file
- Matches the unique use case: AI-only development team

## Structure
1. **Mission** — North Star for every decision (5 lines)
2. **Commands** — Build, test, run, deploy (10 lines)
3. **Architecture** — Conceptual map with key patterns (25 lines)
4. **Conventions** — MUST/SHOULD rules only (7 lines)
5. **Gotchas** — Things that waste debugging time (6 lines)
6. **Verification** — Self-check protocol (5 lines)
7. **Workflow** — Branching, commits, PRs (4 lines)
8. **External Integrations** — API pattern summaries (4 lines)
9. **Strategic Priorities** — Enterprise evolution roadmap (7 lines)

## Key Research Findings
- Official Anthropic guidance: keep concise, use emphasis for critical rules
- Under 150 lines prevents instruction degradation
- Verification section is highest-leverage for autonomous operation
- Gotchas section saves the most debugging time
- Progressive disclosure via `@` imports keeps main file scannable

## Next Steps
- Create `docs/architecture.md` (deep dive)
- Create `docs/api-patterns.md` (OpenAI, TikTok, Apple integration details)
- Create `docs/roadmap.md` (feature backlog with priorities)
