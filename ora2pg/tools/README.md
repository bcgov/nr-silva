# SQL Converter (Oracle -> Postgres) — tools/converter.py

Purpose
- Small utility that converts Oracle INSERT-style migration SQL files into Postgres-friendly SQL:
  - TO_DATE(...) -> TIMESTAMP '...'
  - Replaces THE.<TABLE> -> silva.<table>
  - Ensures INSERT INTO statements are schema-qualified with `silva.` if not present
  - Normalizes column lists to lowercase and trims fractional seconds in TIMESTAMP
- Supports processing multiple files via a JSON config or CLI mappings.

Where to place files
- converter.py and converter.config.json (or converter.config.json.example) live together in:
  ora2pg/tools/
- Input/output SQL files live in your repo (example paths in samples point to backend/src/test/resources/...).

Config modes
- auto-detect
  - Convert every `*.sql` file from `input_dir` into `output_dir` preserving filenames.
  - Required keys: `mode: "auto-detect"`, `input_dir`, `output_dir`.
- manual
  - Explicit list of file mappings. Each mapping may be a string `"src:dst"` or object `{"src":"...", "dst":"..."}`.
  - If `dst` omitted and `output_dir` provided, `dst` will be `output_dir/<basename(src)>`.

Path resolution
- Relative paths in config are resolved relative to the `ora2pg/tools` directory (script location).
- CLI options `--input-dir`, `--output-dir`, and repeated `-m input:output` override config.

Examples

1) Run with config file (default name next to script):
python3 converter.py

2) Run with explicit mapping(s):
python3 converter.py -m migration/oracle/V2.0.0__oracle_code_tables.sql:migration/postgres/V999.0.1_test_data_code_tables.sql

3) Directory-mode override:
python3 converter.py --input-dir migration/oracle --output-dir migration/postgres

Notes
- Script only touches INSERT INTO patterns and simple date/timestamp constructs — test outputs before applying to production DB.
- If you want the script to use your local converter.config.json, place it next to converter.py. Otherwise copy/rename the example to create a per-user config.
