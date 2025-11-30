package org.autumn.morenexomechanics.mechanics;

import com.nexomc.nexo.mechanics.Mechanic;
import com.nexomc.nexo.mechanics.MechanicFactory;

import org.bukkit.configuration.ConfigurationSection;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class PotionAuras extends Mechanic {

    public final List<String> inventorySlots;
    public final Map<String, Integer> potionEffects;

    public PotionAuras(@Nullable MechanicFactory factory, @NotNull ConfigurationSection section) {
        /* Use parent constructor */
        super(factory, section);

        // Read "slots" section
        inventorySlots = section.getStringList("slots");

        // Read "effects" section
        potionEffects = new HashMap<>();

        ConfigurationSection potionEffectsSection = section.getConfigurationSection("effects");
        if (potionEffectsSection != null) {
            for (String effectType : potionEffectsSection.getKeys(false)) {
                int amplifier = potionEffectsSection.getInt(effectType, 0);
                potionEffects.put(effectType, amplifier);
            }
        }
    }

}
