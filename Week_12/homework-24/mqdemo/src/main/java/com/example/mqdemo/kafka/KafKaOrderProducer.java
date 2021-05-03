package com.example.mqdemo.kafka;

import cn.hutool.json.JSONUtil;
import com.example.mqdemo.common.Order;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Objects;
import java.util.Properties;

/**
 * @author leven.chen
 */
@Component
public class KafKaOrderProducer {

    private static final Logger log = LoggerFactory.getLogger(KafKaOrderProducer.class);


    private KafkaProducer<String, String> producer;

    public static final String topic = "order-test1";

    @PostConstruct
    public void init() {
        Properties config = new Properties();
        //服务器配置
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9092,kafka3:9092,kafka2:9092");
        //ACK配置
        config.put(ProducerConfig.ACKS_CONFIG, "all");
        // 重试次数
        config.put(ProducerConfig.RETRIES_CONFIG, "1");
        // 批次大小
        config.put(ProducerConfig.BATCH_SIZE_CONFIG, "16384");
        // 等待时间
        config.put(ProducerConfig.LINGER_MS_CONFIG, "1");
        // RecordAccumulator 缓存区大小
        config.put(ProducerConfig.BUFFER_MEMORY_CONFIG, "33554432");
        // key序列化
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        // value 序列化
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        this.producer = new KafkaProducer<String, String>(config);
    }


    public void create(Order order) {
        String msg = JSONUtil.toJsonStr(order);
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, order.getId(), msg);
        producer.send(record, ((metadata, exception) -> {
            if (Objects.isNull(exception)) {
                log.info("metadata:{}", metadata.toString());

            } else {
                log.info("metadata:{} --> exception:{}", metadata.toString(), exception.getMessage());
            }
        }));

    }

    @PreDestroy
    public void destroy() {
        producer.close();
    }
}
