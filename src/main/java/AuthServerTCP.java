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
    public void traiterRequetes() throws IOException {
        ServerSocket serverSocket = new ServerSocket(28414);
        while (true){
            Socket clientSocket = serverSocket.accept();
            System.out.println("cient connecté");

            Thread thread = new Thread(() -> {
                try{
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    Date date = new Date();
                    while (System.currentTimeMillis() - date.getTime() < 5000) {
                        String message = in.readLine();

                        if (message.startsWith("CHK")) {
                            String[] cmdArgs = message.split(" ");
                            String login = cmdArgs[1];
                            String passwd = cmdArgs[2];
                            boolean isAuthentified = listeAuth.tester(login, passwd);
                            JsonLogger.log("localhost",serverSocket.getLocalPort(),"TCP","?",login,isAuthentified ? "AUTH OK" : "AUTH KO");
                            out.println(isAuthentified ? "AUTH OK" : "AUTH KO");
                        } else if (message.startsWith("DEL")) {
                            String[] cmdArgs = message.split(" ");
                            String login = cmdArgs[1];
                            String passwd = cmdArgs[2];
                            boolean isDeleted = listeAuth.supprimer(login, passwd);
                            JsonLogger.log("localhost",serverSocket.getLocalPort(),"TCP","?",login,isDeleted ? "AUTH OK" : "AUTH KO");
                            out.println(isDeleted ? "DELETED" : "DELETE FAILED");
                        } else if (message.startsWith("MOD")) {
                            String[] cmdArgs = message.split(" ");
                            String login = cmdArgs[1];
                            String passwd = cmdArgs[2];
                            listeAuth.mettreAJour(login, passwd);
                            JsonLogger.log("localhost",serverSocket.getLocalPort(),"TCP","?",login,"MOD OK");
                            out.println("MOD OK");
                        } else if (message.startsWith("ADD")) {
                            String[] cmdArgs = message.split(" ");
                            String login = cmdArgs[1];
                            String passwd = cmdArgs[2];
                            boolean isAdded = listeAuth.creer(login, passwd);
                            JsonLogger.log("localhost",serverSocket.getLocalPort(),"TCP","?",login,isAdded ? "AUTH OK" : "AUTH KO");
                            out.println(isAdded ? "CREATED" : "CREATE FAILED");
                        } else {
                            out.println("Commande invalide");
                        }
                    }
                    clientSocket.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            });
            thread.start();
        }
    }

    public static void main(String [] args) throws IOException {
        AuthServerTCP s = new AuthServerTCP(new ListeAuth("authentif"));
        s.traiterRequetes();
    }
}
