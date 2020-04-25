package com.runetide.services.internal.character.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.runetide.services.internal.xp.common.XPType;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class XPModifier {
    @JsonProperty("x")
    private XPType xpType;
    @JsonProperty("m")
    private double multiplier;

    public XPModifier() {
    }

    public XPModifier(XPType xpType, double multiplier) {
        this.xpType = xpType;
        this.multiplier = multiplier;
    }

    public XPType getXpType() {
        return xpType;
    }

    public void setXpType(XPType xpType) {
        this.xpType = xpType;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        XPModifier that = (XPModifier) o;
        return Double.compare(that.multiplier, multiplier) == 0 &&
                Objects.equals(xpType, that.xpType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(xpType, multiplier);
    }
}
