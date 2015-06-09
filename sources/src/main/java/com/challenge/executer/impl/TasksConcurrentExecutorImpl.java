package com.challenge.executer.impl;

import com.challenge.executer.TasksConcurrentExecutor;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Created by YULIAT on 06/08/2015.
 */
public class TasksConcurrentExecutorImpl<R, C> implements TasksConcurrentExecutor<R, C> {

    private BlockingQueue<Runnable> listingsQueue = new LinkedBlockingQueue<>();
    private ThreadPoolExecutor threadPoolExecutor;
    private Set<Future<R>> futureTasks = new HashSet<>();

    public TasksConcurrentExecutorImpl(int corePoolSize, int maximumPoolSize) {
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 1, TimeUnit.SECONDS, listingsQueue);
    }

    public void submitTask(Callable<R> task) {
        Future<R> futureTask = threadPoolExecutor.submit(task);
        futureTasks.add(futureTask);
    }

    public void start() {
        threadPoolExecutor.prestartAllCoreThreads();
    }

    public void shutdown() {
        threadPoolExecutor.shutdown();
    }

    public C collectResults(ResultsCollector<R, C> collector) {

        synchronized (futureTasks) {
            try {
                for (Future<R> future : futureTasks) {
                    R result = future.get();
                    collector.collectResult(result);
                }
                return collector.getCollectedResults();

            } catch (InterruptedException exception) {
                exception.printStackTrace();
                threadPoolExecutor.shutdown();
                return null;
            } catch (ExecutionException exception) {
                exception.printStackTrace();
                threadPoolExecutor.shutdown();
                return null;
            }
        }
    }
}
