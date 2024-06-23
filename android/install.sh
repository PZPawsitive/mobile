#!/bin/bash

echo "Skrypt zakłada zainstalowane narzędzie ADB i Gradle"
echo "Buduję plik .apk"

chmod +x ./gradlew

./gradlew assembleDebug

if [ $? -eq 0 ]; then
    echo "zbudowano plik APK"
else
    echo "wystąpił problem przy budowaniu pliku APK"
    exit 1
fi

adb reverse tcp:8080 tcp:8080

if ss -tuln | grep -q ":8080"; then
    echo "Port 8080 jest otwarty"
else
    echo "Port 8080 nie jest otwarty"
fi

adb install ./app/build/outputs/apk/debug/app-debug.apk

if [ $? -eq 0 ]; then
    echo "zainstalowano aplikację Pawsitive"
else
    echo "wystąpił problem przy instalowaniu aplikacji Pawsitive"
    exit 1
fi
