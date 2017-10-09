package com.gennlife.masterworker.worker.master;

import com.gennlife.masterworker.worker.Worker;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/** worker - master 模式Master端
 * Created by lz on 2017/9/27.
 */
public class Master implements Callable{
    //任务队列
    private ConcurrentLinkedQueue<String> tasks = new ConcurrentLinkedQueue<String>();
    //任务结果集
    private ConcurrentHashMap<String,Object> resultMap = new ConcurrentHashMap<String, Object>();
    //任务队列
    private ConcurrentHashMap<String,Thread> workers = new ConcurrentHashMap<String, Thread>();

    /**
     * Master 构造函数
     * @param tasks
     * @param resultMap
     * @param numWorkers
     */
    public Master(ConcurrentLinkedQueue<String> tasks, ConcurrentHashMap<String, Object> resultMap, Integer numWorkers) {
        this.tasks = tasks;
        this.resultMap = resultMap;
        for(int i = 0; i < numWorkers; i++){
            Worker worker = new Worker(tasks, resultMap, i);
            workers.put(String.valueOf(i),new Thread(worker,String.valueOf(i)));
        }
    }

    /**
     * 判断所有worker{@link Worker}是否全部执行完成
     * @return {@link Boolean} 进程是否全部执行完成
     */
    public boolean isComplate(){
        Set<Map.Entry<String, Thread>> entries = workers.entrySet();
        for (Map.Entry<String, Thread> task:entries){
            if(task.getValue().getState()!=Thread.State.TERMINATED){
                return false;
            }
        }
        return true;
    }
    public void addTask(String task){
        tasks.add(task);
    }
    /**
     * 获取结果集
     * @return
     */
    public ConcurrentHashMap<String,Object> getResultMap(){
        return resultMap;
    }

    /**
     * 使用futureTask{@link java.util.concurrent.FutureTask} 时使用
     * @return
     * @throws Exception
     */
    public Object call() throws Exception {
        Set<Map.Entry<String, Thread>> entries = workers.entrySet();
        //提交所有任务
        for (Map.Entry<String, Thread> task:entries){
            task.getValue().start();
        }
        //等待所有进程完成
        //实时获取到每一个结果
        //http://blog.csdn.net/ghuil/article/details/41048005
        /**
         * while(true) {
         Set<String> keys = resultMap.keySet();  //开始计算最终结果
         String key = null;
         for(String k : keys) {
         key = k;
         break;
         }
         Integer i = null;
         if(key != null)
         i = (Integer)resultMap.get(key);
         if(i != null)
         re += i; //最终结果
         if(key != null)
         resultMap.remove(key); //移除已被计算过的项目
         if(master.isComplete() && resultMap.size()==0)
         break;
         }
         */
        while (true) {
            if(isComplate()){
                return resultMap;
            }
        }

    }

    /**
     * 执行所有任务队列中的任务{@link Worker}，直接使用Thread {@link Thread} 的start方法时使用
     */
    public void execute(){

        Set<Map.Entry<String, Thread>> entries = workers.entrySet();
        //执行每个任务
        for (Map.Entry<String, Thread> task:entries){
            task.getValue().start();
        }
    }
}
