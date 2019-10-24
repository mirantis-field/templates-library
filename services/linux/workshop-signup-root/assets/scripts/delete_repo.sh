#!/bin/bash

DOMAIN_NAME=west.us.se.dckr.org

read -p 'Enter your admin username: ' ADM_USER
read -sp 'Enter your admin password: ' ADM_PASS
echo
echo Thank you $ADM_USER we now have your login details

read -p 'Enter your non-admin username: ' USER
echo
echo Thank you $USER we now have your organization name

ORG=$USER

for i in dev prod
do
	for j in web api db
	do
		docker -H unix:///var/run/docker.sock run -i --rm mbentley/curl -k -u $ADM_USER:$ADM_PASS -X DELETE "https://dtr.west.us.se.dckr.org/api/v0/repositories/$USER-$i/signup-$j" -H "accept: application/json"
	done
done
for o in ${ORG}-dev ${ORG}-prod
do
	docker -H unix:///var/run/docker.sock run -i --rm mbentley/curl -k -u $ADM_USER:$ADM_PASS -X DELETE "https://ucp.${DOMAIN_NAME}/accounts/$o" -H "accept: application/json" -H  "content-type: application/json"
done
