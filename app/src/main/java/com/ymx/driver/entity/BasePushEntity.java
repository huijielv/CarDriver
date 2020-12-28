package com.ymx.driver.entity;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class BasePushEntity<U> extends BaseEntity {
    private String code;
    private U data;
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public U getData() {
        return data;
    }

    public void setData(U data) {
        this.data = data;
    }

    public static BasePushEntity fromJson(String json, Class clazz) {
        Gson gson = new Gson();
        Type objectType = type(BasePushEntity.class, clazz);
        return gson.fromJson(json, objectType);
    }

    public String toJson(Class<U> clazz) {
        Gson gson = new Gson();
        Type objectType = type(BasePushEntity.class, clazz);
        return gson.toJson(this, objectType);
    }

    static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }

            public Type[] getActualTypeArguments() {
                return args;
            }

            public Type getOwnerType() {
                return null;
            }
        };
    }
}
