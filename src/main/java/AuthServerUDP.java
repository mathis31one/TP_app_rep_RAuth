package main.java;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class AuthServerUDP {
    // objet ListeAuth pour tester le couple login/mdp
    private ListeAuth listeAuth;

    public AuthServerUDP(ListeAuth la) {
        listeAuth = la;
    }

    public void traiterRequetes() throws RuntimeException {

        // creation DatagramSocket
        try {
            DatagramSocket ds = new DatagramSocket(28414);

        // tampon pour recevoir la requête
        byte [] tampon = new byte [256];
        // objet DatagramPacket servant à recevoir des requêtes
        DatagramPacket dp_in = new DatagramPacket(tampon, tampon.length);

        while(true) {
            try {
                // attends une requête
                ds.receive(dp_in);
                // transforme le tampon en chaine de caractère
                String req = new String(dp_in.getData(), 0, dp_in.getLength());
                // traite la requête : découpage, vérification type...
                String[] tokens = req.split(" ");
                String login = tokens[1];
                String passwd = tokens[2];
                String reponse = "";

                if (req.startsWith("CHK")) {
                    boolean isAuthentified = listeAuth.tester(login, passwd);
                    reponse = isAuthentified ? "AUTH OK" : "AUTH KO";
                } else if (req.startsWith("DEL")) {
                    boolean isDeleted = listeAuth.supprimer(login, passwd);
                    reponse = isDeleted ? "GOOD" : "BAD";
                } else if (req.startsWith("MOD")) {
                    boolean todate = listeAuth.mettreAJour(login, passwd);
                    reponse = todate ? "GOOD" : "BAD";
                } else if (req.startsWith("ADD")) {
                    boolean isAdded = listeAuth.creer(login, passwd);
                    reponse = isAdded ? "GOOD" : "BAD";
                } else {
                    reponse = "Commande invalide";
                }

                JsonLogger.log("localhost",ds.getPort(),"UDP","?",login,reponse);
                byte[] data = reponse.getBytes();
                DatagramPacket dp_out = new DatagramPacket(data, data.length, dp_in.getAddress(), dp_in.getPort());
                ds.send(dp_out);

            } catch (IOException e) {
                // gestion d'erreur
                e.printStackTrace();
            }
        }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String [] args) throws SocketException {
        AuthServerUDP s = new AuthServerUDP(new ListeAuth("authentif"));
        s.traiterRequetes();
    }
}