package cn.dbboy.zk;

import cn.dbboy.zk.base.Url;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.List;

/**
 * Created by DB_BOY on 2019/3/20.
 */
public class ZkFirst {

    public static void first() throws IOException, KeeperException, InterruptedException {
        ZooKeeper zk = new ZooKeeper(Url.ip, 5000, null);
        List<String> list = zk.getChildren("/", null);

        for (String s : list) {
            System.out.println(s);
        }
    }
}
