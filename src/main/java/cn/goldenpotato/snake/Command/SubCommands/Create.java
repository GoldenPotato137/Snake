package cn.goldenpotato.snake.Command.SubCommands;

import cn.goldenpotato.snake.Arena.SnakeGame;
import cn.goldenpotato.snake.Command.SubCommand;
import cn.goldenpotato.snake.Config.MessageManager;
import cn.goldenpotato.snake.Snake;
import cn.goldenpotato.snake.Util.Util;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Create extends SubCommand
{
    public Create()
    {
        name = "create";
        permission = "snake.admin";
    }

    //snake create <name> <minSnake> <maxSnake> <playerPerSnake>
    @Override
    public void onCommand(Player player, String[] args)
    {
        if(args.length<4)
        {
            Util.Message(player, MessageManager.msg.SubCommand_Create_Usage);
            return;
        }
        if(Snake.arenas.containsKey(args[0]))
        {
            Util.Message(player, MessageManager.msg.SubCommand_Create_ArenaExist);
            return;
        }
        int maxSnake,minSnake,playerPerSnake;
        try
        {
            minSnake = Integer.parseInt(args[1]);
            maxSnake = Integer.parseInt(args[2]);
            playerPerSnake = Integer.parseInt(args[3]);
        }
        catch (Exception e)
        {
            Util.Message(player,MessageManager.msg.Util_WrongNum);
            return;
        }
        if(playerPerSnake!=1 && playerPerSnake!=2 && playerPerSnake!=4 || minSnake>maxSnake)
        {
            Util.Message(player,MessageManager.msg.SubCommand_Create_Usage);
            return;
        }
        SnakeGame game = new SnakeGame(args[0],minSnake,maxSnake,playerPerSnake,player.getLocation());
        Snake.arenas.put(args[0],game);
    }

    @Override
    public List<String> onTab(Player player, String[] args)
    {
        List<String> result = new ArrayList<>();
        if(args.length==1)
            result.add("name");
        else if(args.length==2)
            result.add("1");
        else if(args.length==3)
        {
            result.add("1");
            result.add("2");
            result.add("4");
        }
        return result;
    }
}
