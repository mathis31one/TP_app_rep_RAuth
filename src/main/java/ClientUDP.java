package main.java;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class ClientUDP {
    public static void main(String[] args) throws IOException {

        // construction de la socket
        DatagramSocket ds = new DatagramSocket();

        // construction de l'adresse et du port du serveur
        InetAddress ia = InetAddress.getByName("localhost");
        int port = 28414;
        // saisir login + mot de passe
        String request = "CHK Toto Toto";
        byte[] data = request.getBytes();
        DatagramPacket dp_out = new DatagramPacket(data, data.length, ia, port);
        ds.send(dp_out);

        // Receive the CHK response
        byte[] tampon = new byte[1024];
        DatagramPacket dp_in = new DatagramPacket(tampon, tampon.length);
        ds.receive(dp_in);
        String response = new String(dp_in.getData(), 0, dp_in.getLength());
        System.out.println("CHK response: " + response);

        // Send a DEL command
        request = "DEL Toto Toto";
        data = request.getBytes();
        dp_out = new DatagramPacket(data, data.length, ia, port);
        ds.send(dp_out);

        // Receive the DEL response
        dp_in = new DatagramPacket(tampon, tampon.length);
        ds.receive(dp_in);
        response = new String(dp_in.getData(), 0, dp_in.getLength());
        System.out.println("DEL response: " + response);

        // Send an ADD command
        request = "ADD Toto Toto";
        data = request.getBytes();
        dp_out = new DatagramPacket(data, data.length, ia, port);
        ds.send(dp_out);

        // Receive the ADD response
        dp_in = new DatagramPacket(tampon, tampon.length);
        ds.receive(dp_in);
        response = new String(dp_in.getData(), 0, dp_in.getLength());
        System.out.println("ADD response: " + response);

        // Send a MOD command
        request = "MOD Toto Toto";
        data = request.getBytes();
        dp_out = new DatagramPacket(data, data.length, ia, port);
        ds.send(dp_out);

        // Receive the MOD response
        dp_in = new DatagramPacket(tampon, tampon.length);
        ds.receive(dp_in);
        response = new String(dp_in.getData(), 0, dp_in.getLength());
        System.out.println("MOD response: " + response);

        // Close the socket
        ds.close();
    }
}
