mvn -f pom-test.xml compile
mvn -f pom-test.xml com.pehrs:spring-rest-api-docs-maven-plugin:docs
diff docs/sample-rest-api/com.pehrs.spring-rest-api-docs-maven-plugin-test-1.0.0.html target/sample-rest-api/com.pehrs.spring-rest-api-docs-maven-plugin-test-1.0.0.html
DIFF_RESULT="$?"
if test "${DIFF_RESULT}" == "0"; then
    echo "Test SUCCESS";
else 
    echo "Test FAILED";
fi