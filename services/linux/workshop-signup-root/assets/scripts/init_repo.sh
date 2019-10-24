#!/bin/bash

DOMAIN_NAME=west.us.se.dckr.org

read -p 'Enter your admin username: ' ADM_USER
read -sp 'Enter your admin password: ' ADM_PASS
echo
echo Thank you $ADM_USER we now have your login details

read -p 'Enter your non-admin username: ' USER
echo
echo Thank you $USER we now have your organization name

AUTH_TOKEN="$(docker -H unix:///var/run/docker.sock run -i --rm mbentley/curl -sk -d '{"username":"'$ADM_USER'","password":"'$ADM_PASS'"}' "https://ucp.${DOMAIN_NAME}/auth/login" | awk -F'"' '{print $4}' 2>/dev/null)"

ORG=$USER

for o in ${ORG}-dev ${ORG}-prod
do
	docker -H unix:///var/run/docker.sock run -i --rm mbentley/curl -sk -X POST "https://ucp.${DOMAIN_NAME}/accounts/" -H "accept: application/json" -H "Authorization: Bearer ${AUTH_TOKEN}" -H  "content-type: application/json" -d "{
		\"isOrg\": true,  
		\"name\": \"$o\"
	}"
done

for o in ${ORG}-dev ${ORG}-prod
do
	for u in jenkins $USER $ADM_USER
	do
		docker -H unix:///var/run/docker.sock run -i --rm mbentley/curl -sk -X PUT "https://ucp.${DOMAIN_NAME}/accounts/$o/members/$u" -H  "accept: application/json" -H  "Authorization: Bearer ${AUTH_TOKEN}" -H  "content-type: application/json" -d "{
			\"isAdmin\": true
		}"
	done
done

for j in web api db
do
	docker -H unix:///var/run/docker.sock run -i --rm mbentley/curl -sk -u $ADM_USER:$ADM_PASS -X POST "https://dtr.${DOMAIN_NAME}/api/v0/repositories/$ORG-dev" -H "accept: application/json" -H "content-type: application/json" -d "{
		\"enableManifestLists\": true, 
		\"immutableTags\": false, 
		\"longDescription\": \"$j Server for Signup app\", 
		\"name\": \"signup-$j\", 
		\"scanOnPush\": false, 
		\"shortDescription\": \"$j Server for Signup app\", 
		\"tagLimit\": 0, 
		\"visibility\": \"private\"
	}"
done

for j in web api db
do
	docker -H unix:///var/run/docker.sock run -i --rm mbentley/curl -sk -u $ADM_USER:$ADM_PASS -X POST "https://dtr.${DOMAIN_NAME}/api/v0/repositories/$ORG-prod" -H "accept: application/json" -H "content-type: application/json" -d "{
		\"enableManifestLists\": true, 
		\"immutableTags\": false, 
		\"longDescription\": \"$j Server for Signup app\", 
		\"name\": \"signup-$j\", 
		\"scanOnPush\": false, 
		\"shortDescription\": \"$j Server for Signup app\", 
		\"tagLimit\": 0, 
		\"visibility\": \"public\"
	}"
done
