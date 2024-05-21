# Adım 1: Sanal Makine Oluşturma

## 1.1. VirtualBox ile Sanal Makine Oluşturma
- Sanal makine terchimi debian'dan yana kullandım.
- Debian kurulumunda en az sayıda paket olması için kurulum sırasında gui dahil tüm paketleri kaldırdım.
- Daha güvenli bir sistem oluşturmak için lvm disk şifrelemesi yaptım.

# Adım 2: Sanal Makine Ayarlarının Yapılandırılması

# 2.1. Ağ Ayarlarının Yapılandırılması
- İlk ağ adaptörünü Köprü bağdaştırıcısı olarak ayarlandı. VirtualBox'ta sanal makineyi seçtikten sonra Ayarlar > Ağ > Şuna takılı > Köprü bağdaştırıcısı seçtim bu şekilde iki sanal makine arasında iletişim kurulabiliriz.

## 2.2. Güvenlik Ayarları
### 2.1.1. Sudo Kurulumu
- Güvenlik amacıyla sudo kuruldu ve sudoers dosyası düzenlendi.
- `apt-get install sudo`
- `visudo`
- `root ALL=(ALL:ALL) ALL` satırının altına aşağıdaki satır eklenir.
- `'kullaniciadi' ALL=(ALL:ALL) ALL` bu sayede kullanıcı sudo yetkisine sahip olur.

### 2.1.2. SSH Güvenliği
- `apt-get install openssh-server`
- SSH güvenliği için sshd_config dosyası düzenlendi.
- `nano /etc/ssh/sshd_config`
- Port XXXX olarak belirlendi.
- `systemctl restart sshd` yaptığımız değişiklikler kaydedilsin diye restart atıldı.
### 2.1.3. Güvenlik Duvarı
- `apt-get install ufw` güvenlik duvarı olan ufw (uncomplicated firewall) kuruldu.
- `ufw enable`
- `ufw allow 4243`
- `ufw allow 443` => Jenkins ssl Web sunucu için
    
## 2.3. Git, Docker Kurulumu
- `apt-get install git`
- `sudo apt install docker.io`
- `sudo systemctl start docker` Bu komut, Docker servisini başlatır. Docker servisi, Jenkins sunucusunda konteyner tabanlı uygulamaları oluşturmak ve çalıştırmak için kullanılır.
- `sudo systemctl enable docker` Bu komut, Docker servisinin otomatik olarak başlamasını sağlar. Bu komut, Docker servisinin sistem başlangıcında otomatik olarak başlamasını sağlar.
- `sudo usermod -aG docker 'username'` Bu komut, 'username' kullanıcısını Docker grubuna ekler. Bu işlem, 'username' kullanıcısının Docker komutlarını çalıştırmasına izin verir.

# Adım 3: Jenkins Kurulumu
## 3.1. Java, wget, gnupg ve jenkins kurulumu
- `apt-get install default-jdk` Bu komut, Jenkins sunucusunda Java çalışma zamanı ortamını (JRE) ve Java geliştirme kitini (JDK) indirir ve kurar.
- `apt-get install wget` wget komutu, internet üzerinden dosya indirmek için kullanılır. Jenkins paketlerini indirmek için gereklidir.
- `apt-get install gnupg` birazdan jenkinsi kurarken kullanacağımız apt-key add komutu için gereklidir. 
        
### 3.1.1. Jenkins paketinin indirilmesi
- `wget -q -O - https://pkg.jenkins.io/debian/jenkins.io.key | sudo apt-key add -` Bu komut Jenkins paketlerinin güvenliğini doğrulamak için Jenkins'in resmi GPG anahtarını indirir ve sisteminizdeki APT anahtar halkalarına ekler. Bu işlem, Jenkins paketlerinin güvenilir olduğunu doğrulamaya yardımcı olur ve güvenli bir kurulum sağlar.

- `sudo sh -c 'echo deb http://pkg.jenkins.io/debian-stable binary/ > /etc/apt/sources.list.d/jenkins.list'` Bu komut, Jenkins paketlerinin depo adresini /etc/apt/sources.list.d/jenkins.list dosyasına ekler. Bu dosya, APT paket yöneticisinin Jenkins paketlerini indireceği depo adresini içerir.

- `sudo apt-get update` Bu komut, APT paket yöneticisini günceller ve Jenkins paketlerinin depo adresini güncellemesini sağlar.
- `sudo apt-get install jenkins` Bu komut, Jenkins paketlerini indirir ve sisteminize kurar. Jenkins'in tüm bağımlılıklarını ve gereksinimlerini karşılar.
- `sudo systemctl start jenkins` Bu komut, Jenkins servisini başlatır. Jenkins servisi, Jenkins sunucusunu çalıştırır ve Jenkins web arayüzüne erişim sağlar.
- `sudo systemctl enable jenkins` Bu komut, Jenkins servisinin otomatik olarak başlamasını sağlar. Bu sayede, sunucunuz yeniden başlatıldığında Jenkins servisi otomatik olarak başlar.
- `sudo systemctl status jenkins` Bu komut, Jenkins servisinin durumunu kontrol eder. Jenkins servisinin çalışıp çalışmadığını ve hata olup olmadığını kontrol eder.

## 3.2. SSL yapılandırması
- `sudo apt install nginx -y` Bu komut, Nginx web sunucusunu indirir ve sisteminize kurar. Nginx, Jenkins web sunucusuna SSL şifrelemesi eklemek için kullanılır.
- `sudo openssl req -new -x509 -nodes -out /etc/nginx/jenkins.crt -keyout /etc/nginx/jenkins.key -days 365` Bu komut, Jenkins web sunucusuna SSL sertifikası oluşturur. Bu komut, Jenkins web sunucusuna HTTPS üzerinden güvenli bir bağlantı sağlar. Bu komutta yazdığımız parametrelerin anlamları şunlardır:
- '-new' : Bu parametre, yeni bir SSL sertifikası oluşturur.
- '-x509' : Bu parametre, bir X.509 sertifikası oluşturur.
- '-nodes' : Bu parametre, SSL sertifikasının şifrelenmemesini sağlar. Bu parametre, SSL sertifikasının şifrelenmemesini sağlar.
- '-out /etc/nginx/jenkins.crt' : Bu parametre, SSL sertifikasının çıktı dosyasını belirtir. Bu dosya, Jenkins web sunucusuna SSL şifrelemesi sağlar.
- '-keyout /etc/nginx/jenkins.key' : Bu parametre, SSL anahtarının çıktı dosyasını belirtir. Bu dosya, Jenkins web sunucusuna SSL şifrelemesi sağlar.
- '-days 365' : Bu parametre, SSL sertifikasının geçerlilik süresini belirtir. Bu parametre, SSL sertifikasının 365 gün boyunca geçerli olmasını sağlar.
- Bu komut, Jenkins web sunucusuna SSL şifrelemesi ekler ve Jenkins web sunucusuna erişim sağlar.

- `sudo nano /etc/nginx/sites-available/jenkins` Bu komut, Nginx web sunucusunun Jenkins yapılandırma dosyasını açar. Bu dosya, Jenkins web sunucusuna SSL şifrelemesi ekler ve Jenkins web sunucusuna erişim sağlar.
```
server {
        listen 80;
        server_name jenkins.example.com;
        return 301 https://$host$request_uri;
    }

    server {
        listen 443 ssl;
        server_name jenkins.example.com;

        ssl_certificate /etc/nginx/jenkins.crt;
        ssl_certificate_key /etc/nginx/jenkins.key;

        location / {
            proxy_pass http://localhost:8080;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```
- `sudo ln -s /etc/nginx/sites-available/jenkins /etc/nginx/sites-enabled/` Bu komut, Nginx web sunucusunun Jenkins yapılandırma dosyasını etkinleştirir. Bu sayede, Jenkins web sunucusuna erişim sağlar.
- `sudo systemctl restart nginx` Bu komut, Nginx web sunucusunu yeniden başlatır. Bu komut, Jenkins web sunucusuna SSL şifrelemesi ekler ve Jenkins web sunucusuna erişim sağlar.
- artık tarayıcıdan https://ipadresi/ adresine giderek Jenkins web arayüzüne erişebilirsiniz.

# Adım 4: Jenkins
- Jenkins'e ilk giriş yaptığımızda karşımıza gelen ekran şifre ekranı oluyor. Bu şifreyi almak için terminal ekranına aşağıdaki komutu yazıyoruz.
- `sudo cat /var/lib/jenkins/secrets/initialAdminPassword` Bu komut, Jenkins web arayüzüne erişim için gerekli olan şifreyi görüntüler. Bu şifre, Jenkins web arayüzüne ilk girişte kullanılır. Path sizde farklı olabilir arayüz üzerinden pathi görebilirsiniz.
- Jenkinste kullanıcı oluşturuken kullanıcı adı ve şifre belirleyip devam ediyoruz.
- Pluginlerin yüklenmesi için Install suggested plugins seçeneğini seçiyoruz. (Jenkins'te yeni olduğum için bu seçeneği seçtim. Daha sonra gelişmiş kullanıcılar için Install selected plugins seçeneği seçilebilir.)
- Jenkins arayüzüne girdikten sonra Manage Jenkins > System > Jenkins URL kısmına https://ipadresi/ yazıyoruz. localhost yazarsak sorunlarla karşılaşabiliriz. Reverse proxy sorunları olabilir.

- Jenkinste Tmp yetersiz uyarısı almıştım aşağıdaki adımları uygulayıp problemimi çözdüm.
- `https://ipadresi/computer/configure`
- Free Temp Space başlığı altındaki Don't mark agents temporarily offline seceneğini işaretleyebilirsiniz. Bu sayede TMP yetersiz uyarısını kaldırabilirsiniz.

## 4.1. Credentials
### 4.1.1. Docker Hub Credentials
- Jenkins arayüzünde Credentials > System > Global credentials (unrestricted) > Add Credentials seçeneğine tıklıyoruz.
- ilk olarak Username with password seçeneğini seçiyoruz.
- Username kısmına Docker Hub kullanıcı adımızı, Password kısmına Docker Hub şifremizi giriyoruz.
- ID kısmına bir isim veriyoruz. (Örneğin: dockerhub-credentials)
- Create seçeneğine tıklıyoruz.

### 4.1.2. Sanal makine SSH Credentials
- bu kısım ikinci sanal makinede çalıştırılacak olan Jenkins pipeline için gerekli olan SSH Credentials'ı eklemek için kullanılır.
- Jenkins arayüzünde Credentials > System > Global credentials (unrestricted) > Add Credentials seçeneğine tıklıyoruz.
- ilk olarak SSH Username with private key seçeneğini seçiyoruz.
- Username kısmına sanal makinedeki kullanıcı adımızı, Private Key kısmına private keyimizi yapıştırıyoruz.
- ID kısmına bir isim veriyoruz. (Örneğin: ssh-credentials)
- Create seçeneğine tıklıyoruz.

### 4.1.3 Agent Node oluşturma
- Jenkins arayüzünde Manage Jenkins > Nodes > New Node seçeneğine tıklıyoruz.
- Node Name kısmına bir isim veriyoruz. (Örneğin: agent-node)
- Permanent Agent seçeneğini seçiyoruz.
- Remote root directory kısmına agentin çalışmasını istediğiniz pathi yazıyoruz.(Örneğin: /home/agent-node)
- Launch method kısmında Launch agents via SSH seçeneğini seçiyoruz.
- Host kısmına agentin çalışmasını istediğiniz sanal makinenin IP adresini yazıyoruz.
- Credentials kısmında Add seçeneğine tıklıyoruz.
- Az önce oluşturduğumuz SSH Credentials'ı seçiyoruz.
- Host Key Verification Strategy kısmında Non verifying Verification Strategy seçeneğini seçiyoruz.
- Advanced kısmında Port kısmını sanal makinede ayarladığınız port numarasına göre değiştiriyoruz.
- Save seçeneğine tıklıyoruz.

## 4.2. Jenkins Pipeline
### 4.2.1. Jenkinsfile Oluşturma
- Jenkins arayüzünde New Item > Pipeline seçeneğine tıklıyoruz.
- Name kısmına bir isim veriyoruz. (Örneğin: pipeline)
- OK seçeneğine tıklıyoruz.
- Pipeline > Definition kısmında Pipeline script from SCM seçeneğini seçiyoruz.
- Çünkü Jenkinsfile'ı bir git deposundan alacağız.
- SCM kısmında Git seçeneğini seçiyoruz.
- Repository URL kısmına Jenkinsfile'ın bulunduğu git deposunun URL'sini yapıştırıyoruz.
- Repository açık bir depo olduğu için Credentials kısmında None seçeneğini seçebiliriz.
- Branch Specifier kısmına Jenkinsfile'ın bulunduğu branch'ı yazıyoruz. (Örneğin: main)
- Script Path kısmına Jenkinsfile'ın dosya adını yazıyoruz. (Örneğin: Jenkinsfile)
- Save seçeneğine tıklıyoruz.
- Bu sayede Git deposundaki Jenkinsfile'ı Jenkins pipeline olarak çalıştırabiliriz.
- pipeline https://github.com/EmirKymz/test/blob/main/Jenkinsfile adresinde açıklamalı olarak bulunmaktadır ondan dolayı burda tekrar açıklamadım.

## 4.3. Jenkins Pipeline Çalıştırma
- Jenkins arayüzünde pipeline'ı oluşturduktan sonra Build Now seçeneğine tıklıyoruz.
- Bu sayede Jenkins pipeline'ı çalıştırılır ve pipeline'ın adımları sırasıyla gerçekleştirilir.
- Pipeline sırasıyla şu işleri gerçekleştiriyor.
- Git Reposunu clone eder.
- java kodunu derleyip çalıştırır.
- Dockerfile'ı build eder.
- Docker image'ı Docker Hub'a push eder.
- Diğer sanal makinede Docker image'ını Docker Hub'tan çeker ve Docker konteynerını çalıştırır.
- Konteynerın içine girip çıktıyı ana makineye gönderir.


# Adım 5: Docker
## 5.1. DockerHub
### 5.1.2. Docker Hub Repository Oluşturma
- Create Repository seçeneğine tıklayın.
- Repository Name kısmına bir isim verin. (Örneğin: my-repo)
- Visibility kısmında Public veya Private seçeneğini seçin.
- Create seçeneğine tıklayın.
- Bu sayede Docker Hub'ta yeni bir repository oluşturabilirsiniz.
## 5.2. Dockerfile Oluşturma
- Dockerfile, Docker konteynerlerini oluşturmak için kullanılan bir dosyadır. Dockerfile, Docker konteynerlerinin nasıl oluşturulacağını ve yapılandırılacağını belirtir. Ben basit bir Dockerfile oluşturdum o yüzden burda açıklamadım. Github adresimde bulabilirsiniz.

# Adım 6: Java
- Basit bir java kodu yazdım temel olarak bir directory oluşturup içine bir dosya oluşturuyor ve dosyanın içine hello world yazıyor.

# Adım 7: Bash Script
`sudo nano /etc/systemd/system/run-jenkins-pipeline.service`
```
[Unit]
Description=Run Jenkins Pipeline at Startup  => Servis açıklaması
After=network.target jenkins.service  => Bu direktif, bir servisin başka bir servis veya hedef (target) başlatıldıktan sonra başlatılmasını belirtir. Ancak, bu sadece başlatılma sırasını tanımlar; diğer servisin çalışmasının zorunlu olduğunu belirtmez.
Requires=jenkins.service  => Bu direktif, bir servisin başka bir servisin çalışmasına bağlı olduğunu belirtir. Eğer belirtilen bağımlı servis başlatılamazsa, bu servis de başlatılmaz.
[Service]
ExecStart=/bin/bash /home/emircan/startup.sh  => Servisin çalıştırılacak komut
[Install]
WantedBy=multi-user.target  => Bu direktif, bir servisin hangi hedeflerde başlatılacağını belirtir. Bu durumda, servis multi-user.target hedefinde başlatılacaktır.
```
- Scripti ilk yazdığımda after ve requires kısmını yazmamıştım ve script çalışmıyordu. Bunun sebebi ise jenkinsin daha yavaş başlaması ve scriptin jenkins başlamadan çalışmaya başlamasıydı.
    
- `sudo systemctl enable run-jenkins-pipeline.service`
- `sudo systemctl start run-jenkins-pipeline.service`
- `nano startup.sh`
- `curl -k -X POST "$JENKINS_URL/job/$JOB_NAME/build" --user $USER:$API_TOKEN`
- $ işareti değişken tanımlamak için kullanılır. Bu sayede JENKINS_URL, JOB_NAME, USER ve API_TOKEN tanımlayabiliriz.
- -k flagi curl komutunda kullanıldığında, curl'un SSL sertifikalarını kontrol etmemesini sağlar. Bu flagi kullanmamım sebebi SSL sertifikamız güvenilir bir otorite tarafından imzalanmadığı içindir.
- jenkins cli kullanmama sebebimde aynı şekilde SSL sertifikamız güvenilir bir otorite tarafından imzalanmadığı için çok fazla hata ile karşılaştım bende alternatif bir yol olarak curl komutunu kullandım.
- Jenkins URL, JOB_NAME, USER ve API_TOKEN değişkenlerini tanımlamak için aşağıdaki adımları uygulayabilirsiniz.
