package cn.goldenpotato.snake.Command.SubCommands;

import cn.goldenpotato.snake.Arena.Food;
import cn.goldenpotato.snake.Arena.SnakeGame;
import cn.goldenpotato.snake.Command.SubCommand;
import cn.goldenpotato.snake.Config.MessageManager;
import cn.goldenpotato.snake.Snake;
import cn.goldenpotato.snake.Util.Coordinate;
import cn.goldenpotato.snake.Util.Util;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Set extends SubCommand
{
    public Set()
    {
        name = "set";
        permission = "snake.admin";
        usage = MessageManager.msg.SubCommand_Set_Usage + "\n" + MessageManager.msg.SubCommand_Set_Usage2 + "\n"
                + MessageManager.msg.SubCommand_Set_Usage3 + "\n" + MessageManager.msg.SubCommand_Set_Usage4;
    }

    //snake set <区域名> food <lifeTime> <birthRate> [foodType:UP|DOWN|LEFT|RIGHT|NORMAL]
    //snake set <区域名> beginPos
    //snake set <区域名> lobbyPos
    //snake set <区域名> leavePos
    @Override
    public void onCommand(Player player, String[] args)
    {
        if (args.length < 2)
        {
            Util.Message(player, usage);
            return;
        }
        SnakeGame arena = Snake.arenas.get(args[0]);
        if (arena == null)
        {
            Util.Message(player, MessageManager.msg.SubCommand_Set_ArenaNotExist);
            return;
        }
        switch (args[1])
        {
            case "food":
                if (args.length >= 5)
                {
                    int lifeTime, birthRate;
                    String foodName = args[2];
                    if (foodName.contains("."))
                    {
                        Util.Message(player, MessageManager.msg.SubCommand_Set_FoodWrongName);
                        return;
                    }
                    for (Food food : arena.foods)
                        if (food.name.equals(foodName))
                        {
                            Util.Message(player, MessageManager.msg.SubCommand_Set_FoodAlreadyExist);
                            return;
                        }
                    try
                    {
                        lifeTime = Integer.parseInt(args[3]);
                        birthRate = Integer.parseInt(args[4]);
                    }
                    catch (NumberFormatException e)
                    {
                        Util.Message(player, MessageManager.msg.Util_WrongNum);
                        return;
                    }
                    Food.FoodType type = Food.FoodType.NORMAL;
                    if (args[5] != null)
                        type = Food.StringToFoodType(args[5]);
                    Util.Message(player, MessageManager.msg.SubCommand_Set_FoodSuccess);
                    Food food = new Food(args[2], player.getLocation(), arena, lifeTime, birthRate, type);
                    arena.foods.add(food);
                }
                else
                {
                    Util.Message(player, MessageManager.msg.Util_WrongCommand);
                }
                return;
            case "beginPos":
                arena.beginPos.add(new Coordinate(player.getLocation().getBlockX(), player.getLocation().getBlockZ()));
                Util.Message(player, MessageManager.msg.SubCommand_Set_BeginPosSuccess);
                return;
            case "lobbyPos":
                arena.lobbyPos = player.getLocation();
                Util.Message(player, MessageManager.msg.SubCommand_Set_LobbyPosSuccess);
                return;
            case "leavePos":
                arena.leavePos = player.getLocation();
                Util.Message(player, MessageManager.msg.SubCommand_Set_LeavePosSuccess);
        }
    }

    @Override
    public List<String> onTab(Player player, String[] args)
    {
        List<String> result = new ArrayList<>();
        if (args.length == 2)
        {
            result.add("food");
            result.add("beginPos");
            result.add("lobbyPos");
            result.add("leavePos");
        }
        else if (args.length == 1)
            result.addAll(Snake.arenas.keySet());
        else
        {
            switch (args[1])
            {
                case "food":
                    if (args.length == 3)
                    {
                        SnakeGame game = Snake.arenas.get(args[0]);
                        if (game == null)
                            result.add("ArenaNotFound");
                        else
                            result.add("fd" + (game.foods.size() + 1));
                    }
                    else if (args.length == 4)
                        result.add("300");
                    else if (args.length == 5)
                        result.add("10");
                    else if (args.length == 6)
                    {
                        result.add("NORMAL");
                        result.add("UP");
                        result.add("DOWN");
                        result.add("LEFT");
                        result.add("RIGHT");
                    }
                    break;
                case "beginPos":
                    break;
            }
        }
        return result;
    }
}
