package org.autumn.morenexomechanics.mechanics.listeners;

import org.autumn.morenexomechanics.mechanics.factories.SwappableEnchantsFactory;
import org.autumn.morenexomechanics.mechanics.SwappableEnchants;
import org.autumn.morenexomechanics.Util.EnchantUtil.Lookup;
import org.autumn.morenexomechanics.Util.EnchantUtil.KeyIter;
import org.autumn.morenexomechanics.Util.EnchantUtil.Policy;
import org.autumn.morenexomechanics.Util.EnchantUtil.Roman;
import org.autumn.morenexomechanics.Util.MsgUtil;

import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.utils.logs.Logs;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.enchantments.Enchantment;

import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.UUID;

public class SwappableEnchantsListener implements Listener {

    private final JavaPlugin plugin;
    private final SwappableEnchantsFactory factory;
    private final Set<UUID> clickedThisTick = new HashSet<>();
    private static final int COOLDOWN_TICKS = 20; // 1 second

    public SwappableEnchantsListener(JavaPlugin plugin, SwappableEnchantsFactory factory) {
        this.plugin= plugin;
        this.factory = factory;
    }

    @EventHandler
    public void onItemInteract(PlayerInteractEvent event) {
        if (!isValidTrigger(event)) return;

        Player player = event.getPlayer();
        if (!canTrigger(player)) return;

        ItemStack item = player.getInventory().getItemInMainHand();

        SwappableEnchants mechanic = getValidMechanic(item);
        if (mechanic == null) return;

        trySwapEnchantments(player, item, mechanic);
    }

    private boolean isValidTrigger(PlayerInteractEvent event) {
        return event.getHand() == EquipmentSlot.HAND &&
                (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) &&
                event.getPlayer().isSneaking();
    }

    private boolean canTrigger(Player player) {
        UUID uuid = player.getUniqueId();
        if (clickedThisTick.contains(uuid)) return false;
        clickedThisTick.add(uuid);
        Bukkit.getScheduler().runTaskLater(plugin, () -> clickedThisTick.remove(uuid), COOLDOWN_TICKS);
        return true;
    }

    private @Nullable SwappableEnchants getValidMechanic(ItemStack item) {
        if (item.getType().isAir()) return null;

        String id = NexoItems.idFromItem(item);
        if (id == null) return null;

        SwappableEnchants mechanic = factory.getMechanic(id);
        if (mechanic == null || mechanic.enchantTypes.size() < 2) return null;

        return mechanic;
    }

    private void trySwapEnchantments(Player player, ItemStack item, SwappableEnchants mechanic) {
        ItemStack clone = item.clone();
        Map<String, Integer> enchants = mechanic.enchantTypes;
        List<String> keys = new ArrayList<>(enchants.keySet());

        Enchantment current = Lookup.getCurrentMatchingEnchantment(clone, enchants.keySet());
        if (current == null) return;

        String currentKey = Lookup.getKeyForEnchantment(current, enchants.keySet());

        String nextKey = KeyIter.keysStepCyclic(keys, currentKey);
        if (nextKey == null) return;

        Enchantment nextEnchant = Lookup.getEnchantmentByName(nextKey);
        if (nextEnchant == null) return;

        int level = enchants.getOrDefault(nextKey, 1);
        clone.removeEnchantment(current);

        switch (Policy.getSafetyLevel(nextEnchant, level)) {
            case Policy.SafetyLevel.SAFE:
                clone.addEnchantment(nextEnchant, level);
                break;
            case Policy.SafetyLevel.UNSAFE_VALID:
                clone.addUnsafeEnchantment(nextEnchant, level);
                break;
            default:
                Logs.logError(MsgUtil.MessageColor.WARN + ": Illegal request 'swap' to " + KeyIter.formatKey(nextKey) + " at level " + Roman.toRoman(level) + " was rejected");
                return;
        }

        if (mechanic.loreLineIdx != -1) updateLore(clone, nextKey, level, mechanic.loreLineIdx);

        player.getInventory().setItemInMainHand(clone);
        MsgUtil.msg(player, ChatColor.GREEN + "Swapped to " + KeyIter.formatKey(nextKey));
    }

    private void updateLore(ItemStack item, String key, int level, int lineIdx) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasLore()) return;

        List<String> lore = meta.getLore();
        if (lore == null || lineIdx >= lore.size()) return;

        if (Policy.shouldDisplayLevel(key)) {
            lore.set(lineIdx, ChatColor.GOLD + "   " + KeyIter.formatKey(key) + " " + Roman.toRoman(level));
        } else {
            lore.set(lineIdx, ChatColor.GOLD + "   " + KeyIter.formatKey(key));
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
    }

}
