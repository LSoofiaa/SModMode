package me.sofdev.smodmode.commands;

import me.sofdev.smodmode.Main;
import me.sofdev.smodmode.api.CC;
import me.sofdev.smodmode.api.Command;
import me.sofdev.smodmode.api.CommandArgs;
import me.sofdev.smodmode.api.Title;
import me.sofdev.smodmode.managers.ModModeManager;
import me.sofdev.smodmode.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author SofDev w/Apreciada
 * @since 14/06/2022 - 02:52:27
 */

/**
* Trata de organizar un poco mas el codigo
* poniendo tags entre otras cosas, estare revisando y mejorando un poco.
*/

public class StaffCommands {

    // Configuration string
    static FileConfiguration config = Main.get().getConfig();

    //Main code
    private Main main;

    public StaffCommands(Main core) {
        this.main = core;
    }

    /*
    Commands:
    Vanish,
    StaffPlugin (1, 2, 3),
    StaffChat,
    Build,
    StaffRandomTeleport,
    Staff,
    CheckVanished,
    CheckModMode
     */

    @Command(name = "vanish", aliases = {"v"}, permission = "smodmode.staff", inGameOnly = true)
    public void vanish(CommandArgs args) {
        Player p = (Player) args.getSender();
        if (ModModeManager.IsOnModMode(p)) {
            if (Main.vanished.contains(p)) {
                int ItemSlot = config.getInt("StaffItems.Vanish.EnableVanish.Slot");
                ModModeManager.DisableVanish(p, ItemSlot);
            } else {
                int ItemSlot = config.getInt("StaffItems.Vanish.EnableVanish.Slot");
                ModModeManager.EnableVanish(p, ItemSlot);
            }
        } else {
            p.sendMessage(CC.translate(Main.get().getConfig().getString("config.staffmode")));
        }
    }

    @Command(name = "staffplugin", aliases = {"smodmode", "helpstaff"}, permission = "smodmode.staff", inGameOnly = true)
    public void staffplugin(CommandArgs args) {
        Player p = (Player) args.getSender();
        Messages.pluginInfo1(p);
    }

    @Command(name = "staffplugin.2", aliases = {"smodmode.2", "helpstaff.2"}, permission = "smodmode.staff", inGameOnly = true)
    public void staffplugin2(CommandArgs args) {
        Player p = (Player) args.getSender();
        Messages.pluginInfo2(p);
    }

    @Command(name = "staffplugin.3", aliases = {"smodmode.3", "helpstaff.3"}, permission = "smodmode.staff", inGameOnly = true)
    public void staffplugin3(CommandArgs args) {
        Player p = (Player) args.getSender();
        Messages.pluginInfo3(p);
    }

    @Command(name = "staffchat", aliases = {"sc"}, permission = "smodmode.staff")
    public void staffchat(CommandArgs args) {
        Player p = (Player) args.getSender();

        if (!Main.staffchat.contains(p)) {
            p.sendMessage(CC.translate(Main.get().getConfig().getString("Messages.StaffChat.Enabled")));
            Main.staffchat.add(p);
        } else {
            p.sendMessage(CC.translate(Main.get().getConfig().getString("Messages.StaffChat.Disabled")));
            Main.staffchat.remove(p);
        }
    }

    @Command(name = "build", permission = "smodmode.staff", inGameOnly = true)
    public void build(CommandArgs args) {
        String bmode = Main.get().getConfig().getString("config.buildmode");
        Player p = (Player) args.getSender();
        if (bmode.equals("true")) {
            if (Main.staff.contains(p)) {
                if (!Main.fix.contains(p)) {
                    Main.fix.add(p);
                    Messages.titleBuildOff(p);
                } else {
                    Main.fix.remove(p);
                    Messages.titleBuildOn(p);
                }
            } else {
                p.sendMessage(CC.translate(Main.get().getConfig().getString("config.staffmode")));
            }
        } else {
            Messages.noPermission(p);
        }
    }

    @Command(name = "staffrandomteleport", aliases = {"srtp"}, permission = "smodmode.staff", inGameOnly = true)
    public void rtp(CommandArgs args) {
        Player p = (Player) args.getSender();

        if (Main.staff.contains(p)) {
            ArrayList<Player> players = new ArrayList<Player>();
            for (Player e1 : Bukkit.getOnlinePlayers()) players.add(e1);
            Player rp = players.get(new Random().nextInt(players.size()));
            Title randomTP = new Title(ChatColor.AQUA + "Random Teleport", ChatColor.GREEN + rp.getName(), 40, 30, 40);
            randomTP.send(p);
            p.teleport(rp.getLocation());
        } else {
            p.sendMessage(CC.translate(Main.get().getConfig().getString("config.staffmode")));
        }
    }

    @Command(name = "staff",
            aliases = {"mod", "staffmode"},
            permission = "smodmode.staff",
            inGameOnly = true)
    public void staffMode(CommandArgs args) {
        Player p = (Player) args.getSender();

        if (!ModModeManager.IsOnModMode(p)) {
            ModModeManager.EnableModMode(p);
        } else {
            ModModeManager.DisableModMode(p);
        }
    }

    @Command(name = "checkmodmode", permission = "smodmode.admin", inGameOnly = true)
    public void checkModMode(CommandArgs args) {
        Player p = (Player) args.getSender();
        Player t = Bukkit.getPlayer(args.getArgs(0));

        if (args.length() != 0) {
            if (!t.isOnline()) {
                p.sendMessage(CC.translate(config.getString("config.notfound")));
            } else {
                if (ModModeManager.IsOnModMode(t)) {
                    p.sendMessage(CC.translate(config.getString("Messages.Check.ModModeTrue").replace("%player%", t.getName())));
                } else {
                    p.sendMessage(CC.translate(config.getString("Messages.Check.ModModeFalse").replace("%player%", t.getName())));
                }
            }
        } else {
            p.sendMessage(CC.translate("&cUsage: /checkmodmode <player>"));
        }
    }

    @Command(name = "checkvanished", permission = "smodmode.admin", inGameOnly = true)
    public void checkVanished(CommandArgs args) {
        Player p = (Player) args.getSender();
        Player t = Bukkit.getPlayer(args.getArgs(0));

        if (args.length() != 0) {
            if (!t.isOnline()) {
                p.sendMessage(CC.translate(config.getString("config.notfound")));
            } else {
                if (ModModeManager.IsVanished(t)) {
                    p.sendMessage(CC.translate(config.getString("Messages.Check.VanishedTrue").replace("%player%", t.getName())));
                } else {
                    p.sendMessage(CC.translate(config.getString("Messages.Check.VanishedFalse").replace("%player%", t.getName())));
                }
            }
        } else {
            p.sendMessage(CC.translate("&cUsage: /checkvanished <player>"));
        }
    }

    @Command(name = "staffplugin.reload", aliases = {"smodmode.reload"}, permission = "smodmode.admin")
    public void reload(CommandArgs args) {
        Player p = (Player) args.getSender();
        for (Player players : Bukkit.getServer().getOnlinePlayers()) {
            if (players.hasPermission("smodmode.staff") || players.hasPermission("smodmode.admin")) {
                if (ModModeManager.IsOnModMode(players)) {
                    players.sendMessage(CC.translate("&7[&DSModMode&7] &cStaffMode has been disabled due to a configuration reload."));
                    players.sendMessage(CC.translate("&7[&dSModMode&7] &cYou may not enable StaffMode for a moment."));
                    ModModeManager.DisableModMode(players);
                }
            }
        }
        p.sendMessage(CC.translate("&7[&dSModMode&7] &cConfiguration succesfully reloaded."));
        Main.get().reloadConfig();
    }
}
