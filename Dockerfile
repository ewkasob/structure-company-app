
# Używamy oficjalnego obrazu Maven do kompilacji aplikacji z Java 21
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build

# Ustawienie katalogu roboczego
WORKDIR /app

# Kopiowanie plików projektu (Maven + aplikacja)
COPY pom.xml .
COPY src ./src

# Budowanie aplikacji
RUN mvn clean package -DskipTests

# Drugi etap: Używamy oficjalnego obrazu Javy 21
FROM eclipse-temurin:21-jdk-alpine

# Ustawienie katalogu roboczego
WORKDIR /app

# Kopiowanie skompilowanej aplikacji z pierwszego etapu
COPY --from=build /app/target/structure-app-1.0.jar .

# Ustawienie portu, na którym aplikacja będzie działać
EXPOSE 8085

# Komenda do uruchomienia aplikacji
ENTRYPOINT ["java", "-jar", "structure-app-1.0.jar"]

