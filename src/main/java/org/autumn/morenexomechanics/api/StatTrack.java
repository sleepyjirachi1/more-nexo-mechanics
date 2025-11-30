package org.autumn.morenexomechanics.api;

import com.nexomc.nexo.mechanics.Mechanic;
import com.nexomc.nexo.mechanics.MechanicFactory;

import org.bukkit.configuration.ConfigurationSection;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class StatTrack extends Mechanic {

    public record StatInfo(int line, String format) {}

    private final Map<String, StatInfo> stats = new HashMap<>();

    /**
     * WARNING: Constructor logic is implicit, it is not designed for manual instantiation.
     * --
     * INSTEAD: StatTrack mechanic = MechanicFactories.getStatTrackFactory().getMechanic(...)
     *          Who's overloads allow construction with either ItemStack or NexoItem ID.
     */
    @ApiStatus.Internal
    public StatTrack(@Nullable MechanicFactory factory, @NotNull ConfigurationSection section) {
        super(factory, section);

        ConfigurationSection statsSection = section.getConfigurationSection("stats");
        if (statsSection == null) return;

        for (String statName : statsSection.getKeys(false)) {

            ConfigurationSection statCfg = statsSection.getConfigurationSection(statName);

            if (statCfg == null) {
                int legacyIndex = statsSection.getInt(statName);
                stats.put(statName.toLowerCase(), new StatInfo(legacyIndex, "%value%"));
                continue;
            }

            int line = statCfg.getInt("line");
            String format = statCfg.getString("format", "%value%");

            stats.put(statName.toLowerCase(), new StatInfo(line, format));
        }
    }

    public boolean tracks(String statName) {
        return stats.containsKey(statName);
    }

    public @Nullable StatInfo getStat(String statName) {
        return stats.get(statName);
    }
}
