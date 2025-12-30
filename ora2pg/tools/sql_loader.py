#!/usr/bin/env python3
"""
Load a combined SQL file into a Postgres database.

Usage:
  python3 sql_loader.py --sql-file combined.sql [--host HOST] [--port PORT] [--dbname DB] [--user USER]
  - If --password is omitted you'll be prompted (input is hidden, no characters echoed).
  - This script requires psql on PATH. If psql is not installed it will print install instructions and exit.

Options:
  --sql-file / -s   : Path to the SQL file (required).
  --host / -H       : Postgres host (default: localhost).
  --port / -P       : Postgres port (default: 5432).
  --dbname / -d     : Database name (default: postgres).
  --user / -U       : Database user (default: postgres).
  --password / -W   : Password (if omitted you'll be prompted; input is not echoed).
  --yes / -y        : Skip confirmation prompt.
"""
import argparse
import os
import shutil
import subprocess
import sys
import getpass

def run_psql(sql_file, host, port, dbname, user, password):
    psql = shutil.which("psql")
    if not psql:
        print("Error: psql not found on PATH.", file=sys.stderr)
        print("On macOS you can install the PostgreSQL client with Homebrew:", file=sys.stderr)
        print("  brew install libpq", file=sys.stderr)
        print("Then either add it to your PATH:", file=sys.stderr)
        print("  export PATH=\"$(brew --prefix libpq)/bin:$PATH\"", file=sys.stderr)
        print("Or install the full PostgreSQL package.", file=sys.stderr)
        sys.exit(1)
    env = os.environ.copy()
    if password:
        env["PGPASSWORD"] = password
    cmd = [
        psql,
        "-h", host,
        "-p", str(port),
        "-U", user,
        "-d", dbname,
        "-f", sql_file
    ]
    subprocess.run(cmd, env=env, check=True)

def main():
    parser = argparse.ArgumentParser(description="Load a combined SQL file into Postgres (psql only).")
    parser.add_argument("--sql-file", "-s", required=True, help="Path to combined SQL file")
    parser.add_argument("--host", "-H", default="localhost", help="Postgres host (default: localhost)")
    parser.add_argument("--port", "-P", default=5432, type=int, help="Postgres port (default: 5432)")
    parser.add_argument("--dbname", "-d", default="postgres", help="Database name (default: postgres)")
    parser.add_argument("--user", "-U", default="postgres", help="Database user (default: postgres)")
    parser.add_argument("--password", "-W", help="Database password (will prompt if omitted; input is hidden)")
    parser.add_argument("--yes", "-y", action="store_true", help="Skip confirmation prompt")
    args = parser.parse_args()

    sql_file = os.path.expanduser(args.sql_file)
    if not os.path.isfile(sql_file):
        print("Error: SQL file not found:", sql_file, file=sys.stderr)
        sys.exit(1)

    # If password not provided on CLI, prompt without echo (like sudo).
    password = args.password
    if password is None:
        try:
            pw_in = getpass.getpass(prompt="Postgres password (leave empty for none): ")
        except (KeyboardInterrupt, EOFError):
            print("\nCancelled.", file=sys.stderr)
            sys.exit(1)
        password = pw_in if pw_in != "" else None

    # Show the target DB info before asking for confirmation
    target_info = f"{args.user}@{args.host}:{args.port}/{args.dbname}"
    print(f"\nAbout to load SQL file:\n  {sql_file}\nInto database:\n  {target_info}\n")

    if not args.yes:
        try:
            resp = input("Proceed? [y/N]: ").strip().lower()
        except (KeyboardInterrupt, EOFError):
            print("\nCancelled.", file=sys.stderr)
            sys.exit(1)
        if resp not in ("y", "yes"):
            print("Cancelled.")
            sys.exit(0)

    try:
        print("Using psql to load the file...")
        run_psql(sql_file, args.host, args.port, args.dbname, args.user, password)
    except subprocess.CalledProcessError as e:
        print("psql exited with non-zero status:", e.returncode, file=sys.stderr)
        sys.exit(e.returncode)
    except Exception as e:
        print("Error loading SQL file:", str(e), file=sys.stderr)
        sys.exit(1)

    print("SQL file loaded successfully.")

if __name__ == "__main__":
    main()
