@echo off

echo Skrypt zakłada zainstalowane narzędzie ADB i Gradle
echo Buduję plik .apk

if exist gradlew (
    echo Nadanie uprawnień do wykonania dla gradlew
    cacls gradlew /G %USERNAME%:F
) else (
    echo Plik gradlew nie został znaleziony.
    exit /b 1
)

call gradlew assembleDebug
if %ERRORLEVEL% equ 0 (
    echo Zbudowano plik APK
) else (
    echo Wystąpił problem przy budowaniu pliku APK
    exit /b 1
)

adb reverse tcp:8080 tcp:8080

for /f "tokens=5" %%i in ('netstat -an ^| find "8080" ^| find "LISTENING"') do (
    set port_status=%%i
)

if "%port_status%" equ "LISTENING" (
    echo Port 8080 jest otwarty
) else (
    echo Port 8080 nie jest otwarty
)

adb install app\build\outputs\apk\debug\app-debug.apk
if %ERRORLEVEL% equ 0 (
    echo Zainstalowano aplikację Pawsitive
) else (
    echo Wystąpił problem przy instalowaniu aplikacji Pawsitive
    exit /b 1
)