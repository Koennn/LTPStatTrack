package me.koenn.ltp;

import me.koenn.LTPSA.API.Data;
import me.koenn.LTPSA.API.DataInput;
import me.koenn.LTPSA.API.DataRequester;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class Main extends JavaPlugin implements Listener {

    public static Main main;

    public ArrayList<Player> rename = new ArrayList<>();
    public HashMap<Player, ItemStack> item = new HashMap<>();
    public HashMap<UUID, Integer> tokens = new HashMap<>();

    public void log(String msg) {
        main = this;
        String prefix = "[ltpstattrack] ";
        Bukkit.getLogger().log(Level.INFO, prefix + msg);
    }

    public void onEnable() {
        log("All credits for this plugin go to Koenn");
        getCommand("stattrack").setExecutor(new CommandHandler(this, this));
        Bukkit.getPluginManager().registerEvents(new Listeners(this, this), this);
        Bukkit.getPluginManager().registerEvents(this, this);
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "LTP");
        for(Player p : Bukkit.getServer().getOnlinePlayers()){
            DataRequester dr = new DataRequester("losttimepark", "tokens");
            Data data = dr.sendDataRequest(this, "uuid", p.getUniqueId().toString());
            try {
                tokens.put(p.getUniqueId(), Integer.parseInt(data.getData("tokens").toString()));
            } catch (Exception ex){
                DataInput di = new DataInput("losttimepark", "tokens");
                List<String> collumns = new ArrayList<>();
                collumns.add("uuid");
                collumns.add("tokens");
                List<String> values = new ArrayList<>();
                values.add(p.getUniqueId().toString());
                values.add("1");
                di.sendDataInput(this, values, collumns, "uuid");
                tokens.put(p.getUniqueId(), 1);
                log("tokens: " + tokens.get(p.getUniqueId()));
                return;
            }
            log("tokens: " + tokens.get(p.getUniqueId()));
        }
    }

    public void onDisable() {
        log("All credits for this plugin go to Koenn");
    }

    public void sendMessage(String msg, Player p) {
        String prefix = translateAlternateColorCodes('&', "&9StatTrack > ( &6");
        p.sendMessage(prefix + msg);
    }

    public static Main getInstance() {
        return main;
    }


    @EventHandler
    @SuppressWarnings("deprecation")
    public void onJoin(PlayerJoinEvent e) {
        DataRequester dr = new DataRequester("losttimepark", "tokens");
        Data data = dr.sendDataRequest(this, "uuid", e.getPlayer().getUniqueId().toString());
        try {
            tokens.put(e.getPlayer().getUniqueId(), Integer.parseInt(data.getData("tokens").toString()));
        } catch (Exception ex){
            DataInput di = new DataInput("losttimepark", "tokens");
            List<String> collumns = new ArrayList<>();
            collumns.add("uuid");
            collumns.add("tokens");
            List<String> values = new ArrayList<>();
            values.add(e.getPlayer().getUniqueId().toString());
            values.add("1");
            di.sendDataInput(this, values, collumns, "uuid");
            tokens.put(e.getPlayer().getUniqueId(), 1);
            log("tokens: " + tokens.get(e.getPlayer().getUniqueId()));
            return;
        }
        log("tokens: " + tokens.get(e.getPlayer().getUniqueId()));
    }

}
