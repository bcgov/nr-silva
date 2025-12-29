#!/usr/bin/env python3
"""
File: combine_sql.py
Description:
    This script combines migration SQL files into one file.
    It expects that each SQL file's filename contains a version in the form "Vx.y.z"
    (for example, V1.0.0, V3.0.8, etc.). The files will be sorted by version (from lowest to highest)
    and then all file contents will be appended in order into a new combined SQL file.
    
Usage:
    python combine_sql.py --input-dir <input_directory> [--output-dir <output_directory>] [--output <output_filename>]
    
    If --output is not provided, the combined file is named "combined.sql".
    If --output-dir is not provided, the output file is written into the input directory.
"""

import sys
import os
import re
import argparse

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

def combine_sql_files(input_dir, output_dir, output_filename):
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
    
    # Ensure output directory exists.
    os.makedirs(output_dir, exist_ok=True)
    output_path = os.path.join(output_dir, output_filename)
    
    with open(output_path, 'w', encoding='utf-8') as outf:
        for filename in files:
            filepath = os.path.join(input_dir, filename)
            outf.write(f"\n-- Begin file: {filename}\n")
            with open(filepath, 'r', encoding='utf-8') as inf:
                outf.write(inf.read())
            outf.write(f"\n-- End file: {filename}\n")
    
    print("Combined SQL file generated at:", output_path)

def main():
    parser = argparse.ArgumentParser(description="Combine versioned SQL files into a single file.")
    parser.add_argument('--input-dir', '-i', required=True, help='Directory containing .sql files to combine')
    parser.add_argument('--output-dir', '-d', help='Directory to write the combined file (defaults to input dir)')
    parser.add_argument('--output', '-o', default='combined.sql', help='Output filename (default: combined.sql)')
    args = parser.parse_args()
    
    input_dir = args.input_dir
    output_dir = args.output_dir if args.output_dir else input_dir
    output_filename = args.output
    
    if not os.path.isdir(input_dir):
        print("Error: Input directory does not exist:", input_dir)
        sys.exit(1)
    
    combine_sql_files(input_dir, output_dir, output_filename)

if __name__ == '__main__':
    main()
