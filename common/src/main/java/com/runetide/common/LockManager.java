package com.runetide.common;

public interface LockManager {
    boolean tryAcquire(final String name);
    boolean tryRelease(final String name);
}
