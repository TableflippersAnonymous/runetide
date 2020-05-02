package com.runetide.services.internal.region.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.runetide.common.services.topics.TopicMessage;
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
