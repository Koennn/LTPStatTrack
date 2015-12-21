package me.koenn.ltp;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class Stattrack {

    private Main main;

    public Stattrack(Main main){
        this.main = main;
    }

    public void apply(Player p){
        ItemStack i = p.getItemInHand();
        String type = i.getType().toString();
        if(type.contains("SWORD")){
            if(i.hasItemMeta() && i.getItemMeta().hasLore()){
                for(String l : i.getItemMeta().getLore()){
                    if(l.contains("Kills: ")){
                        main.sendMessage("This weapon already has stattrack", p);
                        return;
                    }
                }
            }
            ItemMeta im = i.getItemMeta();
            List<String> l = new ArrayList<>();
            if(im.hasLore()){
                l = im.getLore();
            }
            l.add(translateAlternateColorCodes('&', "&5Kills: 0"));
            im.setLore(l);
            i.setItemMeta(im);
            p.setItemInHand(i);
            main.sendMessage("Stattrack applied!", p);
            main.log("Player " + p.getName() + " applied stattrack");
            rename(p);
        }
    }

    public void rename(Player p) {
        ItemStack weapon = p.getItemInHand();
        main.item.put(p, weapon);
        AnvilGUI gui = new AnvilGUI(p, event -> {
            if(event.getSlot() == AnvilGUI.AnvilSlot.OUTPUT){
                event.setWillClose(true);
                event.setWillDestroy(true);
                String suffix = translateAlternateColorCodes('&', "&d&l > Stat Track <");
                ItemMeta im = weapon.getItemMeta();
                im.setDisplayName(ChatColor.GOLD + event.getName() + ChatColor.DARK_GRAY + " - " + suffix);
                weapon.setItemMeta(im);
                p.setItemInHand(weapon);
                main.item.remove(p);
                main.rename.remove(p);
            } else {
                event.setWillClose(false);
                event.setWillDestroy(false);
            }
        });
        gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, p.getItemInHand());
        p.setItemInHand(new ItemStack(Material.AIR));
        gui.open();
        main.sendMessage("Please rename your weapon", p);
        main.rename.add(p);
    }

    public void addKill(Player p) {
        ItemStack i = p.getItemInHand();
        ItemMeta im = i.getItemMeta();
        Integer amount = 0;
        for(String s : p.getItemInHand().getItemMeta().getLore()) {
            if(s.contains("Kills: ")){
                amount = Integer.parseInt(s.split("\\s+")[1]);
                amount++;
            }
        }
        List<String> l = im.getLore();
        l.remove(translateAlternateColorCodes('&', "&5Kills: " + (amount - 1)));
        l.add(translateAlternateColorCodes('&', "&5Kills: " + amount));
        im.setLore(l);
        i.setItemMeta(im);
        p.setItemInHand(i);
    }
}