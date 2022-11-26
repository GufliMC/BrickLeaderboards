package com.guflimc.brick.leaderboards.common;

import com.guflimc.brick.leaderboards.api.LeaderboardsManager;
import com.guflimc.brick.orm.api.database.DatabaseContext;

public abstract class BrickLeaderboardsManager implements LeaderboardsManager {

    private final DatabaseContext databaseContext;

    public BrickLeaderboardsManager(DatabaseContext databaseContext) {
        this.databaseContext = databaseContext;

    }



}
