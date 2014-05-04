package io.github.psgs.oresomedata;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.oresomecraft.OresomeBattles.api.BattlePlayer;
import com.oresomecraft.maps.Map;
import com.oresomecraft.maps.MapsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;

import java.io.IOException;

public class PluginDataManager {

    DB database = MongoDatabaseManager.mongo.getDB(MongoDatabaseManager.mongodb_db);
    DBCollection playerTable = database.getCollection("players");
    DBCollection mapTable = database.getCollection("maps");

    public void updateBattlesData(Player player) {
        if (Bukkit.getServer().getName().equals("battle")) {
            BasicDBObject searchQuery = new BasicDBObject();
            searchQuery.put("uuid", player.getUniqueId().toString());
            DBCursor cursor = playerTable.find(searchQuery);

            BasicDBObject playerDocument = (BasicDBObject) cursor.curr();

            BasicDBObject statistics = new BasicDBObject();
            BattlePlayer battlePlayer = BattlePlayer.getBattlePlayer(player);
            statistics.put("kills", battlePlayer.getKills());
            statistics.put("deaths", battlePlayer.getDeaths());
            statistics.put("kd", battlePlayer.getKills() / battlePlayer.getDeaths());
            // TODO: FFA Wins
            // TODO: Infection Wins
            // TODO: High kill streak

            playerDocument.put("statistics", statistics);
            playerTable.update(cursor.curr(), playerDocument);
        }
    }

    public void updateRaidHouseData(Player player) {
        if (Bukkit.getServer().getName().equals("smp")) {
            BasicDBObject searchQuery = new BasicDBObject();
            searchQuery.put("uuid", player.getUniqueId().toString());
            DBCursor cursor = playerTable.find(searchQuery);

            BasicDBObject playerDocument = (BasicDBObject) cursor.curr();

            BasicDBObject raidHouseStatistics = new BasicDBObject();
            // TODO: Recognition
            // TODO: Tier
            // TODO: Total Raids
            // TODO: Successful Raids
            // TODO: Failed Raids
            // TODO: Reroll Amount

            playerDocument.put("raidhouse", raidHouseStatistics);
            playerTable.update(cursor.curr(), playerDocument);
        }
    }

    public void updateOresomeCoinData(Player player) {
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("uuid", player.getUniqueId().toString());
        DBCursor cursor = playerTable.find(searchQuery);

        BasicDBObject playerDocument = (BasicDBObject) cursor.curr();
        // TODO Coins
        playerTable.update(cursor.curr(), playerDocument);
    }

    public void updateMapsData() {
        if (Bukkit.getServer().equals("battles")) {
            for (Map map : MapsPlugin.getMaps().values()) {
                BasicDBObject searchQuery = new BasicDBObject();
                searchQuery.put("name", map.getName());
                DBCursor cursor = mapTable.find(searchQuery);

                if (cursor.size() != 0) {
                    BasicDBObject mapDocument = (BasicDBObject) cursor.curr();
                    mapDocument.put("_id", map.getName());
                    mapDocument.put("name", map.getName());
                    mapDocument.put("fullName", map.getFullName());

                    String creatorString = map.getCreators();
                    String[] creators = creatorString.replaceAll("\band|,", "").split(" ");
                    mapDocument.put("creators", creators);

                    String gameModesString = map.getModes().toString();
                    String[] gameModes = gameModesString.replaceAll(",", "").split(" ");
                    mapDocument.put("gamemodes", gameModes);

                    // TODO: inRotation()
                    mapTable.update(cursor.curr(), mapDocument);
                }
            }
        }
    }

    public void updateGitHubData() {
        try {
            GitHub github = GitHub.connectUsingOAuth(OresomeData.getInstance().getConfig().getString("githubtoken"));
            GHRepository repository = github.getUser("OresomeCraft").getRepository("MapsPlugin");

            for (GHUser collaborator : repository.getCollaborators()) {
                BasicDBObject searchQuery = new BasicDBObject();
                searchQuery.put("name", collaborator.getName());
                DBCursor cursor = playerTable.find(searchQuery);

                if (cursor.size() == 1) {
                    BasicDBObject playerDocument = (BasicDBObject) cursor.curr();
                    BasicDBObject mapsPluginDocument = new BasicDBObject();

                    // TODO: Commits
                    // TODO: Additions
                    // TODO: Deletions

                    playerDocument.put("mapsplugin", mapsPluginDocument);
                    playerTable.update(cursor.curr(), playerDocument);
                }
            }
        } catch (IOException ex) {
            System.out.println("An error occurred while trying to access the GitHub API!");
        }
    }
}
