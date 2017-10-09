package com.gennlife.guava;

import com.google.common.util.concurrent.*;
import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * Created by liuzhen on 2017/10/9.
 */
public class ListenableFutureTest {
    @Test
    public void testListenableFuture(){
        ListeningExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
        ListenableFuture<Integer> future = executorService.submit(new Callable<Integer>() {
            public Integer call() throws Exception {
                Thread.sleep(5000);
                return 1;
            }
        });

        Futures.addCallback(future, new FutureCallback<Integer>() {
            public void onSuccess(Integer integer) {
                //System.out.println(integer);
            }
            public void onFailure(Throwable throwable) {

            }
        });
        try {
            System.out.println("deal something");
            System.out.println(future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
       /* while (true){
            try {

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("1111111111111");
        }*/
    }
}
