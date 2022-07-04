package me.sofdev.smodmode.managers;

import me.sofdev.smodmode.Main;
import me.sofdev.smodmode.api.CC;
import me.sofdev.smodmode.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;


/**
 *  Created by SofDev w/Apreciada
 *  14/06/2022 - 02:52:27
 */
public class ModModeManager {

    static FileConfiguration config = Main.get().getConfig();

    /*
    StaffMode-Items List:
    RandomTeleport,
    WorldEdit-Wand,
    BuildMode-Item,
    VanishOff-Item (Vanish is automatic updated when StaffMode gets enabled)
     */
    public static void GiveModModeItems(Player p) {

        PlayerInventory pi = p.getInventory();

        int rtpSlot = config.getInt("StaffItems.RandomTeleport.Slot");
        String rtpName = config.getString("StaffItems.RandomTeleport.Name");
        boolean rtpGlow = config.getBoolean("StaffItems.RandomTeleport.Glow");
        List<String> rtpLore = new ArrayList<>();
        for (String l1 : config.getStringList("StaffItems.RandomTeleport.Lore")) {
            rtpLore.add(CC.translate(l1.replace("%player%", p.getName())));
        }
        Material rtpMaterial = Material.valueOf(config.getString("StaffItems.RandomTeleport.Material"));

        ItemStack randomteleport = new ItemStack(rtpMaterial);
        ItemMeta rtpMeta = randomteleport.getItemMeta();
        rtpMeta.setLore(rtpLore);
        if (rtpGlow) {
            rtpMeta.addEnchant(Enchantment.ARROW_DAMAGE, 5, true);
            rtpMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        rtpMeta.setDisplayName(CC.translate(rtpName));
        randomteleport.setItemMeta(rtpMeta);

        int wandSlot = config.getInt("StaffItems.Wand.Slot");
        boolean wandEnabled = config.getBoolean("StaffItems.Wand.Enabled");
        @SuppressWarnings("deprecation")
        ItemStack wand = new ItemStack(271, 1);
        ItemMeta wandMeta = wand.getItemMeta();
        wand.setItemMeta(wandMeta);

        String buildName = config.getString("StaffItems.BuildMode.Name");
        boolean buildGlow = config.getBoolean("StaffItems.BuildMode.Glow");
        Material buildMaterial = Material.valueOf(config.getString("StaffItems.BuildMode.Material"));
        int buildSlot = config.getInt("StaffItems.BuildMode.Slot");
        List<String> buildLore = new ArrayList<>();
        for (String l2 : config.getStringList("StaffItems.BuildMode.Lore")) {
            buildLore.add(CC.translate(l2.replace("%player%", p.getName())));
        }
        ItemStack bMode = new ItemStack(buildMaterial);
        ItemMeta bModeMeta = bMode.getItemMeta();
        if (buildGlow) {
            bModeMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
            bModeMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        bModeMeta.setLore(buildLore);
        bModeMeta.setDisplayName(CC.translate(buildName));
        bMode.setItemMeta(bModeMeta);

        String vanishOffName = config.getString("StaffItems.Vanish.EnableVanish.Name");
        boolean vanishOffGlow = config.getBoolean("StaffItems.Vanish.EnableVanish.Glow");
        Material vanishOffMaterial = Material.valueOf(config.getString("StaffItems.Vanish.EnableVanish.Material"));
        int vanishSlot = config.getInt("StaffItems.Vanish.EnableVanish.Slot");
        List<String> vanishOffLore = new ArrayList<>();
        for (String l3 : config.getStringList("StaffItems.Vanish.EnableVanish.Lore")) {
            vanishOffLore.add(CC.translate(l3.replace("%player%", p.getName())));
        }
        ItemStack vanishO = new ItemStack(vanishOffMaterial);
        ItemMeta vanishOMeta = vanishO.getItemMeta();
        vanishOMeta.setLore(vanishOffLore);
        if (vanishOffGlow) {
            vanishOMeta.addEnchant(Enchantment.DAMAGE_UNDEAD, 1, true);
            vanishOMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        vanishOMeta.setDisplayName(CC.translate(vanishOffName));
        vanishO.setItemMeta(vanishOMeta);

        if (wandEnabled) {
            pi.setItem(wandSlot, wand);
        }
        pi.setItem(rtpSlot, randomteleport); // compass //
        pi.setItem(buildSlot, bMode); // build mode //
        pi.setItem(vanishSlot, vanishO); // vanish //

    }

    public static void RemoveModModeItems(Player p) {
        PlayerInventory pi = p.getInventory();
        pi.clear();
    }

    public static void EnableModMode(Player p) {
        PlayerInventory pi = p.getInventory();
        Main.staff.add(p);

        for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
            pl.hidePlayer(p);
        }
        Main.vanished.add(p);

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.get(), new Runnable() {
            public void run() {
                if (Main.staff.contains(p)) {
                    Main.pArmor.put(p.getUniqueId(), pi.getArmorContents());
                    Main.pItems.put(p.getUniqueId(), pi.getContents());
                    Messages.titleStaffOn(p);
                    pi.setArmorContents(null);
                    pi.clear();
                    p.setGameMode(GameMode.CREATIVE);
                    Main.fix.add(p);
                    GiveModModeItems(p);
                }
            }
        }, 0L);
    }

    public static void DisableModMode(Player p) {
        PlayerInventory pi = p.getInventory();
        RemoveModModeItems(p);
        Main.staff.remove(p);
        pi.setArmorContents(null);
        p.setGameMode(GameMode.SURVIVAL);
        Messages.titleStaffOFF(p);

        for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
            pl.showPlayer(p);
        }
        Main.vanished.remove(p);
        Main.fix.remove(p);

        if (Main.pArmor.containsKey(p.getUniqueId())) {
            pi.setArmorContents((ItemStack[]) Main.pArmor.get(p.getUniqueId()));
        }
        if (Main.pItems.containsKey(p.getUniqueId())) {
            pi.setContents((ItemStack[]) Main.pItems.get(p.getUniqueId()));
        }
    }

    public static void EnableVanish(Player p, int Slot) {
        String ItemName = config.getString("StaffItems.Vanish.EnableVanish.Name");
        Material ItemMaterial = Material.valueOf(config.getString("StaffItems.Vanish.EnableVanish.Material"));
        List<String> ItemLore = new ArrayList<>();
        for (String lore : config.getStringList("StaffItems.Vanish.EnableVanish.Lore")) {
            ItemLore.add(CC.translate(lore.replace("%player%", p.getName())));
        }

        ItemStack Item = new ItemStack(ItemMaterial);
        ItemMeta itemMeta = Item.getItemMeta();
        itemMeta.setDisplayName(CC.translate(ItemName));
        itemMeta.setLore(ItemLore);
        if (config.getBoolean("StaffItems.Vanish.EnableVanish.Glow")) {
            itemMeta.addEnchant(Enchantment.FIRE_ASPECT, 2, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        Item.setItemMeta(itemMeta);

        for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
            if (!pl.hasPermission("smodmode.staff") || !pl.hasPermission("smodmode.admin")) {
                pl.hidePlayer(p);
            }
        }
        Messages.titleVanishOn(p);
        Main.vanished.add(p);
        p.getInventory().setItem(Slot, Item);
    }

    public static void DisableVanish(Player p, int Slot) {
        String ItemName = config.getString("StaffItems.Vanish.DisableVanish.Name");
        Material ItemMaterial = Material.valueOf(config.getString("StaffItems.Vanish.DisableVanish.Material"));
        List<String> ItemLore = new ArrayList<>();
        for (String lore : config.getStringList("StaffItems.Vanish.DisableVanish.Lore")) {
            ItemLore.add(CC.translate(lore.replace("%player%", p.getName())));
        }

        ItemStack Item = new ItemStack(ItemMaterial);
        ItemMeta itemMeta = Item.getItemMeta();
        itemMeta.setDisplayName(CC.translate(ItemName));
        itemMeta.setLore(ItemLore);
        if (config.getBoolean("StaffItems.Vanish.DisableVanish.Glow")) {
            itemMeta.addEnchant(Enchantment.FIRE_ASPECT, 2, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        Item.setItemMeta(itemMeta);

        for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
            pl.showPlayer(p);
        }
        Messages.titleVanishOff(p);
        Main.vanished.remove(p);
        Material OldMaterial = Material.valueOf(config.getString("StaffItems.Vanish.EnableVanish.Material"));
        p.getInventory().remove(OldMaterial);
        p.getInventory().setItem(Slot, Item);
    }

    public static boolean IsOnModMode(Player p) {
        return Main.staff.contains(p);
    }

    public static boolean IsVanished(Player p) {
        return Main.vanished.contains(p);
    }
}
