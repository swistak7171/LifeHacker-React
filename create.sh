#!/bin/bash

apt-get install -y git
git clone https://github.com/swistak7171/LifeHacker-React.git
cd LifeHacker-React
chmod +x gradlew
./gradlew build