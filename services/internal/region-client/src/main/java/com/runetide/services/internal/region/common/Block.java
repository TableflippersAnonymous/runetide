package com.runetide.services.internal.region.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.runetide.common.domain.BlockType;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Block {
    @JsonProperty("t")
    private BlockType type;
    @JsonProperty("v")
    private int variant;
    @JsonProperty("d")
    private Map<Integer, String> data;
    @JsonProperty("n")
    private int naturalLight;
    @JsonProperty("a")
    private int artificialLight;

    public Block() {
    }

    public Block(final BlockType type, final int variant, final Map<Integer, String> data, final int naturalLight,
                 final int artificialLight) {
        this.type = type;
        this.variant = variant;
        this.data = data;
        this.naturalLight = naturalLight;
        this.artificialLight = artificialLight;
    }

    public BlockType getType() {
        return type;
    }

    public void setType(final BlockType type) {
        this.type = type;
    }

    public Map<Integer, String> getData() {
        return data;
    }

    public void setData(final Map<Integer, String> data) {
        this.data = data;
    }

    public int getNaturalLight() {
        return naturalLight;
    }

    public void setNaturalLight(final int naturalLight) {
        this.naturalLight = naturalLight;
    }

    public int getArtificialLight() {
        return artificialLight;
    }

    public void setArtificialLight(final int artificialLight) {
        this.artificialLight = artificialLight;
    }

    public int getVariant() {
        return variant;
    }

    public void setVariant(final int variant) {
        this.variant = variant;
    }

    public byte[] getEncodedData() {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try(final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream)) {
            for(final Map.Entry<Integer, String> entry : data.entrySet()) {
                dataOutputStream.writeInt(entry.getKey());
                dataOutputStream.writeUTF(entry.getValue());
            }
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static Map<Integer, String> decodeData(final byte[] bytes) {
        final Map<Integer, String> map = new HashMap<>();
        if(bytes == null || bytes.length == 0)
            return map;
        try(final DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bytes))) {
            while(dataInputStream.available() > 0) {
                final int key = dataInputStream.readInt();
                final String value = dataInputStream.readUTF();
                map.put(key, value);
            }
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
        return map;
    }
}
