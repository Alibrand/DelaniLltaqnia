package com.CPIS498.delanilltaqnia.models;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class BasicClass {
    public  Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        for (Field field : this.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try { map.put(field.getName(), field.get(this)); } catch (Exception e) { }
        }
        return map;
    }
}
