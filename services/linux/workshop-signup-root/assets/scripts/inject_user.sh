#!/bin/bash

for i in `seq 1 10`
do
curl http://localhost:30081/api/users \
  -H 'Content-Type: application/json' \
  -d '{
  "userName" : "user'$i'",
  "firstName" : "User",
  "lastName" : "One",
  "password" : "abcdef",
  "emailAddress" : "u'$i'@abc.com",
  "dateOfBirth" : "1981-12-31"
}'
done
