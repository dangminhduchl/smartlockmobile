package com.example.smartlockjava.client;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;


import org.json.JSONObject;


public abstract class WebSocketClient {
    private OkHttpClient client;
    private WebSocket webSocket;

    public WebSocketClient() {
        client = new OkHttpClient();
    }

    public void start() {
        Request request = new Request.Builder().url("ws://172.20.10.4:8000/ws/status").build();
        webSocket = client.newWebSocket(request, new EchoWebSocketListener());
    }

    public void close() {
        if (webSocket != null) {
            webSocket.close(1000, null);
        }
    }

    public abstract void onMessage(String text);

    private final class EchoWebSocketListener extends WebSocketListener {
        @Override
        public void onOpen(WebSocket webSocket, okhttp3.Response response) {
            System.out.println("Connected to the server");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            System.out.println("Receiving : " + text);
            WebSocketClient.this.onMessage(text);
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            System.out.println("Receiving bytes : " + bytes.hex());
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(1000, null);
            System.out.println("Closing : " + code + " / " + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
            t.printStackTrace();
        }
    }
}