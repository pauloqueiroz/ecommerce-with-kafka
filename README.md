# ecommerce-with-kafka

## Setup apache kafka

1. Download

Realzar o download do arquivo binary downloads(tgz) - Scala version - A partir do seguinte link:
https://kafka.apache.org/downloads
Exemplo: https://dlcdn.apache.org/kafka/3.9.0/kafka_2.13-3.9.0.tgz

2. Descompactar o arquivo
abrir o terminal e executar o seguinte comando:
```bash
tar zxv kafka-version.tgz
```

3. Rodando o Kafka:
3.1 Executar o zookeeper - Responsável pelo armazenamento de algumas configurações
```bash
bin/zookeeper-server-start.sh config/zookeeper.properties
```

3.2 Executar o kafka
```bash
bin/kafka-server-start.sh config/server.properties
```
4. Criando um novo tópico
```bash
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic LOJA_NOVO_PEDIDO
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic ECOMMERCE_NEW_ORDER  
```
5. Listar tópico
```bash
bin/kafka-topics.sh --list --bootstrap-server localhost:9082
```
6. Rodar produtor de mensagem
```bash
bin/kafka-console-producer.sh -- broker-list localhost:9092 LOJA_NOVO_PEDIDO
> pedido0, 550
```