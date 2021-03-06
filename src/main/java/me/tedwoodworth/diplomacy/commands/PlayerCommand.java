package me.tedwoodworth.diplomacy.commands;

import me.tedwoodworth.diplomacy.nations.NationGuiFactory;
import me.tedwoodworth.diplomacy.nations.ScoreboardManager;
import me.tedwoodworth.diplomacy.players.AccountManager;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerCommand implements CommandExecutor, TabCompleter {
    private static final String incorrectUsage = ChatColor.DARK_RED + "Incorrect usage, try: ";
    private static final String playerUsage = "/player";
    private static final String playerListUsage = "/player list";
    private static final String playerInfoUsage = "/player info <player>";
    private static final String playerAccountsUsage = "/player accounts <player>";
    private static final String playerSetMainUsage = "/player setMain <player>";

    public static void register(PluginCommand pluginCommand) {
        var playerCommand = new PlayerCommand();

        pluginCommand.setExecutor(playerCommand);
        pluginCommand.setTabCompleter(playerCommand);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            player(sender);
        } else if (args[0].equalsIgnoreCase("list")) {
            if (args.length == 1) {
                playerList(sender);
            } else {
                sender.sendMessage(incorrectUsage + playerListUsage);
            }
        } else if (args[0].equalsIgnoreCase("info")) {
            if (args.length == 2) {
                playerInfo(sender, args[1]);
            } else {
                sender.sendMessage(incorrectUsage + playerInfoUsage);
            }
        } else if (args[0].equalsIgnoreCase("accounts")) {
            if (args.length == 2) {
                playerAccounts(sender, args[1]);
            } else {
                sender.sendMessage(incorrectUsage + playerAccountsUsage);
            }
        } else if (args[0].equalsIgnoreCase("setMain")) {
            if (args.length == 2) {
                playerSetMain(sender, args[1]);
            } else {
                sender.sendMessage(incorrectUsage + playerSetMainUsage);
            }
        } else if (args[0].equalsIgnoreCase("exclude")) {
            if (args.length == 2) {
                playerExclude(sender, args[1]);
            } else {
                sender.sendMessage(incorrectUsage + playerUsage);
            }
        } else if (args[0].equalsIgnoreCase("setRank")) {
            if (args.length == 3) {
                playerSetRank(sender, args[1], args[2]);
            } else {
                sender.sendMessage(incorrectUsage + playerUsage);
            }
        } else {
            sender.sendMessage(incorrectUsage + playerUsage);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length != 0) {
            if (args.length == 1) {
                return Arrays.asList("info", "setMain", "accounts", "setRank");
            } else if (args[0].equalsIgnoreCase("info") && args.length == 2) {
                var players = new ArrayList<String>();
                for (var player : Bukkit.getOnlinePlayers()) {
                    if (player.getName().toLowerCase().contains(args[1].toLowerCase()))
                        players.add(player.getName());
                }
                return players;
            } else if (args[0].equalsIgnoreCase("accounts") && args.length == 2) {
                var players = new ArrayList<String>();
                for (var player : Bukkit.getOnlinePlayers()) {
                    if (player.getName().toLowerCase().contains(args[1].toLowerCase()))
                        players.add(player.getName());
                }
                return players;
            } else if (args[0].equalsIgnoreCase("setMain") && args.length == 2) {
                var players = new ArrayList<String>();
                for (var player : Bukkit.getOnlinePlayers()) {
                    if (player.getName().toLowerCase().contains(args[1].toLowerCase()))
                        players.add(player.getName());
                }
                return players;
            }
        }
        return null;
    }

    private void player(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }
        sender.sendMessage(ChatColor.YELLOW + "----" + ChatColor.GOLD + " Players " + ChatColor.YELLOW + "--" + ChatColor.GOLD + " Page " + ChatColor.RED + "1" + ChatColor.GOLD + "/" + ChatColor.RED + "1" + ChatColor.YELLOW + " ----");
        sender.sendMessage(ChatColor.GOLD + "/player list" + ChatColor.WHITE + " Get a list of all players");
        sender.sendMessage(ChatColor.GOLD + "/player groups" + ChatColor.WHITE + " Get a list of groups");
        sender.sendMessage(ChatColor.GOLD + "/player info" + ChatColor.WHITE + " Get info about a player");
        sender.sendMessage(ChatColor.GOLD + "/player accounts" + ChatColor.WHITE + " View a player's alts");
        sender.sendMessage(ChatColor.GOLD + "/player setMain" + ChatColor.WHITE + " Set your main account");
    }

    private void playerInfo(CommandSender sender, String strPlayer) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var player = DiplomacyPlayers.getInstance().get(strPlayer);

        if (player == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Unknown player.");
            return;
        }


        var gui = NationGuiFactory.createPlayer(player.getOfflinePlayer());
        gui.show((Player) sender);
    }

    private void playerList(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Command is currently disabled for maintenance."); //TODO fix
//        if (!(sender instanceof Player)) {
//            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
//            return;
//        }
//
//        sender.sendMessage(ChatColor.GOLD + "Loading player list...");
//
//        var nGui = Guis.getInstance().getPlayers("alphabetically");
//        nGui.show((Player) sender);
    }

    private void playerAccounts(CommandSender sender, String strPlayer) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var player = DiplomacyPlayers.getInstance().get(strPlayer);

        if (player == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Unknown player.");
            return;
        }

        var account = AccountManager.getInstance().getAccount(player.getUUID());
        var mainUUID = account.getMain();
        var main = Bukkit.getOfflinePlayer(mainUUID).getName();

        sender.sendMessage(ChatColor.DARK_GREEN + "Alts:");
        for (var uuid : account.getPlayerIDs()) {
            if (uuid.equals(mainUUID)) continue;
            sender.sendMessage(ChatColor.GREEN + Bukkit.getOfflinePlayer(uuid).getName());
        }
        sender.sendMessage(ChatColor.DARK_GREEN + "Main:\n" + ChatColor.GREEN + main);
    }

    private void playerExclude(CommandSender sender, String strPlayer) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command.");
            return;
        }

        var player = DiplomacyPlayers.getInstance().get(strPlayer);

        if (player == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Unknown player.");
            return;
        }

        var uuid = player.getUUID();
        var account = AccountManager.getInstance().getAccount(uuid);
        if (account.getPlayerIDs().size() == 1) {
            sender.sendMessage(ChatColor.DARK_RED + "This player is not linked to any other accounts.");
            return;
        }

        var excluded = AccountManager.getInstance().getExcluded();
        if (excluded.contains(uuid)) {
            sender.sendMessage(ChatColor.DARK_RED + "Player is already excluded.");
            return;
        }

        AccountManager.getInstance().createExcludeAccount(uuid);
        sender.sendMessage(ChatColor.GREEN + "Player successfully excluded.");
    }

    private void playerSetMain(CommandSender sender, String strPlayer) {
        var player = DiplomacyPlayers.getInstance().get(strPlayer);

        if (player == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Unknown player.");
            return;
        }

        var account = AccountManager.getInstance().getAccount(player.getUUID());

        var main = account.getMain();
        var newMain = player.getUUID();

        if (main.equals(newMain)) {
            sender.sendMessage(ChatColor.RED + "That account is already your main account.");
            return;
        }

        if (!account.getPlayerIDs().contains(newMain)) {
            sender.sendMessage(ChatColor.RED + "That player is not linked to your account.");
            return;
        }

        account.setMain(newMain);
        for (var testUUID : account.getPlayerIDs()) {
            var testPlayer = Bukkit.getPlayer(testUUID);
            if (testPlayer == null) continue;
            testPlayer.sendMessage(ChatColor.DARK_GREEN + "Main account has been set to " + Bukkit.getOfflinePlayer(newMain).getName());
        }
        ScoreboardManager.getInstance().updateScoreboards();
    }

    private void playerSetRank(CommandSender sender, String strPlayer, String rank) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be an admin to use this command.");
            return;
        }

        var player = DiplomacyPlayers.getInstance().get(strPlayer);
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return;
        }

        if (player.getRank().equalsIgnoreCase(rank)) {
            sender.sendMessage(ChatColor.RED + "Rank already set to this.");
            return;
        }
        if (rank.equalsIgnoreCase("Owner")) player.setRank("Owner");
        else if (rank.equalsIgnoreCase("Dev")) player.setRank("Dev");
        else if (rank.equalsIgnoreCase("Admin")) player.setRank("Admin");
        else if (rank.equalsIgnoreCase("Mod")) player.setRank("Mod");
        else if (rank.equalsIgnoreCase("None")) player.setRank("None");
        else if (rank.equalsIgnoreCase("TrialMod")) player.setRank("TrialMod");
        else {
            sender.sendMessage(ChatColor.RED + "Unknown Rank");
            return;
        }
        var offlinePlayer = player.getOfflinePlayer();
        String name;
        if (offlinePlayer != null) {
            name = offlinePlayer.getName();
        } else {
            name = strPlayer;
        }

        sender.sendMessage(ChatColor.AQUA + name + "'s rank has been set to " + player.getRank());
        ScoreboardManager.getInstance().updateScoreboards();
        String r;
        if (player.getRank().equalsIgnoreCase("None")) r = "default";
        else r = rank;

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + strPlayer + " parent set " + r);
    }
}
