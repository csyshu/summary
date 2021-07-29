package com.csy.summary.daily.zookeeper;

import com.google.common.collect.Lists;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.transaction.CuratorTransaction;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ZkTests {

    @Resource
    private CuratorFramework curatorClient;

    /**
     * 创建节点
     * @throws Exception
     */
    @Test
    public void nodeCreateTest() throws Exception {
        //创建结点，输出如下：
        // treeCache, type:NODE_ADDED
        // treeCache, nodePath:/zk data: type:NODE_ADDED
        // nodeChanged,nodePath:/zk data:
        curatorClient.create().creatingParentsIfNeeded().forPath("/zk");

        //创建结点，同时设置值，输出如下：
        // treeCache, type:NODE_ADDED
        // treeCache, nodePath:/zk data: type:NODE_ADDED
        // nodeChanged,nodePath:/zk data:
        curatorClient.create().creatingParentsIfNeeded().forPath("/zk", "this is zk1".getBytes());

        //单独设置结点值
        // treeCache, type:NODE_UPDATED
        // treeCache, nodePath:/zk data:this is zk6 type:NODE_UPDATED
        // nodeChanged,nodePath:/zk data:this is zk6
        curatorClient.setData().forPath("/zk", "this is zk6".getBytes());

        //创建包含父结点的结点，输出如下：
        // treeCache, type:NODE_ADDED
        // treeCache, nodePath:/zk/one data:this is one type:NODE_ADDED
        // pathChildrenCache, type:CHILD_ADDED
        // pathChildrenCache, nodePath:/zk/one data:this is zk one2 type:CHILD_ADDED
        curatorClient.create().creatingParentsIfNeeded().forPath("/zk/one", "this is one".getBytes());

        //单独设置结点值，输出如下：
        // treeCache, type:NODE_ADDED
        // treeCache, nodePath:/zk/one data:this is zk one2 type:NODE_ADDED
        // pathChildrenCache, type:CHILD_ADDED
        // pathChildrenCache, nodePath:/zk/one data:this is zk one2 type:CHILD_ADDED
        curatorClient.setData().forPath("/zk/one", "this is zk one2".getBytes());

        //事务，执行多个操作
        CuratorTransaction curatorTransaction = curatorClient.inTransaction();
        curatorTransaction.create().withMode(CreateMode.EPHEMERAL).forPath("/zk/three", "this is three".getBytes())
                .and().create().withMode(CreateMode.PERSISTENT).forPath("/zk/four", "this is four".getBytes())
                .and().commit();
    }

    @Test
    public void createTest() throws Exception {
        curatorClient.checkExists().creatingParentContainersIfNeeded().forPath("/zk");
    }

    /**
     * 基于底层临时节点和顺序节点实现
     */
    @Test
    public void leaderLatchTest() {
        List<CuratorFramework> clients = Lists.newArrayList();
        List<LeaderLatch> leaderLatches = Lists.newArrayList();

        try {
            for (int i = 0; i < 10; i++) {
                CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", new RetryNTimes(3, 1000));
                clients.add(client);
                client.start();

                LeaderLatch leaderLatch = new LeaderLatch(client, "/leaderlatch", "client#" + i);
                leaderLatches.add(leaderLatch);
                leaderLatch.start();
            }
            // 睡2S用于选举出结果
            TimeUnit.SECONDS.sleep(2);
            for (LeaderLatch leaderLatch : leaderLatches) {
                if (leaderLatch.hasLeadership()) {
                    System.out.println("当前Leader是" + leaderLatch.getId());
                }
            }

            System.in.read();
        } catch (Exception e) {
            System.out.println("异常信息：" + e);
        } finally {
            for (CuratorFramework client : clients) {
                CloseableUtils.closeQuietly(client);
            }
            for (LeaderLatch leaderLatch : leaderLatches) {
                CloseableUtils.closeQuietly(leaderLatch);
            }
        }
    }
    /**
     * 基于锁机制实现，谁获取到谁就是leader
     */
    @Test
    public void leaderSelecterTest() {
        List<CuratorFramework> clients = Lists.newArrayList();
        List<LeaderSelector> leaderSelectors = Lists.newArrayList();

        try {
            for (int i = 0; i < 10; i++) {
                CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", new RetryNTimes(3, 1000));
                clients.add(client);
                client.start();

                LeaderSelector leaderSelector = new LeaderSelector(client, "/leaderselector", new LeaderSelectorListener() {
                    @Override
                    public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
                        System.out.println("当前为Leader是：" + client);
                        // 睡5s代表有5S的leader权，5s后重新选举leader
                        TimeUnit.SECONDS.sleep(5);
                    }

                    @Override
                    public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
                        System.out.println("状态变更："+connectionState.isConnected());
                    }
                });
                leaderSelector.start();
                leaderSelectors.add(leaderSelector);
            }
            // 睡2S用于选举出结果
            TimeUnit.SECONDS.sleep(2);
            for (LeaderSelector leaderSelector : leaderSelectors) {
                if (leaderSelector.hasLeadership()) {
                    System.out.println("当前Leader编号是：" + leaderSelector.getId());
                }
            }

            System.in.read();
        } catch (Exception e) {
            System.out.println("异常信息：" + e);
        } finally {
            for (CuratorFramework client : clients) {
                CloseableUtils.closeQuietly(client);
            }
            for (LeaderSelector leaderSelector : leaderSelectors) {
                CloseableUtils.closeQuietly(leaderSelector);
            }
        }
    }
}