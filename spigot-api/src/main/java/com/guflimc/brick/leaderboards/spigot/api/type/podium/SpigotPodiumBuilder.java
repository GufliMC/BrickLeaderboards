package com.guflimc.brick.leaderboards.spigot.api.type.podium;

import com.guflimc.brick.leaderboards.api.type.podium.PodiumBuilder;
import com.guflimc.brick.maths.api.geo.pos.Location;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Function;

public interface SpigotPodiumBuilder extends PodiumBuilder {

    @Override
    SpigotPodiumBuilder withTitle(@NotNull Component title, @NotNull Location position);

    @Override
    SpigotPodiumBuilder withDisplay(@NotNull Component display);

    @Override
    SpigotPodiumBuilder withPositions(@NotNull Location... positions);

    SpigotPodiumBuilder withItemSupplier(@NotNull Function<UUID, ItemStack> supplier);

    @Override
    SpigotPodium build();
}
