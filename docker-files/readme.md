# Rebuild and run, after change in any container. ex-frontend
```shell
docker-compose down
docker-compose build frontend
docker-compose up -d
```

# To down & Remove volumes
```shell
docker-compose down -v
docker-compose up -d
```

# Stop containers but keep volumes
```shell
docker-compose down
```

# Rebuild and restart
```shell
docker-compose build
docker-compose up -d
```

# Rebuild specific services
```shell
docker-compose build user-service order-service product-service inventory-service
```

# Restart only those services
```shell
docker-compose up -d user-service order-service product-service inventory-service
```

# Check status:
```shell
docker-compose ps
```

# Check logs if any service fails:
```shell
docker-compose logs service-name
```