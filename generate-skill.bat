@echo off
REM ============================================================================
REM orion-kit Claude Code Skill Generator
REM
REM Generates JavaDoc, converts to Markdown, and deploys the orion-kit-docs skill
REM to ~/.claude/skills/
REM
REM Usage:
REM   generate-skill.bat              -- build javadoc + convert + deploy skill
REM   generate-skill.bat --skip-docs  -- convert + deploy only, skip javadoc build
REM ============================================================================

setlocal

set SCRIPT_DIR=%~dp0
set SKILL_NAME=orion-kit-docs
set SKILL_SRC=%SCRIPT_DIR%skill
set SKILL_DST=%USERPROFILE%\.claude\skills\%SKILL_NAME%

echo ============================================
echo   orion-kit Claude Code Skill Generator
echo ============================================
echo.

set MODULES=orion-lang,orion-ext,orion-office,orion-http,orion-net,orion-web,orion-spring,orion-generator

REM Step 1: Clean and Generate JavaDoc HTML (unless --skip-docs)
if "%1"=="--skip-docs" goto skip_docs
echo [1/3] Clean and Generating JavaDoc HTML...
cd /d "%SCRIPT_DIR%"
call mvn clean -q
call mvn javadoc:javadoc -q -pl %MODULES%
echo   JavaDoc HTML generated.
goto after_docs

:skip_docs
echo [1/3] Skipping JavaDoc generation (--skip-docs)

:after_docs
echo.

REM Step 2: Convert JavaDoc HTML -> Markdown directly to skill directory
echo [2/3] Converting JavaDoc HTML to Markdown...
python "%SCRIPT_DIR%javadoc2md.py" --output-dir "%SKILL_DST%\references"
echo.

REM Step 3: Copy SKILL.md and scripts
echo [3/3] Deploying skill to %SKILL_DST%
if not exist "%SKILL_DST%\scripts" mkdir "%SKILL_DST%\scripts"

copy /Y "%SKILL_SRC%\SKILL.md" "%SKILL_DST%\SKILL.md" >nul
copy /Y "%SCRIPT_DIR%generate-skill.bat" "%SKILL_DST%\scripts\generate-skill.bat" >nul
copy /Y "%SCRIPT_DIR%javadoc2md.py" "%SKILL_DST%\scripts\javadoc2md.py" >nul
echo   Copied SKILL.md and scripts
echo.

echo ============================================
echo   Skill deployed: %SKILL_DST%
echo ============================================

endlocal
