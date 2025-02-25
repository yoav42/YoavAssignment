package org.example.weather.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ParallelExecutor {
    private static final int THREAD_COUNT = 10;
    private final ExecutorService executor;

    public ParallelExecutor() {
        this.executor = Executors.newFixedThreadPool(THREAD_COUNT);
    }

    public <T> List<T> executeTasks(List<Callable<T>> tasks, long timeout, TimeUnit timeUnit) {
        List<Future<T>> futures = new ArrayList<>();

        for (Callable<T> task : tasks) {
            futures.add(executor.submit(task));
        }

        List<T> results = new ArrayList<>();
        for (Future<T> future : futures) {
            try {
                results.add(future.get(timeout, timeUnit));
            } catch (Exception e) {
                System.out.println("A task could not be completed: " + e.getMessage());
            }
        }
        return results;
    }

}