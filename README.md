# Permission Service (Java)
Eigenes Lernprojekt zur Verwaltung von Benutzern, Gruppen und Berechtigungen.
Demonstriert Grundlagen von OOP, Service-Architektur, Tests und hexagonaler Architektur.

---

## Features
**User- und Group-Modelle** mit UUIDs 

**Berechnung effektiver Permissions** pro User

**Service-Layer & Interfaces** für saubere Trennung der Business-Logik

**Unit-Tests** (UserServiceTest)

**Controller / Service / Domain** Struktur

Umsetzung nach **Prinzipien der hexagonalen Architektur** (Ports & Adapters)

---

## Projektstruktur (Mini-Diagramm)

permission-service/ ├─ src/ │  ├─ main/java/me/sarah/permissionservice/ │  │  ├─ domain/ │  │  │  ├─ model/ (User, Group) │  │  ├─ service/ (UserService, UserServiceImpl, GroupService) │  │  ├─ controller/ (UserController) │  │  └─ application/ (ApplicationService) │  └─ test/java/me/sarah/permissionservice/ (UserServiceTest) ├─ README.md └─ pom.xml / build.gradle

---

## Technologien
Java (OOP, Collections, Interfaces)

Spring Boot (Grundlagen)

JUnit (Unit-Tests)

IntelliJ IDEA

Git & GitHub

---

## Ziel & Motivation
Dieses Projekt dient als **Demonstrationsprojekt für Junior Softwareentwicklung**.

Ziel: saubere Architektur, testbare Business-Logik und praxisnahes Verständnis von Service-Layern.
Es zeigt, dass ich als Quereinsteigerin bereits in der Lage bin, strukturierte Software zu entwickeln.

---

## Nächste Schritte
Erweiterung der Services (z.B. GroupService)
Weitere Unit-Tests zur Abdeckung von Business-Logik
Integration von Spring Boot Komponenten bei Bedarf
