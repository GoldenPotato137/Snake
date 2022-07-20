package cn.goldenpotato.snake.Command;

import cn.goldenpotato.snake.Command.SubCommands.*;
import cn.goldenpotato.snake.Config.MessageManager;
import cn.goldenpotato.snake.Util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager implements CommandExecutor, TabCompleter
{
    static ArrayList<SubCommand> subCommands;

    public static void Init()
    {
        subCommands = new ArrayList<>();
        subCommands.add(new Join());
        subCommands.add(new Leave());
        subCommands.add(new Reload());
        subCommands.add(new Create());
        subCommands.add(new Set());
        subCommands.add(new Save());
        subCommands.add(new Start());
        subCommands.add(new Stop());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!(sender instanceof Player))
        {
            sender.sendMessage(MessageManager.msg.Command_CommandManager_NoCommandInConsole);
            return true;
        }
        if(args.length==0)
        {
            return true;
        }
        for (SubCommand subCommand : subCommands)
            if(subCommand.name.equals(args[0]))
            {
                if(!sender.hasPermission(subCommand.permission))
                    Util.Message(sender,MessageManager.msg.Util_NoPermission);
                else
                    subCommand.onCommand((Player) sender, Arrays.copyOfRange(args,1,args.length));
                return true;
            }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
    {
        if(!(sender instanceof Player))
            return null;
        if(args.length==1)
        {
            List<String> result = new ArrayList<>();
            for (SubCommand subCommand : subCommands)
                if(sender.hasPermission(subCommand.permission))
                    result.add(subCommand.name);
            return result;
        }
        for (SubCommand subCommand : subCommands)
            if(subCommand.name.equals(args[0]) && sender.hasPermission(subCommand.permission))
                return subCommand.onTab((Player) sender, Arrays.copyOfRange(args,1,args.length));
        return null;
    }
}
