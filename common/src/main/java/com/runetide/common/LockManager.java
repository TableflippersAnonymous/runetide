package com.runetide.common;

public interface LockManager {
    boolean tryAcquire(final String name);
    void release(final String name);
}
