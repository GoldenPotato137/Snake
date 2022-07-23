package cn.goldenpotato.snake.Command.SubCommands;

import cn.goldenpotato.snake.Command.SubCommand;
import cn.goldenpotato.snake.Config.ArenaManager;
import cn.goldenpotato.snake.Config.MessageManager;
import cn.goldenpotato.snake.Util.Util;
import org.bukkit.entity.Player;

import java.util.List;

public class Save extends SubCommand
{
    public Save()
    {
        name = "save";
        permission = "snake.admin";
        usage = MessageManager.msg.SubCommand_Save_Usage;
    }

    @Override
    public void onCommand(Player player, String[] args)
    {
        ArenaManager.SaveFile();
        Util.Message(player, MessageManager.msg.SubCommand_Save_Success);
    }

    @Override
    public List<String> onTab(Player player, String[] args)
    {
        return null;
    }
}
