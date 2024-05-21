pipeline {
    agent { label 'master' } // Jenkins master üzerinde çalıştırılacak
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/EmirKymz/test' // Git repositoryden kodları clone etmek için
            }
        }
        stage('Compile') {
            steps {
                sh 'javac test.java' // Java kodlarını compile ediyoruz
            }
        }
        stage('Run') {
            steps {
                sh 'java test' // Java kodlarını çalıştırıyoruz
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    docker.build("emirkymz/java-uygulama:latest") // Docker image oluşturuyoruz
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerHub', passwordVariable: 'dockerHubPassword', usernameVariable: 'dockerHubUser')]) // DockerHub credentials'larını güvenli bir şekilde kullanmak için
                {
                    sh 'echo $dockerHubPassword | docker login -u $dockerHubUser --password-stdin docker.io' // DockerHub'a login oluyoruz
                    sh 'docker tag emirkymz/java-uygulama:latest docker.io/emirkymz/java-uygulama:latest' // Docker image'ı taglıyoruz
                    sh 'docker push emirkymz/java-uygulama:latest' // Docker image'ı DockerHub'a pushluyoruz
                }
            }
        }
        stage('Deploy') {
            agent { label 'debian-2' } // Debian-2 üzerinde çalıştırılacak
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerHub', passwordVariable: 'dockerHubPassword', usernameVariable: 'dockerHubUser')]) {
                    sh 'echo $dockerHubPassword | docker login -u $dockerHubUser --password-stdin docker.io'
                    sh 'docker pull emirkymz/java-uygulama:latest' // DockerHub'dan image'ı çekiyoruz
                    
                    //Eğer container varsa silme işlemi yapıyoruz
                    sh '''
                    if [ "$(docker ps -aq -f name=java-uygulama)" ]; then
                        docker rm -f java-uygulama
                    fi
                    '''
                    //Docker container'ı çalıştırıyoruz
                    sh 'docker run -d --name java-uygulama -p 8080:8080 emirkymz/java-uygulama:latest'
                }
            }
        }
        stage('Verify') {
            agent { label 'debian-2' } // Debian-2 üzerinde çalıştırılacak 
            steps {
                script {
                    sh "rm -rf /home/emircan1/return/*" // Eğer önceki testlerde oluşturulan dosyalar varsa silme işlemi yapıyoruz
                    def containerId = sh(script: "docker ps -aqf name=java-uygulama", returnStdout: true).trim() // Docker container'ın ID'sini alıyoruz
                    if (containerId.empty) {
                        error "Container 'java-uygulama' bulunamadi!"
                    }
                    sh "docker cp ${containerId}:/usr/src/myapp/test42 /home/emircan1/return" // Docker container'ından dosyaları alıyoruz
                    sh "cat /home/emircan1/return/test42/readme.txt" // Dosyaların içeriğini kontrol ediyoruz
                }
            }
        }
    }
}

// burda sirasiyla checkout, compile, run, build docker image, push docker image, deploy ve verify adinda 7 adet stage tanimladim
// checkout stage'inde git repository'den kodlari clone ediyoruz
// compile stage'inde java kodlarini compile ediyoruz
// run stage'inde java kodlarini calistiriyoruz
// build docker image stage'inde docker image olusturuyoruz
// push docker image stage'inde docker image'i DockerHub'a pushluyoruz
// deploy stage'inde DockerHub'dan image'i cekip container'i calistiriyoruz
// verify stage'inde container'dan dosyalarimizi alip dosyalarin icerigini kontrol ediyoruz
