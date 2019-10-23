import static groovy.io.FileType.DIRECTORIES

DOCKER_CLUSTER_DOMAIN = "build.us.se.dckr.org"
DOCKER_REGISTRY_HOSTNAME = "dtr.${DOCKER_CLUSTER_DOMAIN}"
DOCKER_REGISTRY_URI = "https://${DOCKER_REGISTRY_HOSTNAME}"
DOCKER_REGISTRY_CREDENTIALS_ID = "org.dckr-jenkins"

@NonCPS
def findServices(dir){
    def service = [:]

    dir.eachFile (DIRECTORIES) { directory ->
        serviceName = directory.toString().split("/")[-1]
        service[serviceName] = directory
        println "Found service ${serviceName} in ${directory}"
    }

    return service
}

def docker_image = [:]
def linuxService = [:]
def winService = [:]

pipeline {
    agent none
    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }
    stages {
        stage('Find Services') {
            agent any
            steps {
                checkout scm

                script {
                    println "Searching for Linux services"
                    servicesDir = new File( "${WORKSPACE}/services/linux")
                    linuxService = findServices(servicesDir)

                    println "Searching for Windows services"
                    servicesDir = new File( "${WORKSPACE}/services/win")
                    winService = findServices(servicesDir)
                }
            }
        }
        stage('Build & Push Services'){
            parallel {
                stage('Linux Services') {
                    agent { 
                        label 'linux'
                    }
                    stages {
                        stage("Build") {
                            steps {
                                checkout scm

                                script {
                                    linuxService.each { name, path ->
                                        println "Building service ${name}"

                                        docker_image[name] = docker.build("service-template/${name}", "--build-arg interpolator_image=${INTERPOLATOR_REPO}:linux-${INTERPOLATOR_IMAGE_VERSION} ./services/linux/${name}")
                                    }
                                }
                            }
                        }
                        stage('Push') {
                            steps {
                                script {
                                    linuxService.each { name, path ->
                                        println "Pushing ${name} service"
                                        docker.withRegistry(DOCKER_REGISTRY_URI, DOCKER_REGISTRY_CREDENTIALS_ID) {
                                            docker_image[name].push("latest")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                stage('Windows Services') {
                    agent { 
                        label 'ws-2019'
                    }
                    stages {
                        stage("Build") {
                            steps {
                                checkout scm

                                script {
                                    winService.each { name, path ->
                                        println "Building service ${name}"

                                        docker_image[name] = docker.build("service-template/${name}", "--build-arg interpolator_image=${INTERPOLATOR_REPO}:windows-${INTERPOLATOR_IMAGE_VERSION} ./services/win/${name}")
                                    }
                                }
                            }
                        }
                        stage('Push') {
                            steps {
                                script {
                                    winService.each { name, path ->
                                        println "Pushing ${name} service"
                                        docker.withRegistry(DOCKER_REGISTRY_URI, DOCKER_REGISTRY_CREDENTIALS_ID) {
                                            docker_image[name].push("latest")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        stage('Publish library.yaml to S3') {
            agent any
            steps{
                s3Upload(consoleLogLevel: 'INFO', dontWaitForConcurrentBuildCompletion: false, entries: [[bucket: 'docker-field-application-template/production/latest', excludedFile: '', flatten: false, gzipFiles: false, keepForever: false, managedArtifacts: false, noUploadOnFailure: true, selectedRegion: 'us-west-2', showDirectlyInBrowser: false, sourceFile: 'library.yaml', storageClass: 'STANDARD', uploadFromSlave: false, useServerSideEncryption: false]], pluginFailureResultConstraint: 'FAILURE', profileName: 'docker-field-application-template', userMetadata: [])
            }
        }
    }
}
