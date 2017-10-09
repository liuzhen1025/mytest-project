package com.gennlife.masterworker.worker;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**   worker - master 模式 worker 端
 * Created by lz on 2017/9/27.
 */


public class Worker implements Runnable{
    //任务队列
    private ConcurrentLinkedQueue<String> linkedQueue;
    //保存结果集
    private ConcurrentHashMap<String,Object> resultMap;

    private Integer workerId = 0;
    public Worker(ConcurrentLinkedQueue<String> linkedQueue, ConcurrentHashMap<String, Object> resultMap, Integer workerId) {
        this.linkedQueue = linkedQueue;
        this.resultMap = resultMap;
        this.workerId = workerId;
    }

    /**
     * 执行操作
     */
    public void run() {
        int loop = 0;
        while (true) {
            String poll = (String)linkedQueue.poll();
            if(poll == null || "".equals(poll)){
                break;
            }
            loop++;
            /*try {
                Thread.sleep(5000*workerId);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            resultMap.put(poll.concat(String.valueOf(workerId))+":"+loop,Thread.currentThread().getId());
        }
    }
}