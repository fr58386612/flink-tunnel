package com.example;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

public class TunnelJob {
    public static void main(String[] args) throws Exception {
        try {
            // 解析参数
            ParameterTool parameters = ParameterTool.fromArgs(args);
            int port = parameters.getInt("port", 1080);
            
            System.out.println("[TunnelJob] Starting with port: " + port);

            // 获取执行环境
            final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
            
            // 配置检查点和重启策略
            env.enableCheckpointing(60000); // 每60秒做一次检查点
            env.setParallelism(1); // 设置并行度为1
            
            System.out.println("[TunnelJob] Environment configured");

            // 添加数据源和输出
            env.addSource(new SourceFunction<String>() {
                private volatile boolean isRunning = true;
                private int counter = 0;

                @Override
                public void run(SourceContext<String> ctx) throws Exception {
                    System.out.println("[TunnelJob] Source function started");
                    try {
                        while (isRunning) {
                            counter++;
                            String heartbeat = String.format("heartbeat-%d", counter);
                            ctx.collect(heartbeat);
                            System.out.println("[TunnelJob] Sent: " + heartbeat);
                            Thread.sleep(5000); // 每5秒发送一次心跳
                        }
                    } catch (Exception e) {
                        System.err.println("[TunnelJob] Error in source: " + e.getMessage());
                        e.printStackTrace();
                        throw e; // 重新抛出异常以触发任务失败
                    }
                }

                @Override
                public void cancel() {
                    System.out.println("[TunnelJob] Source function cancelled");
                    isRunning = false;
                }
            })
            .name("Tunnel Source")
            .setParallelism(1)
            .addSink(new SinkFunction<String>() {
                @Override
                public void invoke(String value, Context context) {
                    System.out.println("[TunnelJob] Processed: " + value);
                }
            })
            .name("Tunnel Sink")
            .setParallelism(1);

            System.out.println("[TunnelJob] Executing job");
            env.execute("Flink Tunnel Job");
            
        } catch (Exception e) {
            System.err.println("[TunnelJob] Fatal error: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
