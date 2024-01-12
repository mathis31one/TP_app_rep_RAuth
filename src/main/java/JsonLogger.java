package main.java;

import java.util.ArrayList;
import java.util.Date;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * Classe Singleton qui permet de logger des requêtes vers un serveur de log sur le port 3244 de la machine locale
 * 
 * @author torguet
 *
 */
public class JsonLogger {



	// Attributs à compléter
	private static ArrayList jsons = new ArrayList<JsonObject>();
	private JsonLogger() {

	}
	
	/**
	 * Transforme une requête en Json
	 * 
	 * @param host machine client
	 * @param port port sur la machine client
	 * @param proto protocole de transport utilisé
	 * @param type type de la requête
	 * @param login login utilisé
	 * @param result résultat de l'opération
	 * @return un objet Json correspondant à la requête
	 */
	private JsonObject reqToJson(String host, int port, String proto, String type, String login, String result) {
		JsonObjectBuilder builder = Json.createObjectBuilder();
		builder.add("host", host)
		   	   .add("port", port)
		   	   .add("proto", proto)
			   .add("type", type)
			   .add("login", login)
			   .add("result", result)
			   .add("date", new Date().toString());

		return builder.build();
	}
	
	/**
	 *  singleton
	 */
	private static JsonLogger logger = null;
	
	/**
	 * récupération du logger qui est créé si nécessaire
	 * 
	 * @return le logger
	 */
	private static JsonLogger getLogger() {
		if (logger == null) {
			logger = new JsonLogger();
		}
		return logger;
	}
	
	/**
	 * méthode pour logger
	 * 
	 * @param host machine client
	 * @param port port sur la machine client
	 * @param proto protocole de transport utilisé
	 * @param type type de la requête
	 * @param login login utilisé
	 * @param result résultat de l'opération
	 */
	public static void log(String host, int port, String proto, String type, String login, String result) {
		JsonLogger logger = getLogger();
		JsonObject obj = logger.reqToJson(host,port,proto,type,login,result);
		jsons.add(obj);

	}

	public static ArrayList getJsons() {
		return jsons;
	}
}
