package com.bigfong.spring.framework.orm.framework;

import java.util.Map;

/**
 * @author bigfong
 * @since 2019/10/7
 */
public class KeyHolder {
    private Map<String, Object> keys;

    public Map<String,Object> getKeys() {
        return keys;
    }

    public void setKeys(Map<String,Object> keys) {
        this.keys = keys;
    }
}
