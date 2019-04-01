package cn.dbboy.zk;

import cn.dbboy.zk.base.Url;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;

/**
 * Created by DB_BOY on 2019/3/20.
 */
public class ZkFirst {

    ZooKeeper zk;

    public ZkFirst() {
        try {
            zk = new ZooKeeper(Url.ip, 5000, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void first() throws Exception {
        List<String> list = zk.getChildren("/", null);

        for (String s : list) {
            System.out.println(s);
        }
    }

    /**
     * 递归调用所有节点
     *
     * @param path
     * @throws Exception
     */
    public void getChildNode(String path) throws Exception {
        System.out.println(path);
        List<String> list = zk.getChildren(path, null);

        for (String s : list) {
            if ("/".equals(path)) {
                getChildNode(path + s);
            } else {
                getChildNode(path + "/" + s);
            }
        }
    }


    /**
     * 添加节点数据
     *
     * @param path
     * @param data
     * @throws Exception
     */
    public void addNodeData(String path, String data) throws Exception {
        zk.setData(path, data.getBytes(), 0);
    }

    /**
     * 创建节点
     *
     * @throws Exception 并发创建是序列节点创建
     */
    public void createNode(String path, String data, CreateMode mode) throws Exception {
        zk.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, mode);

        getChildNode("/dbboy");
    }

    /**
     * 添加监听
     */
    public void addWatches() throws Exception {
        final Stat st = new Stat();
        Watcher w = new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                System.out.println("修改数据");
                System.out.println(watchedEvent);
                System.out.println(st);
                try {
                    zk.getData("/dbboy", this, st);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        final byte[] bytes = zk.getData("/dbboy", w, st);

        System.out.println(new String(bytes));
        while (true) {
            Thread.sleep(20000);
        }
    }

}
