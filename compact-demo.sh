#/bin/bash
kafka-topics --create --topic froscon_compact --config cleanup.policy=compact --config segment.ms=5000 --config segment.bytes=500 --partitions=1 --replication-factor=1 --zookeeper=localhost:2181
kafka-console-producer --topic froscon_compact --broker-list localhost:9092 --property "parse.key=true" --property "key.separator=:"

#val1-1
#val2-1
#val1-2
#val2-2

#docker exec -it frosconkafka /bin/bash
#cd kafka/kafka-logs-8369322235b6/froscon_compact-0/
#watch ls -lha
