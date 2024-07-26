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

## Exploring the application

**`oms-config-server` is the Config Server for the application. It runs on port 8888.**

[http://localhost:8888/application/default](http://localhost:8888/application/default) will show the common configurations for all the services.

[http://localhost:8888/inventory-service/default](http://localhost:8888/inventory-service/default) will show the configurations for the Inventory Service.

[http://localhost:8888/order-service/default](http://localhost:8888/order-service/default) will show the configurations for the Order Service.

---

**`inventory-service` runs on port 8081.**

[http://localhost:8081/inventory-service/swagger-ui/index.html](http://localhost:8081/inventory-service/swagger-ui/index.html) will show the Swagger UI for the Inventory Service.

Query the Inventory by product ID:

```sh
curl --request GET --url http://localhost:8081/inventory-service/inventory/1
```

It will return the Inventory details for the Product with productId 1. ProductId 1-6 is available in the Inventory.

Create a new Inventory:

```sh
curl -X 'POST' \
  'http://localhost:8081/inventory-service/inventory' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "productId": 7,
  "quantity": 14
}'
```

If inventory with productId 7 is already present, it will update the quantity. Or else, it will create a new inventory.

`h2-console` for the Inventory Service can be accessed at [http://localhost:8081/inventory-service/h2-console](http://localhost:8081/inventory-service/h2-console).

Credentials for the `h2-console` can be found [here](http://localhost:8888/inventory-service/default)

---

**`order-service` runs on port 8082.**

[http://localhost:8082/order-service/swagger-ui/index.html](http://localhost:8082/order-service/swagger-ui/index.html) will show the Swagger UI for the Order Service.

Before placing an order, make sure the Inventory is available for the Product Id 1:

```sh
curl -X 'GET' 'http://localhost:8081/inventory-service/inventory/1' -H 'accept: */*'
```

Result will show 100 units of Product Id 1 available in the Inventory.

Now create a new Order:

```sh
curl -X 'POST' \
  'http://localhost:8082/order-service/orders' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "productId": 1,
  "quantity": 50
}'
```

Here:

1. It will create a new Order for Product Id 1 with quantity 50.
2. Publish the Order to Kafka topic `order-created`.
3. Inventory Service will consume the Order and update the Inventory.
4. Inventory Service will publish the Inventory update to another Kafka topic `order-processed`.
5. Order Service will consume the Inventory update and update the Order status.

Order Status Criteria:  
if Order Quantity <= Inventory Quantity: Order Status = "PREOCESSED"  
if Order Quantity > Inventory Quantity: Order Status = "OUT_OF_STOCK"
if Product Id not found in Inventory: Order Status = "FAILED"
if Order service is unable to publish the Order to Kafka: Order Status = "FAILED"

You can check the output with the following commands:

```sh
curl -X 'GET' 'http://localhost:8081/inventory-service/inventory/1' -H 'accept: */*'

curl -X 'GET' \
  'http://localhost:8082/order-service/orders/1' \
  -H 'accept: */*'

```

`h2-console` for the Order Service can be accessed at [http://localhost:8082/order-service/h2-console](http://localhost:8082/order-service/h2-console).

Credentials for the `h2-console` can be found [here](http://localhost:8888/order-service/default)

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

## Limitations:

Due to time constraints, the following limitations are present in the application:

1. The application does not have a front end. All operations are done using Swagger UI or curl commands.
2. The application does not have a database. It uses in-memory H2 databases for storing data.
3. The application does not have a monitoring system.
4. The application does not have a CI/CD pipeline.
5. The application does not have a security configured.
6. I was planning to use traefik along with docker for api gateway and service deployment but due to internet I was not able to implement it.
