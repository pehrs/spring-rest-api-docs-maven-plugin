#!/bin/bash

# make sure the plugin is installed
mvn install
RESULT="$?"
if test "${RESULT}" != "0"; then
    echo "Test FAILED";
    exit 1;
fi
# Compile the test project
mvn -f pom-test.xml compile
RESULT="$?"
if test "${RESULT}" != "0"; then
    echo "Test FAILED";
    exit 1;
fi
# Run the plutin
mvn -f pom-test.xml com.pehrs:spring-rest-api-docs-maven-plugin:docs
RESULT="$?"
if test "${RESULT}" != "0"; then
    echo "Test FAILED";
    exit 1;
fi

# Check the result
diff docs/sample-rest-api/com.pehrs.spring-rest-api-docs-maven-plugin-test-1.0.0.html target/sample-rest-api/com.pehrs.spring-rest-api-docs-maven-plugin-test-1.0.0.html
DIFF_RESULT="$?"
if test "${DIFF_RESULT}" == "0"; then
    echo "Test SUCCESS";
else 
    echo "Test FAILED";
fi