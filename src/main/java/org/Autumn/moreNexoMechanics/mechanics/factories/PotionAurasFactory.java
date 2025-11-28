package org.Autumn.moreNexoMechanics.mechanics.factories;

import org.Autumn.moreNexoMechanics.mechanics.PotionAuras;

import com.nexomc.nexo.mechanics.Mechanic;
import com.nexomc.nexo.mechanics.MechanicFactory;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PotionAurasFactory extends MechanicFactory {

    public PotionAurasFactory() { super("potion_auras"); }

    @Override @Nullable
    public PotionAuras getMechanic(String itemID) {
        return (PotionAuras) super.getMechanic(itemID);
    }

    @Override @Nullable
    public PotionAuras getMechanic(ItemStack itemStack) { return (PotionAuras) super.getMechanic(itemStack); }

    @Override
    public @Nullable Mechanic parse(@NotNull ConfigurationSection configSect) {
        PotionAuras mechanic = new PotionAuras(this, configSect);
        addToImplemented(mechanic);
        return mechanic;
    }

}
