package com.netmera.unity.sdk.entity;

import com.google.gson.annotations.SerializedName;
import com.netmera.NetmeraEvent;

import java.util.Map;

public class NetmeraEventImpl extends NetmeraEvent {

    @SerializedName("prms")
    private Map<String, Object> eventParameters;

    private String code;

    private NetmeraEventImpl() {
    }

    public NetmeraEventImpl(String code) {
        this.code = code;
    }

    public NetmeraEventImpl(String code, Map<String, Object> eventParameters) {
        this.code = code;
        this.eventParameters = eventParameters;
    }

    @Override
    protected String eventCode() {
        return code;
    }

    @Override
    public String toString() {
        return "NetmeraEvent: code => " + code + " params => " + eventParameters;
    }
}