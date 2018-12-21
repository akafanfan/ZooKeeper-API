package org.zk.operation;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.zk.callback.Children2CallBack;
import org.zk.callback.ChildrenCallBack;
import org.apache.zookeeper.Watcher.Event.EventType;

public class ZKGetChildrenList implements Watcher {
	private ZooKeeper zookeeper = null;
	public static final String zkServerPath = "172.21.161.255:2181";
	public static final Integer timeout = 5000;
	
	public ZKGetChildrenList() {
	}

	
	public ZKGetChildrenList(String connectString) {
		try {
			zookeeper =	new ZooKeeper(connectString, timeout, new ZKGetChildrenList());
		} catch (IOException e) {
			e.printStackTrace();
			if (zookeeper!=null) {
				try {
					zookeeper.close();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	public ZooKeeper getZookeeper() {
		return zookeeper;
	}


	public void setZookeeper(ZooKeeper zookeeper) {
		this.zookeeper = zookeeper;
	}


	public static String getZkserverpath() {
		return zkServerPath;
	}


	public static Integer getTimeout() {
		return timeout;
	}

	private static CountDownLatch countdown = new CountDownLatch(1);
	public static void main(String[] args) throws Exception  {
		ZKGetChildrenList zkServer = new ZKGetChildrenList(zkServerPath);
		/*
		 * 
		 */
		//同步
//		List<String> children = zkServer.getZookeeper().getChildren("/fan", true);
//		for (String s : children) {
//			System.out.println(s);
//		}
		
		//异步处理
		String ctx ="{'callback':'ChildrenCallback'}";
//		zkServer.getZookeeper().getChildren("/fan", true, new ChildrenCallBack(), ctx);
		zkServer.getZookeeper().getChildren("/fan", true, new Children2CallBack(), ctx);
		
		countdown.await();
	}
	@Override
	public void process(WatchedEvent event) {
		try {
			if (event.getType() == EventType.NodeDataChanged) {
			
			}else if (event.getType() == EventType.NodeCreated) {

			}else if (event.getType() == EventType.NodeChildrenChanged) {
				System.out.println("NodeChildrenChanged");//打印当前事件名称
				ZKGetChildrenList zkServer = new ZKGetChildrenList(zkServerPath);
				List<String> children = zkServer.getZookeeper().getChildren(event.getPath(), false);
				for (String s : children) {
					System.out.println(s);
				}
				countdown.countDown();
			}else if (event.getType() == EventType.NodeDeleted) {

			}
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
