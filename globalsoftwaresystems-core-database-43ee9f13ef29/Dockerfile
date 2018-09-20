FROM openjdk:8
COPY target/database.jar app.jar
ADD docker-run.sh run.sh
RUN bash -c 'chmod +x /run.sh'
ENTRYPOINT ["/run.sh"]