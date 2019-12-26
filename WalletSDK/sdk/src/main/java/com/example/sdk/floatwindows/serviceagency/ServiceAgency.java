package com.example.sdk.floatwindows.serviceagency;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.example.sdk.floatwindows.serviceagency.exception.AgencyException;
import com.example.sdk.floatwindows.serviceagency.utils.ReflectUtil;

public class ServiceAgency {
    private final HashMap<Class, Object> cacheMap = new LinkedHashMap<>();
    private boolean isServiceConfigExists;

    private static class InstanceHolder {
        private static final ServiceAgency instance = new ServiceAgency();
    }

    @SuppressWarnings("unchecked")
    public static <T> T getService(Class<T> tClass) {
        return InstanceHolder.instance.getServiceFromMap(tClass);
    }

    public static void clear() {
        InstanceHolder.instance.clearMap();
    }

    @SuppressWarnings("unchecked")
    public <T> T getServiceFromMap(Class<T> tClass) {
        if (!isServiceConfigExists) {
            try {
                Class.forName("com.example.sdk.floatwindows.serviceagency.ServiceConfig");
                isServiceConfigExists = true;
            } catch (ClassNotFoundException e) {
                throw new AgencyException("No class annotate with ServiceAgent.");
            }
        }
        T service = (T) cacheMap.get(tClass);
        if (service == null) {

            for (ServiceConfig serviceEnum : ServiceConfig.values()) {
                try {
                    Class serviceClass = Class.forName(serviceEnum.className);
                    if (tClass.isAssignableFrom(serviceClass)) {
                        service = ReflectUtil.newInstance((Class<T>) serviceClass);
                        cacheMap.put(tClass, service);
                        return service;
                    }
                } catch (ClassNotFoundException e) {
                    throw new AgencyException(e);
                }
            }
        } else {
        }
        if (service == null) {
            throw new AgencyException("No class implements " + tClass.getName() + " and annotated with ServiceAgent.");
        }
        return service;
    }

    public void clearMap() {
        cacheMap.clear();
    }

}
