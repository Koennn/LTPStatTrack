package me.koenn.ltp;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class Main extends JavaPlugin implements Listener {

    public static Main main;
    private static Connection connection = null;

    public ArrayList<Player> rename = new ArrayList<>();
    public HashMap<Player, ItemStack> item = new HashMap<>();

    public void log(String msg){
        main = this;
        String prefix = "[ltpstattrack] ";
        Bukkit.getLogger().log(Level.INFO, prefix + msg);
    }

    public void onEnable(){
        log("All credits for this plugin go to Koenn");
        getCommand("stattrack").setExecutor(new CommandHandler(this));
        Bukkit.getPluginManager().registerEvents(new Listeners(this, this), this);
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    public synchronized static void openConnection(){
        try{
            connection = DriverManager.getConnection("jdbc:mysql://minecraft124.omgserv.com:3306/minecraft_7237", "minecraft_7237", "u5-Jgqpg");
            Bukkit.getLogger().log(Level.INFO, "[ltpstattrack] Database connection established.");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public synchronized static boolean containsKey(String uuid){
        try{
            PreparedStatement s = connection.prepareStatement("SELECT * FROM `Tokens` WHERE uuid=?;");
            s.setString(1, uuid);
            ResultSet r = s.executeQuery();
            boolean containsKey = r.next();
            s.close();
            r.close();
            Bukkit.getLogger().log(Level.INFO, "[ltpstattrack] Database containsKey " + uuid + " " + containsKey);
            return containsKey;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public void onDisable(){
        log("All credits for this plugin go to Koenn");
        try {
            if(!connection.isClosed()){
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String msg, Player p){
        String prefix = translateAlternateColorCodes('&', "&9StatTrack > ( &6");
        p.sendMessage(prefix + msg);
    }

    public static Main getInstance(){
        return main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        Main.openConnection();
        if(!(Main.containsKey(p.getUniqueId().toString()))){
            try{
                PreparedStatement s = connection.prepareStatement("INSERT INTO `Tokens` values(?,1);");
                s.setString(1, p.getUniqueId().toString());
                s.execute();
                s.close();
                connection.close();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

}
