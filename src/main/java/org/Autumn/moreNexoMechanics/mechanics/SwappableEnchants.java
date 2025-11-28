package org.Autumn.moreNexoMechanics.mechanics;

import com.nexomc.nexo.mechanics.Mechanic;
import com.nexomc.nexo.mechanics.MechanicFactory;
import org.bukkit.configuration.ConfigurationSection;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

public class SwappableEnchants extends Mechanic {

    public final int loreLineIdx;
    public final Map<String, Integer> enchantTypes;

    public SwappableEnchants(@Nullable MechanicFactory factory, @NotNull ConfigurationSection section) {
        super(factory, section);

        // Parse for 'lore-based'
        if (section.contains("lore-based")) { loreLineIdx = Math.max(0, section.getInt("lore-based", 1) - 1); }
        else { loreLineIdx = -1; }

        // Parse for 'enchants'
        enchantTypes = new LinkedHashMap<>();

        ConfigurationSection enchantTypesSection = section.getConfigurationSection("enchants");
        if (enchantTypesSection != null) {
            for (String enchantType : enchantTypesSection.getKeys(false)) {
                int level = enchantTypesSection.getInt(enchantType, 0);
                enchantTypes.put(enchantType, level);
            }
        }
    }

}
