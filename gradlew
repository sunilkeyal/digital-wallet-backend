#!/bin/sh
JAVA_HOME=$(/usr/libexec/java_home -v 21)
export JAVA_HOME
exec "$JAVA_HOME/bin/java" -cp "$(dirname "$0")/gradle/wrapper/gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain "$@"
