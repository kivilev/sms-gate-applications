docker exec -it sms-kafka-broker /bin/sh  

kafka-topics --bootstrap-server sms-kafka-broker:9092 --topic sms-send-topic --create --partitions 1 --replication-factor 1  
kafka-topics --bootstrap-server sms-kafka-broker:9092 --topic sms-status-topic --create --partitions 1 --replication-factor 1  

kafka-topics --bootstrap-server sms-kafka-broker:9092 --topic sms-send-topic --delete  
kafka-topics --bootstrap-server sms-kafka-broker:9092 --topic sms-status-topic --delete  

kafka-console-consumer --bootstrap-server sms-kafka-broker:9092 --topic sms-send-topic --from-beginning  
kafka-console-consumer --bootstrap-server sms-kafka-broker:9092 --topic sms-status-topic --from-beginning  

