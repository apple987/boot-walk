package com.qdone.common.job;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
/**
 * @author 付为地
 *  简单定时任务
 */
public class SpringSimpleJob implements SimpleJob {
    
    @Override
    public void execute(final ShardingContext context) {
    	System.out.println(String.format("Item: %s | Time: %s | Thread: %s | %s",
    			context.getShardingItem(), new SimpleDateFormat("HH:mm:ss").format(new Date()), Thread.currentThread().getId(), "SIMPLE"));
        String shardParamter = context.getShardingParameter();
        System.out.println("分片参数："+shardParamter);
    }

}
