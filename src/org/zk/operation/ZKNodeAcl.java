package org.zk.operation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooDefs.Perms;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.zk.callback.CreateCallBack;
import org.zk.util.AclUtils;

public class ZKNodeAcl implements Watcher{
	private ZooKeeper zookeeper = null;

	public static final String zkServerPath = "172.21.171.46:2181";
	public static final Integer timeout = 5000;

	public ZKNodeAcl() {
	}

	public ZKNodeAcl(String connectString) {
		try {
			zookeeper = new ZooKeeper(connectString, timeout, new ZKNodeAcl());
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
	public void createZKNode(String path, byte[] data, List<ACL> acls) {
		String result = "";
		try {
			result = zookeeper.create(path, data, acls, CreateMode.PERSISTENT);
			System.out.println("创建节点:\t"+result+"\t成功...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws Exception {
		ZKNodeAcl zkServer = new ZKNodeAcl(zkServerPath);
		//acl 任何人都可以访问
//		zkServer.createZKNode("/aclnode","aclnode".getBytes() , Ids.OPEN_ACL_UNSAFE);
		
		//自定义用户认证访问
//		List<ACL> acls = new ArrayList<ACL>();
//		Id yang1 = new Id("digest", AclUtils.getDigestUserPwd("yang1:123456"));
//		Id yang2 = new Id("digest", AclUtils.getDigestUserPwd("yang2:123456"));
//		acls.add(new ACL(Perms.ALL, yang1));
//		acls.add(new ACL(Perms.READ, yang2));
//		acls.add(new ACL(Perms.DELETE|Perms.CREATE,yang2));
//		zkServer.createZKNode("/aclnode/testdigest", "testdigest".getBytes(), acls);
		
		//注册过的用户必须通过addAuthInfo才能操作节点 参照命令行中的 addauth
	//	zkServer.getZookeeper().addAuthInfo("digest", "yang1:123456".getBytes());
//		zkServer.createZKNode("/aclnode/testdigest/childrentest", "children".getBytes(), Ids.CREATOR_ALL_ACL);
			//读取数据
//		Stat stat = new Stat();
//		byte[] data = zkServer.getZookeeper().getData("/aclnode/testdigest", false, stat);
//		System.out.println(new String(data));
			//设置值 ---没用权限
//		zkServer.getZookeeper().setData("/aclnode/testdigest", "now".getBytes(), 1);
		
		
		//ip的acl
//		List<ACL> acls = new ArrayList<>();
//		Id idIp1 = new Id("ip","172.21.171.46");
//		acls.add(new ACL(Perms.ALL, idIp1));
//		zkServer.createZKNode("/aclnode/iptest", "iptest".getBytes(), acls);
			//权限验证
		zkServer.getZookeeper().setData("/aclnode/iptest", "123456".getBytes(), 0);
		Stat stat = new Stat();
		byte[] data = zkServer.getZookeeper().getData("/aclnode/iptest", false, stat);
		System.out.println(new String(data));
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

	@Override
	public void process(WatchedEvent event) {
		
	}
	
}
