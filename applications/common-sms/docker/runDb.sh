docker run --rm --name common-sms-db-docker \
-e POSTGRES_PASSWORD=common-sms-user-pwd \
-e POSTGRES_USER=common-sms-user \
-e POSTGRES_DB=common-sms-db \
-p 5441:5432 \
postgres:14
