package com.github.justadeni.IronFenceGate.misc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class NonCollision {

    private static NonCollision nonCollision;
    private static ScoreboardManager manager;
    private static Scoreboard board;
    private static Team team;

    public static void setup() {
        manager = Bukkit.getScoreboardManager();
        board = manager.getNewScoreboard();
        team = board.registerNewTeam("NonCollision");

        team.setCanSeeFriendlyInvisibles(false);
        team.setAllowFriendlyFire(true);
        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.FOR_OTHER_TEAMS);
        nonCollision = new NonCollision();
    }

    public void add(Entity entity){
        if(!team.hasEntry(entity.getUniqueId().toString()))
            team.addEntry(entity.getUniqueId().toString());
    }

    public void remove(Entity entity){
        if(team.hasEntry(entity.getUniqueId().toString()))
            team.removeEntry(entity.getUniqueId().toString());
    }

    public boolean has(Entity entity){
        return team.hasEntry(entity.getUniqueId().toString());
    }

    public Scoreboard getBoard(){
        return board;
    }

    public static NonCollision get(){
        return nonCollision;
    }
}
