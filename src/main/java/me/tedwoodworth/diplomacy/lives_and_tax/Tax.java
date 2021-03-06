package me.tedwoodworth.diplomacy.lives_and_tax;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.events.NationLeaveEvent;
import me.tedwoodworth.diplomacy.nations.Nations;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Objects;

public class Tax implements Runnable {

    private static final DecimalFormat formatter = new DecimalFormat("#,##0.00");

    Tax() {
    }


    @Override
    public void run() {
        for (var nation : Nations.getInstance().getNations()) {
            for (var member : nation.getMembers()) {
                var memberClass = nation.getMemberClass(member);
                var memberClassID = "0";
                var tax = 0.0;
                if (memberClass != null) {
                    memberClassID = memberClass.getClassID();
                    tax = memberClass.getTax();
                }
                var offlinePlayer = member.getOfflinePlayer();
                var player = offlinePlayer.getPlayer();

                var economy = Diplomacy.getEconomy();


                var memberBalance = economy.getBalance(member.getOfflinePlayer());

                if (tax > memberBalance) {
                    economy.withdrawPlayer(member.getOfflinePlayer(), memberBalance);
                    nation.setBalance(nation.getBalance() + memberBalance);

                    if (memberClassID.equals("8")) {
                        var leaderCount = 0;
                        for (var testMember : nation.getMembers()) {
                            if (nation.getMemberClass(testMember).getClassID().equals("8")) {
                                leaderCount++;
                            }
                        }
                        if (leaderCount > 1) {
                            if (player != null) {
                                player.sendMessage(ChatColor.GREEN + "You have been taxed \u00A4" + formatter.format(tax));
                                player.sendMessage(ChatColor.RED + "You couldn't pay taxes and have been kicked from the nation.");
                            }
                            for (var testMember : nation.getMembers()) {
                                var testPlayer = testMember.getOfflinePlayer().getPlayer();
                                if (testPlayer != null && !Objects.equals(testPlayer, player)) {
                                    testPlayer.sendMessage(ChatColor.AQUA + member.getOfflinePlayer().getName() + " was kicked for not paying taxes.");
                                }
                            }
                            nation.removeMember(member);
                            var set = new HashSet<DiplomacyPlayer>();
                            set.add(member);
                            Bukkit.getPluginManager().callEvent(new NationLeaveEvent(set, nation));
                        } else {
                            if (player != null) {
                                player.sendMessage(ChatColor.GREEN + "You have been taxed \u00A4" + formatter.format(tax));
                                player.sendMessage(ChatColor.RED + "You can't  afford the tax, but you are the only " + memberClass.getName() + " and can't be kicked.");
                            }
                        }
                    } else {
                        if (player != null) {
                            player.sendMessage(ChatColor.GREEN + "You have been taxed \u00A4" + formatter.format(tax));
                            player.sendMessage(ChatColor.RED + "You couldn't afford the tax and have been kicked from the nation.");
                        }

                        for (var testMember : nation.getMembers()) {
                            var testPlayer = testMember.getOfflinePlayer().getPlayer();
                            if (testPlayer != null && !Objects.equals(testPlayer, player)) {
                                testPlayer.sendMessage(ChatColor.AQUA + member.getOfflinePlayer().getName() + " was kicked for not paying taxes.");
                            }
                        }
                        nation.removeMember(member);
                        var set = new HashSet<DiplomacyPlayer>();
                        set.add(member);
                        Bukkit.getPluginManager().callEvent(new NationLeaveEvent(set, nation));
                    }
                } else {
                    economy.withdrawPlayer(member.getOfflinePlayer(), tax);
                    nation.setBalance(nation.getBalance() + tax);
                    if (player != null) {
                        player.sendMessage(ChatColor.GREEN + "You have been taxed \u00A4" + formatter.format(tax));
                    }
                }

            }
        }
    }
}
