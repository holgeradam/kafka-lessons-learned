package net.andreaskluth.kafkaproducer

import org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.DEFAULT_API_TIMEOUT_MS_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.Properties

fun main(args: Array<String>) {
    FrOSConConsumer().consume()
}

class FrOSConConsumer {

    companion object {
        val log: Logger = LoggerFactory.getLogger(FrOSConConsumer::class.java.simpleName)
    }

    private fun config(): Properties {
        val config = Properties()
        config[BOOTSTRAP_SERVERS_CONFIG] = "127.0.0.1:9092"
        config[GROUP_ID_CONFIG] = "rewe-topic-group"
        config[VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name
        config[KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name
        config[MAX_POLL_INTERVAL_MS_CONFIG] = 10 * 60 * 1_000
        config[DEFAULT_API_TIMEOUT_MS_CONFIG] = 10 * 60 * 1_000
        config[SESSION_TIMEOUT_MS_CONFIG] = 60 * 1_000

        config[ENABLE_AUTO_COMMIT_CONFIG] = false

        return config
    }

    fun consume() {
        val consumer = KafkaConsumer<String, String>(config())
        consumer.subscribe(listOf("rewe-topic"))

        consumer.use { con ->
            while (true) {
                con.poll(Duration.ofSeconds(1))
                    .forEach { record ->
                        log.info("Received record with" +
                            " key: ${record.key()} and value: ${record.value()}.")
                    }
                con.commitSync()
            }
        }
    }
}
