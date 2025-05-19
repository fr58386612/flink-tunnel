#!/bin/bash

while true; do
    echo "===== $(date) ====="
    echo "Checking Flink UI..."
    curl --socks5 127.0.0.1:1080 -s http://localhost:8081/overview | grep -q "flink-version" && {
        echo "Flink UI accessible"
        echo "Checking jobs..."
        curl --socks5 127.0.0.1:1080 -s http://localhost:8081/jobs/overview
    } || {
        echo "Flink UI not accessible"
        echo "Restarting proxy..."
        pkill -f ProxyClient
        java -cp target/flink-tunnel-1.0-SNAPSHOT.jar com.example.ProxyClient &
        sleep 2
    }
    echo "------------------------"
    sleep 5
done
