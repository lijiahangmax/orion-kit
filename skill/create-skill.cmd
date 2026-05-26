@echo off
REM ============================================================================
REM orion-kit Claude Code Skill Generator
REM
REM creates JavaDoc, converts to Markdown, deploys the orion-kit skill
REM to ~/.claude/skills/, and bundles CodeGraph DB.
REM
REM Usage:
REM   create-skill.cmd              -- build javadoc + convert + deploy + codegraph
REM   create-skill.cmd --skip-docs  -- convert + deploy + codegraph, skip javadoc build
REM ============================================================================

setlocal

set SKILL_DIR=%~dp0
set PROJECT_DIR=%SKILL_DIR%..
set SKILL_NAME=orion-kit
set SKILL_SRC=%SKILL_DIR%

REM Check if mcc directory exists in parent of project root
set PARENT_DIR=%PROJECT_DIR%\..
if exist "%PARENT_DIR%\mcc" (
    set SKILL_DST=%PARENT_DIR%\mcc\skills\%SKILL_NAME%
) else (
    set SKILL_DST=%USERPROFILE%\.claude\skills\%SKILL_NAME%
)

echo ============================================
echo   orion-kit Claude Code Skill Generator
echo ============================================
echo.

set MODULES=orion-lang,orion-ext,orion-office,orion-http,orion-net,orion-web,orion-spring,orion-generator

REM Step 1: Clean and create JavaDoc HTML (unless --skip-docs)
if "%1"=="--skip-docs" goto skip_docs
echo [1/5] Clean and Generating JavaDoc HTML...
cd /d "%PROJECT_DIR%"
call mvn clean -q
call mvn javadoc:javadoc -q -Dforce=true -pl %MODULES%
echo   JavaDoc HTML created.
goto after_docs

:skip_docs
echo [1/5] Skipping JavaDoc generation (--skip-docs)

:after_docs
echo.

REM Step 2: Convert JavaDoc HTML -> Markdown directly to skill directory
echo [2/5] Converting JavaDoc HTML to Markdown...
python "%SKILL_DIR%javadoc2md.py" --javadoc-dir "%PROJECT_DIR%" --output-dir "%SKILL_DST%\references"
echo.

REM Step 3: Setup CodeGraph (if npm is available)
where npm >nul 2>nul
if errorlevel 1 goto skip_codegraph

echo [3/5] Setting up CodeGraph...
where codegraph >nul 2>nul
if errorlevel 1 call npm i -g @colbymchenry/codegraph
cd /d "%PROJECT_DIR%"
echo   Initializing project index...
call codegraph init -i
echo   CodeGraph initialized.

REM Copy codegraph DB to skill references
if not exist "%PROJECT_DIR%\.codegraph\codegraph.db" goto after_codegraph
copy /Y "%PROJECT_DIR%\.codegraph\codegraph.db" "%SKILL_DST%\references\codegraph.db" >nul
echo   Bundled codegraph.db -^> %SKILL_DST%\references\
goto after_codegraph

:skip_codegraph
echo [3/5] Skipping CodeGraph setup (npm not found)

:after_codegraph
echo.

REM Step 4: Copy SKILL.md and scripts
echo [4/5] Deploying skill to %SKILL_DST%
if not exist "%SKILL_DST%\scripts" mkdir "%SKILL_DST%\scripts"

copy /Y "%SKILL_SRC%SKILL.md" "%SKILL_DST%\SKILL.md" >nul
copy /Y "%SKILL_DIR%create-skill.cmd" "%SKILL_DST%\scripts\create-skill.cmd" >nul
copy /Y "%SKILL_DIR%create-skill.sh" "%SKILL_DST%\scripts\create-skill.sh" >nul
copy /Y "%SKILL_DIR%javadoc2md.py" "%SKILL_DST%\scripts\javadoc2md.py" >nul
echo   Copied SKILL.md and scripts
echo.

REM Step 5: Summary
echo [5/5] Done
echo.
echo ============================================
echo   Skill deployed: %SKILL_DST%
echo ============================================
echo.
echo   Contents:
echo     SKILL.md                - Skill manifest
echo     references\             - API docs (Markdown)
echo     references\codegraph.db - CodeGraph DB (if available)
echo     scripts\                - Build scripts
echo ============================================

endlocal
