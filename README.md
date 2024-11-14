# Weather Viewer

Weather Viewer is a Java-based web application for viewing weather forecasts for multiple locations. It provides a user-friendly interface to search for locations and display detailed weather information, including temperature, weather descriptions, and more.
![weather-viewer-home](https://github.com/user-attachments/assets/3b3f7f53-aa27-4372-a3bd-b6e52341ba84)
![weather-viewer-search](https://github.com/user-attachments/assets/5f96d9b5-7e0c-419e-b05b-d7dcb6e6bb54)

## Features

- **Weather Search**: Search for locations and view weather details like temperature, feels-like temperature, and weather conditions.
- **Location Details**: Provides precise geographical data such as latitude, longitude, and region information.
- **Responsive Design**: Optimized for both desktop and mobile devices.
- **API Integration**: Uses OpenWeatherMap API for weather data.
- **Docker Support**: Easily set up and run with `docker-compose` for development and production environments.

## Prerequisites

- **Java 17+**
- **Gradle**
- **Docker and Docker Compose**
- **PostgreSQL** (or Dockerized PostgreSQL)

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/leofinder/weather-viewer.git
cd weather-viewer
```

### Configuration

1. **Environment Variables**:
   - Copy the `.env.example` file to `.env`:
     ```bash
     cp .env.example .env
     ```
   - Edit the `.env` file and replace placeholders with your values, including your OpenWeatherMap API key.

2. **Development Environment**:
   - Use `docker-compose.dev.yml` for development:
     ```bash
     docker compose -f docker-compose.dev.yml up --build
     ```
   - Access the app at `http://localhost:8080`.

3. **Production Environment**:
   - Use `docker-compose.prod.yml` for deployment:
     ```bash
     docker compose -f docker-compose.prod.yml up --build
     ```
   - Access the app at your server's public IP or domain.

### Setting Environment Variables in Development

For local development, ensure you set up the environment variables as defined in the `.env.example` file. These variables are crucial for running the application and connecting to the database.

#### Steps to Set Up Environment Variables:

1. **Copy the `.env.example` File**:
   ```bash
   cp .env.example .env
   ```

2. **Edit the `.env` File**:
   Open the `.env` file in a text editor and replace placeholder values, such as `input_your_open_weather_api_key`, with your actual values.

3. **Export Environment Variables (Linux/Mac)**:
   ```bash
   export $(grep -v '^#' .env | xargs)
   ```

4. **Export Environment Variables (Windows)**:
   Use `set` or `setx` commands:
   ```cmd
   set POSTGRES_HOST=db
   set POSTGRES_PORT=5432
   set POSTGRES_DB=weather_viewer
   set POSTGRES_USER=postgres
   set POSTGRES_PASSWORD=postgres
   set WEATHER_API_KEY=your_api_key
   ```

5. **Use the IntelliJ IDEA EnvFile Plugin**:
   To simplify variable management in development, you can use the [EnvFile Plugin](https://plugins.jetbrains.com/plugin/7861-envfile). This plugin allows you to load environment variables from `.env` files directly in your IntelliJ IDEA run configurations.

By following these steps, your development environment will be properly configured, allowing smooth integration with the application.
