# MdEditor-Backend

API zur Verwaltung und Versionierung von Markdown-Dokumenten

## Projektstruktur & Features

```text
md-editor-backend/
├─ auth/        - Registrierung & Login mit JWT / User-Management
├─ document/    - CRUD für Dokumente inkl. Versionierung
├─ render/      - Md- und HTML-Render & Export
├─ common/
   ├─ internal/ - Infrastruktur (Error Handling, OpenAPI)
   ├─ api/      - Öffentliche Ressourcen
```

## Architekturhinweise

Modulgrenzen werden mit Spring Modulith validiert
Nur `common.api` ist öffentlich, Rest bleibt intern

## Installation und Start

Java 21 oder höher, sollte lokal installiert sein

```bash
git clone https://gitlab.hs-flensburg.de/maar2838/softwarearchitektur-hausarbeit-sose25.git
cd softwarearchitektur-hausarbeit-sose25/md-editor-backend
./gradlew bootRun
```

Danach erreichbar unter: [http://localhost:8080](http://localhost:8080)

## Dokumentation

Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## Datenbank H2

- Web-UI: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- JDBC: `jdbc:h2:mem:markdowndb`
- Benutzer: `sa`, Passwort: _(leer)_

## Team

Markus Artemov, Johann Rusch
