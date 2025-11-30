package org.autumn.morenexomechanics.mechanics.runnables;

import org.autumn.morenexomechanics.mechanics.factories.PotionAurasFactory;
import org.autumn.morenexomechanics.mechanics.PotionAuras;

import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.utils.logs.Logs;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Locale;
import java.util.Map;
import java.util.HashMap;

public class PotionAurasTask implements Runnable {

    private final JavaPlugin plugin;
    private final PotionAurasFactory factory;

    public PotionAurasTask(JavaPlugin plugin, PotionAurasFactory factory) {
        this.plugin = plugin;
        this.factory = factory;
    }

    public void start() {
        // Schedule to run every 3.5 seconds
        Bukkit.getScheduler().runTaskTimer(plugin, this, 0L, 70L);
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Map<String, ItemStack> equippedSlots = getLabeledInventorySlots(player);

            for (Map.Entry<String, ItemStack> entry : equippedSlots.entrySet()) {
                String slotName = entry.getKey();
                ItemStack item = entry.getValue();

                if (item == null || item.getType().isAir()) continue;

                String id = NexoItems.idFromItem(item);
                if (id == null) continue;

                PotionAuras mechanic = factory.getMechanic(id);
                if (mechanic == null) {
                    continue;
                }

                if (!mechanic.inventorySlots.contains(slotName)) {
                    continue;
                }

                applyPotionEffects(player, mechanic.potionEffects);
            }
        }
    }

    // Maps logical slot names to actual equipped ItemStacks
    private Map<String, ItemStack> getLabeledInventorySlots(Player player) {
        Map<String, ItemStack> slots = new HashMap<>();
        slots.put("mainhand", player.getInventory().getItemInMainHand());
        slots.put("offhand", player.getInventory().getItemInOffHand());
        slots.put("helmet", player.getInventory().getHelmet());
        slots.put("chestplate", player.getInventory().getChestplate());
        slots.put("leggings", player.getInventory().getLeggings());
        slots.put("boots", player.getInventory().getBoots());
        return slots;
    }

    private void applyPotionEffects(Player player, Map<String, Integer> potionEffects) {
        for (Map.Entry<String, Integer> entry : potionEffects.entrySet()) {
            String name = entry.getKey().toLowerCase(Locale.ROOT);
            int amplifier = entry.getValue() - 1;

            NamespacedKey key = NamespacedKey.minecraft(name);

            PotionEffectType type = Bukkit.getRegistry(PotionEffectType.class).get(key);
            if (type == null) {
                Logs.logWarn("Unknown potion effect type: " + name);
                continue;
            }

            PotionEffect effect = new PotionEffect(
                    type,
                    360, // 18 seconds
                    amplifier,
                    true,
                    false,
                    true
            );

            player.addPotionEffect(effect);
        }
    }

}
