package org.zk.util;

import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

public class AclUtils {
	public static String getDigestUserPwd(String id) throws Exception {
		return DigestAuthenticationProvider.generateDigest(id);
	}
	
	//测试
	public static void main(String[] args) throws Exception {
		String id = "yang:yang";
		String idDigested =getDigestUserPwd(id);
		System.out.println("idDigested"+idDigested);
	}
	
}
