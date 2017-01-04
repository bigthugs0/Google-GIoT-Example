package com.example.osama.giotserver;

/**
 * Created by osama on 1/1/17.
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    MainActivity activity;
    ServerSocket serverSocket;

    //Port to be opened for listening
    static final int socketServerPORT = 6666;

    public Server(MainActivity activity) {
        this.activity = activity;

        //Start Server Listening Thread
        Thread socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();
    }


    //Sever Listening Thread
    private class SocketServerThread extends Thread {

        @Override
        public void run() {
            try {
                //Open Server Socket with Specific Port for listening
                serverSocket = new ServerSocket(socketServerPORT);
                while (true) {
                    //Start Server Socket Listening
                    Socket socket = serverSocket.accept();
                    try {
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                        socket.getInputStream()));
                        String line;

                        //Receive Command from client
                        if ((line = in.readLine()) != null) {
                            final String GPIO_PIN_NAME = line.split(" ")[0];
                            final String status = line.split(" ")[1];
                            activity.runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    //Change the LED status according to received command
                                    activity.SwitchLed(GPIO_PIN_NAME,status);
                                }
                            });


                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }




}