# URL Shortener Application

This project is a URL shortener application built using Java, Spring Boot, Redis, and Prometheus for monitoring. It supports creating shortened URLs, resolving them, and caching frequently accessed data to optimize performance. Logging and monitoring are also included.

---

## Features
- REST API for shortening and resolving URLs.
- Caching with Redis for improved performance.
- Logging using Spring Boot and Actuator.
- Monitoring with Prometheus and Grafana.

---

## Prerequisites

Ensure you have the following installed on your system:

- Java 17 or higher
- Maven 3.8+
- Docker
- Redis server (local or Dockerized)

---

## Running the Application

### 1. Clone the Repository
```bash
git clone https://github.com/your-repo/url-shortener.git
cd url-shortener
```

### 2. Build the Application
Use Maven to package the application into a JAR file:
```bash
mvn clean package
```

The packaged JAR file will be available in the `target` directory.

### 3. Run the Application with Redis
#### Option A: Run Redis Locally
1. Start a Redis server:
   ```bash
   redis-server
   ```

2. Run the application:
   ```bash
   java -jar target/url-shortener-1.0.0.jar
   ```

#### Option B: Run Redis with Docker
1. Start a Redis container:
   ```bash
   docker run -d --name redis -p 6379:6379 redis
   ```

2. Run the application:
   ```bash
   java -jar target/url-shortener-1.0.0.jar
   ```

---

## Logging

The application uses Spring Boot's Actuator. Logs will output to the console and include:
- Metrics: JVM, memory, CPU, request counts, etc.
- Health checks: Application health and dependencies.
- Integration with external monitoring tools (e.g., Prometheus, Grafana).

---

## Monitoring with Prometheus and Grafana

### 1. Prometheus Setup

#### Run Prometheus with Docker
1. Place your Prometheus configuration in `src/main/resources/prometheus.yml`.
2. Start a Prometheus container:
   ```bash
   docker run -d --name prometheus -p 9090:9090 \
       -v $(pwd)/src/main/resources/prometheus.yml:/etc/prometheus/prometheus.yml \
       prom/prometheus
   ```
3. Access the Prometheus dashboard:
   ```
   http://localhost:9090
   ```

### 2. Grafana Setup

#### Run Grafana with Docker
1. Start a Grafana container:
   ```bash
   docker run -d --name grafana -p 3000:3000 grafana/grafana
   ```
2. Access the Grafana dashboard:
   ```
   http://localhost:3000
   ```
    - Default username: `admin`
    - Default password: `admin`
3. Add Prometheus as a data source and import pre-built dashboards for JVM or Spring Boot monitoring.

---

## API Endpoints

### 1. Shorten URL
**POST /api/v1/shorten**
- Request:
  ```json
  {
      "originalUrl": "https://example.com"
  }
  ```
- Response:
  ```json
  {
      "shortUrl": "abc123"
  }
  ```

### 2. Resolve URL
**GET /{shortUrl}**
- Response:
  ```json
  {
      "originalUrl": "https://example.com"
  }
  ```

---

## Configuration

### Environment Variables
The application can be configured using environment variables:
- `DB_URL`: Database URL (e.g., `jdbc:h2:mem:testdb`)
- `DB_USERNAME`: Database username
- `DB_PASSWORD`: Database password
- `REDIS_HOST`: Redis host (default: `localhost`)
- `REDIS_PORT`: Redis port (default: `6379`)

## Troubleshooting

### Common Issues

1. **Redis Connection Error**
   Ensure the Redis server is running and accessible at the configured host and port.

2. **Prometheus Configuration Not Loaded**
   Check that the Prometheus container is using the correct `prometheus.yml` file and the scrape target is correctly configured.

3. **Application Fails to Start**
   Verify all required environment variables are set.