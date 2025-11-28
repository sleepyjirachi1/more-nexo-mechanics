package org.Autumn.moreNexoMechanics.Util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class MsgUtil {

    /* Usage example: MessageColor.SUCCESS.color + ... */
    public enum MessageColor {
        SUCCESS(ChatColor.GREEN),
        WARN(ChatColor.YELLOW),
        ERROR(ChatColor.RED);

        public final ChatColor color;
        MessageColor(ChatColor color) { this.color = color; }
    }

    public static final String PREFIX = ChatColor.AQUA + "[MoreNexoMechanics] " + ChatColor.RESET;

    public static void msg(@NotNull CommandSender sender, String contents) {
        sender.sendMessage(PREFIX + contents);
    }
    public static boolean msgReturnsTrue(@NotNull CommandSender sender, String contents) {
        sender.sendMessage(PREFIX + contents);
        return true;
    }
    public static boolean msgReturnsFalse(@NotNull CommandSender sender, String contents) {
        sender.sendMessage(PREFIX + contents);
        return false;
    }
}
