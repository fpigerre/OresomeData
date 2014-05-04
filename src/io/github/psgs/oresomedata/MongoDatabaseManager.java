package io.github.psgs.oresomedata;

import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.Arrays;

public class MongoDatabaseManager {

    public static MongoClient mongo;
    public static String mongodb_host;
    public static String mongodb_port;
    public static String mongodb_db;
    public static String mongodb_user;
    public static String mongodb_password;

    static OresomeData plugin = OresomeData.getInstance();

    public static void setupDatabase() {

        mongodb_host = plugin.getConfig().getString("mongodb.host");
        mongodb_db = plugin.getConfig().getString("mongodb.database");
        mongodb_user = plugin.getConfig().getString("mongodb.username");
        mongodb_password = plugin.getConfig().getString("mongodb.password");
        mongodb_port = plugin.getConfig().getString("mongodb.port");
        
        try {
            // Authenticate using constructor instead of deprecated DB method
            MongoCredential credential = MongoCredential.createMongoCRCredential(mongodb_user, mongodb_db, mongodb_password.toCharArray());
            mongo = new MongoClient(Arrays.asList(new ServerAddress(mongodb_host, Integer.parseInt(mongodb_port))), Arrays.asList(credential));
        } catch (UnknownHostException ex) {
            System.out.println("An error occurred while trying to connect to the MongoDB database!");
        }
    }
}
