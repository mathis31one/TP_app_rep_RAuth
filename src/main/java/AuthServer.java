package main.java;

import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AuthServer {
    private AuthServerTCP authServerTCP;
    private AuthServerUDP authServerUDP;
    private ListeAuth listeAuth;

    public AuthServer(ListeAuth la){
        listeAuth = la;
        authServerTCP = new AuthServerTCP(la);
        authServerUDP = new AuthServerUDP(la);
    }

    public void traiterRequetes() {
        try {
            ExecutorService executorService = Executors.newFixedThreadPool(2);

            // Lancer les threads
            executorService.submit(authServerTCP::traiterRequetes);
            executorService.submit(authServerUDP::traiterRequetes);

            // Fermer le pool de threads
            executorService.shutdown();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String [] args) throws SocketException {
        AuthServer s = new AuthServer(new ListeAuth("authentif"));
        s.traiterRequetes();
    }
}
