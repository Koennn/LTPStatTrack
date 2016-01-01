package me.koenn.ltp;

import me.koenn.LTPSA.API.Data;
import me.koenn.LTPSA.API.DataInput;
import me.koenn.LTPSA.API.DataRequester;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler implements CommandExecutor {

    private Main main;
    private Stattrack st;
    private Plugin pl;

    @Override
    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args){
        main.log("test");
        if(!(sender instanceof Player)){
            sender.sendMessage("This command is for player's only");
            return true;
        }
        Player p = Bukkit.getPlayer(((Player) sender).getUniqueId());
        if(args.length < 1){
            main.sendMessage("Please specify a subcommand", p);
            return true;
        }
        if(args[0].equalsIgnoreCase("apply")){
            if(!(p.hasPermission("ltpstattrack.apply"))){
                main.sendMessage("You do not have permission to do this", p);
                return true;
            }
            main.log("Player " + p.getName() + " executed command " + cmd.getName());
            st.apply(p);
        }
        if(args[0].equalsIgnoreCase("addtokens")){
            main.log("test2");
            if(!(p.hasPermission("ltpstattrack.tokens"))) {
                main.sendMessage("You do not have permission to do this", p);
                return true;
            }
            if(args.length != 3){
                main.sendMessage("Please specify a player and an amount of tokens", p);
                return true;
            }
            Player t;
            try{
                t = Bukkit.getPlayer(args[1]);
            }catch (Exception ex){
                main.sendMessage("Player " + args[1] + "is not online or does not exist.", p);
                return true;
            }
            Integer i;
            try{
                i = Integer.parseInt(args[2]);
            } catch (Exception e){
                main.sendMessage("'" + args[2] + "' is not a number", p);
                return true;
            }
            DataRequester dr = new DataRequester("losttimepark", "tokens");
            Data data = dr.sendDataRequest(pl, "uuid", p.getUniqueId().toString());
            Integer oldTokens = Integer.parseInt(data.getData("tokens").toString());
            DataInput di = new DataInput("losttimepark", "tokens");
            List<String> collumns = new ArrayList<>();
            collumns.add("uuid");
            collumns.add("tokens");
            List<String> values = new ArrayList<>();
            values.add(t.getUniqueId().toString());
            values.add(String.valueOf(oldTokens+i));
            di.sendDataInput(pl, values, collumns, "uuid");
            main.tokens.put(p.getUniqueId(), main.tokens.get(p.getUniqueId())+i);
            main.sendMessage(i + " token(s) got added to your account", p);
            return true;
        }
        if(cmd.getName().equalsIgnoreCase("removetokens")){
            if(!(p.hasPermission("ltpstattrack.tokens"))) {
                main.sendMessage("You do not have permission to do this", p);
                return true;
            }
            if(args.length != 3){
                main.sendMessage("Please specify a player and an amount of tokens", p);
                return true;
            }
            Player t;
            try{
                t = Bukkit.getPlayer(args[1]);
            }catch (Exception ex){
                main.sendMessage("Player " + args[1] + "is not online or does not exist.", p);
                return true;
            }
            Integer i;
            try{
                i = Integer.parseInt(args[2]);
            } catch (Exception e){
                main.sendMessage("'" + args[2] + "' is not a number", p);
                return true;
            }
            DataRequester dr = new DataRequester("losttimepark", "tokens");
            Data data = dr.sendDataRequest(pl, "uuid", p.getUniqueId().toString());
            Integer oldTokens = Integer.parseInt(data.getData("tokens").toString());
            DataInput di = new DataInput("losttimepark", "tokens");
            List<String> collumns = new ArrayList<>();
            collumns.add("uuid");
            collumns.add("tokens");
            List<String> values = new ArrayList<>();
            values.add(t.getUniqueId().toString());
            values.add(String.valueOf(oldTokens-i));
            di.sendDataInput(pl, values, collumns, "uuid");
            main.tokens.put(p.getUniqueId(), main.tokens.get(p.getUniqueId()) - i);
            main.sendMessage(i + " token(s) got removed from your account", p);
            return true;
        }
        return true;
    }

    public CommandHandler(Main main, Plugin pl){
        this.main = main;
        this.st = new Stattrack(main, pl);
        this.pl = pl;
    }

}