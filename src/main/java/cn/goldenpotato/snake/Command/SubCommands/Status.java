package cn.goldenpotato.snake.Command.SubCommands;

import cn.goldenpotato.snake.Arena.SnakeGame;
import cn.goldenpotato.snake.Command.SubCommand;
import cn.goldenpotato.snake.Config.MessageManager;
import cn.goldenpotato.snake.Snake;
import cn.goldenpotato.snake.Util.Util;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Status extends SubCommand
{
    public Status()
    {
        name = "status";
        permission = "snake.status";
        usage = MessageManager.msg.SubCommand_Status_Usage;
    }

    //snake status <arena>
    @Override
    public void onCommand(Player player, String[] args)
    {
        if(args.length<1)
        {
            Util.Message(player,MessageManager.msg.SubCommand_Status_Usage);
            return;
        }
        SnakeGame game = Snake.arenas.get(args[0]);
        if(game==null)
        {
            Util.Message(player, MessageManager.msg.Util_ArenaNotFound);
            return;
        }
        game.PrintStatus(player);
    }

    @Override
    public List<String> onTab(Player player, String[] args)
    {
        List<String> result = new ArrayList<>();
        if(args.length==1)
            for(SnakeGame game : Snake.arenas.values())
                result.add(game.name);
        return result;
    }
}
