package me.koenn.ltp;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {

    private Main main;
    private Stattrack st;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args){
        if(!(sender instanceof Player)){
            sender.sendMessage("This command is for player's only");
            return true;
        }
        Player p = Bukkit.getPlayer(((Player) sender).getUniqueId());
        if(!(p.hasPermission("ltpstattrack.apply"))){
            main.sendMessage("You do not have permission to do this", p);
            return true;
        }
        main.log("Player " + p.getName() + " executed command " + cmd.getName());
        st.apply(p);
        return true;
    }

    public CommandHandler(Main main){
        this.main = main;
        this.st = new Stattrack(main);
    }

}