package me.koenn.ltp;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.sql.PreparedStatement;

public class Listeners implements Listener{

    private Main main;
    private Stattrack st;
    private Plugin pl;

    @EventHandler
    public void onKill(PlayerDeathEvent e){
        Player p = e.getEntity().getKiller();
        if(p.getItemInHand().hasItemMeta()){
            if(p.getItemInHand().getItemMeta().getDisplayName().contains("? Stat Track ?")){
                if(p.getItemInHand().getItemMeta().hasLore()){
                    st.addKill(p);
                }
            }
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
        this.st = new Stattrack(main);
        this.pl = pl;
    }

}
