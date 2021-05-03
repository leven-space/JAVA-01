## Kafka集群搭建

### 1.制作一个简单的Docker镜像
> 详见DockerFile
使用一个打印helloworld的shell脚本来作为main进程。


### 2. 编写docker-compose
> 详见docker-compose.yml

主要两部分
- zk集群
- kafka测试容器

### 3. 容器内配置Kafka集群

- 进入kafka测试容器后，在$KAFKA_HOME/conf/server.properties中进行配置
1. 修改broker.id # 全局唯一，不能重复
   broker.id=0
2. kafka 日志（数据）存在路径
   log.dirs=/usr/local/kafka/data
3. zookeeper集群配置
   zookeeper.connect=zoo1:2181,z002:2181,z003:2181
4. 启动集群
