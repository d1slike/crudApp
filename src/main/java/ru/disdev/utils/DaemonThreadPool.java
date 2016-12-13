package ru.disdev.utils;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DaemonThreadPool {
    private static final ScheduledExecutorService EXECUTOR_SERVICE =
            new ScheduledThreadPoolExecutor(1, r -> {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                return thread;
            });

    public static void schedule(Runnable runnable, long delay, TimeUnit unit) {
        EXECUTOR_SERVICE.schedule(runnable, delay, unit);
    }

    public static void execute(Runnable runnable) {
        EXECUTOR_SERVICE.execute(runnable);
    }
}
