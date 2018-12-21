# 原生ZooKeeper-API
## 一、会话连接与恢复
客户端和zk服务器链接是一个异步过程
当连接成功后，客户端会收的一个watch通知
参数：
   connectString:连接服务器的ip字符串
   比如：192.168.1.1:2181,192.168.1.2:2181, .....
   sessionTimeout:超时时间，心跳收不到了，那就超时
   watcher:通知事件，如果有对应的事件触发，则会收到一个通知：如果不需要，那么设置为null
   canBeReadOnly:可读，当这个物理系节点断开后，还是可以读取到数据，只是不能鞋，此时数据被读取到的可能是旧数据，一般设置为false
   sessionId:会话的id
   sessionPasswd:会话密码 当会话丢失后，可以依据sessionId和sessionPwd重新获取会话
