package org.xiaomu.WorldProtection;

import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commander implements CommandExecutor {
    public void sendHelp(CommandSender commandSender) {
        commandSender.sendMessage("[ WP ] WorldProtection 插件帮助");
        commandSender.sendMessage("/WorldProtection info 查看保护列表");
        commandSender.sendMessage("/WorldProtection explosion 切换爆炸保护状态 (临时切换, 更改请更改配置文件)");
        commandSender.sendMessage("/WorldProtection version 显示插件版本信息");
        commandSender.sendMessage("/WorldProtection reload 重载配置文件");
    }
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sendHelp(commandSender);
            return true;
        }

        if (args[0].equalsIgnoreCase("help")) {
            sendHelp(commandSender);
            return true;
        }

        if (args[0].equalsIgnoreCase("version")) {
            commandSender.sendMessage("[ WP ] WorldProtection v1.2.1");
            commandSender.sendMessage("       another: xiaomu18");
            commandSender.sendMessage("       website: https://github.com/xiaomu18/WorldProtection");
            commandSender.sendMessage("       Compiled in July 2023");
            return true;
        }

        if (args[0].equalsIgnoreCase("info")) {
            String mode = WorldProtection.getInstance().getConfig().getString("ProtectWorlds.mode");
            List<String> ProtectWorlds = WorldProtection.getInstance().getConfig().getStringList("ProtectWorlds.list");

            if (ProtectWorlds.size() < 1) {
                commandSender.sendMessage("保护列表中无世界.");
            } else {
                commandSender.sendMessage("保护列表 (模式: " + mode + "): ");
                for (String world : ProtectWorlds) {
                    commandSender.sendMessage(world);
                }
            }

            commandSender.sendMessage("全局爆炸保护: " + Protector.ProtectExplosion);
            return true;
        }

        if (args[0].equalsIgnoreCase("explosion")) {
            if (Protector.ProtectExplosion) {
                Protector.ProtectExplosion = false;
                commandSender.sendMessage("[ WP ] 爆炸保护已临时关闭.");
            } else {
                Protector.ProtectExplosion = true;
                commandSender.sendMessage("[ WP ] 爆炸保护已临时开启.");
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            WorldProtection.getInstance().reloadConfig();
            Protector.ProtectExplosion = WorldProtection.getInstance().getConfig().getBoolean("AntiExplosion");
            commandSender.sendMessage("插件重载完毕");
            return true;
        }

        commandSender.sendMessage("未知命令.");
        return false;
    }
}
