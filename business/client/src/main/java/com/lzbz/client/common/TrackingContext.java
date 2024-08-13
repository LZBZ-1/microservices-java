package com.lzbz.client.common;

import org.slf4j.MDC;

import java.util.UUID;

public class TrackingContext {
    private static final String TRACKING_ID_KEY = "trackingId";

    public static String initializeTrackingId() {
        String trackingId = UUID.randomUUID().toString();
        MDC.put(TRACKING_ID_KEY, trackingId);
        return trackingId;
    }

    public static void setTrackingId(String trackingId) {
        MDC.put(TRACKING_ID_KEY, trackingId);
    }

    public static String getTrackingId() {
        return MDC.get(TRACKING_ID_KEY);
    }

    public static void clearTrackingId() {
        MDC.remove(TRACKING_ID_KEY);
    }
}
