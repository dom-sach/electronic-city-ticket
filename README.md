# Elektroniczny bilet miejski
System umożliwia użytkownikom korzystanie z wirtualnych biletów do poruszania się po mieście komunikacją zbiorową. Obejmuje trzy główne role:
- Pasażer: może się zarejestrować, przeglądać ofertę biletową, kupować bilety oraz przeglądać ich historię.
- Bileter: może sprawdzać ważność biletu na podstawie unikalnego kodu oraz pojazdu.
- Administrator: może zarządzać ofertą biletową (dodawanie/usuwanie typów biletów).

Każdy bilet posiada unikalny kod i może być:
- jednorazowy (ważny tylko po skasowaniu w danym pojeździe),
- czasowy (ważny przez określony czas od skasowania),
- okresowy (ważny w określonym przedziale czasowym od daty zakupu).

System umożliwia:
- zakup biletu,
- aktywację biletu (np. przez kasownik),
- weryfikację biletu,
- zarządzanie typami biletów (dla testów lub administracji),
- dokumentację API dostępną przez Swagger UI.

## Stack technologiczny
- **Backend**: Java 17+, Spring Boot 3
- **Baza danych**: PostgreSQL
- **Zarządzanie zależnościami**: Maven
- **Bezpieczeństwo**: Spring Security + JWT (rola pasażera i biletera)
- **API**: REST, dokumentowane automatycznie przez SpringDoc OpenAPI (Swagger UI)
- **Testy**: JUnit 5, Mockito

## Wymagania wstępne
- Java 21
- Maven (mvn)
- PostgreSQL
- IntelliJ IDEA (lub inny IDE)

## Uruchomienie

### Backend
Krok 1:
W Postgres utworzyć bazę danych i użytkownika:
```
CREATE DATABASE biletmiejski;
CREATE USER biletuser WITH PASSWORD 'biletpass';
GRANT ALL PRIVILEGES ON DATABASE biletmiejski TO biletuser;
ALTER DATABASE biletmiejski OWNER to biletuser;
```

Krok 2:
Skonfiguruj połączenie w src/main/resources/application.properties:
```
spring.datasource.url=jdbc:postgresql://localhost:5432/biletmiejski
spring.datasource.username=biletuser
spring.datasource.password=biletpass

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
server.port=8080
```

Krok 3:
Z poziomu folderu backend uruchomić projekt i poczekać aż pobierze wszystkie zależności:
```
mvn spring-boot:run
```
