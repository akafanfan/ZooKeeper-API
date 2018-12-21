package org.zk.operation;

import java.io.IOException;
import java.util.List;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.zk.callback.CreateCallBack;
import org.zk.callback.DeleteCallBack;

public class ZKNodeOperation implements Watcher {
	private ZooKeeper zk = null;
//	public static final String zkServerPath = "192.168.1.131:2181";
	public static final String zkServerPath = "172.21.158.134:2181";
	public static final Integer timeout = 5000;

	public ZKNodeOperation() {
	}

	// 构造函数用于创造会话链接
	public ZKNodeOperation(String connectString) throws Exception {
		try {
			zk = new ZooKeeper(connectString, timeout, new ZKNodeOperation());

		} catch (IOException e) {
			e.printStackTrace();
			if (zk != null) {
				try {
					zk.close();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}

	}

	// 创建ZK节点
	/*
	 * 同步或者异步创建节点，都不支持节点的递归创建，异步有一个callback函数 参数： path:创建的路径 data:存储的数据的byte[]
	 * acl:控制权限策略 Ids.OPEN_ACL_UNSAFE --> world:anyone:cdrwa CREATOR_ALL_ACL -->
	 * auth:user:password:cdrwa createMode:节点类型，是一个枚举 PERSISTENT:持久节点
	 * PERSISTENT_SEQUENTIAL:持久顺序点 EPHEMERAL:临时节点 EPHEMERAL_SEQUENTIAL:临时顺序节点
	 * 
	 * 
	 */
	@SuppressWarnings("static-access")
	public void createZKNode(String path, byte[] data, List<ACL> acls) {
		String result = "";
		try {

			// 临时节点的创建
			// result = zk.create(path, data, acls, CreateMode.EPHEMERAL);//EPHEMERAL:临时节点

			// 持久节点
			String ctx = "{'create':'success'}";
			zk.create(path, data, acls, CreateMode.PERSISTENT, new CreateCallBack(), ctx);
			
			System.out.println("创建节点:\t" + result + "\t成功");
			
			new Thread().sleep(2000);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process(WatchedEvent event) {
	}

	public ZooKeeper getZk() {
		return zk;
	}

	public void setZk(ZooKeeper zk) {
		this.zk = zk;
	}

	public static void main(String[] args) throws Exception {
		ZKNodeOperation zkServer = new ZKNodeOperation(zkServerPath);
	//创建zookeeper节点
		//zkServer.createZKNode("/testnode", "testnode".getBytes(), Ids.OPEN_ACL_UNSAFE);

	//修改zookeeper节点
		/*
		 * 参数：
		 * 	path:节点路径
		 * 	data：数据
		 * 	version:数据状态
		 */
		//new Thread().sleep(2000);
		//Stat stat = zkServer.getZk().setData("/testnode", "123".getBytes(), -1);
		//System.out.println(stat.getVersion());
	//删除zookeeper节点
		zkServer.getZk().delete("/testnode", 0);
		//异步
//		zkServer.createZKNode("/test-delete", "123".getBytes(), Ids.OPEN_ACL_UNSAFE);
//		String ctx ="{'delete':'success'}";
//		zkServer.getZk().delete("/test-delete", 0, new DeleteCallBack(), ctx);
//		Thread.sleep(2000);
	}
}
// ERROR
/*
 * KeeperException$ConnectionLossException: KeeperErrorCode = ConnectionLoss for
 * 一般是由于连接还未完成就执行zookeeper的get/create/exsit操作引起的.
 */
