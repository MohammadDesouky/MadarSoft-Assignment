# MadarSoft Assignment - Simple Android User Management App

A modern Android application using Clean Architecture, MVI pattern, and Room database for local user data management.

## Features

- Add users with name, age, job title, and gender
- Display list of saved users
- Local storage with Room database
- Form validation with error messages
- Material 3 design
- Keyboard navigation between fields

## Architecture

**Clean Architecture + MVI Pattern:**

### Data Layer
- `UserDao` - Room database access object
- `UserDatabase` - Room database configuration
- `User` entity - Database table structure
- `UserRepositoryImpl` - Data operations implementation
- `UserMapper` - Entity to domain model conversion

### Domain Layer
- `UserModel` - Business entity
- `UserRepository` - Data contract interface
- `GetAllUsersUseCase` - Retrieve users business logic
- `InsertUserUseCase` - Add user business logic

### Presentation Layer
- `AddUserScreen/ViewModel` - User input with MVI state management
- `DisplayUsersScreen/ViewModel` - User list display
- `AddUserIntent/State` - MVI pattern for add user flow
- `DisplayUsersIntent/State` - MVI pattern for display flow

### DI & Common
- `DatabaseModule` - Hilt database dependencies
- `RepositoryModule` - Hilt repository dependencies
- `StringProvider` - Localized string access
- `AppNavigation` - Compose navigation setup

## Technology Stack

- **Kotlin** - Modern Android development
- **Jetpack Compose** - Declarative UI framework
- **Material 3** - Latest Material Design
- **Room Database** - Local data persistence
- **Hilt** - Dependency injection
- **Coroutines & StateFlow** - Async programming and state management

## Testing

Comprehensive unit testing with 100% coverage:
- ViewModel tests for MVI state transitions
- Repository tests for data operations
- Use case tests for business logic
- Tools: JUnit, Mockito-Kotlin, Coroutines Test

## Getting Started

### Prerequisites
- Android Studio Koala | 2024.1.1+
- JDK 11+
- Android SDK 36+
- Minimum SDK 24

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/MohammadDesouky/MadarSoft-Assignment.git
   ```
2. Open in Android Studio
3. Sync Gradle dependencies
4. Run on device or emulator

### Build Commands
```bash
./gradlew assembleDebug          # Build debug APK
./gradlew testDebugUnitTest      # Run unit tests
./gradlew jacocoTestReport       # Generate coverage report
```
