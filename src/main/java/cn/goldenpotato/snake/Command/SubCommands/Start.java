package cn.goldenpotato.snake.Command.SubCommands;

import cn.goldenpotato.snake.Arena.SnakeGame;
import cn.goldenpotato.snake.Command.SubCommand;
import cn.goldenpotato.snake.Config.MessageManager;
import cn.goldenpotato.snake.Snake;
import cn.goldenpotato.snake.Util.Util;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * 强制开启游戏
 */
public class Start extends SubCommand
{
    public Start()
    {
        name="start";
        permission = "snake.start";
    }

    //snake start <arena>
    @Override
    public void onCommand(Player player, String[] args)
    {
        if(args.length==0)
        {
            Util.Message(player, MessageManager.msg.SubCommand_Start_Usage);
            return;
        }
        SnakeGame game = Snake.arenas.get(args[0]);
        if(game==null)
        {
            Util.Message(player,MessageManager.msg.SubCommand_Start_ArenaNotExist);
            return;
        }
        if(game.gameStatus== SnakeGame.GameStatus.IN_GAME)
            Util.Message(player,MessageManager.msg.SubCommand_Start_AlreadyStart);
        else
            game.Start();
    }

    @Override
    public List<String> onTab(Player player, String[] args)
    {
        List<String> result = new ArrayList<>();
        if(args.length==1)
            result.addAll(Snake.arenas.keySet());
        return result;
    }
}
