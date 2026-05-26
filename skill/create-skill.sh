#!/bin/bash
# ============================================================================
# orion-kit Claude Code Skill Generator
#
# creates JavaDoc, converts to Markdown, deploys the orion-kit skill
# to ~/.claude/skills/, and bundles CodeGraph DB.
#
# Usage:
#   ./create-skill.sh              -- build javadoc + convert + deploy + codegraph
#   ./create-skill.sh --skip-docs  -- convert + deploy + codegraph, skip javadoc build
# ============================================================================

set -e

SKILL_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_DIR="$(dirname "$SKILL_DIR")"
SKILL_NAME="orion-kit"
SKILL_SRC="$SKILL_DIR"

# Check if mcc directory exists in parent of project root
PARENT_DIR="$(dirname "$PROJECT_DIR")"
if [ -d "$PARENT_DIR/mcc" ]; then
    SKILL_DST="$PARENT_DIR/mcc/skills/$SKILL_NAME"
else
    SKILL_DST="$HOME/.claude/skills/$SKILL_NAME"
fi

echo "============================================"
echo "  orion-kit Claude Code Skill Generator"
echo "============================================"
echo

MODULES="orion-lang,orion-ext,orion-office,orion-http,orion-net,orion-web,orion-spring,orion-generator"

# Step 1: Clean and create JavaDoc HTML (unless --skip-docs)
if [ "$1" = "--skip-docs" ]; then
    echo "[1/5] Skipping JavaDoc generation (--skip-docs)"
else
    echo "[1/5] Clean and Generating JavaDoc HTML..."
    cd "$PROJECT_DIR"
    mvn clean -q
    mvn javadoc:javadoc -q -Dforce=true -pl "$MODULES"
    echo "  JavaDoc HTML created."
fi
echo

# Step 2: Convert JavaDoc HTML -> Markdown directly to skill directory
echo "[2/5] Converting JavaDoc HTML to Markdown..."
python3 "$SKILL_DIR/javadoc2md.py" --javadoc-dir "$PROJECT_DIR" --output-dir "$SKILL_DST/references"
echo

# Step 3: Setup CodeGraph (if npm is available)
if ! command -v npm &> /dev/null; then
    echo "[3/5] Skipping CodeGraph setup (npm not found)"
else
    echo "[3/5] Setting up CodeGraph..."
    if ! command -v codegraph &> /dev/null; then
        echo "  Installing codegraph globally..."
        npm i -g @colbymchenry/codegraph
    fi
    cd "$PROJECT_DIR"
    echo "  Initializing project index..."
    codegraph init -i
    echo "  CodeGraph initialized."

    # Copy codegraph DB to skill references
    CODEGRAPH_DB="$PROJECT_DIR/.codegraph/codegraph.db"
    if [ -f "$CODEGRAPH_DB" ]; then
        cp -f "$CODEGRAPH_DB" "$SKILL_DST/references/codegraph.db"
        echo "  Bundled codegraph.db -> $SKILL_DST/references/"
    fi
fi
echo

# Step 4: Copy SKILL.md and scripts
echo "[4/5] Deploying skill to $SKILL_DST"
mkdir -p "$SKILL_DST/scripts"

cp -f "$SKILL_SRC/SKILL.md" "$SKILL_DST/SKILL.md"
cp -f "$SKILL_DIR/create-skill.bat" "$SKILL_DST/scripts/create-skill.bat"
cp -f "$SKILL_DIR/create-skill.sh" "$SKILL_DST/scripts/create-skill.sh"
cp -f "$SKILL_DIR/javadoc2md.py" "$SKILL_DST/scripts/javadoc2md.py"
echo "  Copied SKILL.md and scripts"
echo

# Step 5: Summary
echo "[5/5] Done"
echo
echo "============================================"
echo "  Skill deployed: $SKILL_DST"
echo "============================================"
echo
echo "  Contents:"
echo "    SKILL.md          - Skill manifest"
echo "    references/       - API docs (Markdown)"
echo "    references/codegraph.db - CodeGraph DB (if available)"
echo "    scripts/          - Build scripts"
echo "============================================"
