# Order Management System (OMS)

## Overview

This project consists of multiple microservices that are started and stopped using shell scripts.

## Directory Structure

- `oms-kafka`: Directory containing the Kafka setup usign Docker Compose.
- `oms-config-server`: Directory containing the OMS Config Server code.
- `config-dir`: Directory containing configuration files for the services. Used by the OMS Config Server.
- `order-service`: Directory containing the Order Service code.
- `inventory-service`: Directory containing the Inventory Service code.
- `logs`: Directory for storing logs (created automatically when starting services).

## System architecture

![System Architecture](./SystemArchitechture.drawio.svg)

## Prerequisites

- Docker
- Gradle
- Java (JDK 17 or higher)

## Starting the Services

To start the services, use the provided `start_services.sh` script. This script will:

1. Start a Kafka container.
2. Run the OMS Config Server.
3. Run the Order Service.
4. Run the Inventory Service.

### Steps to Start

1. Open a terminal.
2. Navigate to the root directory of the project.
3. Run the following command:

   ```sh
   sh start_services.sh
   ```

Logs for each service will be stored in the `logs` directory, which is created if it does not exist.

## Stopping the Services

To stop the services, use the provided `stop_services.sh` script. This script will:

1. Stop the OMS Config Server.
2. Stop the Order Service.
3. Stop the Inventory Service.
4. Stop the Kafka container.
5. Remove the `logs` directory.

### Steps to Stop

1. Open a terminal.
2. Navigate to the root directory of the project.
3. Run the following command:

   ```sh
   sh stop_services.sh
   ```

Logs for the stop operations will be stored in `logs/stop_services.log` before the `logs` directory is removed.

## Additional Information

- Ensure Docker is running on your machine before starting the services.
- Make sure the ports 9092, 8888, 8081, and 8082 are not in use by other applications.

## License

This project is licensed under the MIT License. See the `LICENSE` file for more details.
