package org.autumn.morenexomechanics.mechanics.listeners;

import org.autumn.morenexomechanics.api.StatTrack;
import org.autumn.morenexomechanics.mechanics.factories.StatTrackFactory;
import org.autumn.morenexomechanics.api.util.StatTrackUtil;

import com.nexomc.nexo.api.NexoItems;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public class StatTrackListener implements Listener {
    private static final String BLOCKS_MINED = "blocks_mined";
    private static final String PLAYERS_KILLED = "players_killed";

    private final Set<ItemStack> pendingUpdates = Collections.newSetFromMap(new WeakHashMap<>());

    private final JavaPlugin plugin;
    private final StatTrackFactory factory;

    public StatTrackListener(JavaPlugin plugin, StatTrackFactory factory) {
        this.plugin = plugin;
        this.factory = factory;
    }

    /**
     * Handles block break tracking.
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;

        Player player = event.getPlayer();
        tryUpdateStats(player, BLOCKS_MINED);
    }

    /**
     * Handles player kill tracking.
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.isCancelled()) return;

        Player killer = event.getEntity().getKiller();
        if (killer == null) return;

        tryUpdateStats(killer, PLAYERS_KILLED);
    }

    private void queueLoreUpdate(ItemStack item, Runnable rewrite) {
        if (pendingUpdates.add(item)) {
            Bukkit.getScheduler().runTask(plugin, () -> {
                rewrite.run();
                pendingUpdates.remove(item);
            });
        }
    }

    private void tryUpdateStats(Player player, String statName) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().isAir()) return;

        String id = NexoItems.idFromItem(item);
        if (id == null) return;

        StatTrack mechanic = factory.getMechanic(id);
        if (mechanic == null) return;

        if (!mechanic.tracks(statName)) return;

        if (!StatTrackUtil.has(item, statName)) {
            StatTrackUtil.set(item, statName, 0L);
        }

        long newValue = StatTrackUtil.increment(item, statName);

        queueLoreUpdate(item, () -> setLore(item, mechanic, statName, newValue));
    }

    private void setLore(ItemStack item, StatTrack mechanic, String statName, long value) {
        ItemMeta meta = item.getItemMeta();
        StatTrackUtil.setLore(meta, mechanic, statName, value);
        item.setItemMeta(meta);
    }
}
