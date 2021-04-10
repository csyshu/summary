package com.csy.summary.daily.lock_test;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author shuyun.cheng
 * @date Create in 14:03 2019/4/11
 */
public class ReadWriteLockTest2 {
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

    public static void main(String[] args) {
        ReadWriteLockTest2 test2 = new ReadWriteLockTest2();
        new Thread() {
            public void run() {
                test2.read(Thread.currentThread());
            }
        }.start();
        new Thread() {
            public void run() {
                test2.write(Thread.currentThread());
            }
        }.start();
    }

    public void read(Thread thread) {
        lock.readLock().lock();
        try {
            for (int i = 0; i < 20; i++) {
                System.out.println(thread.getName() + "读操作" + i);
            }
            System.out.println("读操作完毕");
        } finally {
            lock.readLock().unlock();
        }
    }

    public void write(Thread thread) {
        lock.writeLock().lock();
        try {
            for (int i = 0; i < 20; i++) {
                System.out.println(thread.getName() + "写操作" + i);
            }
            System.out.println("写操作完毕");
        } finally {
            lock.writeLock().unlock();
        }
    }
}
