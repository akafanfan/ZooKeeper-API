package org.zk.operation;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZKConnect implements Watcher{
	final static Logger log = LoggerFactory.getLogger(ZKConnect.class);
    
	public static final String zkServerPath="192.168.1.131:2181";
    public static final Integer timeout = 5000;
    
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws Exception{
    	
		ZooKeeper zk = new ZooKeeper(zkServerPath,timeout,new ZKConnect());
    	log.warn("客户端开始连接zookeeper服务器...",zk.getState());
    	log.warn("连接状态：{}",zk.getState());
    	
    	new Thread().sleep(2000);
    	
    	log.warn("连接状态：{}",zk.getState());
    }

	@Override
	public void process(WatchedEvent event) {
		log.warn("接收到watch通知:{}",event);
	}
}