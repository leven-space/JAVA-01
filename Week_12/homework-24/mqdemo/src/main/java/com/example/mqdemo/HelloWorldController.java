package com.example.mqdemo;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.example.mqdemo.common.Order;
import com.example.mqdemo.kafka.KafKaOrderConsumer;
import com.example.mqdemo.kafka.KafKaOrderProducer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leven.chen
 */
@RestController
public class HelloWorldController {
    @Autowired
    private KafKaOrderProducer orderProducer;

    private List<KafKaOrderConsumer> consumers = new ArrayList<>();

    int i = 0;

    int consumeCounter = 0;

    @GetMapping("/hello")
    public String hello() {

        return "hello-";
    }

    @GetMapping("/test-kafka-producer")
    public String testKafkaProducer(@RequestParam int size) {
        if (size <= 0) {
            size = 1;
        }
        for (int j = 0; j < size; j++) {
            Order order = new Order();
            order.setId(IdUtil.objectId());
            order.setName("Kafka-" + i);
            order.setPrice(RandomUtil.randomDouble());
            orderProducer.create(order);
        }

        return "success-" + i;
    }

    @GetMapping("/test-kafka-consumer")
    public String testKafkaConsumer(@RequestParam String group) {

        KafKaOrderConsumer consumer = new KafKaOrderConsumer();
        consumer.init(group);

        new Thread(consumer::subscribe, group + "-consumer-thread-" + consumeCounter).start();

        consumers.add(consumer);
        consumeCounter++;
        return "success-" + consumeCounter;
    }

    @GetMapping("/clear-consumer")
    public String clearConsumer() {

        consumers.clear();

        return "success";

    }
}
