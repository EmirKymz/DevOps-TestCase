pipeline {
    agent { label 'master' }
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/EmirKymz/test'
            }
        }
        stage('Compile') {
            steps {
                sh 'javac test.java'
            }
        }
        stage('Run') {
            steps {
                sh 'java test'
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    docker.build("emirkymz/java-uygulama:latest")
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerHub', passwordVariable: 'dockerHubPassword', usernameVariable: 'dockerHubUser')]) {
                    sh 'echo $dockerHubPassword | docker login -u $dockerHubUser --password-stdin docker.io'
                    sh 'docker tag emirkymz/java-uygulama:latest docker.io/emirkymz/java-uygulama:latest'
                    sh 'docker push emirkymz/java-uygulama:latest'
                }
            }
        }
        stage('Deploy') {
            agent { label 'debian-2' }
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerHub', passwordVariable: 'dockerHubPassword', usernameVariable: 'dockerHubUser')]) {
                    sh 'echo $dockerHubPassword | docker login -u $dockerHubUser --password-stdin docker.io'
                    sh 'docker pull emirkymz/java-uygulama:latest'

                    sh '''
                    if [ "$(docker ps -aq -f name=java-uygulama)" ]; then
                        docker rm -f java-uygulama
                    fi
                    '''

                    sh 'docker run -d --name java-uygulama -p 8080:8080 emirkymz/java-uygulama:latest'
                }
            }
        }
        stage('Verify') {
            agent { label 'debian-2' }
            steps {
                script {
                    sh "rm -rf /home/emircan1/return/*"
                    def containerId = sh(script: "docker ps -aqf name=java-uygulama", returnStdout: true).trim()
                    if (containerId.empty) {
                        error "Container 'java-uygulama' bulunamadi!"
                    }
                    sh "docker cp ${containerId}:/usr/src/myapp/test42 /home/emircan1/return"
                    sh "cat /home/emircan1/return/test42/readme.txt"
                }
            }
        }
    }
}
