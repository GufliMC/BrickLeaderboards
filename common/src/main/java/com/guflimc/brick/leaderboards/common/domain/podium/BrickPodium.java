package com.guflimc.brick.leaderboards.common.domain.podium;

import com.guflimc.brick.leaderboards.api.domain.podium.Podium;
import com.guflimc.brick.leaderboards.common.domain.BrickLeaderboard;
import com.guflimc.brick.maths.api.geo.pos.Location;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class BrickPodium extends BrickLeaderboard implements Podium {

    private final Location[] positions;
    private final Title title;
    private final Component name;

    private final List<Member> members = new ArrayList<>();

    public BrickPodium(@NotNull Location[] positions, Title title, Component name) {
        this.positions = positions;
        this.title = title;
        this.name = name;
    }

    @Override
    public @NotNull Location[] positions() {
        return positions;
    }

    @Override
    public @Nullable Title title() {
        return title;
    }

    @Override
    public @NotNull Component name() {
        return name;
    }

    @Override
    public @NotNull List<Member> members() {
        return Collections.unmodifiableList(members);
    }

    @Override
    public void update(@NotNull UUID entityId, int score) {
        Member member = new Member(entityId, score);
        members.add(member);
        members.sort(Comparator.comparingInt(Member::score).reversed());

        while ( members.size() > positions.length ) {
            members.remove(members.size() - 1);
        }

        if ( !members.contains(member) ) {
            return;
        }

        render();
    }

    protected abstract void render();

}
