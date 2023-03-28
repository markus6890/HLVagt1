package com.gmail.markushygedombrowski.sign;

import com.gmail.markushygedombrowski.HLvagt;
import com.gmail.markushygedombrowski.buff.BuffGui;
import com.gmail.markushygedombrowski.model.Settings;
import com.gmail.markushygedombrowski.utils.Logger;
import com.gmail.markushygedombrowski.utils.VagtUtils;
import com.gmail.markushygedombrowski.cooldown.VagtCooldown;
import com.gmail.markushygedombrowski.utils.VagtWorldGuardUtils;
import com.gmail.markushygedombrowski.vagtShop.VagtShop;
import com.gmail.markushygedombrowski.vagtShop.VagtShopEnchant;
import com.gmail.markushygedombrowski.warp.VagtSpawnManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class VagtSigns implements Listener {
    private Settings settings;
    private HLvagt plugin;
    private int pay;
    private VagtSpawnManager vagtSpawnManager;
    private RepairGUI repairGUI;
    private VagtShop vagtShop;
    private VagtShopEnchant vagtShopEnchant;
    private BuffGui buffGui;
    private Logger logger;

    public VagtSigns(VagtSpawnManager vagtSpawnManager, Settings settings, HLvagt plugin, RepairGUI repairGUI, VagtShop vagtShop, VagtShopEnchant vagtShopEnchant, BuffGui buffGui, Logger logger) {
        this.vagtSpawnManager = vagtSpawnManager;
        this.settings = settings;
        this.plugin = plugin;
        this.repairGUI = repairGUI;
        this.vagtShop = vagtShop;
        this.vagtShopEnchant = vagtShopEnchant;
        this.buffGui = buffGui;
        this.logger = logger;
    }


    @EventHandler
    public void onPlayerClickSign(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getType() == Material.SIGN || event.getClickedBlock().getType() == Material.SIGN_POST || event.getClickedBlock().getType() == Material.WALL_SIGN) {
                Sign sign = (Sign) event.getClickedBlock().getState();
                if (sign.getLine(0).equalsIgnoreCase("§8===============") && sign.getLine(1).equalsIgnoreCase("§cKøb dig ud") && sign.getLine(2).equalsIgnoreCase("§cFor §764 §cCobble") && sign.getLine(3).equalsIgnoreCase("§8===============")) {
                    ArrayList<ItemStack> items = new ArrayList<>();
                    items.add(new ItemStack(Material.COBBLESTONE, 64));
                    items.add(new ItemStack(Material.STONE, 64));


                    String rank;
                    if (VagtUtils.removeItems(p, items)) {
                        if (p.hasPermission("direktør")) {
                            rank = "§4§lDirektøren ";
                        } else if (p.hasPermission("inspektør")) {
                            rank = "§2§lInspektøren ";
                        } else if (p.hasPermission("viceinspektør")) {
                            rank = "§2§lVice-Inspektøren ";
                        } else if (p.hasPermission("officer")) {
                            rank = "§6§lOfficeren ";
                        } else {
                            rank = "§6§lVagten ";
                        }
                        Bukkit.broadcastMessage("§7§l----------§c§lVAGT§7§l----------");
                        Bukkit.broadcastMessage(rank + "§c" + p.getName());
                        Bukkit.broadcastMessage("§7Har købt sig ud af§a§l spasser minen");
                        Bukkit.broadcastMessage("§7§l----------§c§lVAGT§7§l----------");
                        p.teleport(vagtSpawnManager.getWarpInfo("vagtc").getLocation());
                        p.getInventory().clear();
                    } else {
                        p.sendMessage("§cDu har ikke nok!");
                        p.sendMessage("§cDu skal bruge 64 stone og 64 cobble");

                    }
                    return;

                }

                if (sign.getLine(0).equalsIgnoreCase("§8===============") && sign.getLine(1).equalsIgnoreCase("§cKlik for") && sign.getLine(2).equalsIgnoreCase("§4Heal") && sign.getLine(3).equalsIgnoreCase("§8===============")) {
                    if (VagtUtils.notHasPermission(p, "vagtbuff")) return;
                    p.setHealth(20);
                    p.setFoodLevel(20);
                    p.setSaturation(10);
                    plugin.econ.withdrawPlayer(p, 500);
                    p.sendMessage("§aDu er blevet §cHealet");
                    return;

                }

                if (sign.getLine(0).equalsIgnoreCase("§8===============") && sign.getLine(1).equalsIgnoreCase("§cKlik for") && sign.getLine(2).equalsIgnoreCase("§4Buff") && sign.getLine(3).equalsIgnoreCase("§8===============")) {
                    if (VagtUtils.notHasPermission(p, "vagtbuff")) return;

                    buffGui.create(p);
                    return;

                }

                if (sign.getLine(0).equalsIgnoreCase("§e§lRepair") && sign.getLine(1).equalsIgnoreCase("Klik for at") && sign.getLine(2).equalsIgnoreCase("Repair dine ting") && sign.getLine(3).equalsIgnoreCase("§e===============")) {
                    if (VagtUtils.notHasPermission(p, "vagtRepair")) return;
                    logger.formatMessage(p.getName() + " has opened repair sign", "REPAIRSIGN: ");
                    repairGUI.create(p);
                    return;
                }

                if (sign.getLine(0).equalsIgnoreCase("§8===============") && sign.getLine(1).equalsIgnoreCase("§cKlik her for") && sign.getLine(2).equalsIgnoreCase("§cGratis pickaxe") && sign.getLine(3).equalsIgnoreCase("§8===============")) {
                    if (VagtUtils.notHasPermission(p, "vagt")) return;

                    if (VagtCooldown.isCooling(p.getName(), "sppickaxe")) {
                        p.sendMessage("§7Du kan tage en pickaxe om " + ChatColor.AQUA + VagtCooldown.getRemaining(p.getName(), "sppickaxe") + " Minuter");
                        return;
                    }
                    ItemStack itemStack = new ItemStack(Material.WOOD_PICKAXE);
                    itemStack.addEnchantment(Enchantment.SILK_TOUCH, 1);
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.setDisplayName("§cGratis pickaxe!");
                    itemStack.setItemMeta(itemMeta);
                    p.getInventory().addItem(itemStack);
                    VagtCooldown.add(p.getName(), "sppickaxe", 400, System.currentTimeMillis());
                    return;

                }

                if (sign.getLine(0).equalsIgnoreCase("§1===============") && sign.getLine(1).equalsIgnoreCase("§cVagt Shop") && sign.getLine(2).equalsIgnoreCase("§8Klik her") && sign.getLine(3).equalsIgnoreCase("§1===============")) {
                    if (VagtUtils.notHasPermission(p, "vagt")) return;

                    ItemStack hand = p.getItemInHand();
                    if (!(hand.getType() == Material.AIR)) {
                        if (hand.getItemMeta().getDisplayName().contains("§cC") || hand.getItemMeta().getDisplayName().contains("§bB") || hand.getItemMeta().getDisplayName().contains("§aA")) {
                            vagtShopEnchant.enchantItem(p, hand);
                            return;
                        }
                    }
                    if (VagtWorldGuardUtils.isLocInRegion(p.getLocation(), "A")) {
                        vagtShop.vagtShop(p, "§aA");
                    } else if (VagtWorldGuardUtils.isLocInRegion(p.getLocation(), "B")) {
                        vagtShop.vagtShop(p, "§bB");
                    } else {
                        vagtShop.vagtShop(p, "§cC");
                    }


                }
            }
        }
    }
}
