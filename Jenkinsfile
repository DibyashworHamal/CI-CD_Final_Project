pipeline {
    agent any

    environment {
        REGISTRY = 'harbor.registry.local'
        IMAGE_NAME = 'ebs-app'
        IMAGE_TAG = "${env.BUILD_NUMBER}"
        PROJECT_DIR = 'MajorProject/Complete_CICD/'
        SCANNER_HOME = tool 'sonarscanner8.1'
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out source'
                checkout scm
            }
        }

        stage('Compile') {
            agent {
                docker {
                    image 'maven:3.9-eclipse-temurin-21'
                    label 'dkr-vm'
                    args "-v ${env.HOME}/.m2:/root/.m2" 
                }
            }
            steps {
                dir(env.PROJECT_DIR) {
                    echo 'Compiling Spring Boot application'
                    sh 'mvn -B clean compile'
                }
            }
        }

        stage('Checkstyle Analysis') {
             agent {
                 docker {
                    image 'maven:3.9-eclipse-temurin-21'
                    label 'dkr-vm'
                    args "-v ${env.HOME}/.m2:/root/.m2"
                }
             }
             steps {
                 dir(env.PROJECT_DIR) {
                 sh 'mvn -f pom.xml checkstyle:check -Dcheckstyle.failOnViolation=false'
                }
            }
             post {
                 always {
                    // Publish the HTML report in Jenkins UI
                    publishHTML(target: [
                    reportDir: "${env.PROJECT_DIR}/target/site",
                    reportFiles: 'checkstyle.html',
                    reportName: 'Checkstyle Report'
                    ])
                }
            }
        }

        stage('Test') {
            agent {
                docker {
                    image 'maven:3.9-eclipse-temurin-21'
                    label 'dkr-vm'
                    args "-v ${env.HOME}/.m2:/root/.m2"
                }
            }
            steps {
                dir(env.PROJECT_DIR) {
                    echo 'Running Maven tests'
                    sh 'mvn -B test'
                }
            }
        }

        stage('Package') {
            agent {
                docker {
                    image 'maven:3.9-eclipse-temurin-21'
                    label 'dkr-vm'
                    args "-v ${env.HOME}/.m2:/root/.m2"
                }
            }
            steps {
                dir(env.PROJECT_DIR) {
                    echo 'Packaging Spring Boot application'
                    sh 'mvn -B package -DskipTests'
                }
                stash includes: "${env.PROJECT_DIR}/target/**, ${env.PROJECT_DIR}/src/**", name: 'build-artifacts'
            }
            post {
                success {
                    archiveArtifacts artifacts: "${env.PROJECT_DIR}/target/*.jar", followSymlinks: false, onlyIfSuccessful: true
                }
            }
        }

        stage('SonarQube Analysis') {
            agent { label 'dkr-vm' }
            steps {
                unstash 'build-artifacts'
                dir(env.PROJECT_DIR) {
                    withSonarQubeEnv('sonarqube-server') {
                        sh """${SCANNER_HOME}/bin/sonar-scanner \
                            -Dsonar.projectKey=ebs-app \
                            -Dsonar.projectName='Event Booking System' \
                            -Dsonar.projectVersion=${env.BUILD_NUMBER} \
                            -Dsonar.sources=src/main/java \
                            -Dsonar.tests=src/test/java \
                            -Dsonar.java.binaries=target/classes \
                            -Dsonar.junit.reportPaths=target/surefire-reports/ \
                            -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
                            -Dsonar.java.checkstyle.reportPaths=target/checkstyle-result.xml"""
                    }
                }
            }
        }

        stage("UploadArtifact") {
            agent { label 'dkr-vm' }
            steps {
                unstash 'build-artifacts'
                script {
                    def jarFile = sh(
                        script: "ls ${env.PROJECT_DIR}/target/*.jar | head -1",
                        returnStdout: true
                    ).trim()

                    nexusArtifactUploader(
                        nexusVersion: 'nexus3',
                        protocol: 'http',
                        nexusUrl: '3.80.79.59:8081',
                        groupId: 'QA',
                        version: "${env.BUILD_NUMBER}-${env.BUILD_TIMESTAMP}",
                        repository: 'ebs-app-repo',
                        credentialsId: 'awsNexus-artifactory',
                        artifacts: [
                            [artifactId: 'ebs-app',
                            classifier: '',
                            file: jarFile, 
                            type: 'jar']
                        ]
                    )
                }
            }
        }

        stage('Create Docker Image') {
            agent { label 'dkr-vm' }
            steps {
                dir(env.PROJECT_DIR) {
                    echo 'Creating Docker Image'
                    sh "docker image build -t ${env.IMAGE_NAME}:${env.IMAGE_TAG} ."
                }
            }
        }

        stage('Tag Docker Image') {
            agent { label 'dkr-vm' }
            steps {
                dir(env.PROJECT_DIR) {
                    echo 'Tagging an Image'
                    sh "docker image tag ${env.IMAGE_NAME}:${env.IMAGE_TAG} ${env.REGISTRY}/devops-project/${env.IMAGE_NAME}:${env.IMAGE_TAG}"
                }
            }
        }

        stage('Push Docker Image') {
            agent { label 'dkr-vm' }
            steps {
                dir(env.PROJECT_DIR) {
                    echo 'Logging into Harbor Registry and pushing image'
                    withDockerRegistry([credentialsId: 'harbor-password', url: "https://${env.REGISTRY}"]) {
                        sh "docker image push ${env.REGISTRY}/devops-project/${env.IMAGE_NAME}:${env.IMAGE_TAG}"
                    }
                }
            }
        }

        stage('Deploy to production environment') {
            agent { label 'dkr-vm' }
            steps {
                timeout(time: 1, unit: 'HOURS') {
                    input message: 'Approve PRODUCTION Deployment?'
                }
                echo 'Running app on production environment'
                sh "sudo chown -R \$(whoami) \$WORKSPACE || true"
                sh "sudo chown -R \$(whoami) \${WORKSPACE}@tmp || true"
                dir(env.PROJECT_DIR) {
                    // Securely pull the .env file from Jenkins Credentials vault
                    withCredentials([file(credentialsId: 'prod-env-file', variable: 'SECRET_ENV_FILE')]) {
                        sh """
                        # 0. Forcefully remove any leftover read-only .env file from previous failed builds
                        rm -f .env

                        # 1. Use 'cat' instead of 'cp' so the new .env file has write permissions
                        cat \$SECRET_ENV_FILE > .env

                        # 2. Dynamically append our image tags to the bottom of the file securely
                        echo "REGISTRY=${env.REGISTRY}" >> .env
                        echo "IMAGE_NAME=${env.IMAGE_NAME}" >> .env
                        echo "IMAGE_TAG=${env.IMAGE_TAG}" >> .env

                        # 3. Deploy securely!
                        docker compose down || true
                        docker compose up -d
                        
                        # 4. Delete the .env file after deployment so it doesn't sit around
                        rm -f .env
                        """
                    }
                }
            }   
        }
    }

    post {
        success {
            mail bcc: '', body: """Hello Team,
BUILD #${env.BUILD_NUMBER} of Event Booking System was successful.
You can find the build details at: ${env.BUILD_URL}
Regards,
DevOps Team""", cc: '', from: '', replyTo: '', subject: "Build #${env.BUILD_NUMBER} Successful - Event Booking System",
            to: 'hamaldivyashwor2057@gmail.com'
        }
        failure {
            mail bcc: '', body: """Hello Team,
BUILD #${env.BUILD_NUMBER} of Event Booking System has failed.
Please check the build details at: ${env.BUILD_URL}
Regards,
DevOps Team""", cc: '', from: '', replyTo: '', subject: "Build #${env.BUILD_NUMBER} Failed - Event Booking System",
            to: 'hamaldivyashwor2057@gmail.com'
        }
    }
}