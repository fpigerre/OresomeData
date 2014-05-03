package io.github.psgs.oresomedata;

import com.mongodb.*;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ConnectionListener implements Listener {

    public ConnectionListener() {
        Bukkit.getPluginManager().registerEvents(this, OresomeData.getInstance());
    }

    DB database = MongoDatabaseManager.mongo.getDB(MongoDatabaseManager.mongodb_db);
    DBCollection table = database.getCollection("users");

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("uuid", event.getPlayer().getUniqueId().toString());
        DBCursor cursor = table.find(searchQuery);

        if (cursor.size() == 0) {
            BasicDBObject playerDocument = new BasicDBObject();
            playerDocument.put("uuid", event.getPlayer().getUniqueId());
            playerDocument.put("name", event.getPlayer().getName());
            playerDocument.put("coins", 0);
        } else {
            BasicDBObject playerDocument = (BasicDBObject) cursor.curr();
            playerDocument.put("name", event.getPlayer().getName());
            table.update(cursor.curr(), playerDocument);
        }
    }
}
