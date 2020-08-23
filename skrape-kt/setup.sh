#!/bin/bash
rm settings.gradle
echo "include 'skrape-core'" >> settings.gradle
echo "include 'skrape-chrome'" >> settings.gradle
echo "include 'skrape-jsoup'" >> settings.gradle
