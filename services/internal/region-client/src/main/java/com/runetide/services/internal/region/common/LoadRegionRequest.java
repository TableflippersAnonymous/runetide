package com.runetide.services.internal.region.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.runetide.common.dto.RegionRef;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoadRegionRequest {
    @JsonProperty
    private RegionRef region;
}
