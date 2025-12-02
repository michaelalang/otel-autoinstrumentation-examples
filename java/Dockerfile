FROM registry.access.redhat.com/ubi9/openjdk-21:latest

USER root

WORKDIR /app

COPY VirtualThreadSyncDemo.java .

RUN javac VirtualThreadSyncDemo.java

USER 185

EXPOSE 8080

CMD ["java", "VirtualThreadSyncDemo"]
