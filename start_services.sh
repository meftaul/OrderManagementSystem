#!/bin/bash

mkdir -p logs

log() {
    echo "$(date '+%Y-%m-%d %H:%M:%S') - $1" | tee -a logs/start_services.log
}

log "Starting Kafka..."
docker run -p 9092:9092 apache/kafka:3.7.1 &> logs/kafka.log &
log "Kafka started."

log "Starting oms-config-server..."
cd oms-config-server
./gradlew bootRun &> ../logs/oms-config-server.log &
log "oms-config-server started."
cd ..

log "Starting order-service..."
cd order-service
./gradlew bootRun &> ../logs/order-service.log &
log "order-service started."
cd ..

log "Starting inventory-service..."
cd inventory-service
./gradlew bootRun &> ../logs/inventory-service.log &
log "inventory-service started."
cd ..

log "All services started."
