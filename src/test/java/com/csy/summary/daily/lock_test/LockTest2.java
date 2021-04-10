package com.csy.summary.daily.lock_test;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author shuyun.cheng
 * Description: 全局锁
 * @date Create in 10:58 2019/4/11
 */
public class LockTest2 {
    private final ArrayList<Integer> arrayList = new ArrayList<>();
    private final Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        final LockTest2 test = new LockTest2();
        new Thread() {
            public void run() {
                test.insert(Thread.currentThread());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        new Thread() {
            public void run() {
                test.insert(Thread.currentThread());
            }
        }.start();
    }

    public void insert(Thread thread) {
        lock.lock();
        try {
            System.out.println(thread.getName() + "得到了锁");
            for (int i = 0; i < 5; i++) {
                arrayList.add(i);
            }
        } catch (Exception e) {
            //TODO: handle exception
        } finally {
            System.out.println(thread.getName() + "释放了锁");
            lock.unlock();
        }
    }
}
