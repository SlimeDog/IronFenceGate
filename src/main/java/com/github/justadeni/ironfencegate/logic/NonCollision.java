package com.github.justadeni.ironfencegate.logic;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class NonCollision {

    private static volatile NonCollision nonCollision;
    private ScoreboardManager manager;
    private Scoreboard board;
    private Team team;

    private NonCollision() {
        manager = Bukkit.getScoreboardManager();
        board = manager.getNewScoreboard();
        team = board.registerNewTeam("NonCollision");

        team.setCanSeeFriendlyInvisibles(false);
        team.setAllowFriendlyFire(true);
        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.FOR_OTHER_TEAMS);
    }

    public static NonCollision getInstance(){
        NonCollision cached = nonCollision;
        if (cached == null)
            cached = nonCollision = new NonCollision();
        return cached;
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
}
