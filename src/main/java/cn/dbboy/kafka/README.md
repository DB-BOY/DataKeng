##### kafka笔记

### 0. Kafka
    分布式流处理平台。
	在系统之间构建实时数据流管道。
	以topic分类对记录进行存储
	每个记录包含key-value+timestamp
	每秒钟百万消息吞吐量。


	producer		//消息生产者
	consumer		//消息消费者
	consumer group		//消费者组
	kafka server		//broker,kafka服务器
	topic			//主题,副本数,分区.
	zookeeper		//hadoop namenoade + RM HA | hbase | kafka

##### 安装
    
1. 下载
 
    wget http://mirrors.shu.edu.cn/apache/kafka/2.2.0/kafka_2.12-2.2.0.tgz
    
2. 解压

    tar -zxvf kafka_2.12-2.2.0.tgz

3. 环境变量
    
    * 建立符号连接 ln -s xxx /soft/kafka
    * 配置/etc/profile



### 1. Kafka

0. 配置server.properties

        [kafka/config/server.properties]
        ...
        broker.id=1
        ...
        listeners=PLAINTEXT://:9092
        ...
        log.dirs=/home/centos/kafka/logs
        ...
        zookeeper.connect=localhost:2181

1. 启动kafka

    a. 先启动zk
    
    b. 启动kafka
        
        kafka-server-start.sh config/server.properties
        kafka-server-start.sh daemon config/server.properties
        
2. 创建主题
    
        kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 3 --partitions 3 --topic hellokfk
    
3. 查看主题

        kafka-topics.sh --list --zookeeper localhost:2181

4. 控制台生产者
    
        kafka-console-producer.sh --broker-list localhost:9092 --topic hellokfk

5. 控制台消费者

        kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic hellokfk --from-beginning


### 1. Kafka在zk中的配置

    /brokers/ids/203
    /brokers/topics/test/partitions/0/state
    /brokers/seqid
    
    /admin/delete_topics
    
    /isr_change_notification
    
### 2. Productor/Consumer
> 详见测试代码