package cn.dbboy.kafka;

/**
 * <br>
 * Created by DB_BOY on 2019/3/26.
 * <br>
 */
public class KafkaProducerTest {

    public static void main(String args[]) {
        KaProducer producer = new KaProducer();
        KaConsumer consumer = new KaConsumer("hellokfk");
        Thread pt = new Thread(producer);
        Thread pc = new Thread(consumer);
        pt.start();
        pc.start();
    }
}
