package org.zk.operation;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
//获取节点数据
public class ZKGetNodeData implements Watcher {
	private ZooKeeper zookeeper = null;

	public static final String zkServerPath = "172.21.171.46:2181";
	public static final Integer timeout = 5000;
	public static Stat stat = new Stat();

	public ZKGetNodeData() {
	}

	public ZKGetNodeData(String connectString) {
		try {
			zookeeper = new ZooKeeper(zkServerPath, timeout, new ZKGetNodeData());
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

	// 只进行一次watch事件监听
	private static CountDownLatch countdown = new CountDownLatch(1);

	public ZooKeeper getZookeeper() {
		return zookeeper;
	}

	public static String getZkserverpath() {
		return zkServerPath;
	}

	public static Integer getTimeout() {
		return timeout;
	}

	public static Stat getStat() {
		return stat;
	}

	public static void main(String[] args) throws Exception {
		ZKGetNodeData zkServer = new ZKGetNodeData(zkServerPath);
		/*
		 * getData(path, watch, stat) 参数： path:节点路径 watch:true或者false,注册一个watch事件
		 * stat:状态
		 */
		byte[] resByte = zkServer.getZookeeper().getData("/fan", true, stat);
		String result = new String(resByte);
		System.out.println("当前值：" + result);
		countdown.await(); // 挂起
	}

	@Override
	public void process(WatchedEvent event) {
		try {
			if (event.getType() == EventType.NodeDataChanged) {
				ZKGetNodeData zkServer = new ZKGetNodeData(zkServerPath);
				byte[] resByte = zkServer.getZookeeper().getData("/fan", false, stat);
				String result = new String(resByte);
				System.out.println("更改后的值:" + result);
				System.out.println("版本号变化dversion:" + stat.getVersion());
				countdown.countDown();
			}else if (event.getType() == EventType.NodeCreated) {

			}else if (event.getType() == EventType.NodeChildrenChanged) {

			}else if (event.getType() == EventType.NodeDeleted) {

			}
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
