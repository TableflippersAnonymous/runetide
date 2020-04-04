package com.runetide.common;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.nodes.GroupMember;

import java.io.Closeable;
import java.util.UUID;

public class LoadingToken implements Closeable {
    private final GroupMember groupMember;

    public LoadingToken(final CuratorFramework curatorFramework, final String objectName, final String key) {
        groupMember = new GroupMember(curatorFramework,
                Constants.ZK_SVC_INTEREST + objectName + "/" + key, UUID.randomUUID().toString());
    }

    public void start() {
        groupMember.start();
    }

    @Override
    public void close() {
        groupMember.close();
    }
}
