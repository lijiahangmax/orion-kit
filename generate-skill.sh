#!/bin/bash
# ============================================================================
# orion-kit Claude Code Skill Generator
#
# Generates JavaDoc, converts to Markdown, and deploys the orion-kit-docs skill
# to ~/.claude/skills/
#
# Usage:
#   ./generate-skill.sh              # build javadoc + convert + deploy skill
#   ./generate-skill.sh --skip-docs  # convert + deploy only, skip javadoc build
# ============================================================================

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
SKILL_NAME="orion-kit-docs"
SKILL_SRC="$SCRIPT_DIR/skill"
SKILL_DST="$HOME/.claude/skills/$SKILL_NAME"

echo "============================================"
echo "  orion-kit Claude Code Skill Generator"
echo "============================================"
echo ""

MODULES="orion-lang,orion-ext,orion-office,orion-http,orion-net,orion-web,orion-spring,orion-generator"

# Step 1: Clean and Generate JavaDoc HTML (unless --skip-docs)
if [ "$1" = "--skip-docs" ]; then
    echo "[1/3] Skipping JavaDoc generation (--skip-docs)"
else
    echo "[1/3] Clean and Generating JavaDoc HTML..."
    cd "$SCRIPT_DIR"
    mvn clean -q
    mvn javadoc:javadoc -P '!skip-docs' -q -pl "$MODULES" 2>&1 | tail -5
    echo "  JavaDoc HTML generated."
fi
echo ""

# Step 2: Convert JavaDoc HTML -> Markdown directly to skill directory
echo "[2/3] Converting JavaDoc HTML -> Markdown..."
python "$SCRIPT_DIR/javadoc2md.py" --output-dir "$SKILL_DST/references"
echo ""

# Step 3: Copy SKILL.md and scripts
echo "[3/3] Deploying skill to $SKILL_DST"
mkdir -p "$SKILL_DST/scripts"
cp "$SKILL_SRC/SKILL.md" "$SKILL_DST/SKILL.md"
cp "$SCRIPT_DIR/generate-skill.sh" "$SKILL_DST/scripts/generate-skill.sh"
cp "$SCRIPT_DIR/javadoc2md.py" "$SKILL_DST/scripts/javadoc2md.py"
echo "  Copied SKILL.md and scripts"
echo ""

# Summary
total=$(du -sh "$SKILL_DST" | cut -f1)
echo "============================================"
echo "  Skill deployed: $SKILL_DST"
echo "  Total size: $total"
echo "============================================"
