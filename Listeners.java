package me.koenn.ltp;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import me.koenn.LTPFL.LTPFactionLevels;
import mkremins.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.Plugin;

public class Listeners implements Listener{

    private Main main;
    private Stattrack st;
    private Plugin pl;

    @EventHandler
    public void onKill(PlayerDeathEvent e){
        try{
            Player p = e.getEntity().getKiller();
            LTPFactionLevels.getInstance().addLevels(p.getUniqueId(), 3.0);
            String weapon = e.getEntity().getKiller().getItemInHand().getItemMeta().getDisplayName();
            if(weapon == null){
                weapon = e.getEntity().getKiller().getItemInHand().getType().toString().toLowerCase();
            }
            String name = weapon;
            if(!e.getEntity().getKiller().getItemInHand().getItemMeta().hasDisplayName()){
                String rawName = weapon.replaceAll("_", " ").toLowerCase();
                String[] arr = rawName.split(" ");
                StringBuilder sb = new StringBuilder();
                for (String anArr : arr) {
                    sb.append(Character.toUpperCase(anArr.charAt(0))).append(anArr.substring(1)).append(" ");
                }
                name = sb.toString().trim();
            }
            for(Player x : Bukkit.getOnlinePlayers()){
                new FancyMessage(ChatColor.BLUE + "Slayer > " + ChatColor.GRAY + e.getEntity().getName() + " was slain by " + p.getName() + " using [" + ChatColor.WHITE + name + ChatColor.GRAY + "]")
                        .itemTooltip(e.getEntity().getKiller().getItemInHand()).send(x);
            }
            if(p.getItemInHand().hasItemMeta()){
                if(p.getItemInHand().getItemMeta().getDisplayName().contains("> Stat Track <")){
                    if(p.getItemInHand().getItemMeta().hasLore()){
                        ActionBarAPI.sendActionBar(p, ChatColor.GREEN + "" + ChatColor.BOLD + "+1 kill");
                        st.addKill(p);
                    }
                }
            }
        } catch (Exception ex){
            Bukkit.broadcastMessage(ChatColor.BLUE + "Slayer > " + ChatColor.WHITE + e.getDeathMessage());
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e){
        Player p = (Player) e.getPlayer();
        if(main.rename.contains(p)){
            p.setItemInHand(main.item.get(p));
            Bukkit.getScheduler().scheduleSyncDelayedTask(pl, () -> st.rename(p), 1);
            main.item.remove(p);
            main.rename.remove(p);
        }
    }

    public Listeners(Main main, Plugin pl){
        this.main = main;
        this.st = new Stattrack(main, pl);
        this.pl = pl;
    }

}
