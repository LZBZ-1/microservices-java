package com.lzbz.client.common;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("@annotation(com.lzbz.client.common.Tracked)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String trackingId = TrackingContext.getTrackingId();

        logger.info("Tracking ID: {} - Entering method: {}", trackingId, methodName);

        Object result = joinPoint.proceed();

        if (result instanceof Mono) {
            return ((Mono<?>) result)
                    .doOnSuccess(r -> logger.info("Tracking ID: {} - Exiting method: {} with result: {}", trackingId, methodName, r))
                    .doOnError(e -> logger.error("Tracking ID: {} - Error in method: {}: {}", trackingId, methodName, e.getMessage()));
        } else {
            logger.info("Tracking ID: {} - Exiting method: {} with result: {}", trackingId, methodName, result);
            return result;
        }
    }
}
