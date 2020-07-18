FROM postgres:11
ENV POSTGRES_USER postgres
ENV POSTGRES_PASSWORD postgres
ENV POSTGRES_DB postgres
ENV POSTGRES_MULTIPLE_DATABASES="cardservice"
ENV POSTGRES_MULTIPLE_USERS="cardservice"
ENV POSTGRES_MULTIPLE_PASSWORDS='123456'

COPY /docker-entrypoint-initdb.d/create-multiple-postgresql-databases.sh /docker-entrypoint-initdb.d/
