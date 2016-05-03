package org.jasig.cas.nhnent.util;

import net.spy.memcached.MemcachedClientIF;

import java.util.Objects;

/**
 * Created by ruaa on 2016. 4. 25..
 */
public class MemcachedIO {
    private static final int EXPIRATION_SECONDS = 30 * 24 * 60 * 60;
    private MemcachedClientIF client;

    public MemcachedIO(MemcachedClientIF client) {
        this.client = client;
    }

    public void set(String key, Object value) {
        this.client.set(key, EXPIRATION_SECONDS, value);
    }

    public Object get(String key) {
        return this.client.get(key);
    }

    public void delete(String key) {
        this.client.delete(key);
    }
}
