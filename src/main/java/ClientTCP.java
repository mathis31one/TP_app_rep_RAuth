package main.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientTCP {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 28414);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
        // Send a CHK command
        out.println("CHK Toto Toto");
        String response = in.readLine();
        System.out.println("CHK response: " + response);

        // Send a DEL command
        out.println("DEL Toto Toto");
        response = in.readLine();
        System.out.println("DEL response: " + response);

        // Send an ADD command
        out.println("ADD Toto Toto");
        response = in.readLine();
        System.out.println("ADD response: " + response);

        // Send a MOD command
        out.println("MOD Toto Toto");
        response = in.readLine();
        System.out.println("MOD response: " + response);

        socket.close();
    }
}
