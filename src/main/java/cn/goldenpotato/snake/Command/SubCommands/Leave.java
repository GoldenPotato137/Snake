package cn.goldenpotato.snake.Command.SubCommands;

import cn.goldenpotato.snake.Arena.SnakeGame;
import cn.goldenpotato.snake.Command.SubCommand;
import cn.goldenpotato.snake.Config.MessageManager;
import cn.goldenpotato.snake.Snake;
import cn.goldenpotato.snake.Util.Util;
import org.bukkit.entity.Player;

import java.util.List;

public class Leave extends SubCommand
{

    public Leave()
    {
        name = "leave";
        permission = "snake.play";
    }

    //snake leave
    @Override
    public void onCommand(Player player, String[] args)
    {
        SnakeGame game = null;
        for(SnakeGame snakeGame : Snake.arenas.values())
            if(snakeGame.players.contains(player.getUniqueId()))
                game = snakeGame;
        if(game==null)
        {
            Util.Message(player, MessageManager.msg.SubCommand_Leave_NotInGame);
            return;
        }
        game.Leave(player.getUniqueId());
        Util.Message(player, MessageManager.msg.SubCommand_Leave_Success);
    }

    @Override
    public List<String> onTab(Player player, String[] args)
    {
        return null;
    }
}
