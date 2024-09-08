# Structure Company App

**Structure Company App** to aplikacja napisana w Spring Boot, służąca do zarządzania strukturą firmy, w tym jej działami, zespołami, projektami oraz menedżerami . Aplikacja oferuje operacje CRUD (tworzenie, odczyt, aktualizacja, usuwanie) na encjach firmowych i jest zintegrowana z bazą danych PostgreSQL. Całość jest konteneryzowana za pomocą Dockera, co ułatwia wdrażanie i uruchamianie aplikacji.

## Funkcjonalności

- Operacje **CRUD** dla firm które zawierają działy, zespoły, projekty i menedżerów.
- **Integracja z bazą danych** PostgreSQL.
- **REST API** obsługujące format JSON.
- Dokumentacja API za pomocą **Swagger/OpenAPI**.
- **Konteneryzacja** aplikacji za pomocą Dockera (wielostopniowa budowa).
- **Testy jednostkowe i integracyjne** z wykorzystaniem JUnit i Mockito.
- Obsługa **Java 21** z Mavenem do zarządzania zależnościami i budową projektu.

## Technologie

- **Java 21**: Najnowsza wersja języka Java.
- **Spring Boot**: Framework używany do budowy aplikacji.
- **Maven**: Narzędzie do zarządzania zależnościami i budowy aplikacji.
- **PostgreSQL**: Relacyjna baza danych do przechowywania danych firmowych.
- **IntelliJ IDE**: środowisko programistyczne (IDE) 
- **Docker** oraz **Docker Compose** narzędzia do uruchomienia aplikacji w kontenerach.
- **Swagger**: Dokumentacja i testowanie API.
- **JUnit & Mockito**: Frameworki do testowania aplikacji.

## Uruchomienie aplikacji

### 1. Klonowanie repozytorium

Aby sklonować repozytorium z GitHub:

```bash
git clone https://github.com/ewkasob/structure-company-app.git
```

### 2. Uruchamianie aplikacji z Dockerem

Aby uruchomić aplikację z bazą danych PostgreSQL za pomocą Dockera:
```bash
docker-compose up --build
```

Aplikacja będzie dostępna pod adresem **http://localhost:8085**

Aby zatrzymać kontenery, uruchom:

```bash
docker-compose down
```
### 3. Uruchamianie testów

Aby uruchomić testy jednostkowe i integracyjne, użyj polecenia:

```bash
mvn test
```

### 4. Dokumentacja Swagger

Dokumentacja API, wraz z możliwością testowania, jest dostępna pod adresem:

**http://localhost:8085/swagger-ui.html**
## Konfiguracja

### 1. Konfiguracja Dockera

Aplikacja jest skonfigurowana do uruchamiania za pomocą Dockera, co ułatwia wdrażanie i zarządzanie zależnościami.
Plik **docker-compose.yml** zawiera konfigurację dla aplikacji oraz bazy danych PostgreSQL. 
Plik **Dockerfile** to plik tekstowy używany do automatyzacji procesu budowania obrazów Docker

### 2. Konfiguracja bazy danych

Aplikacja domyślnie jest skonfigurowana do pracy z bazą danych PostgreSQL.
Dane konfiguracyjne znajdują się w pliku **application.yml**.

## Punkty końcowe API

   - **GET /api/structure/companies**: Pobiera listę wszystkich firm.
   - **GET /api/structure/companies/{id}**: Pobiera szczegóły konkretnej firmy na podstawie jej ID.
   - **POST /api/structure/companies**: Tworzy nową firmę.
   - **PUT /api/structure/companies/{id}**: Aktualizuje dane istniejącej firmy.
   - **DELETE /api/structure/companies/{id}**: Usuwa firmę.
