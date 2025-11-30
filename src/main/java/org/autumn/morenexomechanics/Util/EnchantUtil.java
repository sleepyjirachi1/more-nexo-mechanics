package org.autumn.morenexomechanics.Util;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import lombok.experimental.UtilityClass;

import java.util.Set;
import java.util.List;

@UtilityClass
public class EnchantUtil {

    @UtilityClass
    public static class Lookup {

        public static Enchantment getCurrentMatchingEnchantment(ItemStack item, Set<String> enchantNames) {
            if (enchantNames == null) return null;

            for (String enchantName : enchantNames) {
                Enchantment trueEnchantment = getEnchantmentByName(enchantName);
                if (trueEnchantment == null) continue;
                if (item.containsEnchantment(trueEnchantment)) return trueEnchantment;
            }

            return null;
        }

        public static String getKeyForEnchantment(Enchantment target, Set<String> keys) {
            for (String key : keys) {
                Enchantment enchantment = getEnchantmentByName(key);
                if (enchantment != null && enchantment.equals(target)) return key;
            }

            return null;
        }

        public static Enchantment getEnchantmentByName(String enchantName) {
            try {
                NamespacedKey key = NamespacedKey.minecraft(enchantName.toLowerCase());
                return Registry.ENCHANTMENT.get(key);
            } catch (Exception err) { return null; }
        }

    }

    @UtilityClass
    public static class KeyIter {

        public static String keysStepCyclic(List<String> keys, String currentKey) {
            int idx = keys.indexOf(currentKey);
            int next = (idx + 1 + keys.size()) % keys.size();
            return keys.get(next);
        }

        public static String formatKey(String key) {
            String[] parts = key.split("_");
            StringBuilder builder = new StringBuilder();

            for (String part : parts) {
                if (part.isEmpty()) continue;

                builder.append(Character.toUpperCase(part.charAt(0)))
                        .append(part.substring(1).toLowerCase())
                        .append(" ");
            }

            return builder.toString().trim();
        }

    }

    @UtilityClass
    public static class Policy {

        private static final Set<Enchantment> DISPLAY_LEVEL_ENCHANTS = Set.of(
                Enchantment.FORTUNE, Enchantment.SHARPNESS, Enchantment.EFFICIENCY,
                Enchantment.POWER, Enchantment.PROTECTION, Enchantment.UNBREAKING,
                Enchantment.FIRE_PROTECTION, Enchantment.FEATHER_FALLING, Enchantment.BLAST_PROTECTION,
                Enchantment.PROJECTILE_PROTECTION, Enchantment.RESPIRATION, Enchantment.AQUA_AFFINITY,
                Enchantment.LOOTING, Enchantment.DEPTH_STRIDER, Enchantment.KNOCKBACK,
                Enchantment.FIRE_ASPECT, Enchantment.SMITE, Enchantment.BANE_OF_ARTHROPODS
        );

        public static boolean shouldDisplayLevel(Enchantment enchant) {
            return DISPLAY_LEVEL_ENCHANTS.contains(enchant);
        }

        public static boolean shouldDisplayLevel(String enchantName) {
            Enchantment enchant = Lookup.getEnchantmentByName(enchantName);
            return shouldDisplayLevel(enchant);
        }

        public enum SafetyLevel {SAFE, UNSAFE_VALID, ILLEGAL}

        public static SafetyLevel getSafetyLevel(Enchantment enchant, int level) {
            if (0 < level && level <= enchant.getMaxLevel()) return SafetyLevel.SAFE;
            else if (shouldDisplayLevel(enchant)) return SafetyLevel.UNSAFE_VALID;
            else return SafetyLevel.ILLEGAL;
        }

    }

    @UtilityClass
    public static class Roman {

        public static String toRoman(int number) {
            if (number <= 0) return "?";

            int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
            String[] numerals = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

            StringBuilder numeral = new StringBuilder();

            for (int i = 0; i < values.length; i++) {
                while (number >= values[i]) {
                    number -= values[i];
                    numeral.append(numerals[i]);
                }
            }

            return numeral.toString();
        }

    }

}
