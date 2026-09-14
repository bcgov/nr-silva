#!/usr/bin/env node

/**
 * sonar-api.mjs
 *
 * Zero-dependency, zero-token CLI script to query public SonarCloud REST APIs
 * for nr-silva-backend and nr-silva-frontend.
 *
 * Usage:
 *   node sonar-api.mjs --status
 *   node sonar-api.mjs --pr <number>
 *   node sonar-api.mjs --blockers
 *   node sonar-api.mjs --rule <rule_id>
 *   node sonar-api.mjs --status --json
 */

import process from 'node:process';

const SONAR_HOST = 'https://sonarcloud.io';
const DEFAULT_PROJECTS = {
  backend: 'nr-silva-backend',
  frontend: 'nr-silva-frontend'
};

// Metric display labels and formatting
const METRIC_LABELS = {
  new_reliability_rating: 'Reliability Rating',
  new_security_rating: 'Security Rating',
  new_maintainability_rating: 'Maintainability Rating',
  new_coverage: 'Line Coverage',
  new_duplicated_lines_density: 'Duplicated Lines',
  new_security_hotspots_reviewed: 'Security Hotspots'
};

const RATING_MAP = {
  '1': 'A',
  '1.0': 'A',
  '2': 'B',
  '2.0': 'B',
  '3': 'C',
  '3.0': 'C',
  '4': 'D',
  '4.0': 'D',
  '5': 'E',
  '5.0': 'E'
};

function formatValue(metricKey, val) {
  if (val === undefined || val === null) return 'N/A';
  if (metricKey.endsWith('_rating')) {
    return `${RATING_MAP[val] || val} (${val})`;
  }
  if (metricKey.includes('coverage') || metricKey.includes('density') || metricKey.includes('reviewed')) {
    return `${val}%`;
  }
  return String(val);
}

function parseArgs(args) {
  const options = {
    status: false,
    pr: null,
    blockers: false,
    rule: null,
    project: null,
    json: false,
    help: false
  };

  for (let i = 0; i < args.length; i++) {
    const arg = args[i];
    if (arg === '--status' || arg === '-s') {
      options.status = true;
    } else if (arg === '--pr') {
      options.pr = args[++i];
    } else if (arg.startsWith('--pr=')) {
      options.pr = arg.split('=')[1];
    } else if (arg === '--blockers' || arg === '-b') {
      options.blockers = true;
    } else if (arg === '--rule' || arg === '-r') {
      options.rule = args[++i];
    } else if (arg.startsWith('--rule=')) {
      options.rule = arg.split('=')[1];
    } else if (arg === '--project' || arg === '-p') {
      options.project = args[++i];
    } else if (arg.startsWith('--project=')) {
      options.project = arg.split('=')[1];
    } else if (arg === '--json') {
      options.json = true;
    } else if (arg === '--help' || arg === '-h') {
      options.help = true;
    }
  }

  return options;
}

function printHelp() {
  console.log(`
SonarCloud Audit CLI (nr-silva)
Zero-token, zero-dependency audit tool for SonarCloud Quality Gates.

Usage:
  node sonar-api.mjs [options]

Commands & Options:
  -s, --status              Fetch current Quality Gate status for projects on main
      --pr <number>         Fetch Quality Gate and open issues for a specific Pull Request
  -b, --blockers            List all unresolved BLOCKER and CRITICAL issues across projects
  -r, --rule <rule_id>      List all unresolved issues for a specific rule (e.g. S5778, java:S2095)
  -p, --project <project>   Filter by project: 'backend', 'frontend', or full key
      --json                Output results in JSON format
  -h, --help                Show this help message

Examples:
  node sonar-api.mjs --status
  node sonar-api.mjs --status --json
  node sonar-api.mjs --pr 1437
  node sonar-api.mjs --blockers
  node sonar-api.mjs --rule java:S5778
  node sonar-api.mjs --project frontend --status
`);
}

function resolveProjects(projectArg) {
  if (!projectArg) {
    return Object.values(DEFAULT_PROJECTS);
  }
  const lower = projectArg.toLowerCase();
  if (lower === 'backend' || lower === 'nr-silva-backend') {
    return [DEFAULT_PROJECTS.backend];
  }
  if (lower === 'frontend' || lower === 'nr-silva-frontend') {
    return [DEFAULT_PROJECTS.frontend];
  }
  return [projectArg];
}

function cleanComponentPath(component, projectKey) {
  if (!component) return 'unknown';
  const prefix = `${projectKey}:`;
  let path = component;
  if (path.startsWith(prefix)) {
    path = path.slice(prefix.length);
  }
  if (projectKey === DEFAULT_PROJECTS.backend && !path.startsWith('backend/')) {
    path = `backend/${path}`;
  } else if (projectKey === DEFAULT_PROJECTS.frontend && !path.startsWith('frontend/')) {
    path = `frontend/${path}`;
  }
  return path;
}

async function fetchJson(url) {
  const res = await fetch(url, {
    headers: {
      'Accept': 'application/json',
      'User-Agent': 'nr-silva-sonar-cli'
    }
  });

  if (!res.ok) {
    if (res.status === 404) {
      return { error: 'Not Found (404)', status: 404 };
    }
    const text = await res.text();
    return { error: `HTTP ${res.status}: ${text}`, status: res.status };
  }

  return await res.json();
}

async function getProjectStatus(projectKey, pullRequest = null) {
  let url = `${SONAR_HOST}/api/qualitygates/project_status?projectKey=${encodeURIComponent(projectKey)}`;
  if (pullRequest) {
    url += `&pullRequest=${encodeURIComponent(pullRequest)}`;
  }

  const data = await fetchJson(url);
  if (data.error) {
    return { projectKey, pullRequest, error: data.error, status: data.status };
  }

  const ps = data.projectStatus || {};
  return {
    projectKey,
    pullRequest,
    gateStatus: ps.status, // OK, ERROR, WARN
    conditions: (ps.conditions || []).map((c) => ({
      metric: c.metricKey,
      metricLabel: METRIC_LABELS[c.metricKey] || c.metricKey,
      status: c.status, // OK, ERROR, WARN
      actual: c.actualValue,
      threshold: c.errorThreshold,
      comparator: c.comparator,
      displayActual: formatValue(c.metricKey, c.actualValue),
      displayThreshold: formatValue(c.metricKey, c.errorThreshold)
    })),
    period: ps.periods && ps.periods[0] ? ps.periods[0] : null
  };
}

async function getIssues({ projects, severities, rules, pullRequest }) {
  let url = `${SONAR_HOST}/api/issues/search?resolved=false&ps=100`;
  if (projects && projects.length > 0) {
    url += `&projects=${encodeURIComponent(projects.join(','))}`;
  }
  if (severities && severities.length > 0) {
    url += `&severities=${encodeURIComponent(severities.join(','))}`;
  }
  if (rules && rules.length > 0) {
    url += `&rules=${encodeURIComponent(rules.join(','))}`;
  }
  if (pullRequest) {
    url += `&pullRequest=${encodeURIComponent(pullRequest)}`;
  }

  const data = await fetchJson(url);
  if (data.error) {
    return { error: data.error, status: data.status, issues: [], total: 0 };
  }

  const issues = (data.issues || []).map((iss) => ({
    key: iss.key,
    rule: iss.rule,
    severity: iss.severity,
    type: iss.type,
    project: iss.project,
    file: cleanComponentPath(iss.component, iss.project),
    line: iss.line || (iss.textRange ? iss.textRange.startLine : null),
    message: iss.message,
    url: `${SONAR_HOST}/project/issues?id=${encodeURIComponent(iss.project)}&open=${encodeURIComponent(iss.key)}`
  }));

  return {
    total: data.total || issues.length,
    issues
  };
}

function renderStatusTerminal(statusResults) {
  console.log('\n================================================================');
  console.log('                 SONARCLOUD QUALITY GATE STATUS                 ');
  console.log('================================================================');

  for (const res of statusResults) {
    const isPR = Boolean(res.pullRequest);
    const title = isPR
      ? `${res.projectKey} (PR #${res.pullRequest})`
      : res.projectKey;

    console.log(`\n📦 Project: ${title}`);

    if (res.error) {
      if (res.status === 404) {
        console.log(`   ℹ️ No SonarCloud analysis found for this target.`);
      } else {
        console.log(`   ❌ Error fetching status: ${res.error}`);
      }
      continue;
    }

    const gateBadge = res.gateStatus === 'OK' ? '🟢 PASSED (OK)' : '🔴 FAILED (ERROR)';
    console.log(`   Gate Status: ${gateBadge}`);
    console.log(`   Dashboard:   ${SONAR_HOST}/summary/new_code?id=${res.projectKey}`);
    console.log('   Conditions:');

    if (!res.conditions || res.conditions.length === 0) {
      console.log('      (No conditions evaluated)');
      continue;
    }

    for (const c of res.conditions) {
      const icon = c.status === 'OK' ? '✅' : '❌';
      const compStr = c.comparator === 'LT' ? '>=' : '<=';
      console.log(`      ${icon} ${c.metricLabel.padEnd(24)} : Actual ${c.displayActual.padEnd(8)} (Threshold ${compStr} ${c.displayThreshold})`);
    }
  }
  console.log('\n================================================================\n');
}

function renderIssuesTerminal(title, issues, total) {
  console.log('\n================================================================');
  console.log(` ${title.toUpperCase()} (Total: ${total})`);
  console.log('================================================================');

  if (issues.length === 0) {
    console.log('\n   🎉 No unresolved issues found!\n');
    console.log('================================================================\n');
    return;
  }

  for (let i = 0; i < issues.length; i++) {
    const iss = issues[i];
    const loc = iss.line ? `${iss.file}:${iss.line}` : iss.file;
    const sevBadge = iss.severity === 'BLOCKER' ? '🚨 BLOCKER' : iss.severity === 'CRITICAL' ? '⚠️ CRITICAL' : `ℹ️ ${iss.severity}`;

    console.log(`\n[${i + 1}/${issues.length}] ${sevBadge} (${iss.type})`);
    console.log(`   Rule:    ${iss.rule}`);
    console.log(`   Location: ${loc}`);
    console.log(`   Message:  ${iss.message}`);
    console.log(`   URL:      ${iss.url}`);
  }

  if (total > issues.length) {
    console.log(`\n   ... and ${total - issues.length} more issues. (Showing first ${issues.length})`);
  }

  console.log('\n================================================================\n');
}

async function main() {
  const options = parseArgs(process.argv.slice(2));

  if (options.help || (!options.status && !options.pr && !options.blockers && !options.rule)) {
    printHelp();
    return;
  }

  const projects = resolveProjects(options.project);

  // 1. Status Check
  if (options.status) {
    const statusResults = [];
    for (const project of projects) {
      const res = await getProjectStatus(project);
      statusResults.push(res);
    }

    if (options.json) {
      console.log(JSON.stringify({ statusResults }, null, 2));
    } else {
      renderStatusTerminal(statusResults);
    }
  }

  // 2. PR Check
  if (options.pr) {
    const statusResults = [];
    for (const project of projects) {
      const res = await getProjectStatus(project, options.pr);
      statusResults.push(res);
    }

    const prIssuesResult = await getIssues({
      projects,
      pullRequest: options.pr
    });

    if (options.json) {
      console.log(JSON.stringify({ pr: options.pr, statusResults, issues: prIssuesResult }, null, 2));
    } else {
      renderStatusTerminal(statusResults);
      renderIssuesTerminal(`PR #${options.pr} Issues`, prIssuesResult.issues, prIssuesResult.total);
    }
  }

  // 3. Blockers Check
  if (options.blockers) {
    const blockersResult = await getIssues({
      projects,
      severities: ['BLOCKER', 'CRITICAL']
    });

    if (options.json) {
      console.log(JSON.stringify(blockersResult, null, 2));
    } else {
      renderIssuesTerminal('Blocker & Critical Issues', blockersResult.issues, blockersResult.total);
    }
  }

  // 4. Rule Check
  if (options.rule) {
    let ruleArg = options.rule.trim();
    // Expand shorthand rule (e.g. S5778 -> java:S5778,typescript:S5778) if no language prefix given
    const rulesToQuery = ruleArg.includes(':')
      ? [ruleArg]
      : [`java:${ruleArg}`, `typescript:${ruleArg}`, `javascript:${ruleArg}`];

    const ruleResult = await getIssues({
      projects,
      rules: rulesToQuery
    });

    if (options.json) {
      console.log(JSON.stringify({ rule: ruleArg, ...ruleResult }, null, 2));
    } else {
      renderIssuesTerminal(`Rule Issues (${ruleArg})`, ruleResult.issues, ruleResult.total);
    }
  }
}

main().catch((err) => {
  console.error('Fatal CLI Error:', err);
  process.exit(1);
});
