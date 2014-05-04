package io.github.psgs.oresomedata;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class CommandHandler {

    OresomeData plugin;

    public CommandHandler(OresomeData pl) {
        plugin = pl;
    }

    @Command(aliases = {"data"},
            usage = "<player> <options>",
            desc = "Displays a summary of data applicable to a player",
            min = 1,
            max = 2)
    @CommandPermissions({"oresomedata.view"})
    public void transact(CommandContext args, CommandSender sender) {
        DB database = MongoDatabaseManager.mongo.getDB(MongoDatabaseManager.mongodb_db);
        DBCollection playerTable = database.getCollection("players");

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("name", args.getString(0));
        DBCursor cursor = playerTable.find(searchQuery);

        if (cursor.size() > 0) {
            if (cursor.size() == 1) {
                BasicDBObject playerDocument = (BasicDBObject) cursor.curr();

                StringBuilder overview = new StringBuilder();
                StringBuilder statisticString = new StringBuilder();
                StringBuilder raidHouseString = new StringBuilder();

                overview.append(ChatColor.GREEN + "++++++++ " + ChatColor.AQUA + args.getString(0) + ChatColor.GREEN + " ++++++++\n");
                overview.append(ChatColor.GOLD + "UUID: " + playerDocument.getString("uuid") + "\n");
                overview.append(ChatColor.GOLD + "Coins: " + playerDocument.getString("coins") + "\n");

                if (playerDocument.get("statistics") != null) {
                    BasicDBObject statistics = (BasicDBObject) playerDocument.get("statistics");
                    statisticString.append(ChatColor.GREEN + "--------- Stats --------\n");
                    statisticString.append(ChatColor.GOLD + "Kills: " + statistics.getString("kills") + "\n");
                    statisticString.append(ChatColor.GOLD + "Deaths: " + statistics.getString("deaths") + "\n");
                    statisticString.append(ChatColor.GOLD + "K/D Ratio: " + statistics.getString("ks") + "\n");
                    statisticString.append(ChatColor.GOLD + "FFA Wins: " + statistics.getString("ffawins") + "\n");
                    statisticString.append(ChatColor.GOLD + "Infection Wins: " + statistics.getString("infectionwins") + "\n");
                    statisticString.append(ChatColor.GOLD + "Highest Streak: " + statistics.getString("highestkillstreak") + "\n");
                    //statisticString.append(ChatColor.GREEN + "-------- -------- --------\n");
                }

                if (playerDocument.get("raidhouse") != null) {
                    BasicDBObject raidHouse = (BasicDBObject) playerDocument.get("raidhouse");
                    raidHouseString.append(ChatColor.GREEN + "-------- RaidHouse --------\n");
                    raidHouseString.append(ChatColor.GOLD + "Recognition: " + raidHouse.getString("recognition") + "\n");
                    raidHouseString.append(ChatColor.GOLD + "Tier: " + raidHouse.getString("tier") + "\n");
                    raidHouseString.append(ChatColor.GOLD + "Total Raids: " + raidHouse.getString("totalraids") + "\n");
                    raidHouseString.append(ChatColor.GOLD + "Successful Raids: " + raidHouse.getString("successfulraids") + "\n");
                    raidHouseString.append(ChatColor.GOLD + "Failed Raids: " + raidHouse.getString("failedraids") + "\n");
                    raidHouseString.append(ChatColor.GOLD + "Reroll Amount: " + raidHouse.getString("rerollamount") + "\n");
                    //raidHouseString.append(ChatColor.GREEN + "-------- -------- --------\n");
                }

                //sender.sendMessage(ChatColor.GREEN + "++++++++ ++++++++ ++++++++");

                SortedMap<Integer, String> map = new TreeMap<Integer, String>();
                if (overview.toString().length() > 0) {
                    map.put(1, overview.toString());
                }
                if (statisticString.toString().length() > 0) {
                    map.put(2, statisticString.toString());
                }
                if (raidHouseString.length() > 0) {
                    map.put(3, raidHouseString.toString());
                }
                if (args.getString(1) != null) {
                    paginate(sender, map, Integer.parseInt(args.getString(1)), 7);
                } else {
                    paginate(sender, map, 1, 7);
                }
            } else {
                sender.sendMessage(ChatColor.RED + "More than one result was found! Are you sure you spelt the player's name correctly?");
            }
        } else {

        }
    }

    /**
     * Paginate a message
     *
     * @param sender     The CommandSender that's requesting the paginated resource
     * @param map        The page number / string to add to the page
     * @param page       The page number
     * @param pageLength The length of the page to return
     * @author gomeow
     */
    public void paginate(CommandSender sender, SortedMap<Integer, String> map, int page, int pageLength) {
        sender.sendMessage(ChatColor.YELLOW + "List: Page (" + String.valueOf(page) + " of " + (((map.size() % pageLength) == 0) ? map.size() / pageLength : (map.size() / pageLength) + 1));
        int i = 0, k = 0;
        page--;
        for (final Map.Entry<Integer, String> e : map.entrySet()) {
            k++;
            if ((((page * pageLength) + i + 1) == k) && (k != ((page * pageLength) + pageLength + 1))) {
                i++;
                sender.sendMessage(ChatColor.YELLOW + " - " + e.getValue());
            }
        }
    }
}
