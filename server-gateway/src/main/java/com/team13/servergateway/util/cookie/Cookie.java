package com.team13.servergateway.util.cookie;

import lombok.Getter;

@Getter
public class Cookie {
    private String name;
    private String value;

    public Cookie(String cookieValue) {
        if (cookieValue.contains("=")) {
            String[] values = cookieValue.split("=");

            this.name = values[0];
            this.value = values[1];
        }
    }
}
