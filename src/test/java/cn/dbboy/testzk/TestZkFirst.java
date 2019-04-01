package cn.dbboy.testzk;

import cn.dbboy.zk.ZkFirst;
import org.apache.zookeeper.CreateMode;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by DB_BOY on 2019/3/20.
 */
public class TestZkFirst {
    ZkFirst first;

    @Before
    public void beforeTest() {
        first = new ZkFirst();
    }

    @Test
    public void ls() throws Exception {
        first.first();
    }

    @Test
    public void getAllNode() throws Exception {
        first.getChildNode("/");
    }

    @Test
    public void addData() throws Exception {
        first.addNodeData("/dbboy", "test");
    }

    @Test
    public void crateNode() throws Exception {
        first.createNode("/dbboy/tmp", "tmplslssslslsl", CreateMode.EPHEMERAL);
    }

    @Test
    public void testWatch() throws Exception {
        first.addWatches();
    }
}
