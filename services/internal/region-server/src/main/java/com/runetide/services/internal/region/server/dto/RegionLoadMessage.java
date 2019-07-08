package com.runetide.services.internal.region.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.runetide.common.TopicMessage;
import com.runetide.common.dto.RegionRef;

public class RegionLoadMessage extends TopicMessage {
    @JsonProperty
    private RegionRef region;

    public RegionLoadMessage() {
    }

    public RegionLoadMessage(final RegionRef region) {
        this.region = region;
    }

    public RegionRef getRegion() {
        return region;
    }

    public void setRegion(final RegionRef region) {
        this.region = region;
    }
}
