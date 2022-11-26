package com.guflimc.brick.leaderboards.common;

import com.guflimc.brick.leaderboards.api.LeaderboardsManager;
import com.guflimc.brick.leaderboards.api.type.podium.Podium;
import com.guflimc.brick.leaderboards.api.type.podium.PodiumBuilder;
import com.guflimc.brick.leaderboards.common.domain.DStatsLeaderboard;
import com.guflimc.brick.leaderboards.common.domain.DStatsPodium;
import com.guflimc.brick.maths.api.geo.pos.Location;
import com.guflimc.brick.orm.api.database.DatabaseContext;
import com.guflimc.brick.stats.api.StatsAPI;
import com.guflimc.brick.stats.api.key.StatsKey;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class BrickLeaderboardsManager implements LeaderboardsManager {

    private final DatabaseContext databaseContext;

    private final Set<DStatsLeaderboard> statsLeaderboards = new CopyOnWriteArraySet<>();

    public BrickLeaderboardsManager(DatabaseContext databaseContext) {
        this.databaseContext = databaseContext;

        // load all permanent leaderboards
        statsLeaderboards.addAll(databaseContext.findAllAsync(DStatsPodium.class).join());

        // podiums
        statsLeaderboards.stream()
                .filter(sl -> sl instanceof DStatsPodium)
                .map(sl -> (DStatsPodium) sl)
                .forEach(this::spawn);
    }

    protected abstract Podium.Member member(@NotNull UUID entityId, int score);

    private void spawn(DStatsPodium sp) {
        PodiumBuilder pb = podium();
        if (sp.display() != null) {
            pb.withDisplay(sp.display());
        }
        if (sp.title() != null) {
            pb.withTitle(sp.title(), sp.titleLocation());
        }
        pb.withPositions(sp.positions());
        Podium podium = pb.build();

        StatsAPI.get().subscribe()
                .filter(event -> event.key().equals(sp.statsKey()))
                .handler(event -> podium.update(member(event.record().id(), event.record().value())))
                .change();
    }

    //

    public Optional<DStatsLeaderboard> findStatsLeaderboard(@NotNull String name) {
        return statsLeaderboards.stream()
                .filter(sl -> sl.name().equals(name))
                .findFirst();
    }

    public Optional<DStatsPodium> findStatsPodium(@NotNull String name) {
        return findStatsLeaderboard(name)
                .flatMap(sl -> Optional.ofNullable(sl instanceof DStatsPodium sp ? sp : null));
    }

    public CompletableFuture<DStatsPodium> createStatsPodium(@NotNull String name, @NotNull StatsKey statsKey, @NotNull Location[] positions) {
        DStatsPodium statsPodium = new DStatsPodium(name, statsKey, positions);
        statsLeaderboards.add(statsPodium);
        spawn(statsPodium);
        return persist(statsPodium).thenApply(v -> statsPodium);
    }

    public CompletableFuture<Void> persist(@NotNull DStatsLeaderboard entity) {
        return databaseContext.persistAsync(entity);
    }

    public CompletableFuture<Void> remove(@NotNull DStatsLeaderboard entity) {
        statsLeaderboards.remove(entity);
        return databaseContext.removeAsync(entity);
    }
}
