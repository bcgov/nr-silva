#!/usr/bin/env python3
import re
import json
import argparse
from pathlib import Path
from typing import List, Tuple, Optional, Dict, Any

CONFIG_NAME = "converter.config.json"

def convert_text(text: str) -> str:
    text = re.sub(r"TO_DATE\(\s*'([^']+)'\s*,\s*'[^']+'\s*\)", r"TIMESTAMP '\1'", text, flags=re.IGNORECASE)
    text = re.sub(r"\bTHE\.([A-Za-z_][A-Za-z0-9_]*)\b",
                  lambda m: f"silva.{m.group(1).lower()}",
                  text,
                  flags=re.IGNORECASE)
    insert_pattern = re.compile(
        r"(?i)(\bINSERT\s+INTO\s+)(\"[^\"]+\"|[A-Za-z_][A-Za-z0-9_]*(?:\.[A-Za-z_][A-Za-z0-9_]*)?)"
    )
    def insert_repl(m):
        prefix, tbl = m.group(1), m.group(2)
        if tbl.startswith('"') and tbl.endswith('"'):
            if '.' in tbl:
                return f"{prefix}{tbl}"
            return f'{prefix}"silva".{tbl}'
        else:
            if '.' in tbl:
                return f"{prefix}{tbl.lower()}"
            return f"{prefix}silva.{tbl.lower()}"
    text = insert_pattern.sub(insert_repl, text)
    text = re.sub(
        r"(?i)(INSERT\s+INTO\s+[A-Za-z0-9_.\"']+\s*)\(\s*([A-Za-z0-9_,\s]+?)\s*\)",
        lambda m: m.group(1) + "(" + ", ".join(c.strip().lower() for c in m.group(2).split(",")) + ")",
        text
    )
    text = re.sub(r"(TIMESTAMP\s*')(\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2})\.\d+(')", r"\1\2\3", text)
    text = re.sub(r"\b(silva)\.\s*silva\.", r"\1.", text, flags=re.IGNORECASE)
    text = re.sub(r"\bsilva\.silva\.", "silva.", text, flags=re.IGNORECASE)
    return text

def resolve_path(base: Optional[Path], p: Optional[str]) -> Optional[Path]:
    if not p:
        return None
    path = Path(p)
    if path.is_absolute():
        return path.resolve()
    if base:
        return (base / path).resolve()
    return path.resolve()

def load_config(script_dir: Path, config_path: Optional[Path]) -> Dict[str, Any]:
    cfg_file = config_path or (script_dir / CONFIG_NAME)
    if not cfg_file.exists():
        return {}
    return json.loads(cfg_file.read_text(encoding="utf-8"))

def build_mappings_from_config(cfg: Dict[str, Any], script_dir: Path) -> List[Tuple[Path, Path]]:
    mode = cfg.get("mode", "auto-detect")
    input_dir = resolve_path(script_dir, cfg.get("input_dir"))
    output_dir = resolve_path(script_dir, cfg.get("output_dir"))
    mappings: List[Tuple[Path, Path]] = []

    if mode == "auto-detect":
        if not input_dir or not output_dir:
            raise ValueError("auto-detect mode requires input_dir and output_dir in config")
        for src in sorted(input_dir.glob("*.sql")):
            mappings.append((src, output_dir / src.name))
        return mappings

    # manual mode
    map_entries = cfg.get("mappings", [])
    for entry in map_entries:
        if isinstance(entry, str):
            if ":" not in entry:
                raise ValueError(f"invalid mapping string: {entry}")
            src_s, dst_s = entry.split(":", 1)
        elif isinstance(entry, dict):
            src_s = entry.get("src")
            dst_s = entry.get("dst")
            if not src_s:
                raise ValueError(f"mapping entry missing src: {entry}")
        else:
            raise ValueError(f"unsupported mapping entry: {entry}")

        # resolve src relative to input_dir if provided, else script_dir
        src = resolve_path(input_dir or script_dir, src_s)
        # resolve dst: if provided, resolve relative to output_dir if present, else script_dir
        if dst_s:
            dst = resolve_path(output_dir or script_dir, dst_s)
        else:
            if not output_dir:
                raise ValueError("dst missing in mapping and output_dir not set")
            dst = output_dir / Path(src_s).name
            dst = dst.resolve()
        mappings.append((src, dst))
    return mappings

def convert_file(src: Path, dst: Path) -> None:
    if not src.exists():
        print(f"Skipping missing input: {src}")
        return
    text = src.read_text(encoding="utf-8")
    out = convert_text(text)
    dst.parent.mkdir(parents=True, exist_ok=True)
    dst.write_text(out, encoding="utf-8")
    print(f"Wrote: {dst}")

def parse_cli_args() -> argparse.Namespace:
    ap = argparse.ArgumentParser(description="Convert Oracle INSERT SQL -> Postgres with silva schema prefix.")
    ap.add_argument("--config", help="Path to JSON config file (defaults to converter.config.json next to script).")
    ap.add_argument("-m", "--map", action="append",
                    help="Mapping input.sql:output.sql (can be repeated). Overrides config.")
    ap.add_argument("--input-dir", help="Input directory for auto mode or prefix for manual map src paths.")
    ap.add_argument("--output-dir", help="Output directory for auto mode or prefix for manual map dst paths.")
    return ap.parse_args()

def main():
    script_dir = Path(__file__).parent.resolve()
    args = parse_cli_args()
    cfg = load_config(script_dir, Path(args.config) if args.config else None)

    # CLI overrides
    if args.input_dir:
        cfg["input_dir"] = args.input_dir
    if args.output_dir:
        cfg["output_dir"] = args.output_dir

    mappings: List[Tuple[Path, Path]] = []

    if args.map:
        for mapping in args.map:
            if ":" not in mapping:
                print(f"Ignoring invalid map (need input:output): {mapping}")
                continue
            src_s, dst_s = mapping.split(":", 1)
            src = resolve_path(script_dir, src_s)
            dst = resolve_path(script_dir, dst_s)
            mappings.append((src, dst))
    else:
        try:
            mappings = build_mappings_from_config(cfg, script_dir)
        except Exception as e:
            print(f"Config error: {e}")
            print("Provide mappings with -m or set up converter.config.json")
            return

    for src, dst in mappings:
        convert_file(src, dst)

if __name__ == "__main__":
    main()
