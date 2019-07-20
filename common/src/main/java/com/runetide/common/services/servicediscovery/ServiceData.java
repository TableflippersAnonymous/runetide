package com.runetide.common.services.servicediscovery;

import java.io.Serializable;

public class ServiceData implements Serializable {
    private int load;

    public ServiceData(final int load) {
        this.load = load;
    }

    public int getLoad() {
        return load;
    }

    public void setLoad(final int load) {
        this.load = load;
    }
}
