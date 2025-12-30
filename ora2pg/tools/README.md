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

## Combiner (combine_sql.py / combiner.py)

Purpose
- Combine multiple versioned migration SQL files into a single combined SQL file.
- Files are expected to include a version token like `V1.0.0` (case-insensitive) in the filename; files are sorted by version before concatenation.
- Each file's contents are wrapped with `-- Begin file: <name>` / `-- End file: <name>` markers in the output.

Usage
- Run the combiner script with flags:
  - `--input-dir / -i` : Directory containing .sql files to combine (required)
  - `--output-dir / -d` : Directory to write the combined file (defaults to input dir)
  - `--output / -o` : Output filename (default: combined.sql)

Examples
- Combine files in-place (output in same directory):
  `python3 combiner.py --input-dir migration/postgres`

- Specify output directory and filename:
  `python3 combiner.py -i migration/postgres -d migration/combined -o all_in_one.sql`

Notes
- The script finds files ending with `.sql` (case-insensitive).
- Files without a parsable version (Vx.y.z) are sorted last.
- Output directory will be created if it doesn't exist.
- Review the combined file before applying it to your database.

## SQL Loader (sql_loader.py)

Purpose
- Load a combined SQL file (produced by the combiner) into a PostgreSQL database using the `psql` client.
- The script requires `psql` on PATH; if not present it prints install hints and exits.
- Password prompt hides input (no characters echoed).

Defaults
- host: localhost
- port: 5432
- dbname: postgres
- user: postgres

Usage
- Basic:
  `python3 sql_loader.py --sql-file /path/to/combined.sql`

- With explicit connection options:
  `python3 sql_loader.py --sql-file combined.sql --host db.host --port 5432 --dbname mydb --user myuser`
  - If `--password` omitted the script prompts (input hidden).

Options
- --sql-file / -s   : Path to the SQL file to load (required).
- --host / -H       : Postgres host (default: localhost).
- --port / -P       : Postgres port (default: 5432).
- --dbname / -d     : Database name (default: postgres).
- --user / -U       : Database user (default: postgres).
- --password / -W   : Password (if omitted you'll be prompted; input is not echoed).
- --yes / -y        : Skip confirmation prompt.

Behavior
- The script shows the SQL file path and the target DB info (user@host:port/dbname) before asking for confirmation.
- Uses the `PGPASSWORD` environment variable to pass the password to `psql` if provided.
- If `psql` is not found, the script prints platform hints (macOS Homebrew example) and exits with an error.

Examples
- Prompt for password and confirm before running:
  `python3 sql_loader.py -s combined.sql -U postgres`

- Non-interactive (skip confirmation; still hidden password prompt unless provided):
  `python3 sql_loader.py -s combined.sql -U postgres -W secret -y`

Notes / install hints
- On macOS you can install the PostgreSQL client with Homebrew:
  `brew install libpq`
  then add it to PATH:
  `export PATH="$(brew --prefix libpq)/bin:$PATH"`
- Always review the combined SQL before loading into a production database.
