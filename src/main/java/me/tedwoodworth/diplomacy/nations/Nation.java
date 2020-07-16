package me.tedwoodworth.diplomacy.nations;

import com.google.common.collect.ImmutableMap;
import me.tedwoodworth.diplomacy.groups.DiplomacyGroup;
import me.tedwoodworth.diplomacy.groups.DiplomacyGroups;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class Nation {

    private ConfigurationSection configSection;
    private String nationID;
    private String name;
    private List<UUID> leaderIDs;
    private List<NationClass> classes;
    private List<MemberInfo> memberInfos;
    private int balance;
    private long created;
    private UUID founderID;
    private String color;
    private List<UUID> outlawIDs;
    private List<String> allyNationIDs;

    Nation(String nationID, ConfigurationSection nationSection) {
        this.configSection = nationSection;
        this.nationID = nationID;
        this.name = nationSection.getString("Name");
        this.leaderIDs = new ArrayList<>();
        var leadersStr = nationSection.getStringList("Leaders");
        for (var LeaderID : leadersStr) {
            leaderIDs.add(UUID.fromString(LeaderID));
        }

        this.classes = new ArrayList<>(9);
        for (var classID = 0; classID < 9; classID++) {
            var strClassID = String.valueOf(classID);
            var classSection = nationSection.getConfigurationSection("Classes." + classID);
            var className = classSection.getString("Name");
            var classPrefix = classSection.getString("Prefix");
            var classTax = classSection.getInt("Tax");
            Map<String, Boolean> classPermissions = new HashMap<>();
            var permissionSection = classSection.getConfigurationSection("Permissions");
            var permissionSet = permissionSection.getKeys(false);
            for (var permission : permissionSet) {
                classPermissions.put(permission, permissionSection.getBoolean(permission));
            }
            var nationClass = new NationClass(strClassID, className, classPrefix, classTax, classPermissions, nationID);
            classes.add(nationClass);
        }

        this.memberInfos = new ArrayList<>();
        var membersStr = nationSection.getConfigurationSection("Members").getKeys(false);
        for (var memberID : membersStr) {
            var member = DiplomacyPlayers.getInstance().get(UUID.fromString(memberID));
            var memberClassID = nationSection.getString("Members." + memberID);
            var memberInfo = new MemberInfo(member, memberClassID);
            memberInfos.add(memberInfo);
        }

        this.balance = nationSection.getInt("Balance");
        this.created = nationSection.getInt("Created");
        this.founderID = UUID.fromString(Objects.requireNonNull(nationSection.getString("Founder")));
        this.color = nationSection.getString("Color");
        this.outlawIDs = new ArrayList<>();
        var outlawsStr = nationSection.getStringList("Outlaws");
        for (var outlaw : outlawsStr) {
            outlawIDs.add(UUID.fromString(outlaw));
        }
        this.allyNationIDs = nationSection.getStringList("AllyNations");
    }

    public static ConfigurationSection initializeNation(ConfigurationSection nationSection, OfflinePlayer leader, String name) {
        Map<String, String> membersMap = ImmutableMap.of(
                leader.getUniqueId().toString(), "8");

        nationSection.createSection("Members", membersMap);
        nationSection.set("Founder", leader.getUniqueId().toString());

        List<String> leaders = new ArrayList<>(1);
        leaders.add(leader.getUniqueId().toString());
        nationSection.set("Leaders", leaders);
        nationSection.set("Created", Instant.now().getEpochSecond());
        nationSection.set("Name", name);
        return nationSection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var nation = (Nation) o;
        return nationID.equals(nation.nationID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nationID);
    }

    public String getNationID() {
        return nationID;
    }

    public List<MemberInfo> getMemberInfos() {
        return memberInfos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<DiplomacyChunk> getChunks() {

        var chunkMaps = configSection.getMapList("Chunks");
        return chunkMaps.stream()
                .map(DiplomacyChunks.getInstance()::configMapToChunk)
                .collect(Collectors.toSet());
    }

    public boolean hasChunk(DiplomacyChunk diplomacyChunk) {
        var diplomacyChunkMap = DiplomacyChunks.getInstance().chunkToConfigMap(diplomacyChunk);
        var chunkMaps = configSection.getMapList("Chunks");
        return chunkMaps.contains(diplomacyChunkMap);
    }

    public void addChunk(DiplomacyChunk diplomacyChunk) {
        var list = configSection.getMapList("Chunks");
        list.add(DiplomacyChunks.getInstance().chunkToConfigMap(diplomacyChunk));
        configSection.set("Chunks", list);
    }

    public void removeChunk(DiplomacyChunk diplomacyChunk) {
        var list = configSection.getMapList("Chunks");
        list.remove(DiplomacyChunks.getInstance().chunkToConfigMap(diplomacyChunk));
        configSection.set("Chunks", list);
    }

    public List<DiplomacyGroup> getGroups() {
        List<DiplomacyGroup> groups = new ArrayList<>();
        for (var group : DiplomacyGroups.getInstance().getGroups()) {
            if (Objects.equals(group.getNation(), this)) {
                groups.add(group);
            }
        }
        return groups;
    }

    @Nullable
    public NationClass getNationClass(String classID) {
        for (var nationClass : classes) {
            if (nationClass.getClassID().equals(classID)) {
                return nationClass;
            }
        }
        return null;
    }

    public List<String> getAllyNationIDs() {
        return allyNationIDs;
    }

    public List<String> getEnemyNationIDs() {
        var list = configSection.getStringList("EnemyNations");
        return list;
    }

    public void addEnemyNation(Nation nation) {
        var list = configSection.getStringList("EnemyNations");
        list.add(nation.getNationID());
        configSection.set("EnemyNations", list);
    }

    public void removeEnemyNation(Nation nation) {
        var list = configSection.getStringList("EnemyNations");
        list.remove(nation.getNationID());
        configSection.set("EnemyNations", list);
    }

    public boolean getIsOpen() {
        var isOpen = configSection.getBoolean("IsOpen");
        return isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        configSection.set("IsOpen", isOpen);
    }
}
