package com.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProxyClient {
    private static final int LOCAL_PORT = 1080;
    private static final String FLINK_HOST = "localhost";  // 修改为实际的Flink服务器地址
    private static final int FLINK_PORT = 8081;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(LOCAL_PORT)) {
            System.out.println("本地SOCKS代理启动在端口: " + LOCAL_PORT);
            ExecutorService executor = Executors.newCachedThreadPool();
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                executor.submit(() -> handleConnection(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleConnection(Socket clientSocket) {
        try {
            // 读取SOCKS5握手请求
            byte[] request = new byte[1024];
            clientSocket.getInputStream().read(request);

            // 发送SOCKS5握手响应
            byte[] response = {5, 0};
            clientSocket.getOutputStream().write(response);

            // 读取SOCKS5连接请求
            clientSocket.getInputStream().read(request);

            // 连接到Flink服务器
            Socket flinkSocket = new Socket(FLINK_HOST, FLINK_PORT);
            System.out.println("已连接到Flink服务器: " + FLINK_HOST + ":" + FLINK_PORT);

            // 发送SOCKS5连接响应
            response = new byte[]{5, 0, 0, 1, 0, 0, 0, 0, 0, 0};
            clientSocket.getOutputStream().write(response);

            // 开始双向转发数据
            transferData(clientSocket, flinkSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void transferData(Socket client, Socket target) throws IOException {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // 客户端 -> 目标
        executor.submit(() -> {
            try {
                transfer(client.getInputStream(), target.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // 目标 -> 客户端
        executor.submit(() -> {
            try {
                transfer(target.getInputStream(), client.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static void transfer(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
            out.flush();
        }
    }
}
