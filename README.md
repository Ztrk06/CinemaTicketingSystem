# Cinema Ticketing System

A console-based cinema ticket booking system written in Java (Dokuz Eylül Cinema Ticket System). Users can browse movies and sessions, pick seats, pay with a credit card and manage their tickets. Admins can manage movies and sessions.

## Features

- User registration and login (plus a built-in admin account)
- Movie and session listing
- Seat selection with a colored seat map
- Ticket booking, cancelling and payment
- Student and standard pricing
- Data stored in CSV files under `data/`

## Architecture & Design Patterns

The code follows a layered structure under `src/cinema`:

| Layer | Package | Description |
|-------|---------|-------------|
| Presentation | `cinema.presentation` | Console menus (`Main`, `UserMenu`, `AdminMenu`) |
| Service | `cinema.service` | Business logic and repository interfaces |
| Domain | `cinema.domain` | Entities (`Movie`, `Session`, `Ticket`, `User`, ..) |
| Infrastructure | `cinema.infrastructure` | CSV-based repository implementations |

Design patterns used:

- **State**: ticket lifecycle (`AvailableState`, `LockedState`, `BookedState`, `CancelledState`)
- **Strategy**: pricing (`StandardPricing`, `StudentPricing`)
- **Facade**: `BookingFacade` combines the ticket and payment services
- **Factory**: `RepositoryFactory` creates the repositories
- **Repository**: interfaces in the service layer, file-based implementations in infrastructure

## Getting Started

Requirements: JDK 17 or later.

### IntelliJ IDEA

Open the project folder and run `cinema.presentation.Main`.

### Command line

Run these from the project root so the `data/` folder can be found:

```bash
javac -d out $(find src -name "*.java")
java -cp out cinema.presentation.Main
```

On Windows PowerShell:

```powershell
javac -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp out cinema.presentation.Main
```

### Demo Accounts

| Role | Username | Password |
|------|----------|----------|
| Admin | `admin` | `admin123` |
| User | `deniz.aksoy@example.com` | `Kx7pQ2mv` |
| User | `selin.yalcin@example.com` | `tR4wZ9nb` |

## Sample Data

> **Note:** All data in the `data/` folder (users, credit cards, movies, sessions and tickets) is **AI-generated sample data** for demonstration only. It does not belong to real people, and the credit card numbers are not real.

## Contributors

- [Baran Öztürk](https://github.com/Ztrk06)
- [AlihaydarObay](https://github.com/AlihaydarObay)
- [ArdaBerke4](https://github.com/ArdaBerke4)
