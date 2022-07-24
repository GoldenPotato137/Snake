package cn.goldenpotato.snake.Command.SubCommands;

import cn.goldenpotato.snake.Command.CommandManager;
import cn.goldenpotato.snake.Command.SubCommand;
import cn.goldenpotato.snake.Config.MessageManager;
import cn.goldenpotato.snake.Util.Util;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Help extends SubCommand
{
    public Help()
    {
        name = "help";
        permission = "snake.help";
        usage = MessageManager.msg.SubCommand_Help_Usage;
    }

    @Override
    public void onCommand(Player player, String[] args)
    {
        if(args.length==0)
        {
            for(SubCommand subCommand : CommandManager.subCommands)
                if(player.hasPermission(subCommand.permission))
                    Util.Message(player,subCommand.name+" - "+subCommand.usage);
            return;
        }
        String command = args[0];
        for (SubCommand subCommand : CommandManager.subCommands)
            if(subCommand.name.equals(command))
            {
                if(!player.hasPermission(subCommand.permission))
                    Util.Message(player,MessageManager.msg.Util_NoPermission);
                else
                    Util.Message(player,subCommand.usage);
                return;
            }
        Util.Message(player,MessageManager.msg.SubCommand_Help_NoSuchCommand);
    }

    @Override
    public List<String> onTab(Player player, String[] args)
    {
        List<String> result = new ArrayList<>();
        if(args.length==1)
            for(SubCommand subCommand : CommandManager.subCommands)
                if(player.hasPermission(subCommand.permission))
                    result.add(subCommand.name);
        return result;
    }
}
