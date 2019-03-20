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

0. Client
   
    从server获取信息，周期性发送数据给server，表示自己还活着。
    client连接时，server回传ack信息。
    如果client没有收到response，自动重定向到另一个server.

1. Server

	zk集群中的一员，向client提供所有service，回传ack信息给client，表示自己还活着。

2. ensemble

	一组服务器。
	最小节点数是3.

3. Leader

	如果连接的节点失败，自定恢复，zk服务启动时，完成leader选举。

4. Follower
	追寻leader指令的节点。