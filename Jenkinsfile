pipeline {
    agent {label 'master'} // bu kısım jenkinsin hangi agent üzerinde çalışacağını belirler
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/EmirKymz/test' // bu kısım projenin github adresini belirtir ve projeyi çeker
            }
        }
        stage('Compile') {
            steps {
                sh 'javac test.java' // bu kısım projeyi derler
            }
        }
        stage('Run') {
            steps {
                sh 'java test' // bu kısım projeyi çalıştırır
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    docker.build("emirkymz/java-uygulama:latest") // bu kısım projeyi docker image haline getirir
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerHub', passwordVariable: 'dockerHubPassword', usernameVariable: 'dockerHubUser')]) { // bu kısım docker huba push etmek için gerekli olan kullanıcı adı ve şifreyi alır
                    sh "docker login -u ${env.dockerHubUser} -p ${env.dockerHubPassword} docker.io" // bu kısım docker huba giriş yapar
                    sh "docker tag emirkymz/java-uygulama:latest docker.io/emirkymz/java-uygulama:latest" // bu kısım docker image taglar
                    sh 'docker push emirkymz/java-uygulama:latest' // bu kısım docker image pushlar
                }
            }
        }
        stage('Deploy') {
            agent { label 'debian-2' } // bu sefer agent olarak debian-2 seçilmiştir çünkü docker containerı burada çalıştırılacak
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'dockerHub', passwordVariable: 'dockerHubPassword', usernameVariable: 'dockerHubUser')]) {
                        sh "docker login -u ${env.dockerHubUser} -p ${env.dockerHubPassword} docker.io"
                        sh 'docker pull emirkymz/java-uygulama:latest' // bu kısım docker image çeker

                        // Eğer java-uygulama adında bir container varsa siler
                        sh '''
                        if [ "$(docker ps -aq -f name=java-uygulama)" ]; then
                            docker rm -f java-uygulama
                        fi
                        '''

                        sh 'docker run -d --name java-uygulama -p 8080:8080 emirkymz/java-uygulama:latest'  // bu kısım docker containerı çalıştırır
                    }
                }
            }
        }
        stage('Verify') {
            agent { label 'debian-2' }
                steps {
                    script {
                        sh "rm -rf /home/emircan1/return/*" // bu kısım önceki testlerden kalan dosyaları siler
                        def containerId = sh(script: "docker ps -aqf name=java-uygulama", returnStdout: true).trim() // bu kısım container id alır 
                        if (containerId.empty) {
                            error "Container 'java-uygulama' bulunamadi!"
                        } // bu kısım container id boşsa hata verir
                        sh "docker cp ${containerId}:/usr/src/myapp/test42 /home/emircan1/return" // bu kısım containerdaki dosyaları alır
                        sh "cat /home/emircan1/return/test42/readme.txt" // bu kısım dosyayı okur
                    }
                }
            }
    }
}
// Yaptıklarımı stage stage anlatmak gerekirse
// 1. Checkout: Projeyi githubdan çeker
// 2. Compile: Projeyi derler
// 3. Run: Projeyi çalıştırır
// 4. Build Docker Image: Projeyi docker image haline getirir
// 5. Push Docker Image: Docker imageı docker huba push eder
// 6. Deploy: Docker imageı çeker ve containerı çalıştırır
// 7. Verify: Containerdaki dosyayı alır çalıştırır ve çıktıyı ikinci vmdeki klasöre yazar
