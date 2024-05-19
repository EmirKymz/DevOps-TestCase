# Temel imaj olarak OpenJDK kullan
FROM openjdk:17

# Uygulama dosyasını konteynere kopyala
COPY test.java /usr/src/myapp/

# Çalışma dizinine geç
WORKDIR /usr/src/myapp

# Java dosyasını derle
RUN javac test.java

# Uygulamayı çalıştır
CMD ["java", "test"]
