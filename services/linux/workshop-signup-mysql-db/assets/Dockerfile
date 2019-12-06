#FROM dtr.build.us.se.dckr.org/library/mysql:{{ .Parameters.mysqlVersion }}
FROM mysql:{{ .Parameters.mysqlVersion }}

COPY docker-entrypoint-initdb.d/ /docker-entrypoint-initdb.d/

ENV MYSQL_DATABASE={{ .Parameters.mysqlDatabaseName }}
ENV MYSQL_USER={{ .Parameters.mysqlUserName }}
ENV MYSQL_PASSWORD={{ .Parameters.mysqlPassword }}

ENTRYPOINT ["docker-entrypoint.sh"]

CMD ["mysqld", "--default-authentication-plugin=mysql_native_password"]