#!/bin/bash
cd D:/Kursach-4-0/docker
docker-compose down
docker rmi $(docker images -q) -f
docker builder prune -f
mkdir -p D:/Kursach-4-0/backend/backups
touch D:/Kursach-4-0/backend/commercial_offer.db
docker-compose up --build -d
echo "Waiting for backend to be healthy..."
until docker inspect --format='{{.State.Health.Status}}' $(docker ps -q --filter "name=backend") | grep -q "healthy"; do
    sleep 1
done
echo "Application is running at http://localhost:3000"