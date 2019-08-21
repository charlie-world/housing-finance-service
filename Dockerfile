FROM openjdk:8-jre-alpine

COPY /jar/app.jar ./app.jar

CMD ["java", \
  "-XX:+UnlockExperimentalVMOptions", \
  "-XX:+UseCGroupMemoryLimitForHeap", \
  "-XX:MaxRAMFraction=1", \
  "-jar", "app.jar"]
