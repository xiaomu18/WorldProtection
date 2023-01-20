package org.xiaomu.WorldProtection;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import java.util.List;

public class Protector implements Listener {
    public static boolean ProtectExplosion = WorldProtection.getInstance().getConfig().getBoolean("AntiExplosion");
    public static void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }

    public static String getColor(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static boolean IsPlayerNeedManage(Player player, String worldName) {
        return !player.hasPermission("WorldProtection.bypass." + worldName);
    }
    public static boolean IsWorldNeedProtection(String worldName) {
        String mode = WorldProtection.getInstance().getConfig().getString("ProtectWorlds.mode");
        if (mode == null) return false;

        switch (mode)
        {
            case "need":
                return WorldProtection.getInstance().getConfig().getStringList("ProtectWorlds.list").contains(worldName);
            case "not":
                return (!WorldProtection.getInstance().getConfig().getStringList("ProtectWorlds.list").contains(worldName));
            case "all":
                return true;
            default:
                return false;
        }
    }

    public boolean NeedCancelled(Player player) {
        String worldName = player.getWorld().getName();

        if (IsWorldNeedProtection(worldName)) {
            return IsPlayerNeedManage(player, worldName);
        } else return false;
    }

    public String getPromptMessage(String worldName) {
        String path = "CustomMessage." + worldName;

        if (WorldProtection.getInstance().getConfig().contains(path)) {
            return getColor(WorldProtection.getInstance().getConfig().getString(path));
        } else {
            return getColor(WorldProtection.getInstance().getConfig().getString("DefaultMessage"));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void BreakBlock(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (NeedCancelled(player)) {
            event.setCancelled(true);
            sendActionBar(player, getPromptMessage(player.getWorld().getName()));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlaceBlock(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (NeedCancelled(player)) {
            event.setCancelled(true);
            sendActionBar(player, getPromptMessage(player.getWorld().getName()));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void Fill(PlayerBucketFillEvent event) {
        Player player = event.getPlayer();
        if (NeedCancelled(player)) {
            event.setCancelled(true);
            sendActionBar(player, getPromptMessage(player.getWorld().getName()));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void Empty(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        if (NeedCancelled(player)) {
            event.setCancelled(true);
            sendActionBar(player, getPromptMessage(player.getWorld().getName()));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent explodeEvent) {
        if (ProtectExplosion) {
            List<Block> blocks = explodeEvent.blockList();
            blocks.clear();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (WorldProtection.getInstance().getConfig().getBoolean("AntiWorldFly.enable") && WorldProtection.getInstance().getConfig().getStringList("AntiWorldFly.worlds").contains(player.getWorld().getName())) {
            if (IsPlayerNeedManage(player, player.getWorld().getName())) {
                if (event.getPlayer().getAllowFlight()) {
                    event.getPlayer().setAllowFlight(false);
                    player.sendMessage(getColor(WorldProtection.getInstance().getConfig().getString("AntiWorldFly.message")));
                }
            }
        }
    }
}
