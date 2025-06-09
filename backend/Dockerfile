# Etap budowania aplikacji Java
FROM openjdk:21-jdk-slim AS build

# Instalacja Maven
RUN apt-get update && apt-get install -y maven

# Ustawienie katalogu roboczego
WORKDIR /app

# Kopiowanie pliku pom.xml i zależności
COPY pom.xml .

# Instalowanie zależności (jeżeli używasz Mavena)
RUN mvn dependency:go-offline

# Kopiowanie kodu źródłowego aplikacji
COPY src ./src

# Budowanie aplikacji
RUN mvn clean package -DskipTests

# Tworzenie obrazu produkcyjnego
FROM openjdk:21-jdk-slim

WORKDIR /app

# Kopiowanie zbudowanego pliku JAR
COPY --from=build /app/target/*.jar app.jar

# Uruchamianie aplikacji
ENTRYPOINT ["java", "-jar", "app.jar"]
