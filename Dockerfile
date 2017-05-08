FROM alexejk/java8:latest

ADD build/libs/metrics-demo-*.jar /metrics.jar

EXPOSE 8080

CMD ["java", "-jar", "metrics.jar"]
