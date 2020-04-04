package com.runetide.common;

public interface LockManager {
    boolean tryAcquire(final String name);
    boolean acquire(final String name);
    void release(final String name);
}
