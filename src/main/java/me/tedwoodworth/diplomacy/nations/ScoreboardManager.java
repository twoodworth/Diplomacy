package me.tedwoodworth.diplomacy.nations;

import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Objects;

public class ScoreboardManager {

    private static ScoreboardManager instance = null;

    public static ScoreboardManager getInstance() {
        if (instance == null) {
            instance = new ScoreboardManager();
        }
        return instance;
    }

    public void createScoreboard(Player player) {
        var manager = Bukkit.getScoreboardManager();
        var scoreboard = manager.getNewScoreboard();
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        var nation = Nations.getInstance().get(diplomacyPlayer);

        scoreboard.registerNewTeam("Wilderness");
        scoreboard.registerNewObjective("Nation", "Nation", "Wilderness");
        var wildernessTeam = scoreboard.getTeam("Wilderness");
        wildernessTeam.setDisplayName("Wilderness");
        wildernessTeam.setPrefix(ChatColor.GRAY + "[" + ChatColor.DARK_GRAY + "Wilderness" + ChatColor.GRAY + "] ");
        wildernessTeam.setColor(ChatColor.GRAY);

        for (var testNation : Nations.getInstance().getNations()) {
            scoreboard.registerNewTeam(String.valueOf(testNation.getNationID()));
            var team = scoreboard.getTeam(String.valueOf(testNation.getNationID()));
            team.setDisplayName(testNation.getName());
            var color = ChatColor.BLUE;
            if (nation != null) {
                if (nation.getAllyNationIDs().contains(testNation.getNationID())) {
                    color = ChatColor.DARK_GREEN;
                } else if (nation.getEnemyNationIDs().contains(testNation.getNationID())) {
                    color = ChatColor.RED;
                } else if (Objects.equals(nation, testNation)) {
                    color = ChatColor.GREEN;
                }
            }
            team.setPrefix(ChatColor.GRAY + "[" + color + testNation.getName() + ChatColor.GRAY + "] ");
        }

        for (var testPlayer : DiplomacyPlayers.getInstance().getPlayers()) {
            var testNation = Nations.getInstance().get(testPlayer);

            if (testNation == null) {
                wildernessTeam.addEntry(testPlayer.getPlayer().getName());
            } else {
                scoreboard.getTeam(String.valueOf(testNation.getNationID())).addEntry(testPlayer.getPlayer().getName());
            }
        }//TODO fix bug where everyone sees the same team

        player.setScoreboard(scoreboard);
    }

    public void updateScoreboards() {
        for (var player : Bukkit.getOnlinePlayers()) {
            createScoreboard(player);
        }
    }
}