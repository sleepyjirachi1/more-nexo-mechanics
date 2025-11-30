package org.autumn.morenexomechanics.mechanics.factories;

import org.autumn.morenexomechanics.mechanics.SwappableEnchants;
import com.nexomc.nexo.mechanics.Mechanic;
import com.nexomc.nexo.mechanics.MechanicFactory;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SwappableEnchantsFactory extends MechanicFactory {

    public SwappableEnchantsFactory() { super("swappable_enchants"); }

    @Override @Nullable
    public SwappableEnchants getMechanic(String itemID) {
        return (SwappableEnchants) super.getMechanic(itemID);
    }

    @Override @Nullable
    public SwappableEnchants getMechanic(ItemStack itemStack) {
        return (SwappableEnchants) super.getMechanic(itemStack);
    }

    @Override
    public @Nullable Mechanic parse(@NotNull ConfigurationSection configSect) {
        SwappableEnchants mechanic = new SwappableEnchants(this, configSect);
        addToImplemented(mechanic);
        return mechanic;
    }

}
