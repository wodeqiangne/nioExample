package com.leixie;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author xielei
 */
public class BlockingIO {

    @Test
    public void startServer() throws IOException {
        ServerSocket sc = new ServerSocket(8080);
        try {
            while(true) {
                Socket client = sc.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                String request, response;
                while ((request = in.readLine()) != null) {
                    response = request;
                    System.out.println(request);
                    out.println(response);
                    if ("Done".equals(request)) {
                        break;
                    }
                }
            }
        } finally {
            sc.close();
        }
    }
}
