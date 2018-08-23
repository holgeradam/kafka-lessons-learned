kafka-topics --delete --topic froscon_compact --if-exists --zookeeper localhost:2181
kafka-topics --delete --topic froscon_delete --if-exists --zookeeper localhost:2181
docker-compose down
docker-compose up -d
