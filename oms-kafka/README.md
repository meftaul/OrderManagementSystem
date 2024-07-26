# Kafka

## Download kafka

```bash
wget https://www.apache.org/dyn/closer.cgi?path=/kafka/3.7.1/kafka_2.13-3.7.1.tgz
unzip kafka_2.13-3.7.1.tgz
```

```bash
docker-compose up
```

## Helpful kafka commands

```bash
bin/kafka-topics.sh --bootstrap-server localhost:9092 --list

bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic <topic-name> --from-beginning

```
