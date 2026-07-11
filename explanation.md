Welcome! It is great to see you diving into CI/CD. This Jenkinsfile represents a standard, industry-grade declarative pipeline. It takes a Java Spring Boot application, compiles it, tests it, scans it for code quality, uploads the packaged application (artifact) to a repository, builds a Docker image, pushes it to a private container registry, and finally deploys it using Docker Compose.

Looking at your folder structure, we can see how the files map directly to the pipeline stages:
*   **`src`** and **`pom.xml`** are used during compilation, testing, and static analysis stages.
*   **`Dockerfile`** is used to build the application container.
*   **`docker-compose.yml`** and **`.env`** are used in the final deployment stage to run the application.

Let's break down this Jenkinsfile line by line.

---

### **Pipeline Structure & Setup**

```groovy
pipeline {
```
*   **What it does:** This marks the beginning of a **Declarative Pipeline**. It tells Jenkins that everything inside these curly braces defines the pipeline's structure and execution steps.

```groovy
    agent any
```
*   **What it does:** The `agent` directive specifies where the pipeline will execute. Setting it to `any` means Jenkins can run the starting stages on any available build agent (machine) connected to the master.

---

### **Environment Variables**

```groovy
    environment {
        REGISTRY = 'harbor.registry.local'
        IMAGE_NAME = 'ebs-app'
        IMAGE_TAG = "${env.BUILD_NUMBER}"
        PROJECT_DIR = 'MajorProject/Complete_CICD/'
        SCANNER_HOME = tool 'sonarscanner8.1'
    }
```
*   **`environment { ... }`**: Defines global variables accessible by any stage in this pipeline.
*   **`REGISTRY = 'harbor.registry.local'`**: The URL of your private Harbor container registry.
*   **`IMAGE_NAME = 'ebs-app'`**: The name of your Docker image (stands for Event Booking System app).
*   **`IMAGE_TAG = "${env.BUILD_NUMBER}"`**: Automatically tags your Docker image with the current Jenkins build number (e.g., build `1`, `2`, `3`).
*   **`PROJECT_DIR = 'MajorProject/Complete_CICD/'`**: Tells Jenkins where your files are located within the workspace (matching the folder structure shown in your screenshot).
*   **`SCANNER_HOME = tool 'sonarscanner8.1'`**: Points to the SonarQube Scanner tool configured under Global Tool Configuration in your Jenkins dashboard.

---

### **The Stages**

```groovy
    stages {
```
*   **What it does:** This block contains all the individual steps (stages) that our code will go through sequentially.

#### **Stage 1: Checkout**
```groovy
        stage('Checkout') {
            steps {
                echo 'Checking out source'
                checkout scm
            }
        }
```
*   **`stage('Checkout')`**: This stage pulls the latest code from your version control system (like GitHub or GitLab).
*   **`checkout scm`**: A built-in Jenkins command that automatically clones the repository configured in the Jenkins job.

---

#### **Stage 2: Compile**
```groovy
        stage('Compile') {
            agent {
                docker {
                    image 'maven:3.9-eclipse-temurin-21'
                    label 'dkr-vm'
                    args "-v ${env.HOME}/.m2:/root/.m2" 
                }
            }
```
*   **`agent { docker { ... } }`**: Instead of running directly on the host machine, this stage runs inside a Docker container. 
*   **`image 'maven:3.9-eclipse-temurin-21'`**: Uses a Maven container pre-configured with Java 21 to compile the application. This ensures consistent build environments regardless of what is installed on the host.
*   **`label 'dkr-vm'`**: Instructs Jenkins to spin up this container specifically on a build agent labeled `dkr-vm`.
*   **`args "-v ..."`**: Mounts the host's `.m2` directory (where Maven downloads dependencies) to the container's `.m2` directory. This acts as a cache so Maven doesn't have to download all your dependencies from scratch every run.

```groovy
            steps {
                dir(env.PROJECT_DIR) {
                    echo 'Compiling Spring Boot application'
                    sh 'mvn -B clean compile'
                }
            }
        }
```
*   **`dir(env.PROJECT_DIR)`**: Changes the directory to your subdirectory (`MajorProject/Complete_CICD/`) before running commands.
*   **`sh 'mvn -B clean compile'`**: Runs the Maven command in non-interactive "batch" mode (`-B`). It deletes old build artifacts (`clean`) and compiles the source code (`compile`).

---

#### **Stage 3: Checkstyle Analysis**
```groovy
        stage('Checkstyle Analysis') {
             agent {
                 docker {
                     image 'maven:3.9-eclipse-temurin-21'
                     label 'dkr-vm'
                     args "-v ${env.HOME}/.m2:/root/.m2"
                }
             }
```
*   Uses the same Maven Docker container to maintain environment consistency.

```groovy
             steps {
                 dir(env.PROJECT_DIR) {
                     sh 'mvn -f pom.xml checkstyle:check -Dcheckstyle.failOnViolation=false'
                 }
             }
```
*   **`mvn -f pom.xml checkstyle:check ...`**: Runs static analysis on your code formatting based on pre-defined rules.
*   **`-Dcheckstyle.failOnViolation=false`**: Ensures that even if your code formatting violates styling rules, the pipeline does not stop or fail.

```groovy
             post {
                 always {
                    publishHTML(target: [
                        reportDir: "${env.PROJECT_DIR}/target/site",
                        reportFiles: 'checkstyle.html',
                        reportName: 'Checkstyle Report'
                    ])
                }
            }
        }
```
*   **`post { always { ... } }`**: No matter if this stage passes or fails, Jenkins will always run this section.
*   **`publishHTML(...)`**: Takes the generated formatting report (`checkstyle.html`) and displays it inside the Jenkins UI as a readable web tab.

---

#### **Stage 4: Test**
```groovy
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
```
*   **`sh 'mvn -B test'`**: Executes your unit tests using JUnit/TestNG. If any tests fail, the build will stop here.

---

#### **Stage 5: Package**
```groovy
        stage('Package') {
            agent {
                docker {
                    image 'maven:3.9-eclipse-temurin-21'
                    label 'dkr-vm'
                    args "-v ${env.HOME}/.m2:/root/.m2"
                }
            }
```
*   Prepares to package the final application bundle.

```groovy
            steps {
                dir(env.PROJECT_DIR) {
                    echo 'Packaging Spring Boot application'
                    sh 'mvn -B package -DskipTests'
                }
                stash includes: "${env.PROJECT_DIR}/target/**, ${env.PROJECT_DIR}/src/**", name: 'build-artifacts'
            }
```
*   **`sh 'mvn -B package -DskipTests'`**: Packages your application into an executable `.jar` file. We skip tests here (`-DskipTests`) because we already thoroughly ran them in the previous stage.
*   **`stash ...`**: Saves the generated jar, build folders, and source files into a temporary cloud zip labeled `'build-artifacts'`. This is important because the Docker containers used inside the agent might destroy local files once the agent block finishes. Stashing lets us pass these files across different machines or stages.

```groovy
            post {
                success {
                    archiveArtifacts artifacts: "${env.PROJECT_DIR}/target/*.jar", followSymlinks: false, onlyIfSuccessful: true
                }
            }
        }
```
*   **`post { success { ... } }`**: If this stage succeeds, Jenkins archives (saves) the compiled `.jar` file directly onto the Jenkins controller. This makes it downloadable from the Jenkins UI build page.

---

#### **Stage 6: SonarQube Analysis**
```groovy
        stage('SonarQube Analysis') {
            agent { label 'dkr-vm' }
            steps {
                unstash 'build-artifacts'
```
*   **`agent { label 'dkr-vm' }`**: Runs directly on the host VM (labeled `dkr-vm`) without a nested Docker container because we need to run the system's SonarQube Scanner.
*   **`unstash 'build-artifacts'`**: Retrieves the code and packaged items we stashed during the **Package** stage.

```groovy
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
```
*   **`withSonarQubeEnv('sonarqube-server')`**: Wraps the scanner step with your pre-configured Jenkins SonarQube credentials and URL.
*   **`sh """${SCANNER_HOME}/bin/sonar-scanner ..."""`**: Runs the SonarQube CLI tool. It uploads code style, test coverage reports, unit tests, binaries, and source code to your SonarQube dashboard for vulnerability and bug analysis.

---

#### **Stage 7: Upload Artifact to Nexus**
```groovy
        stage("UploadArtifact") {
            agent { label 'dkr-vm' }
            steps {
                unstash 'build-artifacts'
                script {
                    def jarFile = sh(
                        script: "ls ${env.PROJECT_DIR}/target/*.jar | head -1",
                        returnStdout: true
                    ).trim()
```
*   **`unstash 'build-artifacts'`**: Restores the packaged folders once again.
*   **`def jarFile = sh(...)`**: Executes a shell command to locate the generated `.jar` file path dynamically and stores it inside the variable `jarFile`.

```groovy
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
```
*   **`nexusArtifactUploader(...)`**: A Jenkins plugin that uploads the packaged `.jar` file to a Sonatype Nexus Repository Manager (at IP address `3.80.79.59:8081`). This allows other teams or systems to retrieve specific, versioned releases of your application.

---

#### **Stage 8: Create Docker Image**
```groovy
        stage('Create Docker Image') {
            agent { label 'dkr-vm' }
            steps {
                dir(env.PROJECT_DIR) {
                    echo 'Creating Docker Image'
                    sh "docker image build -t ${env.IMAGE_NAME}:${env.IMAGE_TAG} ."
                }
            }
        }
```
*   **`docker image build -t ... .`**: Standard Docker command that reads your `Dockerfile` (located in the workspace) and packages your application, along with its runtime dependencies, into a container image.

---

#### **Stage 9: Tag Docker Image**
```groovy
        stage('Tag Docker Image') {
            agent { label 'dkr-vm' }
            steps {
                dir(env.PROJECT_DIR) {
                    echo 'Tagging an Image'
                    sh "docker image tag ${env.IMAGE_NAME}:${env.IMAGE_TAG} ${env.REGISTRY}/devops-project/${env.IMAGE_NAME}:${env.IMAGE_TAG}"
                }
            }
        }
```
*   **`docker image tag ...`**: Tags your local image with a tag format that specifies the registry URL and repository path (`harbor.registry.local/devops-project/...`). This is a prerequisite before pushing the image online.

---

#### **Stage 10: Push Docker Image**
```groovy
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
```
*   **`withDockerRegistry(...)`**: Securely handles logging into Harbor using credentials saved inside Jenkins under the ID `harbor-password`.
*   **`sh "docker image push ..."`**: Pushes the tagged Docker image up to your Harbor container registry so deployment servers can download it.

---

#### **Stage 11: Deploy to Production Environment**
```groovy
        stage('Deploy to production environment') {
            agent { label 'dkr-vm' }
            steps {
                timeout(time: 1, unit: 'HOURS') {
                    input message: 'Approve PRODUCTION Deployment?'
                }
```
*   **`timeout(...)`**: Stops the pipeline if nobody responds to the deployment prompt within 1 hour.
*   **`input message: ...`**: Pauses the pipeline and prompts an authorized user to manually click an "Approve" button before the app deploys.

```groovy
                echo 'Running app on production environment'
                sh "sudo chown -R \$(whoami) \$WORKSPACE || true"
                sh "sudo chown -R \$(whoami) \${WORKSPACE}@tmp || true"
```
*   **`sh "sudo chown ..."`**: Adjusts directory ownership permissions to ensure the current shell user can edit files in the workspace. The `|| true` makes sure the build continues even if this permission adjustment command fails.

```groovy
                dir(env.PROJECT_DIR) {
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
```
*   **`withCredentials([file(...)])`**: Retrieves your secret `.env` template stored securely inside the Jenkins credentials vault under the ID `'prod-env-file'`.
*   **`rm -f .env`**: Deletes any stale `.env` configuration file on disk.
*   **`cat \$SECRET_ENV_FILE > .env`**: Copies the vault's production configuration directly into a new `.env` file in the project workspace.
*   **`echo ... >> .env`**: Appends the specific Registry, Image name, and build tag to the bottom of your newly created `.env` file. This tells Docker Compose exactly which image version to spin up.
*   **`docker compose down || true`**: Stops and removes old running container instances of this application (if any).
*   **`docker compose up -d`**: Starts the containers in detached (background) mode, reading the configuration settings directly from the `docker-compose.yml` and `.env` files.
*   **`rm -f .env`**: Deletes the plain-text `.env` file from the build workspace directory so secrets are not left exposed on the server's hard drive.

---

### **Post-Execution Step (Notifications)**

```groovy
    post {
        success {
            mail bcc: '', body: """Hello Team,
BUILD #${env.BUILD_NUMBER} of Event Booking System was successful.
You can find the build details at: ${env.BUILD_URL}
Regards,
DevOps Team""", cc: '', from: '', replyTo: '', subject: "Build #${env.BUILD_NUMBER} Successful - Event Booking System",
            to: 'hamaldivyashwor2057@gmail.com'
        }
```
*   **`post { success { mail ... } }`**: If every stage in the pipeline completes successfully, Jenkins sends an automated email notifying the team that the deployment succeeded.

```groovy
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
```
*   **`post { failure { mail ... } }`**: If any stage fails (e.g., compile error, test failure, scanner issue), Jenkins skips the remaining stages and sends an email alerting the team of the failure, pointing them to the Jenkins logs.