package com.csy.summary.daily.lock_test;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: shuyun.cheng
 * @description tryLock 尝试获取锁
 * @date Create in 10:58 2019/4/11
 */
public class LockTest3 {

    private final ArrayList<Integer> arrayList = new ArrayList<>();
    private final Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        final LockTest3 test = new LockTest3();
        new Thread() {
            public void run() {
                test.insert(Thread.currentThread());
            }

            ;
        }.start();
        new Thread() {
            public void run() {
                test.insert(Thread.currentThread());
            }

            ;
        }.start();
    }

    public void insert(Thread thread) {

        if (lock.tryLock()) {
            try {
                System.out.println(thread.getName() + "得到了锁");
                for (int i = 0; i < 200; i++) {
                    arrayList.add(i);
                }
            } catch (Exception e) {
                //TODO: handle exception
            } finally {
                System.out.println(thread.getName() + "释放了锁");
                lock.unlock();
            }
        } else {
            System.out.println(thread.getName() + "获取锁失败");
        }
    }
}
