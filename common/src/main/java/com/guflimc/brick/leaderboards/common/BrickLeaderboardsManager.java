package com.guflimc.brick.leaderboards.common;

import com.guflimc.brick.leaderboards.api.LeaderboardsManager;
import com.guflimc.brick.leaderboards.api.type.podium.Podium;
import com.guflimc.brick.leaderboards.api.type.podium.PodiumBuilder;
import com.guflimc.brick.leaderboards.common.domain.DStatsLeaderboard;
import com.guflimc.brick.leaderboards.common.domain.DStatsPodium;
import com.guflimc.brick.leaderboards.common.type.podium.BrickPodium;
import com.guflimc.brick.maths.api.geo.pos.Location;
import com.guflimc.brick.orm.api.database.DatabaseContext;
import com.guflimc.brick.stats.api.StatsAPI;
import com.guflimc.brick.stats.api.event.Subscription;
import com.guflimc.brick.stats.api.key.StatsKey;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class BrickLeaderboardsManager implements LeaderboardsManager {

    private final DatabaseContext databaseContext;

    private final Set<DStatsLeaderboard> statsLeaderboards = new CopyOnWriteArraySet<>();
    private final Set<SpawnedPodium> spawnedPodiums = new CopyOnWriteArraySet<>();

    private record SpawnedPodium(BrickPodium podium, DStatsLeaderboard leaderboard, Subscription subscription) {}

    public BrickLeaderboardsManager(DatabaseContext databaseContext) {
        this.databaseContext = databaseContext;

        // load all permanent leaderboards
        statsLeaderboards.addAll(databaseContext.findAllAsync(DStatsPodium.class).join());
    }

    protected abstract Podium.Member member(@NotNull UUID entityId, int score);

    protected abstract void inject(PodiumBuilder podiumBuilder);

    protected void spawn(DStatsPodium sp) {
        PodiumBuilder pb = podium();
        if (sp.display() != null) {
            pb.withDisplay(sp.display());
        }
        if (sp.title() != null) {
            pb.withTitle(sp.title(), sp.titleLocation());
        }
        pb.withPositions(sp.positions());
        inject(pb);
        Podium podium = pb.build();

        StatsAPI.get().select("PLAYER", sp.statsKey(), sp.positions().length)
                .forEach(r -> podium.update(member(r.actors().first().id(), r.value())));

        Subscription sub = StatsAPI.get().subscribe()
                .filter(event -> event.record().key().equals(sp.statsKey()))
                .filter(event -> event.record().actors().size() == 1)
                .handler(event -> podium.update(member(event.record().actors().first().id(), event.record().value())))
                .change();

        podium.render();

        spawnedPodiums.add(new SpawnedPodium((BrickPodium) podium, sp, sub));
    }

    //

    public Collection<DStatsLeaderboard> leaderboards() {
        return Collections.unmodifiableCollection(statsLeaderboards);
    }

    public Optional<DStatsLeaderboard> findStatsLeaderboard(@NotNull String name) {
        return statsLeaderboards.stream()
                .filter(sl -> sl.name().equals(name))
                .findFirst();
    }

    public Optional<DStatsPodium> findStatsPodium(@NotNull String name) {
        return findStatsLeaderboard(name)
                .flatMap(sl -> Optional.ofNullable(sl instanceof DStatsPodium sp ? sp : null));
    }

    public CompletableFuture<DStatsPodium> createStatsPodium(@NotNull String name, @NotNull StatsKey statsKey,
                                                             @NotNull String actorType, @NotNull Location[] positions) {
        DStatsPodium statsPodium = new DStatsPodium(name, statsKey, actorType, positions);
        statsLeaderboards.add(statsPodium);
        spawn(statsPodium);
        return persist(statsPodium).thenApply(v -> statsPodium);
    }

    public CompletableFuture<Void> persist(@NotNull DStatsLeaderboard entity) {
        return databaseContext.persistAsync(entity);
    }

    public CompletableFuture<Void> remove(@NotNull DStatsLeaderboard entity) {
        statsLeaderboards.remove(entity);

        spawnedPodiums.stream().filter(s -> s.leaderboard.equals(entity))
                .forEach(s -> {
                    s.subscription.unsubscribe();
                    s.podium.remove();
                });

        return databaseContext.removeAsync(entity);
    }
}
