package cn.goldenpotato.snake.Config;

import cn.goldenpotato.snake.Arena.Food;
import cn.goldenpotato.snake.Arena.SnakeGame;
import cn.goldenpotato.snake.Snake;
import cn.goldenpotato.snake.Util.Coordinate;
import cn.goldenpotato.snake.Util.Util;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ArenaManager
{
    private static final File arenaFile = new File(Snake.instance.getDataFolder(), "arenas.yml");
    public static FileConfiguration arenaReader;

    public static void LoadArenas(HashMap<String,SnakeGame> arenas)
    {
        arenas.clear();
        if(!arenaFile.exists())
            Snake.instance.saveResource("arenas.yml",false);
        arenaReader= YamlConfiguration.loadConfiguration(arenaFile);
        if(arenaReader.getConfigurationSection("ArenaList")==null)
            return;
        for(String name : Objects.requireNonNull(arenaReader.getConfigurationSection("ArenaList")).getKeys(false))
            arenas.put(name,new SnakeGame(name));
    }

    private static void SaveArena(SnakeGame arena)
    {
        ConfigurationSection out = arenaReader.getConfigurationSection("ArenaList."+arena.name);
        if (out==null)
            out=arenaReader.createSection("ArenaList."+arena.name);
        out.set("maxSnake",arena.maxSnake);
        out.set("minSnake",arena.minSnake);
        out.set("playerPerSnake",arena.playerPerSnake);
        out.set("world",arena.world.getName());
        out.set("initialSpeed",arena.initialSpeed);
        if(arena.victoryCondition == 0)
            out.set("victoryCondition","length");
        else
            out.set("victoryCondition","snakeNum");
        out.set("y",arena.y);
        out.set("victory",arena.victory);
        out.set("lobbyPos.x",arena.lobbyPos.getBlockX());
        out.set("lobbyPos.y",arena.lobbyPos.getBlockY());
        out.set("lobbyPos.z",arena.lobbyPos.getBlockZ());
        out.set("leavePos.x",arena.leavePos.getBlockX());
        out.set("leavePos.y",arena.leavePos.getBlockY());
        out.set("leavePos.z",arena.leavePos.getBlockZ());
        for (int i=0;i<arena.beginPos.size();i++)
        {
            Coordinate c=arena.beginPos.get(i);
            out.set("beginPos."+i+".x",c.x);
            out.set("beginPos."+i+".z",c.z);
        }
        for(Food food: arena.foods)
            SaveFood(arena,food);
    }

    private static void SaveFood(SnakeGame arena,Food food)
    {
        ConfigurationSection out = arenaReader.getConfigurationSection("ArenaList."+arena.name+".food."+ food.name);
        if(out==null)
            out = arenaReader.createSection("ArenaList."+arena.name+".food."+food.name);
        out.set("loc.x",food.coordinate.x);
        out.set("loc.z",food.coordinate.z);
        out.set("lifeTime",food.lifeTime);
        out.set("birthRate",food.birthRate);
        out.set("type",food.type.toString());
    }

    public static void ShutDown()
    {
        Util.Log("Saving arenas");
        for(SnakeGame game : Snake.arenas.values())
        {
            game.Stop();
            List<UUID> toLeave = new ArrayList<>(game.players);
            for (UUID uuid : toLeave)
                game.Leave(uuid);
        }
        SaveFile();
    }

    public static void SaveFile()
    {
        for(SnakeGame arena : Snake.arenas.values())
            SaveArena(arena);
        try
        {
            arenaReader.save(arenaFile);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
