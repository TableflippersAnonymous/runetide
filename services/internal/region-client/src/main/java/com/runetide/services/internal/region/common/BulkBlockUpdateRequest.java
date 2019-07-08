package com.runetide.services.internal.region.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BulkBlockUpdateRequest {
    @JsonProperty("u")
    private List<BulkBlockUpdateEntry> updates;

    public BulkBlockUpdateRequest() {
    }

    public BulkBlockUpdateRequest(final List<BulkBlockUpdateEntry> updates) {
        this.updates = updates;
    }

    public List<BulkBlockUpdateEntry> getUpdates() {
        return updates;
    }

    public void setUpdates(final List<BulkBlockUpdateEntry> updates) {
        this.updates = updates;
    }
}
