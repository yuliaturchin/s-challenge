package com.challenge.executer;

import java.util.concurrent.Callable;

/**
 * Created by YULIAT on 06/08/2015.
 */

/**
 *
 * @param <R> the Result of the single task execution
 * @param <C> collected Results of all the submitted tasks
 */
public interface TasksConcurrentExecutor<R, C> {


    public interface  ResultsCollector<R,C> {
        void collectResult (R result);
        C getCollectedResults();
    }

    /**
     * Registers the task for asynchronous execution
     * @param task
     */
    public void submitTask(Callable<R> task);

    /**
     * Starts concurrent execution as new tasks are being submitted
     */
    public void start();

    /**
     * Shutdowns concurrent execution of submitted tasks
     */
    public void shutdown();

    /**
     * Collects results from all the concurrent tasks submitted to the executor.
     * @param collector
     * @return
     */
    public C collectResults(ResultsCollector<R,C> collector);

}
