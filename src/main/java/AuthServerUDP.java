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

    public void traiterRequetes() {

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

                if (tokens.length != 2) {
                    throw new IOException("Format de requête incorrect");
                }
                // traitement ListeAuth avec appel méthode tester()
                boolean estAuthentifie = listeAuth.tester(tokens[0], tokens[1]);
                // construction DatagramPacket pour réponse
                String reponse = estAuthentifie ? "AUTH OK" : "AUTH KO";
                byte[] data = reponse.getBytes();
                DatagramPacket dp_out = new DatagramPacket(data, data.length, dp_in.getAddress(), dp_in.getPort());
                // renvoie la réponse au client
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