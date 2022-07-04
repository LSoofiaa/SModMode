package me.sofdev.smodmode.listener;

import me.sofdev.smodmode.Main;
import me.sofdev.smodmode.api.CC;
import me.sofdev.smodmode.managers.ModModeManager;
import me.sofdev.smodmode.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;


/**
 *  Created by SofDev w/Apreciada
 *  14/06/2022 - 02:52:27
 */

public class StaffListener implements Listener {

    static FileConfiguration config = Main.get().getConfig();
    private final ArrayList<Player> vanished = new ArrayList<>();
    private final ArrayList<Player> respawn = new ArrayList<>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player joined = e.getPlayer();
        if (joined.hasPermission("smodmode.staff") || joined.hasPermission("smodmode.admin")) {
            joined.performCommand("staff");
            for (Player staffs : Bukkit.getOnlinePlayers()) {
                if (staffs.hasPermission("smodmode.staff") || staffs.hasPermission("smodmode.admin")) {
                    Messages.staffJoin(joined, staffs);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        Player leaved = e.getPlayer();
        if (leaved.hasPermission("smodmode.staff") || leaved.hasPermission("smodmode.admin")) {
            for (Player staffs : Bukkit.getOnlinePlayers()) {
                if (staffs.hasPermission("smodmode.staff") || staffs.hasPermission("smodmode.admin")) {
                    Messages.staffLeave(leaved, staffs);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent e) {
        Player leaved = e.getPlayer();
        if (leaved.hasPermission("smodmode.staff") || leaved.hasPermission("smodmode.admin")) {
            for (Player staffs : Bukkit.getOnlinePlayers()) {
                if (staffs.hasPermission("smodmode.staff") || staffs.hasPermission("smodmode.admin")) {
                    Messages.staffLeave(leaved, staffs);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getPlayer().hasPermission("smodmode.staff") || e.getPlayer().hasPermission("smodmode.admin")) {
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Player p = e.getPlayer();

                ItemStack stack = p.getItemInHand();
                if (Main.staff.contains(p)) {
                    int rtpSlot = config.getInt("StaffItems.RandomTeleport.Slot");
                    String rtpName = config.getString("StaffItems.RandomTeleport.Name");
                    boolean rtpGlow = config.getBoolean("StaffItems.RandomTeleport.Glow");
                    List<String> rtpLore = new ArrayList<>();
                    for (String l1 : config.getStringList("StaffItems.RandomTeleport.Lore")) {
                        rtpLore.add(CC.translate(l1.replace("%player%", p.getName())));
                    }
                    Material rtpMaterial = Material.valueOf(config.getString("StaffItems.RandomTeleport.Material"));

                    if (stack != null && stack.getType() == rtpMaterial
                            && stack.hasItemMeta()
                            && stack.getItemMeta().getDisplayName().equals(CC.translate(rtpName))
                            && stack.getItemMeta().getLore().equals(rtpLore)) {
                        p.performCommand("srtp");
                    }
                }

                if (Main.staff.contains(p)) {
                    String ItemName = config.getString("StaffItems.Vanish.DisableVanish.Name");
                    Material ItemMaterial = Material.valueOf(config.getString("StaffItems.Vanish.DisableVanish.Material"));
                    List<String> ItemLore = new ArrayList<>();
                    for (String lore : config.getStringList("StaffItems.Vanish.DisableVanish.Lore")) {
                        ItemLore.add(CC.translate(lore.replace("%player%", p.getName())));
                    }
                    if (stack != null && stack.getType() == ItemMaterial
                            && stack.hasItemMeta()
                            && stack.getItemMeta().getDisplayName().equals(CC.translate(ItemName))
                            && stack.getItemMeta().getLore().equals(ItemLore)) {
                        p.performCommand("vanish");
                    }
                }

                if (Main.staff.contains(p)) {
                    String ItemName = config.getString("StaffItems.Vanish.EnableVanish.Name");
                    Material ItemMaterial = Material.valueOf(config.getString("StaffItems.Vanish.EnableVanish.Material"));
                    List<String> ItemLore = new ArrayList<>();
                    for (String lore : config.getStringList("StaffItems.Vanish.EnableVanish.Lore")) {
                        ItemLore.add(CC.translate(lore.replace("%player%", p.getName())));
                    }

                    if (stack != null && stack.getType() == ItemMaterial
                            && stack.hasItemMeta()
                            && stack.getItemMeta().getDisplayName().equals(CC.translate(ItemName))
                            && stack.getItemMeta().getLore().equals(ItemLore)) {
                        p.performCommand("vanish");
                    }
                }
                if (Main.staff.contains(p)) {
                    String buildName = config.getString("StaffItems.BuildMode.Name");
                    boolean buildGlow = config.getBoolean("StaffItems.BuildMode.Glow");
                    Material buildMaterial = Material.valueOf(config.getString("StaffItems.BuildMode.Material"));
                    int buildSlot = config.getInt("StaffItems.BuildMode.Slot");
                    List<String> buildLore = new ArrayList<>();
                    for (String l2 : config.getStringList("StaffItems.BuildMode.Lore")) {
                        buildLore.add(CC.translate(l2.replace("%player%", p.getName())));
                    }

                    if (stack != null && stack.getType() == buildMaterial
                            && stack.hasItemMeta()
                            && stack.getItemMeta().getDisplayName().equals(CC.translate(buildName))
                            && stack.getItemMeta().getLore().equals(buildLore)) {
                        p.performCommand("build");
                    }
                }
            }
        }
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent e) {
        if (Main.fix.contains(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (Main.fix.contains(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryDrop(PlayerDropItemEvent e) {
        if (e.getItemDrop() != null) {
            if (Main.fix.contains(e.getPlayer())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInvPickup(PlayerPickupItemEvent e) {
        if (Main.fix.contains(e.getPlayer())) {
            e.setCancelled(true);
        }
    }


    @EventHandler
    public void onInvMove(InventoryClickEvent e) {
        if (e.getClickedInventory() != null) {
            if (Main.fix.contains((Player) e.getWhoClicked())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            if (Main.fix.contains((Player) e.getDamager())) {
                e.setCancelled(true);
                return;
            }

            if (Main.vanished.contains((Player) e.getDamager())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String format = Main.get().getConfig().getString("Messages.StaffChat.Format");

        if (Main.staffchat.contains(p)) {
            for (Player staffs : Bukkit.getServer().getOnlinePlayers()) {
                staffs.sendMessage(CC.translate(format
                        .replace("%player%", p.getName())
                        .replace("%message%", e.getMessage())));
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();

        if (Main.staff.contains(p)) {
            p.performCommand("staff");
            e.getDrops().clear();
            respawn.add(p);
            if (vanished.contains(p)) {
                vanished.remove(p);
            }
        }
    }

    @EventHandler
    public void gamemode(PlayerGameModeChangeEvent e) {
        Player p = e.getPlayer();
        if (ModModeManager.IsOnModMode(p)) {
            if (e.getNewGameMode() == GameMode.SURVIVAL ||
                    e.getNewGameMode() == GameMode.ADVENTURE ||
                    e.getNewGameMode() == GameMode.SPECTATOR) {
                p.sendMessage(CC.translate(Main.get().getConfig().getString("config.gamemode")));
                p.setGameMode(GameMode.CREATIVE);
            }
        }
    }

    @EventHandler
    public void setRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();

        if (respawn.contains(p)) {
            respawn.remove(p);
            p.performCommand("staff");
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        Player p = (Player) e.getEntity();

        if (Main.staff.contains(p)) {
            e.setCancelled(true);
        }
    }
}

