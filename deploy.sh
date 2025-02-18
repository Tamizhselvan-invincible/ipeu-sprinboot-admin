#!/bin/bash

echo "🚀 Building the Spring Boot JAR..."
mvn clean package -DskipTests

echo "🛑 Stopping existing containers..."
docker compose down

echo "🔨 Building new Docker image..."
docker compose build

echo "🚀 Starting Docker containers..."
docker compose up -d

echo "✅ Deployment completed!"
