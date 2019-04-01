package cn.dbboy.kafka;

import cn.dbboy.zk.base.Url;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * <br>
 * Created by DB_BOY on 2019/3/26.
 * <br>
 */
public class KaProducer implements Runnable {

    KafkaProducer<String, String> producer;

    public static void main(String[] args) {
        new KaProducer().send();
    }

    public void create() {
        Properties props = new Properties();
        //broker列表
        props.put("bootstrap.servers", Url.ka_bootstrap);
        //串行化
        props.put("group.id", "hellokfk");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", StringSerializer.class.getName());
        props.put("batch.size", 16384);
        props.put("acks", "all");
        props.put("retries", 0);

        producer = new KafkaProducer<String, String>(props);
    }

    public void send() {

        String msg;
        try {
            for (int i = 0; i < 10; i++) {
                msg = "--producer: ms: " + i;
                producer.send(new ProducerRecord<String, String>("hellokfk", msg));
                System.out.println(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }
        System.out.println("send over!");
    }

    @Override
    public void run() {
        create();
        System.out.println("---------开始生产---------");
        send();
    }
}
