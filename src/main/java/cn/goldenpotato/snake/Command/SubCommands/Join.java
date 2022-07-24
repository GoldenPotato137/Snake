package cn.goldenpotato.snake.Command.SubCommands;

import cn.goldenpotato.snake.Arena.SnakeGame;
import cn.goldenpotato.snake.Command.SubCommand;
import cn.goldenpotato.snake.Config.MessageManager;
import cn.goldenpotato.snake.Snake;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static cn.goldenpotato.snake.Util.Util.Message;

public class Join extends SubCommand
{
    public Join()
    {
        name = "join";
        permission = "snake.play";
        usage = MessageManager.msg.SubCommand_Join_Usage;
    }
    @Override
    public void onCommand(Player player, String[] args)
    {
        if(args.length<1)
        {
            Message(player, MessageManager.msg.SubCommand_Join_Usage);
            return;
        }
        for(SnakeGame game : Snake.arenas.values())
            if(game.players.contains(player.getUniqueId()))
            {
                Message(player, MessageManager.msg.SubCommand_Join_AlreadyInArena);
                return;
            }
        SnakeGame game = Snake.arenas.get(args[0]);
        if(game!=null)
        {
            if(game.maxPlayer > game.players.size())
                game.Join(player.getUniqueId(), player.getInventory());
            else
                Message(player,MessageManager.msg.SubCommand_Join_Full);
        }
        else
            Message(player,MessageManager.msg.SubCommand_Join_ArenaNotExist);
    }

    @Override
    public List<String> onTab(Player player, String[] args)
    {
        if(args.length!=1) return null;
        return new ArrayList<>(Snake.arenas.keySet());
    }
}
