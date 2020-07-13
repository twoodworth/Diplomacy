package me.tedwoodworth.diplomacy.nations.contest;

import me.tedwoodworth.diplomacy.nations.DiplomacyChunk;
import me.tedwoodworth.diplomacy.nations.Nation;
import me.tedwoodworth.diplomacy.nations.Nations;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayer;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.Objects;

public class Contest {

    private double progress = 0.0;
    private final Nation attackingNation;
    private final DiplomacyChunk diplomacyChunk;
    private final boolean isWilderness;

    Contest(Nation attackingNation, DiplomacyChunk diplomacyChunk, boolean isWilderness) {
        this.attackingNation = attackingNation;
        this.diplomacyChunk = diplomacyChunk;
        this.isWilderness = isWilderness;
    }

    public boolean isNearChunk(Player player, Chunk chunk) {
        var chunkCenter = new Location(chunk.getWorld(), chunk.getX() * 16 + 8, player.getLocation().getY(), chunk.getZ() * 16 + 8);
        return player.getWorld().equals(chunk.getWorld()) && player.getLocation().distanceSquared(chunkCenter) < 10000;
    }

    public void sendParticles() {
        var defendingNation = diplomacyChunk.getNation();
        var chunk = diplomacyChunk.getChunk();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (isNearChunk(player, chunk)) {
                var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());

                sendPlayerParticles(chunk, diplomacyPlayer, attackingNation, defendingNation);
            }
        }
    }

    public void sendPlayerParticles(Chunk chunk, DiplomacyPlayer diplomacyPlayer, Nation attackingNation, Nation defendingNation) {
        var player = Bukkit.getPlayer(diplomacyPlayer.getUUID());
        var baseX = chunk.getX() * 16;
        var baseZ = chunk.getZ() * 16;
        var world = chunk.getWorld();
        var defendingNationIsWilderness = Nations.isWilderness(defendingNation);

        var isAttackingNationAlly = attackingNation.getAllyNationIDs().contains(Nations.getInstance().get(diplomacyPlayer).getNationID());
        var isDefendingNationAlly = !defendingNationIsWilderness && defendingNation.getAllyNationIDs().contains(Nations.getInstance().get(diplomacyPlayer).getNationID());
        var isAttackingNation = Objects.equals(Nations.getInstance().get(diplomacyPlayer), attackingNation);
        var isDefendingNation = Objects.equals(Nations.getInstance().get(diplomacyPlayer), defendingNation);

        var particle = Particle.REDSTONE;
        Particle.DustOptions dustOptions;
        if (isAttackingNation || isAttackingNationAlly && !isDefendingNationAlly) {
            dustOptions = new Particle.DustOptions(Color.LIME, 1);
        } else if (!isWilderness && isDefendingNation || isDefendingNationAlly && !isAttackingNationAlly) {
            dustOptions = new Particle.DustOptions(Color.RED, 1);
        } else {
            dustOptions = new Particle.DustOptions(Color.YELLOW, 1);
        }
        var min = Math.max(0, player.getLocation().getBlockY() - 20);
        var max = Math.min(255, player.getLocation().getBlockY() + 20);
        for (var y = min; y < max; y++) {
            for (var i = 0; i < 16; i += 2) {
                player.spawnParticle(particle, new Location(world, baseX + i, y, baseZ), 1, dustOptions);
                player.spawnParticle(particle, new Location(world, baseX + 16, y, baseZ + i), 1, dustOptions);
                player.spawnParticle(particle, new Location(world, baseX + 16 - i, y, baseZ + 16), 1, dustOptions);
                player.spawnParticle(particle, new Location(world, baseX, y, baseZ + 16 - i), 1, dustOptions);

            }

        }

    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public Nation getAttackingNation() {
        return attackingNation;
    }

    public DiplomacyChunk getDiplomacyChunk() {
        return diplomacyChunk;
    }

    public boolean isWilderness() {
        return isWilderness;
    }
}