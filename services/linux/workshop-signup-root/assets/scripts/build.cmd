cd ./database
docker image build -t database .

cd ../java-app-v2
docker image build -t java_web:2 .

cd ../dotnet-api
docker image build -t dotnet_api:core .

cd ..
