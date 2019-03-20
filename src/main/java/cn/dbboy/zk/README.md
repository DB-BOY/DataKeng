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

### 1. zk架构       
> C/S

<style>
table th:first-of-type {
	width: 100px;
}
</style>

| 部分 | 描述 |
| :----- | :----- |
|Client(客户端)|应用集群中的一个节点，从服务器访问信息。<br>对于特定的时间间隔，每个客户端向服务器发送消息以使服务器知道客户端是活跃的。<br>类似地，当客户端连接时，服务器发送确认码。如果连接的服务器没有响应，<br>客户端会自动将消息重定向到另一个服务器。|
|Server(服务端)|ZooKeeper总体中的一个节点，为客户端提供所有的服务。向客户端发送确认码以告知服务器是活跃的。|
|Ensemble|ZooKeeper服务器组。形成ensemble所需的最小节点数为3。|
|Leader|任何连接的节点失败，则执行自动恢复。Leader在服务启动时被选举。|
|Follower|跟随leader指令的服务器节点。|