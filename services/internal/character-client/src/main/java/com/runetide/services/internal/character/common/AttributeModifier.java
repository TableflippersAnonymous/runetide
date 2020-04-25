package com.runetide.services.internal.character.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AttributeModifier {
    @JsonProperty("a")
    private Attribute attribute;
    @JsonProperty("m")
    private double multiplier;

    public AttributeModifier() {
    }

    public AttributeModifier(Attribute attribute, double multiplier) {
        this.attribute = attribute;
        this.multiplier = multiplier;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
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
        AttributeModifier that = (AttributeModifier) o;
        return Double.compare(that.multiplier, multiplier) == 0 &&
                attribute == that.attribute;
    }

    @Override
    public int hashCode() {
        return Objects.hash(attribute, multiplier);
    }
}
