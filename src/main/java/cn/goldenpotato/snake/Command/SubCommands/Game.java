package cn.goldenpotato.snake.Command.SubCommands;

import cn.goldenpotato.snake.Command.SubCommand;
import cn.goldenpotato.snake.Config.MessageManager;
import cn.goldenpotato.snake.Util.Util;
import org.bukkit.entity.Player;

import java.util.List;

public class Game extends SubCommand
{
    public Game()
    {
        name = "game";
        permission = "snake.play";
        usage = MessageManager.msg.SubCommand_Game_Usage;
    }

    @Override
    public void onCommand(Player player, String[] args)
    {
        Util.Message(player, MessageManager.msg.SubCommand_Game_Help_1+"\n"+MessageManager.msg.SubCommand_Game_Help_2
                +"\n"+MessageManager.msg.SubCommand_Game_Help_3+"\n"+MessageManager.msg.SubCommand_Game_Help_4);
    }

    @Override
    public List<String> onTab(Player player, String[] args)
    {
        return null;
    }
}
