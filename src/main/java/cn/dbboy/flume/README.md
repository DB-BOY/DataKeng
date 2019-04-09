##### flume学习笔记

### 0. 安装

1. 下载
 
    wget http://apache.fayea.com/flume/1.7.0/apache-flume-1.7.0-bin.tar.gz 
    
2. 解压

    tar -zxvf apache-flume-1.7.0-bin.tar.gz

3. 环境变量
    
    * 建立符号连接 ln -s xxx /soft/flume
    * 配置/etc/profile

4. 启动
    
    flume-ng agent -f /soft/flume/conf/nc2kafka.conf -n a1 -Dflume.root.logger=INFO,console


### 1. 自定义Interceptor
