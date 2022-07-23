package cn.goldenpotato.snake.Command.SubCommands;

import cn.goldenpotato.snake.Command.SubCommand;
import cn.goldenpotato.snake.Config.ArenaManager;
import cn.goldenpotato.snake.Config.ConfigManager;
import cn.goldenpotato.snake.Config.MessageManager;
import cn.goldenpotato.snake.Snake;
import org.bukkit.entity.Player;

import java.util.List;

public class Reload extends SubCommand
{
    public Reload()
    {
        name = "reload";
        permission = "snake.admin";
        usage = MessageManager.msg.SubCommand_Reload_Usage;
    }

    @Override
    public void onCommand(Player player, String[] args)
    {
        ConfigManager.LoadConfig();
        MessageManager.LoadMessage();
        ArenaManager.LoadArenas(Snake.arenas);
    }

    @Override
    public List<String> onTab(Player player, String[] args)
    {
        return null;
    }
}
