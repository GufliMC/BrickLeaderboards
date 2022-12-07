package com.guflimc.brick.leaderboards.common.domain;

import com.guflimc.brick.orm.jpa.converters.ComponentConverter;
import com.guflimc.brick.stats.api.key.StatsKey;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
public class DStatsLeaderboard {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String statsKey;

    @Column(nullable = false)
    private String actorType;

    @Convert(converter = ComponentConverter.class)
    protected Component title;

    @WhenCreated
    private Instant createdAt;

    @WhenModified
    private Instant updatedAt;

    //

    public DStatsLeaderboard() {
    }

    public DStatsLeaderboard(@NotNull String name, @NotNull StatsKey statsKey, @NotNull String actorType) {
        this.name = name;
        this.statsKey = statsKey.name();
        this.actorType = actorType;
    }

    public UUID id() {
        return id;
    }

    public String name() {
        return name;
    }

    public StatsKey statsKey() {
        return StatsKey.of(statsKey);
    }

    public String actorType() {
        return actorType;
    }

    public Component title() {
        return title;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

}
