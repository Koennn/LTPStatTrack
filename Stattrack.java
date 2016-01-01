package me.koenn.ltp;

import me.koenn.LTPSA.API.DataInput;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class Stattrack {

    private Main main;
    private Plugin pl;

    public Stattrack(Main main, Plugin pl){
        this.main = main;
        this.pl = pl;
    }

    @SuppressWarnings("deprecation")
    public void apply(Player p){
        if(main.tokens.get(p.getUniqueId()) < 1){
            main.sendMessage("You do not have enough tokens to do this!", p);
            return;
        }
        //Bukkit.getScheduler().scheduleAsyncDelayedTask(pl, () -> Main.addTokens(p.getUniqueId().toString(), -1), 1);
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
            Integer tokens = main.tokens.get(p.getUniqueId());
            main.tokens.put(p.getUniqueId(), (tokens - 1));
            DataInput di = new DataInput("losttimepark", "tokens");
            List<String> collumns = new ArrayList<>();
            collumns.add("uuid");
            collumns.add("tokens");
            List<String> values = new ArrayList<>();
            values.add(p.getUniqueId().toString());
            values.add(String.valueOf(1));
            di.sendDataInput(pl, values, collumns, "uuid");
            main.sendMessage("1 token(s) got removed from your account!", p);
            main.sendMessage("Stattrack applied!", p);
            main.log("Player " + p.getName() + " applied stattrack");
            ItemMeta im = i.getItemMeta();
            List<String> l;
            if(im.hasLore()){
                l = im.getLore();
                l.set(l.size()-1, translateAlternateColorCodes('&', "&bKills: 0"));
            } else {
                main.sendMessage("Invalid tool, please report to a staff member.", p);
                return;
            }
            im.setLore(l);
            i.setItemMeta(im);
            p.setItemInHand(i);
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
                String suffix = translateAlternateColorCodes('&', "&d&l> Stat Track <");
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
        gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, weapon);
        gui.setSlot(AnvilGUI.AnvilSlot.OUTPUT, weapon);
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
        l.remove(translateAlternateColorCodes('&', "&bKills: " + (amount - 1)));
        l.add(translateAlternateColorCodes('&', "&bKills: " + amount));
        im.setLore(l);
        i.setItemMeta(im);
        p.setItemInHand(i);
    }
}