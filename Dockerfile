# Temel imaj olarak OpenJDK kullan 
# alpine kullanmamım temel sebebi daha az pakete sahip olması
#FROM openjdk:17
FROM openjdk:17-jdk-alpine
# Uygulama dosyasını konteynere kopyala
COPY test.java /usr/src/myapp/
# Çalışma dizinine geç
WORKDIR /usr/src/myapp
# Java dosyasını derle
RUN javac test.java
# Uygulamayı çalıştır
CMD ["java", "test"]
