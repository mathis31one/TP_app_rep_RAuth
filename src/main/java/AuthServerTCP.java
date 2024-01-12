package main.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class AuthServerTCP {
    private ListeAuth listeAuth;

    public AuthServerTCP(ListeAuth la) {
        listeAuth = la;
    }
    public void traiterRequetes() {
        try {
            ServerSocket serverSocket = new ServerSocket(28414);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("cient connecté");

                Thread thread = new Thread(() -> {
                    try {
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                        Date date = new Date();
                        while (System.currentTimeMillis() - date.getTime() < 5000) {
                            String message = in.readLine();
                            if(message != null) {
                                String[] cmdArgs = message.split(" ");
                                String login = cmdArgs[1];
                                String passwd = cmdArgs[2];
                                String reponse = "";
                                if (message.startsWith("CHK")) {
                                    boolean isAuthentified = listeAuth.tester(login, passwd);
                                    reponse = isAuthentified ? "GOOD" : "BAD";
                                } else if (message.startsWith("DEL")) {
                                    boolean isDeleted = listeAuth.supprimer(login, passwd);
                                    reponse = isDeleted ? "GOOD" : "BAD";
                                } else if (message.startsWith("MOD")) {
                                    boolean isUpdated = listeAuth.mettreAJour(login, passwd);
                                    reponse = isUpdated ? "GOOD" : "BAD";
                                } else if (message.startsWith("ADD")) {
                                    boolean isAdded = listeAuth.creer(login, passwd);
                                    reponse = isAdded ? "GOOD" : "BAD";
                                } else {
                                    reponse = "Commande invalide";
                                }
                                JsonLogger.log("localhost", serverSocket.getLocalPort(), "TCP", "?", login, reponse);
                                out.println(reponse);
                            }else {
                                out.println("message null");
                            }

                        }
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                thread.start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String [] args) throws IOException {
        AuthServerTCP s = new AuthServerTCP(new ListeAuth("authentif"));
        s.traiterRequetes();
    }
}
