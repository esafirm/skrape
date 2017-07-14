#!/bin/bash
rm settings.gradle
echo "include ':skrape-core'"
./gradlew clean build publishToMavenLocal
