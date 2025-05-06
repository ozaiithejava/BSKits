package org.ozaii.bskits.services;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceRegistry {

    // Mapping for services
    private static ConcurrentHashMap<Class<?>, Object> services = new ConcurrentHashMap<>();

    // Service Registery Using Type
    public static <T> void registerService(Class<T> clazz, T service) {
        if (services.containsKey(clazz)) {
            throw new IllegalArgumentException(clazz.getSimpleName() + " aldredy registired.");
        }
        services.put(clazz, service);
    }

    // getServices for clazz
    public static <T> T getService(Class<T> clazz) {
        if (!services.containsKey(clazz)) {

            throw new RuntimeException(clazz.getSimpleName() + " not a exist!!");
        }
        return (T) services.get(clazz);
    }

    // check is available
    public static boolean hasService(Class<?> clazz) {
        return services.containsKey(clazz);
    }

    // clear all
    public static void clearServices() {
        services.clear();
    }
}

