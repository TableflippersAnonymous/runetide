package com.runetide.services.internal.worldgen.server.domain;

public abstract class SubSeed<T extends BaseSeed> extends BaseSeed {
    private final T parent;

    protected SubSeed(final T parent, final byte[] seed) {
        super(seed);
        this.parent = parent;
    }

    protected T getParent() {
        return parent;
    }
}
