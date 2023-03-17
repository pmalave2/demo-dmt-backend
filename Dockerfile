FROM eclipse-temurin:17-jre

RUN adduser --system --group --no-create-home newuser

WORKDIR /home/app

COPY target/dmt-backend.jar .

EXPOSE 8082

USER newuser

ENTRYPOINT ["java","-jar","dmt-backend.jar"]