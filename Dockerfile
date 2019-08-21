FROM openjdk:8-jre-alpine as builder

ENV SCALA_VERSION 2.12.7
ENV SCALA_HOME /usr/local/scala

RUN apk add --no-cache --virtual=.build-dependencies wget ca-certificates && \
    apk add --no-cache bash && \
    apk add --update alpine-sdk && \
    wget "https://downloads.typesafe.com/scala/${SCALA_VERSION}/scala-${SCALA_VERSION}.tgz" && \
    tar xzf "scala-${SCALA_VERSION}.tgz" && \
    rm "scala-${SCALA_VERSION}/bin/"*.bat && \
    mkdir "${SCALA_HOME}" && \
    mv "scala-${SCALA_VERSION}/bin" "scala-${SCALA_VERSION}/lib" "${SCALA_HOME}" && \
    ln -s "${SCALA_HOME}/bin/"* "/usr/bin/" && \
    apk del .build-dependencies && \
    rm -rf "scala-${SCALA_VERSION}" && \
    rm "scala-${SCALA_VERSION}.tgz" && \
    scala -version

ENV SBT_VERSION 1.2.6
ENV SBT_HOME /usr/local/sbt

ENV PATH ${PATH}:${SBT_HOME}/bin

RUN curl -sL "https://github.com/sbt/sbt/releases/download/v$SBT_VERSION/sbt-$SBT_VERSION.tgz" | gunzip | tar -x -C /usr/local && \
    echo -ne "- with sbt $SBT_VERSION\n" >> /root/.built && \
    sbt sbtVersion

# assembly to jar
WORKDIR /app

COPY ./project ./project
COPY ./build.sbt ./build.sbt
COPY ./src ./src

RUN sbt 'set test in assembly := {}' clean assembly

# Runtime
FROM openjdk:8-jre-alpine

WORKDIR /app

COPY --from=builder /app/target/scala-2.12/app.jar ./app.jar

CMD ["java", \
  "-XX:+UnlockExperimentalVMOptions", \
  "-XX:+UseCGroupMemoryLimitForHeap", \
  "-XX:MaxRAMFraction=1", \
  "-jar", "app.jar"]
