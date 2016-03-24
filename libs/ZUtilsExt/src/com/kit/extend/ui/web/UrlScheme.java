package com.kit.extend.ui.web;

import java.util.Locale;

public enum UrlScheme {
    HTTP("http://"),
    HTTPS("https://"),
    WWW("www."),
    FILE("file://"),
    CONTENT("content://"),
    ASSETS("assets://"),
    DRAWABLE("drawable://"),
    UNKNOWN("");

    private String scheme;
    private String uriPrefix;

    UrlScheme(String scheme) {
        this.scheme = scheme;
        uriPrefix = scheme ;
    }

    public static UrlScheme ofUri(String uri) {
        if (uri != null) {
            for (UrlScheme s : values()) {
                if (s.belongsTo(uri)) {
                    return s;
                }
            }
        }
        return UNKNOWN;
    }

    public String crop(String uri) {
        if (!belongsTo(uri)) {
            throw new IllegalArgumentException(String.format("URI [%1$s] doesn't have expected scheme [%2$s]", uri, scheme));
        }
        return uri.substring(uriPrefix.length());
    }

    public String toUri(String path) {
        return uriPrefix + path;
    }

    private boolean belongsTo(String uri) {
        return uri.toLowerCase(Locale.US).startsWith(uriPrefix);
    }
}