package com.guflimc.brick.leaderboards.common.domain;

import com.guflimc.brick.maths.api.geo.pos.Location;
import com.guflimc.brick.maths.database.api.LocationConverter;
import com.guflimc.brick.orm.jpa.converters.ComponentConverter;
import com.guflimc.brick.stats.api.key.StatsKey;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "stats_podiums")
public class DStatsPodium extends DStatsLeaderboard {

    @Column(nullable = false, length = 4096)
    private PositionList positions;

    @Convert(converter = LocationConverter.class)
    private Location titleLocation;

    @Convert(converter = ComponentConverter.class)
    private Component display;

    public record PositionList(List<Location> positions) {
        Location[] array() {
            return positions.toArray(Location[]::new);
        }
    }

    //

    public DStatsPodium() {
    }

    public DStatsPodium(@NotNull String name, @NotNull StatsKey statsKey, @NotNull String actorType, @NotNull Location[] positions) {
        super(name, statsKey, actorType);
        this.positions = new PositionList(List.of(positions));
        this.display = Component.text(statsKey.name() + ": {0}");
    }

    public Location[] positions() {
        return positions.array();
    }

    public Location titleLocation() {
        return titleLocation;
    }

    public Component display() {
        return display;
    }

}
