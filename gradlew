#!/usr/bin/env bash
# Wrapper forwarder for CI: runs the Gradle wrapper inside android_frontend
set -euo pipefail
cd "$(dirname "$0")/android_frontend"
exec ./gradlew "$@"
