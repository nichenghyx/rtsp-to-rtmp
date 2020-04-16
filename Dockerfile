FROM daocloud.io/brave8/maven-jdk8:latest

ADD pom.xml /tmp/build/
RUN cd /tmp/build && mvn -q dependency:resolve

ADD src /tmp/build/src

ADD startup.sh /

RUN cd /tmp/build && mvn -q -DskipTests=true package \
     #拷贝编译结果到指定目录
     && mv target/*.jar /app.jar \
     #清理编译痕迹
     && cd / && rm -rf /tmp/build

VOLUME /tmp

EXPOSE 6666

ENTRYPOINT ["sh", "/startup.sh"]