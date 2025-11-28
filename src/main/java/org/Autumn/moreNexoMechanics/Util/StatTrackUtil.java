package org.Autumn.moreNexoMechanics.Util;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class StatTrackUtil {

    private static final String NAMESPACE = "morenexo";

    private static NamespacedKey key(String statName) {
        return NamespacedKey.fromString(NAMESPACE + ":" + statName);
    }

    public static boolean has(ItemStack item, String statName) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        return pdc.has(key(statName), PersistentDataType.LONG);
    }

    public static long get(ItemStack item, String statName) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return 0L;

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        Long value = pdc.get(key(statName), PersistentDataType.LONG);
        return value != null ? value : 0L;
    }

    public static void set(ItemStack item, String statName, long value) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(key(statName), PersistentDataType.LONG, value);

        item.setItemMeta(meta);
    }

    public static long increment(ItemStack item, String statName) {
        long current = get(item, statName);
        long next = current + 1;
        set(item, statName, next);
        return next;
    }
}
