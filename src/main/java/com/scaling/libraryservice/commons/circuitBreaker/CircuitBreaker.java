package com.scaling.libraryservice.commons.circuitBreaker;

import com.scaling.libraryservice.mapBook.domain.ApiObserver;
import com.scaling.libraryservice.mapBook.dto.ApiStatus;
import com.scaling.libraryservice.mapBook.util.ApiQuerySender;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j @RequiredArgsConstructor
public class CircuitBreaker {

    private static final List<ApiObserver> observingConnections = new ArrayList<>();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final Map<ApiStatus, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();
    public static boolean checkIsAvailable(ApiStatus apiStatus){

        return ApiQuerySender.checkConnection(apiStatus);
    }

    public static void receiveError(ApiObserver observer,Exception e) {

        Objects.requireNonNull(observer);

        ApiStatus status = observer.getApiStatus();

        String apiUri = status.getApiUri();
        Integer errorCnt = status.getErrorCnt();

        if(!observingConnections.contains(observer)) observingConnections.add(observer);

        status.upErrorCnt();

        if (status.getErrorCnt() > status.DEFAULT_MAX_ERROR_CNT) closeObserver(status);

        log.error("[{}] is received - request api url : [{}] , current error cnt : [{}]"
            ,e.getMessage(),apiUri, errorCnt);
    }

    public static void closeObserver(ApiStatus status) {

        status.closeAccess();
        log.info(status.getApiUri() + " is closed by nested api server error at [{}]",
            status.getClosedTime());

        Runnable checkAvailabilityTask = () -> {
            if (checkIsAvailable(status)) {
                scheduledTasks.get(status).cancel(false);
                scheduledTasks.remove(status);
                status.openAccess();
            }
        };

        ScheduledFuture<?> scheduledFuture = scheduler.scheduleAtFixedRate(checkAvailabilityTask, 1, 1, TimeUnit.HOURS);
        scheduledTasks.put(status, scheduledFuture);
    }

}
