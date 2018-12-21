package org.zk.countdownlatch;

import java.util.concurrent.CountDownLatch;

/*
 * 它是一个计数器
 * 用于多线程，可以暂停也可以继续
 * .await() 阻塞当前线程	
 * .countDown() 减一操作 直到为0才继续
 */
//抽象类，用于演示 危险品化工监控中心 统一检查
public abstract class DangerCenter implements Runnable{
	private CountDownLatch countdown;	//计数器
	private String station;				//调度站
	private boolean ok;					//调度站针对当前自己的站点进行检查，是否检查ok标志
	
	public DangerCenter(CountDownLatch countdown, String station, boolean ok) {
		super();
		this.countdown = countdown;
		this.station = station;
		this.ok = false;//初始默认为false
	}
	
	@Override
	public void run() {
		try {
			check();
			ok=true;
		} catch (Exception e) {
			e.printStackTrace();
			ok = false;
		} finally {
			if(countdown!=null) {
				countdown.countDown();
			}
		}
	}
	
	public abstract void check();

	public CountDownLatch getCountdown() {
		return countdown;
	}

	public void setCountdown(CountDownLatch countdown) {
		this.countdown = countdown;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}
	
	
}
