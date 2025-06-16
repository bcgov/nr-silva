#!/usr/bin/env python3
"""
File: combine_sql.py
Description:
    This script combines migration SQL files into one file.
    It expects that each SQL file's filename contains a version in the form "Vx.y.z"
    (for example, V1.0.0, V3.0.8, etc.). The files will be sorted by version (from lowest to highest)
    and then all file contents will be appended in order into a new combined SQL file.
    
Usage:
    python combine_sql.py <input_directory> [output_filename]
    
    If output_filename is not provided, the combined file is named "combined.sql".
"""

import sys
import os
import re

def extract_version(filename):
    """
    Extract the version number as a tuple from the filename.
    
    For example, for "V1.0.0__code_tables.sql" the version tuple is (1, 0, 0).
    If no version is found, returns a tuple that sorts at the end.
    """
    match = re.search(r'v(\d+)\.(\d+)\.(\d+)', filename, re.IGNORECASE)
    if match:
        return (int(match.group(1)), int(match.group(2)), int(match.group(3)))
    else:
        return (float('inf'),)

def combine_sql_files(input_dir, output_filename):
    """
    Combine all .sql files found in input_dir into one output file.
    The files are sorted by the extracted version number.
    """
    # Get all .sql files in the directory.
    files = [f for f in os.listdir(input_dir) if f.lower().endswith('.sql')]
    
    if not files:
        print("No SQL files found in", input_dir)
        sys.exit(1)
    
    # Sort the files by version.
    files.sort(key=extract_version)
    
    print("SQL files in sorted order:")
    for f in files:
        print(" -", f)
    
    output_path = os.path.join(input_dir, output_filename)
    
    with open(output_path, 'w', encoding='utf-8') as outf:
        for filename in files:
            filepath = os.path.join(input_dir, filename)
            outf.write(f"\n-- Begin file: {filename}\n")
            with open(filepath, 'r', encoding='utf-8') as inf:
                outf.write(inf.read())
            outf.write(f"\n-- End file: {filename}\n")
    
    print("Combined SQL file generated at:", output_path)

def main():
    if len(sys.argv) < 2:
        print("Usage: python combine_sql.py <input_directory> [output_filename]")
        sys.exit(1)
    
    input_dir = sys.argv[1]
    output_filename = sys.argv[2] if len(sys.argv) > 2 else "combined.sql"
    
    if not os.path.isdir(input_dir):
        print("Error: Input directory does not exist:", input_dir)
        sys.exit(1)
    
    combine_sql_files(input_dir, output_filename)

if __name__ == '__main__':
    main()
