package com.guflimc.brick.leaderboards.spigot.type.podium;

import com.guflimc.brick.leaderboards.common.type.podium.BrickPodiumBuilder;
import com.guflimc.brick.leaderboards.spigot.api.type.podium.SpigotPodium;
import com.guflimc.brick.leaderboards.spigot.api.type.podium.SpigotPodiumBuilder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Function;

public class SpigotBrickPodiumBuilder extends BrickPodiumBuilder<SpigotBrickPodiumBuilder> implements SpigotPodiumBuilder {

    private final JavaPlugin plugin;
    private Function<UUID, ItemStack> supplier;

    public SpigotBrickPodiumBuilder(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public SpigotPodiumBuilder withItemSupplier(@NotNull Function<UUID, ItemStack> supplier) {
        this.supplier = supplier;
        return this;
    }

    @Override
    public SpigotPodium build() {
        if (positions == null || positions.length == 0) {
            throw new IllegalStateException("No positions defined");
        }
        if (supplier == null) {
            throw new IllegalStateException("No item supplier defined");
        }

        return new SpigotBrickPodium(positions, title, display, plugin, supplier);
    }
}
