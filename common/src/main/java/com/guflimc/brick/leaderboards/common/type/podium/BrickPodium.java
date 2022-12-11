package com.guflimc.brick.leaderboards.common.type.podium;

import com.guflimc.brick.leaderboards.api.type.podium.Podium;
import com.guflimc.brick.maths.api.geo.pos.Location;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class BrickPodium implements Podium {

    private final Location[] positions;
    private final Title title;
    private final Component display;

    private final List<Member> members = new ArrayList<>();

    public BrickPodium(@NotNull Location[] positions, Title title, Component display) {
        this.positions = positions;
        this.title = title;
        this.display = display;
    }

    public abstract void remove();

    @Override
    public @NotNull Location[] positions() {
        return positions;
    }

    @Override
    public @Nullable Title title() {
        return title;
    }

    @Override
    public @NotNull Component display() {
        return display;
    }

    @Override
    public @NotNull List<Member> members() {
        return Collections.unmodifiableList(members);
    }

    @Override
    public void update(@NotNull Member member) {
        members.removeIf(m -> m.entityId().equals(member.entityId()));
        members.add(member);
        members.sort(Comparator.comparingInt(Member::score).reversed());

        while (members.size() > positions.length) {
            members.remove(members.size() - 1);
        }

        if (!members.contains(member)) {
            return;
        }

        render();
    }

}
