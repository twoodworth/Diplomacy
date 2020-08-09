package me.tedwoodworth.diplomacy.commands;

import me.tedwoodworth.diplomacy.groups.DiplomacyGroups;
import me.tedwoodworth.diplomacy.groups.GroupGuiFactory;
import me.tedwoodworth.diplomacy.nations.DiplomacyChunks;
import me.tedwoodworth.diplomacy.nations.Nation;
import me.tedwoodworth.diplomacy.nations.Nations;
import me.tedwoodworth.diplomacy.nations.contest.ContestManager;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayer;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class GroupCommand implements CommandExecutor, TabCompleter {
    private static final String incorrectUsage = ChatColor.DARK_RED + "Incorrect usage, try: ";
    private static final String groupCreateUsage = "/group create <name>";
    private static final String groupInfoUsage = "/group info <group>";
    private static final String groupRenameUsage = "/group rename <group> <name>";
    private static final String groupSurrenderUsage = "/group surrender <group> <nation>";
    private static final String groupDisbandUsage = "/group disband <group>";
    private static final String groupListUsage = "/group list";
    private static final String groupAddUsage = "/group add <player> <group>";
    private static final String groupLeaveUsage = "/group leave <group>";
    private static final String groupKickUsage = "/group kick <player> <group>";
    private static final String groupBannerUsage = "/group banner <group>";
    private static final String groupClaimUsage = "/group claim <group>";
    private static final String groupUnclaimUsage = "/group unclaim";
    private static final String groupPromoteUsage = "/group promote <player> <group>";
    private static final String groupDemoteUsage = "/group demote <player> <group>";
    private static final String groupUsage = "/group";

    public static void register(PluginCommand pluginCommand) {
        var groupCommand = new GroupCommand();

        pluginCommand.setExecutor(groupCommand);
        pluginCommand.setTabCompleter(groupCommand);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            group(sender);
        } else if (args[0].equalsIgnoreCase("create")) {
            if (args.length == 2) {
                groupCreate(sender, args[1]);
            } else {
                sender.sendMessage(incorrectUsage + groupCreateUsage);
            }
        } else if (args[0].equalsIgnoreCase("info")) {
            if (args.length == 2) {
                groupInfo(sender, args[1]);
            } else {
                sender.sendMessage(incorrectUsage + groupInfoUsage);
            }
        } else if (args[0].equalsIgnoreCase("rename")) {
            if (args.length == 3) {
                groupRename(sender, args[1], args[2]);
            } else {
                sender.sendMessage(incorrectUsage + groupRenameUsage);
            }
        } else if (args[0].equalsIgnoreCase("surrender")) {
            if (args.length == 3) {
                groupSurrender(sender, args[1], args[2]);
            } else {
                sender.sendMessage(incorrectUsage + groupSurrenderUsage);
            }
        } else if (args[0].equalsIgnoreCase("disband")) {
            if (args.length == 2) {
                groupDisband(sender, args[1]);
            } else {
                sender.sendMessage(incorrectUsage + groupDisbandUsage);
            }
        } else if (args[0].equalsIgnoreCase("list")) {
            if (args.length == 2) {
                groupList(sender);
            } else {
                sender.sendMessage(incorrectUsage + groupListUsage);
            }
        } else if (args[0].equalsIgnoreCase("add")) {
            if (args.length == 3) {
                groupAdd(sender, args[1], args[2]);
            } else {
                sender.sendMessage(incorrectUsage + groupAddUsage);
            }
        } else if (args[0].equalsIgnoreCase("leave")) {
            if (args.length == 2) {
                groupLeave(sender, args[1]);
            } else {
                sender.sendMessage(incorrectUsage + groupLeaveUsage);
            }
        } else if (args[0].equalsIgnoreCase("kick")) {
            if (args.length == 3) {
                groupKick(sender, args[1], args[2]);
            } else {
                sender.sendMessage(incorrectUsage + groupKickUsage);
            }
        } else if (args[0].equalsIgnoreCase("banner")) {
            if (args.length == 2) {
                groupBanner(sender, args[1]);
            } else {
                sender.sendMessage(incorrectUsage + groupBannerUsage);
            }
        } else if (args[0].equalsIgnoreCase("claim")) {
            if (args.length == 2) {
                groupClaim(sender, args[1]);
            } else {
                sender.sendMessage(incorrectUsage + groupClaimUsage);
            }
        } else if (args[0].equalsIgnoreCase("unclaim")) {
            if (args.length == 1) {
                groupUnclaim(sender);
            } else {
                sender.sendMessage(incorrectUsage + groupUnclaimUsage);
            }
        } else if (args[0].equalsIgnoreCase("promote")) {
            if (args.length == 3) {
                groupPromote(sender, args[1], args[2]);
            } else {
                sender.sendMessage(incorrectUsage + groupPromoteUsage);
            }
        } else if (args[0].equalsIgnoreCase("demote")) {
            if (args.length == 3) {
                groupDemote(sender, args[1], args[2]);
            } else {
                sender.sendMessage(incorrectUsage + groupDemoteUsage);
            }
        } else {
            sender.sendMessage(incorrectUsage + groupUsage);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length != 0) {
            if (args.length == 1) {
                return Arrays.asList(
                        "create",
                        "info",
                        "rename",
                        "surrender",
                        "disband",
                        "list",
                        "add",
                        "leave",
                        "kick",
                        "banner",
                        "claim",
                        "unclaim",
                        "promote",
                        "demote");
            } else if (args[0].equalsIgnoreCase("create")) {
                return null;
            } else if (args[0].equalsIgnoreCase("info")) {
                List<String> groups = new ArrayList<>();
                for (var group : DiplomacyGroups.getInstance().getGroups()) {
                    groups.add(group.getName());
                }
                return groups;
            } else if (args[0].equalsIgnoreCase("rename")) {
                if (args.length == 2) {
                    List<String> groups = new ArrayList<>();
                    for (var group : DiplomacyGroups.getInstance().getGroups()) {
                        groups.add(group.getName());
                    }
                    return groups;
                } else {
                    return null;
                }
            } else if (args[0].equalsIgnoreCase("banner")) {
                if (args.length == 2) {
                    List<String> groups = new ArrayList<>();
                    for (var group : DiplomacyGroups.getInstance().getGroups()) {
                        groups.add(group.getName());
                    }
                    return groups;
                } else {
                    return null;
                }
            } else if (args[0].equalsIgnoreCase("surrender")) {
                if (args.length == 2) {
                    List<String> groups = new ArrayList<>();
                    for (var group : DiplomacyGroups.getInstance().getGroups()) {
                        groups.add(group.getName());
                    }
                    return groups;
                } else if (args.length == 3) {
                    List<String> nations = new ArrayList<>();
                    for (var nation : Nations.getInstance().getNations())
                        nations.add(nation.getName());
                    return nations;
                } else {
                    return null;
                }
            } else if (args[0].equalsIgnoreCase("disband")) {
                if (args.length == 2) {
                    List<String> groups = new ArrayList<>();
                    for (var group : DiplomacyGroups.getInstance().getGroups()) {
                        groups.add(group.getName());
                    }
                    return groups;
                } else {
                    return null;
                }
            } else if (args[0].equalsIgnoreCase("list")) {
                if (args.length == 2) {
                    List<String> groups = new ArrayList<>();
                    for (var group : DiplomacyGroups.getInstance().getGroups()) {
                        groups.add(group.getName());
                    }
                    return groups;
                } else {
                    return null;
                }
            } else if (args[0].equalsIgnoreCase("add")) {
                if (args.length == 2) {
                    List<String> players = new ArrayList<>();
                    for (var player : Bukkit.getOnlinePlayers()) {
                        players.add(player.getName());
                    }
                    return players;
                } else if (args.length == 3) {
                    List<String> groups = new ArrayList<>();
                    for (var group : DiplomacyGroups.getInstance().getGroups()) {
                        groups.add(group.getName());
                    }
                    return groups;
                }
                return null;
            } else if (args[0].equalsIgnoreCase("leave")) {
                if (args.length == 2) {
                    List<String> groups = new ArrayList<>();
                    for (var group : DiplomacyGroups.getInstance().getGroups()) {
                        groups.add(group.getName());
                    }
                    return groups;
                } else {
                    return null;
                }
            } else if (args[0].equalsIgnoreCase("kick")) {
                if (args.length == 2) {
                    List<String> players = new ArrayList<>();
                    for (var player : Bukkit.getOnlinePlayers()) {
                        players.add(player.getName());
                    }
                    return players;
                } else if (args.length == 3) {
                    List<String> groups = new ArrayList<>();
                    for (var group : DiplomacyGroups.getInstance().getGroups()) {
                        groups.add(group.getName());
                    }
                    return groups;
                } else {
                    return null;
                }
            } else if (args[0].equalsIgnoreCase("claim")) {
                if (args.length == 2) {
                    List<String> groups = new ArrayList<>();
                    for (var group : DiplomacyGroups.getInstance().getGroups()) {
                        groups.add(group.getName());
                    }
                    return groups;
                } else {
                    return null;
                }
            } else if (args[0].equalsIgnoreCase("unclaim")) {
                return null;
            } else if (args[0].equalsIgnoreCase("promote")) {
                if (args.length == 2) {
                    List<String> players = new ArrayList<>();
                    for (var player : Bukkit.getOnlinePlayers()) {
                        players.add(player.getName());
                    }
                    return players;
                } else if (args.length == 3) {
                    List<String> groups = new ArrayList<>();
                    for (var group : DiplomacyGroups.getInstance().getGroups()) {
                        groups.add(group.getName());
                    }
                    return groups;
                } else {
                    return null;
                }
            } else if (args[0].equalsIgnoreCase("demote")) {
                if (args.length == 2) {
                    List<String> players = new ArrayList<>();
                    for (var player : Bukkit.getOnlinePlayers()) {
                        players.add(player.getName());
                    }
                    return players;
                } else if (args.length == 3) {
                    List<String> groups = new ArrayList<>();
                    for (var group : DiplomacyGroups.getInstance().getGroups()) {
                        groups.add(group.getName());
                    }
                    return groups;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    private void group(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }
        sender.sendMessage("" + ChatColor.GREEN + ChatColor.BOLD + "Manage Groups:");
        sender.sendMessage(ChatColor.AQUA + groupListUsage + ChatColor.GRAY + " View all groups");
        sender.sendMessage(ChatColor.AQUA + groupInfoUsage + ChatColor.GRAY + " Get info about a group");
        sender.sendMessage(ChatColor.AQUA + groupCreateUsage + ChatColor.GRAY + " Create a group");
        sender.sendMessage(ChatColor.AQUA + groupRenameUsage + ChatColor.GRAY + " Rename a group");
        sender.sendMessage(ChatColor.AQUA + groupAddUsage + ChatColor.GRAY + " Add a player to a group");
        sender.sendMessage(ChatColor.AQUA + groupKickUsage + ChatColor.GRAY + " Kick a player from a group");
        sender.sendMessage(ChatColor.AQUA + groupLeaveUsage + ChatColor.GRAY + " Leave a group");
        sender.sendMessage(ChatColor.AQUA + groupClaimUsage + ChatColor.GRAY + " Claim a plot for a group");
        sender.sendMessage(ChatColor.AQUA + groupUnclaimUsage + ChatColor.GRAY + " Unclaim a plot for a group");
        sender.sendMessage(ChatColor.AQUA + groupSurrenderUsage + ChatColor.GRAY + " Surrender a group");
        sender.sendMessage(ChatColor.AQUA + groupDisbandUsage + ChatColor.GRAY + " Disband a group");
        sender.sendMessage(ChatColor.AQUA + groupBannerUsage + ChatColor.GRAY + " Set a group's banner");
        sender.sendMessage(ChatColor.AQUA + groupPromoteUsage + ChatColor.GRAY + " Promote a member to leader");
        sender.sendMessage(ChatColor.AQUA + groupDemoteUsage + ChatColor.GRAY + " Demote a member from leader");
    }

    private void groupInfo(CommandSender sender, String strGroup) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var player = (Player) sender;
        var group = DiplomacyGroups.getInstance().get(strGroup);

        if (group == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Group not found.");
            return;
        }

        var gui = new GroupGuiFactory().create(group, player);
        gui.show(player);
    }

    private void groupCreate(CommandSender sender, String name) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var uuid = ((OfflinePlayer) sender).getUniqueId();
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(uuid);
        var nation = Nations.getInstance().get(diplomacyPlayer);

        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be in a nation to use this command.");
            return;
        }

        var memberClass = nation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        boolean canCreateGroups = permissions.get("CanCreateGroups");

        if (!canCreateGroups) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to create groups.");
            return;
        }

        var sameName = DiplomacyGroups.getInstance().get(name);
        if (sameName != null) {
            sender.sendMessage(ChatColor.DARK_RED + "That name is already taken.");
            return;
        }

        DiplomacyGroups.getInstance().createGroup(name, diplomacyPlayer, nation);
        for (var onlinePlayer : Bukkit.getOnlinePlayers()) {
            var testPlayer = DiplomacyPlayers.getInstance().get(onlinePlayer.getUniqueId());
            var testNation = Nations.getInstance().get(testPlayer);
            if (Objects.equals(nation, testNation)) {
                onlinePlayer.sendMessage(ChatColor.AQUA + "The group " + ChatColor.BLUE + name + ChatColor.AQUA + " has been founded.");
            }
        }
    }

    private void groupRename(CommandSender sender, String strGroup, String name) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var uuid = ((OfflinePlayer) sender).getUniqueId();
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(uuid);
        var nation = Nations.getInstance().get(diplomacyPlayer);

        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be in a nation to use this command.");
            return;
        }

        var group = DiplomacyGroups.getInstance().get(strGroup);
        if (group == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Unknown group.");
            return;
        }

        var isGroupLeader = diplomacyPlayer.getGroupsLed().contains(group.getGroupID());

        var memberClass = nation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        boolean canLeadAllGroups = permissions.get("CanLeadAllGroups");

        if (!(isGroupLeader || canLeadAllGroups)) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to rename this group.");
            return;
        }

        var sameName = DiplomacyGroups.getInstance().get(name);
        if (sameName != null) {
            sender.sendMessage(ChatColor.DARK_RED + "That name is already taken.");
            return;
        }


        for (var onlinePlayer : Bukkit.getOnlinePlayers()) {
            var testPlayer = DiplomacyPlayers.getInstance().get(onlinePlayer.getUniqueId());
            var testNation = Nations.getInstance().get(testPlayer);
            if (Objects.equals(nation, testNation) || group.getMembers().contains(testPlayer)) {
                onlinePlayer.sendMessage(ChatColor.AQUA + "The group " + ChatColor.BLUE + group.getName() + ChatColor.AQUA + " has been renamed to " + ChatColor.BLUE + name + ChatColor.AQUA + ".");
            }
        }

        group.setName(name);

    }

    private void groupSurrender(CommandSender sender, String strGroup, String strOtherNation) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        Player player = (Player) sender;
        DiplomacyPlayer diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        Nation nation = Nations.getInstance().get(diplomacyPlayer);

        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be in a nation to use this command.");
            return;
        }

        var memberClass = nation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        boolean canSurrender = permissions.get("CanSurrenderGroup");

        if (!canSurrender) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to surrender groups.");
            return;
        }

        var group = DiplomacyGroups.getInstance().get(strGroup);
        if (group == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Unknown group.");
            return;
        }

        if (!Objects.equals(group.getNation(), nation)) {
            sender.sendMessage(ChatColor.DARK_RED + "That group does not belong to your nation.");
            return;
        }

        Nation otherNation = Nations.getInstance().get(strOtherNation);
        if (otherNation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Unknown nation.");
            return;
        }

        if (Objects.equals(nation, otherNation)) {
            sender.sendMessage(ChatColor.DARK_RED + "You cannot surrender to your own nation.");
            return;
        }

        for (var onlinePlayer : Bukkit.getOnlinePlayers()) {
            var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(onlinePlayer.getUniqueId());
            if (group.getMembers().contains(testDiplomacyPlayer) || Objects.equals(Nations.getInstance().get(testDiplomacyPlayer), nation) || Objects.equals(Nations.getInstance().get(testDiplomacyPlayer), otherNation)) {
                var testNation = Nations.getInstance().get(testDiplomacyPlayer);
                if (testNation != null) {
                    var color1 = ChatColor.BLUE;
                    var color2 = ChatColor.BLUE;
                    if (testNation.getEnemyNationIDs().contains(otherNation.getNationID())) {
                        color2 = ChatColor.RED;
                    } else if (testNation.getAllyNationIDs().contains(otherNation.getNationID()) || Objects.equals(testNation, otherNation)) {
                        color2 = ChatColor.GREEN;
                    }
                    if (testNation.getEnemyNationIDs().contains(nation.getNationID())) {
                        color1 = ChatColor.RED;
                    } else if (testNation.getAllyNationIDs().contains(nation.getNationID()) || Objects.equals(testNation, nation)) {
                        color1 = ChatColor.GREEN;
                    }
                    onlinePlayer.sendMessage(color1 + nation.getName() + ChatColor.AQUA + " has surrendered the group " + ChatColor.BLUE + group.getName() + ChatColor.AQUA + " to " + color2 + otherNation.getName() + ChatColor.AQUA + ".");
                } else {
                    onlinePlayer.sendMessage(ChatColor.BLUE + nation.getName() + ChatColor.AQUA + " has surrendered the group " + ChatColor.BLUE + group.getName() + ChatColor.AQUA + " to " + ChatColor.BLUE + otherNation.getName() + ChatColor.AQUA + ".");
                }
            }
        }

        for (var testPlayer : Bukkit.getOnlinePlayers()) {
            var testDiplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(player.getLocation().getChunk());
            var testNation = testDiplomacyChunk.getNation();
            if (Objects.equals(testNation, nation)) {
                var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(testPlayer.getUniqueId());
                Nation testPlayerNation = Nations.getInstance().get(testDiplomacyPlayer);
                if (testPlayerNation == null) {
                    testPlayer.sendTitle(ChatColor.BLUE + otherNation.getName(), null, 5, 40, 10);
                } else if (otherNation.getEnemyNationIDs().contains(testPlayerNation.getNationID())) {
                    testPlayer.sendTitle(ChatColor.RED + otherNation.getName(), null, 5, 40, 10);
                } else if (otherNation.getAllyNationIDs().contains(testPlayerNation.getNationID()) || otherNation.equals(testPlayerNation)) {
                    testPlayer.sendTitle(ChatColor.GREEN + otherNation.getName(), null, 5, 40, 10);
                } else {
                    testPlayer.sendTitle(ChatColor.BLUE + otherNation.getName(), null, 5, 40, 10);
                }
            }
        }

        var contests = ContestManager.getInstance().getContests();
        for (var contest : contests) {
            var attackingNation = contest.getAttackingNation();
            if (attackingNation.equals(nation)) {
                ContestManager.getInstance().endContest(contest);
            } else if (Objects.equals(contest.getDiplomacyChunk().getNation(), nation)) {
                if (attackingNation.equals(otherNation)) {
                    ContestManager.getInstance().winContest(contest);
                } else if (attackingNation.getAllyNationIDs().contains(nation.getNationID())) {
                    ContestManager.getInstance().endContest(contest);
                }
            }
        }

        var diplomacyChunks = group.getChunks();
        for (var diplomacyChunk : diplomacyChunks) {
            nation.removeChunk(diplomacyChunk);
            otherNation.addChunk(diplomacyChunk);
        }

        group.setNation(otherNation);
    }

    private void groupDisband(CommandSender sender, String strGroup) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        Player player = (Player) sender;
        DiplomacyPlayer diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        Nation nation = Nations.getInstance().get(diplomacyPlayer);

        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be in a nation to use this command.");
            return;
        }

        var memberClass = nation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        boolean canSurrender = permissions.get("CanSurrenderGroup");

        if (!canSurrender) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to disband groups.");
            return;
        }

        var group = DiplomacyGroups.getInstance().get(strGroup);
        if (group == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Unknown group.");
            return;
        }

        if (!Objects.equals(group.getNation(), nation)) {
            sender.sendMessage(ChatColor.DARK_RED + "That group does not belong to your nation.");
            return;
        }

        for (var onlinePlayer : Bukkit.getOnlinePlayers()) {
            var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(onlinePlayer.getUniqueId());
            if (group.getMembers().contains(testDiplomacyPlayer) || Objects.equals(Nations.getInstance().get(testDiplomacyPlayer), nation)) {
                var testNation = Nations.getInstance().get(testDiplomacyPlayer);
                if (testNation != null) {
                    var color = ChatColor.BLUE;
                    if (testNation.getEnemyNationIDs().contains(nation.getNationID())) {
                        color = ChatColor.RED;
                    } else if (testNation.getAllyNationIDs().contains(nation.getNationID()) || Objects.equals(testNation, nation)) {
                        color = ChatColor.GREEN;
                    }
                    onlinePlayer.sendMessage(color + nation.getName() + ChatColor.AQUA + " has disbanded the group " + ChatColor.BLUE + group.getName() + ChatColor.AQUA + ".");
                } else {
                    onlinePlayer.sendMessage(ChatColor.BLUE + nation.getName() + ChatColor.AQUA + " has disbanded the group " + ChatColor.BLUE + group.getName() + ChatColor.AQUA + ".");
                }
            }
        }

        for (var offlinePlayer : Bukkit.getOfflinePlayers()) {
            var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(offlinePlayer.getUniqueId());
            if (testDiplomacyPlayer.getGroups().contains(group)) {
                testDiplomacyPlayer.removeGroup(group);
            }
            if (testDiplomacyPlayer.getGroupsLed().contains(group)) {
                testDiplomacyPlayer.removeGroupLed(group);
            }
        }
        DiplomacyGroups.getInstance().removeGroup(group);
    }

    private void groupList(CommandSender sender) {
        //TODO add (and also add all groups to nationGui)
    }

    private void groupAdd(CommandSender sender, String strName, String strGroup) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var group = DiplomacyGroups.getInstance().get(strGroup);
        if (group == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Unknown group.");
            return;
        }

        Player player = (Player) sender;
        DiplomacyPlayer diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        var isGroupLeader = diplomacyPlayer.getGroupsLed().contains(group.getGroupID());

        Nation nation = Nations.getInstance().get(diplomacyPlayer);
        var memberClass = nation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        boolean canLeadAllGroups = permissions.get("CanLeadAllGroups");
        var groupNation = group.getNation();

        if (!(isGroupLeader || (canLeadAllGroups && Objects.equals(nation, groupNation)))) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to add players to this group.");
            return;
        }

        var otherPlayer = Bukkit.getPlayer(strName);
        if (otherPlayer == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Unknown player.");
            return;
        }

        var otherDiplomacyPlayer = DiplomacyPlayers.getInstance().get(otherPlayer.getUniqueId());
        var otherNation = Nations.getInstance().get(otherDiplomacyPlayer);
        var sameNation = Objects.equals(otherNation, group.getNation());
        var otherCanLeadAllGroups = false;
        if (otherNation != null) {
            otherCanLeadAllGroups = otherNation.getMemberClass(otherDiplomacyPlayer).getPermissions().get("CanLeadAllGroups");
        }

        if (sameNation && otherCanLeadAllGroups) {
            sender.sendMessage(ChatColor.DARK_RED + otherPlayer.getName() + " is already a leader of all " + group.getNation().getName() + " groups.");
            return;
        }

        if (group.getMembers().contains(otherDiplomacyPlayer)) {
            sender.sendMessage(ChatColor.DARK_RED + otherPlayer.getName() + " is already a member of that group.");
            return;
        }

        var color = ChatColor.BLUE;
        if (otherNation != null) {
            if (otherNation.getEnemyNationIDs().contains(nation.getNationID())) {
                color = ChatColor.RED;
            } else if (otherNation.getAllyNationIDs().contains(nation.getNationID())) {
                color = ChatColor.GREEN;
            }
        }

        for (var onlinePlayer : Bukkit.getOnlinePlayers()) {
            var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(onlinePlayer.getUniqueId());
            if (group.getMembers().contains(testDiplomacyPlayer)) {
                onlinePlayer.sendMessage(color + otherPlayer.getName() + ChatColor.AQUA + " has been added to the group " + ChatColor.BLUE + group.getName() + ChatColor.AQUA + ".");
            }
        }
        otherDiplomacyPlayer.addGroup(group);
        otherPlayer.sendMessage(ChatColor.AQUA + "You have been added to the group " + ChatColor.BLUE + group.getName() + ChatColor.AQUA + ".");
    }

    private void groupLeave(CommandSender sender, String strGroup) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var group = DiplomacyGroups.getInstance().get(strGroup);
        if (group == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Unknown group.");
            return;
        }
        Player player = (Player) sender;

        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        var nation = Nations.getInstance().get(diplomacyPlayer);
        var sameNation = Objects.equals(Nations.getInstance().get(diplomacyPlayer), group.getNation());
        var canLeadAllGroups = false;
        if (nation != null) {
            canLeadAllGroups = nation.getMemberClass(diplomacyPlayer).getPermissions().get("CanLeadAllGroups");
        }

        if (sameNation && canLeadAllGroups) {
            sender.sendMessage(ChatColor.DARK_RED + "As a leader of all groups in your nation, you cannot leave.");
            return;
        }

        if (!diplomacyPlayer.getGroups().contains(group.getGroupID())) {
            sender.sendMessage(ChatColor.DARK_RED + "You are not a member of that group.");
        }

        diplomacyPlayer.removeGroup(group);
        player.sendMessage(ChatColor.AQUA + "You have left " + ChatColor.BLUE + group.getName() + ChatColor.AQUA + ".");


        for (var onlinePlayer : Bukkit.getOnlinePlayers()) {
            var color = ChatColor.BLUE;
            var otherDiplomacyPlayer = DiplomacyPlayers.getInstance().get(onlinePlayer.getUniqueId());
            var otherNation = Nations.getInstance().get(otherDiplomacyPlayer);
            if (otherNation != null) {
                if (otherNation.getEnemyNationIDs().contains(nation.getNationID())) {
                    color = ChatColor.RED;
                } else if (otherNation.getAllyNationIDs().contains(nation.getNationID())) {
                    color = ChatColor.GREEN;
                }
            }
            var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(onlinePlayer.getUniqueId());
            if (group.getMembers().contains(testDiplomacyPlayer)) {
                onlinePlayer.sendMessage(color + player.getName() + ChatColor.AQUA + " has left " + ChatColor.BLUE + group.getName() + ChatColor.AQUA + ".");
            }
        }


        if (diplomacyPlayer.getGroupsLed().contains(group.getGroupID())) {
            diplomacyPlayer.removeGroupLed(group);
        }

    }

    private void groupKick(CommandSender sender, String strPlayer, String strGroup) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var group = DiplomacyGroups.getInstance().get(strGroup);
        if (group == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Unknown group.");
            return;
        }

        var player = (Player) sender;
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        var isGroupLeader = diplomacyPlayer.getGroupsLed().contains(group.getGroupID());

        var nation = Nations.getInstance().get(diplomacyPlayer);
        var memberClass = nation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        var canLeadAllGroups = permissions.get("CanLeadAllGroups");
        var groupNation = group.getNation();

        if (!(isGroupLeader || (canLeadAllGroups && Objects.equals(nation, groupNation)))) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to kick players from this group.");
            return;
        }

        var otherPlayer = Bukkit.getPlayer(strPlayer);
        if (otherPlayer == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Unknown player.");
            return;
        }

        var otherDiplomacyPlayer = DiplomacyPlayers.getInstance().get(otherPlayer.getUniqueId());
        var otherNation = Nations.getInstance().get(otherDiplomacyPlayer);
        var sameNation = Objects.equals(Nations.getInstance().get(otherDiplomacyPlayer), group.getNation());
        var otherCanLeadAllGroups = false;
        if (otherNation != null) {
            otherCanLeadAllGroups = otherNation.getMemberClass(otherDiplomacyPlayer).getPermissions().get("CanLeadAllGroups");
        }

        if (sameNation && otherCanLeadAllGroups) {
            sender.sendMessage(ChatColor.DARK_RED + otherPlayer.getName() + " cannot be kicked from that group.");
            return;
        }

        if (!group.getMembers().contains(otherDiplomacyPlayer)) {
            sender.sendMessage(ChatColor.DARK_RED + otherPlayer.getName() + " is not a member of that group.");
            return;
        }
        otherDiplomacyPlayer.removeGroup(group);
        otherPlayer.sendMessage(ChatColor.AQUA + "You have been kicked from " + ChatColor.BLUE + group.getName() + ChatColor.AQUA + ".");

        var color = ChatColor.BLUE;
        if (otherNation != null) {
            if (otherNation.getEnemyNationIDs().contains(nation.getNationID())) {
                color = ChatColor.RED;
            } else if (otherNation.getAllyNationIDs().contains(nation.getNationID())) {
                color = ChatColor.GREEN;
            }
        }
        for (var onlinePlayer : Bukkit.getOnlinePlayers()) {
            var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(onlinePlayer.getUniqueId());
            if (group.getMembers().contains(testDiplomacyPlayer)) {
                onlinePlayer.sendMessage(color + otherPlayer.getName() + ChatColor.AQUA + " has been kicked from " + ChatColor.BLUE + group.getName() + ChatColor.AQUA + ".");
            }
        }


        if (otherDiplomacyPlayer.getGroupsLed().contains(group.getGroupID())) {
            otherDiplomacyPlayer.removeGroupLed(group);
        }

    }

    private void groupBanner(CommandSender sender, String strGroup) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var group = DiplomacyGroups.getInstance().get(strGroup);

        if (group == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Unknown group.");
            return;
        }

        Player player = (Player) sender;
        DiplomacyPlayer diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        var isGroupLeader = diplomacyPlayer.getGroupsLed().contains(group.getGroupID());

        var nation = Nations.getInstance().get(diplomacyPlayer);
        var memberClass = nation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        var canLeadAllGroups = permissions.get("CanLeadAllGroups");
        var groupNation = group.getNation();

        if (!(isGroupLeader || (canLeadAllGroups && Objects.equals(nation, groupNation)))) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to change this group's banner.");
            return;
        }

        var heldItem = player.getInventory().getItemInMainHand();
        if (!(heldItem.getItemMeta() instanceof BannerMeta)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be holding a banner.");
            return;
        }

        if (group.getBanner().equals(heldItem)) {
            sender.sendMessage(ChatColor.DARK_RED + "This is already the group banner.");
            return;
        }

        group.setBanner(heldItem);

        for (var testPlayer : Bukkit.getOnlinePlayers()) {
            var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(testPlayer.getUniqueId());
            if (group.getMembers().contains(testDiplomacyPlayer)) {
                testPlayer.sendMessage(ChatColor.AQUA + "The banner of " + ChatColor.BLUE + group.getName() + ChatColor.AQUA + " has been updated.");
            }
        }
    }

    private void groupUnclaim(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var player = (Player) sender;
        var chunk = player.getLocation().getChunk();
        var diplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(chunk);
        var group = DiplomacyGroups.getInstance().get(diplomacyChunk);

        if (group == null) {
            sender.sendMessage(ChatColor.DARK_RED + "This plot does not belong to any groups.");
            return;
        }


        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        var isGroupLeader = diplomacyPlayer.getGroupsLed().contains(group.getGroupID());

        var nation = Nations.getInstance().get(diplomacyPlayer);
        var memberClass = nation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        var canLeadAllGroups = permissions.get("CanLeadAllGroups");
        var groupNation = group.getNation();

        if (!(isGroupLeader || (canLeadAllGroups && Objects.equals(nation, groupNation)))) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to unclaim land for this group.");
            return;
        }

        var canManagePlotsOfLedGroups = permissions.get("CanManagePlotsOfLedGroups");

        if (!canManagePlotsOfLedGroups) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to unclaim land for this group.");
            return;
        }

        group.removeChunk(diplomacyChunk);
        sender.sendMessage(ChatColor.AQUA + "Plot unclaimed.");

    }

    private void groupClaim(CommandSender sender, String strGroup) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var group = DiplomacyGroups.getInstance().get(strGroup);

        if (group == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Unknown group.");
            return;
        }

        var player = (Player) sender;
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        var isGroupLeader = diplomacyPlayer.getGroupsLed().contains(group.getGroupID());

        var nation = Nations.getInstance().get(diplomacyPlayer);
        var memberClass = nation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        var canLeadAllGroups = permissions.get("CanLeadAllGroups");
        var groupNation = group.getNation();

        if (!(isGroupLeader || (canLeadAllGroups && Objects.equals(nation, groupNation)))) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to claim land for this group.");
            return;
        }

        var canManagePlotsOfLedGroups = permissions.get("CanManagePlotsOfLedGroups");

        if (!canManagePlotsOfLedGroups) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to claim land for this group.");
            return;
        }

        var chunk = player.getLocation().getChunk();
        var diplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(chunk);
        var otherNation = diplomacyChunk.getNation();

        if (!Objects.equals(otherNation, groupNation)) {
            sender.sendMessage(ChatColor.DARK_RED + "This land doesn't belong to the same nation as " + group.getName() + ".");
            return;
        }


        if (group.hasChunk(diplomacyChunk)) {
            sender.sendMessage(ChatColor.DARK_RED + group.getName() + " has already claimed this chunk.");
            return;
        }

        var otherGroup = DiplomacyGroups.getInstance().get(diplomacyChunk);
        if (otherGroup != null) {
            var isOtherGroupLeader = diplomacyPlayer.getGroupsLed().contains(otherGroup.getGroupID());
            if (!(isOtherGroupLeader || canLeadAllGroups)) {
                sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to claim over land belonging to the group " + otherGroup.getName() + ".");
                return;
            }
            otherGroup.removeChunk(diplomacyChunk);
        }


        group.addChunk(diplomacyChunk);
        sender.sendMessage(ChatColor.AQUA + "Plot claimed by " + ChatColor.BLUE + group.getName() + ChatColor.AQUA + ".");

    }

    private void groupPromote(CommandSender sender, String strPlayer, String strGroup) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var group = DiplomacyGroups.getInstance().get(strGroup);

        if (group == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Unknown group.");
            return;
        }

        var otherPlayer = Bukkit.getPlayer(strPlayer);
        if (otherPlayer == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Unknown player.");
        }

        var player = (Player) sender;
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        var isGroupLeader = diplomacyPlayer.getGroupsLed().contains(group.getGroupID());

        var nation = Nations.getInstance().get(diplomacyPlayer);
        var memberClass = nation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        var canLeadAllGroups = permissions.get("CanLeadAllGroups");
        var groupNation = group.getNation();

        if (!(isGroupLeader || (canLeadAllGroups && Objects.equals(nation, groupNation)))) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to promote members to leaders in this group.");
            return;
        }

        var otherDiplomacyPlayer = DiplomacyPlayers.getInstance().get(otherPlayer.getUniqueId());
        var otherNation = Nations.getInstance().get(otherDiplomacyPlayer);
        var sameNation = Objects.equals(otherNation, group.getNation());
        var otherCanLeadAllGroups = false;
        if (otherNation != null) {
            otherCanLeadAllGroups = otherNation.getMemberClass(otherDiplomacyPlayer).getPermissions().get("CanLeadAllGroups");
        }

        if (sameNation && otherCanLeadAllGroups) {
            sender.sendMessage(ChatColor.DARK_RED + otherPlayer.getName() + " is already a leader of all " + group.getNation().getName() + " groups.");
            return;
        }

        if (!(otherDiplomacyPlayer.getGroups().contains(group.getGroupID()))) {
            sender.sendMessage(ChatColor.DARK_RED + otherPlayer.getName() + " is not a member of this group.");
            return;
        }

        if (otherDiplomacyPlayer.getGroupsLed().contains(group.getGroupID())) {
            sender.sendMessage(ChatColor.DARK_RED + otherPlayer.getName() + " is already a leader of this group.");
            return;
        }

        otherDiplomacyPlayer.addGroupLed(group);

        for (var onlinePlayer : Bukkit.getOnlinePlayers()) {
            var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(onlinePlayer.getUniqueId());
            if (group.getMembers().contains(testDiplomacyPlayer)) {
                onlinePlayer.sendMessage(ChatColor.BLUE + otherPlayer.getName() + ChatColor.AQUA + " has been promoted to leader of " + ChatColor.BLUE + group.getName() + ChatColor.AQUA + ".");
            }
        }
    }

    private void groupDemote(CommandSender sender, String strPlayer, String strGroup) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var group = DiplomacyGroups.getInstance().get(strGroup);

        if (group == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Unknown group.");
            return;
        }

        var otherPlayer = Bukkit.getPlayer(strPlayer);
        if (otherPlayer == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Unknown player.");
        }

        var player = (Player) sender;
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        var isGroupLeader = diplomacyPlayer.getGroupsLed().contains(group.getGroupID());

        var nation = Nations.getInstance().get(diplomacyPlayer);
        var memberClass = nation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        var canLeadAllGroups = permissions.get("CanLeadAllGroups");
        var groupNation = group.getNation();

        if (!(isGroupLeader || (canLeadAllGroups && Objects.equals(nation, groupNation)))) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to demote leaders in this group.");
            return;
        }

        var otherDiplomacyPlayer = DiplomacyPlayers.getInstance().get(otherPlayer.getUniqueId());
        var otherNation = Nations.getInstance().get(otherDiplomacyPlayer);
        var sameNation = Objects.equals(otherNation, group.getNation());
        var otherCanLeadAllGroups = false;
        if (otherNation != null) {
            otherCanLeadAllGroups = otherNation.getMemberClass(otherDiplomacyPlayer).getPermissions().get("CanLeadAllGroups");
        }

        if (sameNation && otherCanLeadAllGroups) {
            sender.sendMessage(ChatColor.DARK_RED + otherPlayer.getName() + " cannot be demoted as a leader of all " + group.getNation().getName() + " groups.");
            return;
        }

        if (!(otherDiplomacyPlayer.getGroupsLed().contains(group.getGroupID()))) {
            sender.sendMessage(ChatColor.DARK_RED + otherPlayer.getName() + " is not a member of this group.");
            return;
        }

        if (otherDiplomacyPlayer.getGroupsLed().contains(group.getGroupID())) {
            sender.sendMessage(ChatColor.DARK_RED + otherPlayer.getName() + " is not a leader of this group.");
            return;
        }

        otherDiplomacyPlayer.removeGroupLed(group);

        for (var onlinePlayer : Bukkit.getOnlinePlayers()) {
            var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(onlinePlayer.getUniqueId());
            if (testDiplomacyPlayer.getGroups().contains(group.getGroupID())) {
                onlinePlayer.sendMessage(ChatColor.BLUE + otherPlayer.getName() + ChatColor.AQUA + " has been demoted from leader of " + ChatColor.BLUE + group.getName() + ChatColor.AQUA + ".");
            }
        }
    }
}


