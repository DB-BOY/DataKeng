package cn.dbboy.kafka;

import cn.dbboy.zk.base.Url;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

/**
 * <br>
 * Created by DB_BOY on 2019/4/1.
 * <br>
 */
public class KaConsumer implements Runnable {

    public String topic;
    KafkaConsumer<String, String> kafkaConsumer;

    public KaConsumer(String topic) {
        this.topic = topic;
    }

    public static void main(String[] args) {
        KaConsumer c = new KaConsumer("hellokfk");
        c.consumer();
    }

    public void create() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", Url.ka_bootstrap);
        properties.put("group.id", "hellokfk");
        properties.put("enable.auto.commit", "true");
        properties.put("auto.commit.interval.ms", "1000");
        properties.put("auto.offset.reset", "earliest");
        properties.put("session.timeout.ms", "30000");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        kafkaConsumer = new KafkaConsumer(properties);
    }

    public void consumer() {
        kafkaConsumer.subscribe(Arrays.asList(topic));
        try {
            while (true) {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("------> offset = %d, value = %s", record.offset(), record.value());
                    System.out.println();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            kafkaConsumer.close();
        }

    }


    @Override
    public void run() {
        create();
        System.out.println("---------开始消费---------");
        consumer();
    }
}
