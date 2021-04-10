package com.csy.summary.daily.lock_test;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author shuyun.cheng
 * Description: 可重入读写锁
 * 我们可以在创建ReentrantLock对象时，通过以下方式来设置锁的公平性（
 * 当一个线程释放锁后，第一个获取锁的是等待时间最久的线程，叫公平锁，非公平锁却不管等待时间）：
 * ReentrantLock lock = new ReentrantLock(true); 默认时false，非公平锁
 * @date Create in 13:49 2019/4/11
 */
public class ReadWriteLockTest1 {
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

    public static void main(String[] args) {
        final ReadWriteLockTest1 test = new ReadWriteLockTest1();
        new Thread() {
            public void run() {
                test.get(Thread.currentThread());
            }
        }.start();
        new Thread() {
            public void run() {
                test.get(Thread.currentThread());
            }
        }.start();
    }

    public void get(Thread thread) {
        rwl.readLock().lock();
        try {
            long start = System.currentTimeMillis();
            while (System.currentTimeMillis() - start <= 1) {
                System.out.println(thread.getName() + "正在进行读操作");
            }
            System.out.println(thread.getName() + "读操作完毕");
        } finally {
            rwl.readLock().unlock();
        }
    }
}
