# escape=`
FROM microsoft/dotnet:{{ .Parameters.workshopDotNetCoreSdkVersion }} as builder

WORKDIR /src
COPY SignupApiCore/SignupApiCore.csproj .
RUN dotnet restore

COPY SignupApiCore/ .
RUN dotnet publish

# app image
FROM microsoft/dotnet:{{ .Parameters.workshopDotNetCoreRuntimeVersion }}

CMD ["dotnet", "SignupApiCore.dll"]

WORKDIR /dotnetapp
COPY --from=builder /src/bin/Debug/netcoreapp2.2/publish .