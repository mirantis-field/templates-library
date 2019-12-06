ARG build_image=maven:{{ .Parameters.workshopJavaAppMavenVersion }}
ARG base_image=tomcat:{{ .Parameters.workshopJavaAppJavaVersion }}

###############################################################################
# BUILD
###############################################################################
FROM ${build_image} AS devenv

WORKDIR /usr/src/workshop-signup

COPY app/pom.xml .

RUN mvn -B -f pom.xml -s /usr/share/maven/ref/settings-docker.xml dependency:resolve

COPY ./app .

RUN mvn -B -s /usr/share/maven/ref/settings-docker.xml package -DskipTests





###############################################################################
# FINAL
###############################################################################
FROM ${base_image}

COPY --from=devenv /usr/src/workshop-signup/target/workshop-signup.war /usr/local/tomcat/webapps/

COPY tomcat /usr/local/tomcat/

CMD ["catalina.sh", "jpda", "run"]
