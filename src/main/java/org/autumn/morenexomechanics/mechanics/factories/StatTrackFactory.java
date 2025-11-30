package org.autumn.morenexomechanics.mechanics.factories;

import org.autumn.morenexomechanics.api.StatTrack;

import com.nexomc.nexo.mechanics.Mechanic;
import com.nexomc.nexo.mechanics.MechanicFactory;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StatTrackFactory extends MechanicFactory {

    public StatTrackFactory() {
        super("stat_track");
    }

    @Override @Nullable
    public StatTrack getMechanic(String itemID) {
        return (StatTrack) super.getMechanic(itemID);
    }

    @Override @Nullable
    public StatTrack getMechanic(ItemStack itemStack) {
        return (StatTrack) super.getMechanic(itemStack);
    }

    @Override
    public @Nullable Mechanic parse(@NotNull ConfigurationSection configSect) {
        StatTrack mechanic = new StatTrack(this, configSect);
        addToImplemented(mechanic);
        return mechanic;
    }

}
