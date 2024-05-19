# Temel imaj olarak OpenJDK kullan
FROM openjdk:17

# Uygulama dosyasını konteynere kopyala
COPY test.java /usr/src/myapp/src

# Çalışma dizinine geç
WORKDIR /usr/src/myapp

# Java dosyasını derle
RUN javac src/test.java

# Uygulamayı çalıştır
CMD ["java", "-cp", "/usr/src/myapp/src", "test"]
