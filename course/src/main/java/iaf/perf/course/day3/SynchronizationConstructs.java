package iaf.perf.course.day3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SynchronizationConstructs {

	private final Object lockHolder = new Object();
	private boolean shouldWake = false;
	
	private final Lock lock = new ReentrantLock();
	private final Condition condition = lock.newCondition();
	
	public void syncWait() throws InterruptedException {
		synchronized(lockHolder) {
			while (!shouldWake) {
				lockHolder.wait();
			}
		}
	}
	
	public void syncNotify() {
		synchronized(lockHolder) {
			shouldWake = true; //why is shouldWake not required to be volatile???>41@$!@#!@$!$
			lockHolder.notifyAll();
		}
	}
	
	public void lockWait() throws InterruptedException {
		try {
			lock.lock();
			condition.await();
		} finally {
			lock.unlock();
		}
	}
	
	public void lockWake() {
		try {
			lock.lock();
			condition.signalAll();
		} finally {
			lock.unlock();
		}
	}
	
}
