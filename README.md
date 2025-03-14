# Scoreboard CLI Application


This project provides a **Command-Line Interface (CLI)** for managing and interacting with a sports scoreboard. It allows users to start, update, and finish matches while providing live summaries of scores.
## Features
- **Start Match**: Create a new match by selecting teams.
- **Update Match**: Update scores for ongoing matches.
- **Finish Match**: Mark a match as finished and move it to history.
- **Live Match Summary**: View a summary of all active and completed matches.

## Prerequisites
- **Java 17+**
- **Gradle** (build tool)
- A terminal or IDE supporting Java execution.

## Installation
1. Clone the repository: git clone https://github.com/isesar/scoreboard1.1.git
2. Please make sure that you are using **develop** branch
2. Build and run
3. CLI app should start

## Running Tests
To run tests, please change {user.name} in build.gradle file to use your own local username

## Testing Frameworks Used
- **JUnit 5**: For unit testing the application.
- **Mockito**: For mocking dependencies in tests.

## Project Structure
- **cli**: Handles the command-line interface logic.
- **service**: Contains the `ScoreboardService`, which manages match lifecycle logic.
- **model**: Contains the models for Match and interface for scoreboard repository.
- **repository**: Following repository pattern, this folder contains in-memory repository for matches
- **enums**: Includes enumerations like `Team` for representing teams.
- **tests**: JUnit 5-based unit tests for ensuring application stability.
