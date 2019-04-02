##### zookeeper笔记

### 0. Zookeeper

0. 管理大量主机的协同服务。
1. 分布式应用，实现分布式读写技术。
2. zk提供的服务

    * Naming service				//按名称区分集群中的节点.
    * Configuration management	    //对加入节点的最新化处理。
    * Cluster management			//实时感知集群中节点的增减.
    * Leader election				//leader选举
    * Locking and synchronization service	//修改时锁定数据，实现容灾.
    * Highly reliable data registry		//节点宕机数据也是可用的。

3. 常用命令
        
        zkServer.sh start
        zkServer.sh status
        
        zkCli.sh -server xxx:2181
        [zkCli]ls /
        [zkCli]create / ""
        [zkCli]get /
        [zkCli]set /
        [zkCli]rmr /

### 1. zk架构       
> C/S

| 部分 | 描述 |
| :----- | :----- |
|Client(客户端)|应用集群中的一个节点，从服务器访问信息。<br>对于特定的时间间隔，每个客户端向服务器发送消息以使服务器知道客户端是活跃的。<br>类似地，当客户端连接时，服务器发送确认码。如果连接的服务器没有响应，<br>客户端会自动将消息重定向到另一个服务器。|
|Server(服务端)|zk总体中的一个节点，为客户端提供所有的服务。向客户端发送确认码以告知服务器是活跃的。|
|Ensemble|zk服务器组。形成ensemble所需的最小节点数为3。|
|Leader|任何连接的节点失败，则执行自动恢复。Leader在服务启动时被选举。|
|Follower|跟随leader指令的服务器节点。|

### 2. znode
> 乐观锁
0. 结构
1. 类型
> 持久节点(persistent)，顺序节点(sequential)，临时节点(ephemeral)

| 类型 | 描述 |
| :----- | :----- |
|持久节点|Client退出后，节点仍然存在|
|临时节点|Client活动时有效，断开后自动删除，不能有子节点，在leader选举时起到重要作用|
|顺序节点|可以是临时节点，也可以是持久节点，创建后在节点名后加10位数序列号，在锁和同步中起重要作用。|

### 3. Session
>FIFO

    一旦client连接到Server，就建立Session，sessionid分配给client。
    client以固定时间向服务器发送心跳，session有效。
    zk集群在超时的时候，没有收到心跳，判定为断开连接，此时，临时节点会被删除。
    
### 4. Watches
> 对于Client类似观察者机制，以接口的形式表现出来
    
    client在读取特定znode时设置Watches。
    Watches会向注册的client发送任何znode（客户端注册表）更改的通知。
    Znode更改是与znode相关的数据的修改或znode的子项中的更改。
    只触发一次watches。
    如果client想要再次通知，则必须通过另一个读取操作来完成。
    当Session过期时，client与Server断开连接，相关的watches也将被删除。

### 5. 工作流程
> zk集群启动后，等待client连接，client连接到zk的任何一个节点(node)，该node可以是Leader或是follower。一旦Client连接，node会给client分配一个sessionid，并向该client确认信息(ack)。如果client没有收到ack，client会去尝试请求另一个节点。一旦成功连接到节点，client会发送心跳，确保不会丢失连接。

   * client读取特定的znode
           
            将向含有znode路径的节点发送请求，节点通过查询自己的数据库，
            把读取到的znode数据返回给client。zk集群中读取数据的速度很快。
   
   * client存储znode到zk集群中
        
            将znode的路径&数据发送到server，server将请求转发给Leader，
            leader向所有的follwer发送写入请求，当大部分(半数以上)节点成功相应，则写入成功，返回给Client。
            
##### zk中的节点
> zk中节点个数不同，效果不同，推荐奇数节点(半数节点故障容易处理)。

1. 1个节点，那么该节点故障，zk集群就会失效，不建议生产环境使用。
2. 2个节点，如果1个节点故障，没有占多数。
3. 3个节点，如果1个节点故障，剩余2个节点，占据大多数，这是最低要求，生产环境必须至少3个以上节点。
4. 4个节点，如果2个节点故障，再次故障类似3个节点情况，额外节点不用于任何目的。

故：节点数最好为奇数。如：3、5、7

zk的写入过程要比读取过程更耗资源，以为所有节点都需要在自身数据库写入相同的数据。因此，对于平衡环境来说更少节点(3/5/7)要比大量节点更好。

|组件/操作|描述|
|:----|:-----|
|写(write)|写入过程由leader节点处理。leader将写请求转发所有的Znode，然后等待来自znode的恢复。如果一半的Znodes回复，您可以确定写入过程已完成。|
|读(read)|读取由连接特定的znode在内部执行，因此不需要与集群交互。|
|数据库复制(Replicated Database)|在zk中存储数据，每个Znode都有自己的数据库，在一致性的帮助下每次都有相同的数据。|
|Leader|负责写入请求的znode|
|Follower|负责接收clien的请求，转发给leader|
|请求处理器(Request Processor)|只在leader中，用于管理follower的写入请求。|
|原子广播(atomic broadcasts)|负责广播从leader到follower的变化|

![workflow](./pic/zk-workflow.png)

### 6. zk的选举(最小号选举)
    1.所有节点在同一目录下创建临时序列节点。
	2.节点下会生成/xxx/xx000000001等节点。
	3.序号最小的节点就是leader，其余就是follower.
	4.每个节点观察小于自己节点的主机。(注册观察者)
	5.如果leader挂了，对应znode删除了。
	6.观察者收到通知。
