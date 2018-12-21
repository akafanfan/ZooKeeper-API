package org.zk.operation;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class ZKNodeExist implements Watcher {
	private ZooKeeper zookeeper = null;

	public static final String zkServerPath = "172.21.171.46:2181";
	public static final Integer timeout = 5000;

	public ZKNodeExist() {
	}

	public ZKNodeExist(String connectString) {
		try {
			zookeeper = new ZooKeeper(connectString, timeout, new ZKNodeExist());
		} catch (IOException e) {
			e.printStackTrace();
			if (zookeeper != null) {
				try {
					zookeeper.close();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	private static CountDownLatch countDown = new CountDownLatch(1);

	public static void main(String[] args) throws KeeperException, InterruptedException {
		ZKNodeExist zkServer = new ZKNodeExist(zkServerPath);
		Stat stat = zkServer.getZookeeper().exists("/afan", true);
		if (stat != null) {
			System.out.println("查询节点版本为dataVersion:" + stat.getVersion());
		} else {
			System.out.println("该节点不存在");
		}

		countDown.await();
	}

	@Override
	public void process(WatchedEvent event) {
		if (event.getType() == EventType.NodeDataChanged) {
			System.err.println("节点数据改变");
			countDown.countDown();
		} else if (event.getType() == EventType.NodeCreated) {
			System.err.println("节点创建");
			countDown.countDown();
		} else if (event.getType() == EventType.NodeDeleted) {
			System.err.println("节点删除");
			countDown.countDown();
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

}
