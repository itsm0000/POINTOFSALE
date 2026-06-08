@echo off
echo Compiling Cafe POS...
javac -d bin src/cafepos/model/*.java src/cafepos/util/*.java src/cafepos/ui/*.java

if %errorlevel% neq 0 (
    echo.
    echo Compilation FAILED. Press any key to exit.
    pause >nul
    exit /b 1
)

echo Starting Cafe POS...
java -cp bin cafepos.ui.MainFrame
