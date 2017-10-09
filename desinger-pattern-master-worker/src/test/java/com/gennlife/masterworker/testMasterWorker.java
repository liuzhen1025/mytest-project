package com.gennlife.masterworker;

import com.gennlife.masterworker.worker.master.Master;
import com.gennlife.math.OperatorEnum;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Created by lz on 2017/9/27.
 */
public class testMasterWorker {
    private static ConcurrentLinkedQueue<String> task;
    private static ConcurrentHashMap<String,Object> resultMap;
    private static ConcurrentHashMap<String,Thread> thread;
    @BeforeClass
    public static void before(){
        task = new ConcurrentLinkedQueue<String>();
        resultMap = new ConcurrentHashMap<String, Object>();
        thread = new ConcurrentHashMap<String, Thread>();
    }
    @Test
    public void futureModelTest(){
        task.add("task1");
        task.add("task2");
        task.add("task3");
        task.add("task4");
        task.add("task5");
        task.add("task6");
        task.add("task7");
        task.add("task8");
        task.add("task9");
        task.add("task10");
        Master master = new Master(task,resultMap,2);
        FutureTask<ConcurrentHashMap<String,Object>> futureTask = new FutureTask(master);
        ExecutorService executor = Executors.newFixedThreadPool(1); //使用线程池
        executor.submit(futureTask);
        System.out.println("我在做其他任务！");
        for (int j = 11; j < 30; j++){
            master.addTask("task"+j);
        }
        try {
            ConcurrentHashMap<String,Object> o = futureTask.get();
            Set<String> keys = o.keySet();
            for (String key:keys) {
                System.out.println(resultMap.get(key));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void executeTest(){
        task.add("task1");
        task.add("task2");
        task.add("task3");
        task.add("task4");
        task.add("task5");
        task.add("task6");
        task.add("task7");
        task.add("task8");
        task.add("task9");
        task.add("task10");
        Master master = new Master(task,resultMap,5);
        master.execute();
        /*try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        while (true){
            if(master.isComplate()){
                ConcurrentHashMap<String, Object> resultMap = master.getResultMap();
                Set<String> keys = resultMap.keySet();
                for (String key:keys) {
                    System.out.println(resultMap.get(key));
                }
                System.out.println("任务执行完成");
                break;
            }
        }
    }
    @Test
    public void operatorEnumTest(){
        System.out.println(OperatorEnum.Add.name().equals("Add"));
    }
}
