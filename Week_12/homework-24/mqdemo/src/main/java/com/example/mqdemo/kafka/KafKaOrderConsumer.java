package com.example.mqdemo.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

/**
 * @author leven.chen
 */

public class KafKaOrderConsumer {

    private KafkaConsumer<String, String> consumer;

    private static final Logger log = LoggerFactory.getLogger(KafKaOrderConsumer.class);

    public void init(String group) {
        Properties config = new Properties();
        //服务器配置
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9092,kafka3:9092,kafka2:9092");
        // key序列化
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        // value 序列化
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        // 开启自动提交offset
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        // 自动提交offset的间隔时间
        config.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        // 消费者group
        config.put(ConsumerConfig.GROUP_ID_CONFIG, group);

        this.consumer = new KafkaConsumer<>(config);


    }


    public void subscribe() {

        consumer.subscribe(Collections.singletonList(KafKaOrderProducer.topic));

        while (true) {
            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofSeconds(10));

            for (ConsumerRecord<String, String> record : consumerRecords) {
                log.info("threadName={} --> record rec: {} ", Thread.currentThread().getName(), record.toString());
            }
        }

    }


    @Override
    protected void finalize() throws Throwable {
        consumer.close();
    }
}
