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
        Scanner sc = new Scanner(System.in);
        System.out.print("Login : ");
        String login = sc.nextLine();
        System.out.print("Mot de passe : ");
        String motDePasse = sc.nextLine();
        // construction de la requÃªte (chaÃ®ne de caractÃ¨res)
        String req = login + " " + motDePasse;
        System.out.println(req);

        // construction du DatagramPacket pour envoyer la requÃªte
        byte[] data = req.getBytes();
        DatagramPacket dp_out = new DatagramPacket(data, data.length, ia, port);

        // émission de la requête
        ds.send(dp_out);

        // tampon pour recevoir les rÃ©ponses
        byte[] tampon = new byte[1024];

        // construction du DatagramPacket pour recevoir la rÃ©ponse
        DatagramPacket dp_in = new DatagramPacket(tampon, tampon.length);

        // attente de la rÃ©ponse
        String reponse = new String(dp_in.getData(), 0, dp_in.getLength());
        System.out.println("Réponse : " + reponse);

        // affiche la rÃ©ponse
        ds.close();
    }
}
