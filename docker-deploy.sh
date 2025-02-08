# Stop containers but keep volumes
docker compose down

# Rebuild and restart
docker compose build
docker compose up -d