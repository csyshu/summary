package com.csy.summary.daily.lock_test;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author shuyun.cheng
 * Description 局部锁
 * @date Create in 10:58 2019/4/11
 */
public class LockTest1 {
    private final ArrayList<Integer> arrayList = new ArrayList<>();

    public static void main(String[] args) {
        final LockTest1 test = new LockTest1();
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
        Lock lock = new ReentrantLock();//注意这个地方
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
