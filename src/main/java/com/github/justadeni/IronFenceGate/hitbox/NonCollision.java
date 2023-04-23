package com.github.justadeni.IronFenceGate.hitbox;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class NonCollision {

    private static NonCollision nonCollision;
    private ScoreboardManager manager;
    public Scoreboard board;
    private Team team;

    public NonCollision() {
        this.manager = Bukkit.getScoreboardManager();
        this.board = manager.getNewScoreboard();
        this.team = board.registerNewTeam("NonCollision");

        team.setCanSeeFriendlyInvisibles(false);
        team.setAllowFriendlyFire(true);
        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.FOR_OTHER_TEAMS);
        nonCollision = this;
    }

    public void add(Entity entity){
        if(!team.hasEntry(entity.getUniqueId().toString()))
            team.addEntry(entity.getUniqueId().toString());
    }

    public void remove(Entity entity){
        if(team.hasEntry(entity.getUniqueId().toString()))
            team.removeEntry(entity.getUniqueId().toString());
    }

    public static NonCollision get(){
        return nonCollision;
    }
}
