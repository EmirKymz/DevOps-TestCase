# Temel imaj olarak OpenJDK kullan
FROM openjdk:17

# Uygulama dosyasını konteynere kopyala
COPY App.java /usr/src/myapp/src

# Çalışma dizinine geç
WORKDIR /usr/src/

# Java dosyasını derle
RUN javac App.java

# Uygulamayı çalıştır
CMD ["java", "-cp", "/usr/src/myapp/src", "App"]
