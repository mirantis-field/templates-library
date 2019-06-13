import static groovy.io.FileType.DIRECTORIES

DOCKER_CLUSTER_DOMAIN = "build.us.se.dckr.org"
DOCKER_REGISTRY_HOSTNAME = "dtr.${DOCKER_CLUSTER_DOMAIN }"
DOCKER_REGISTRY_URI = "https://${DOCKER_REGISTRY_HOSTNAME}"
DOCKER_REGISTRY_CREDENTIALS_ID = "org.dckr-jenkins"

node {
    def docker_image = [:];
    def service = [:];

    stage('Clone') {
        checkout scm
    }

    stage('Build') {
        def currentDir = pwd()
        def servicesDir = new File( "${currentDir}/services")

        servicesDir.eachFileRecurse (DIRECTORIES) { directory ->
            serviceName = directory.toString().split("/")[-1]
            service[serviceName] = directory
        }

        service.each { name, path ->
            println "Building service ${name}"

            docker_image[name] = docker.build("service-template/${name}", "-f ./services/Dockerfile --build-arg service_name=${name} ./services/${name}")
        }
    }

    stage('Push') {
        service.each { name, path ->
            println "Pushing ${name} service"
            docker.withRegistry(DOCKER_REGISTRY_URI, DOCKER_REGISTRY_CREDENTIALS_ID) {
                docker_image[name].push("latest")
            }
        }
    }


    stage('Publish library.yaml to S3') {
        s3Upload acl: 'PublicRead', consoleLogLevel: 'INFO', dontWaitForConcurrentBuildCompletion: false, entries: [[bucket: 'docker-field-application-template/production/latest', excludedFile: '', flatten: false, gzipFiles: false, keepForever: false, managedArtifacts: false, noUploadOnFailure: true, selectedRegion: 'us-west-2', showDirectlyInBrowser: false, sourceFile: 'library.yaml', storageClass: 'STANDARD', uploadFromSlave: false, useServerSideEncryption: false]], pluginFailureResultConstraint: 'FAILURE', profileName: 'docker-field-application-template', userMetadata: []
    }
}
