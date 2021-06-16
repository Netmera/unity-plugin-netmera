package com.netmera.unity.sdk.entity;

import com.google.gson.annotations.SerializedName;
import com.netmera.NetmeraUser;

import java.util.Map;

public class NetmeraUserImpl extends NetmeraUser {
    @SerializedName("prms")
    private Map<String, Object> userParameters;

    public NetmeraUserImpl(Map<String, Object> userParameters) {
        this.userParameters = userParameters;
    }

    @Override
    public String toString() {
        return "NetmeraUser: => " + userParameters;
    }
}
