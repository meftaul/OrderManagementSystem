#!/bin/bash

# Function to log messages with timestamp
log() {
    echo "$(date '+%Y-%m-%d %H:%M:%S') - $1" | tee -a logs/stop_services.log
}

# Function to stop a service running on a specific port
stop_service() {
    PORT=$1
    SERVICE_NAME=$2

    log "Stopping $SERVICE_NAME on port $PORT..."
    PID=$(lsof -ti tcp:$PORT)
    
    if [ -z "$PID" ]; then
        log "$SERVICE_NAME on port $PORT is not running."
    else
        kill -9 $PID
        log "$SERVICE_NAME on port $PORT stopped."
    fi
}

# Stop services
stop_service 8888 "oms-config-server"
stop_service 8081 "order-service"
stop_service 8082 "inventory-service"

# Stop Kafka
log "Stopping Kafka..."
KAFKA_CONTAINER_ID=$(docker ps -q --filter ancestor=apache/kafka:3.7.1)

if [ -z "$KAFKA_CONTAINER_ID" ]; then
    log "Kafka is not running."
else
    docker stop $KAFKA_CONTAINER_ID
    log "Kafka stopped."
fi

log "All services stopped."

# Remove the logs directory
log "Removing logs directory..."
cd ..
rm -rf logs
log "Logs directory removed."
