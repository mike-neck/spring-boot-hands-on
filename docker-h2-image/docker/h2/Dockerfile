FROM mikeneck/jdk8

RUN \
  groupadd -r db && \
  useradd -m -g db h2

ADD h2.jar /home/h2/h2.jar

RUN \
  chown h2 /home/h2/h2.jar && \
  chmod 700 /home/h2/h2.jar

USER h2
WORKDIR /home/h2/
EXPOSE 9092

ENTRYPOINT ["java", "-cp", "h2.jar", "org.h2.tools.Server", "-tcp", "-tcpAllowOthers"]
