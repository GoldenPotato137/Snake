package cn.goldenpotato.snake.Arena;

import cn.goldenpotato.snake.Config.ArenaManager;
import cn.goldenpotato.snake.Util.Coordinate;
import cn.goldenpotato.snake.Util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class SnakeGame
{
    public enum GameStatus
    {
        IN_GAME, WAITING
    }

    public enum Heading
    {
        UP, DOWN, LEFT, RIGHT
    }

    public String name;
    public int maxSnake,maxPlayer,playerPerSnake;
    public World world;
    public List<Food> foods = new ArrayList<>();
    public List<Snake> snakes = new ArrayList<>();
    private int cntAliveSnake;
    public HashMap<UUID,Snake> playerToGame = new HashMap<>();
    public List<Coordinate> beginPos = new ArrayList<>();
    public Location lobbyPos;
    public Location leavePos;
    public List<UUID> players = new ArrayList<>();
    public int y;
    public GameStatus gameStatus = GameStatus.WAITING;

    void Init()
    {
        maxPlayer = maxSnake * playerPerSnake;
    }

    /**
     * 加载游戏用
     */
    public SnakeGame(String name)
    {
        ConfigurationSection in = ArenaManager.arenaReader.getConfigurationSection("ArenaList." + name);
        assert in != null;
        this.name = name;
        maxSnake = in.getInt("maxSnake", 1);
        playerPerSnake = in.getInt("playerPerSnake", 1);
        world = Bukkit.getWorld(Objects.requireNonNull(in.getString("world", "world")));
        y = in.getInt("y", 64);
        //beginPos
        in = ArenaManager.arenaReader.getConfigurationSection("ArenaList." + name + ".beginPos");
        if(in!=null)
            for (String no : in.getKeys(false))
                beginPos.add(new Coordinate(in.getInt(no + ".x"), in.getInt(no + ".z")));
        //lobbyPos
        in = ArenaManager.arenaReader.getConfigurationSection("ArenaList." + name + ".lobbyPos");
        lobbyPos = new Location(world,0,64,0);
        if(in!=null)
            lobbyPos = new Location(world,in.getInt("x",0),in.getInt("y",64),in.getInt("z",0));
        //leavePos
        in = ArenaManager.arenaReader.getConfigurationSection("ArenaList." + name + ".leavePos");
        leavePos = new Location(world,0,64,0);
        if(in!=null)
            leavePos = new Location(world,in.getInt("x",0),in.getInt("y",64),in.getInt("z",0));
        //foods
        in = ArenaManager.arenaReader.getConfigurationSection("ArenaList." + name + ".food");
        if (in != null)
            for (String fdName : in.getKeys(false))
            {
                Location foodLoc = new Location(world, in.getInt(fdName + ".loc.x", 0), y, in.getInt(fdName + ".loc.z", 0));
                int lifeTime = in.getInt(fdName + ".lifeTime", 1);
                int birthRate = in.getInt(fdName + ".birthRate", 1);
                String tempFoodType = in.getString(fdName + ".type", "NORMAL");
                foods.add(new Food(fdName, foodLoc, this, lifeTime, birthRate, Food.StringToFoodType(tempFoodType)));
            }
        Init();
    }

    public SnakeGame(String name, int maxSnake,int playerPerSnake, Location location)
    {
        this.name = name;
        this.maxSnake = maxSnake;
        this.playerPerSnake = playerPerSnake;
        this.y = location.getBlockY();
        this.beginPos.add(new Coordinate(location.getBlockX(), location.getBlockZ()));
        this.lobbyPos = location;
        this.leavePos = location;
        this.world = location.getWorld();
        Init();
    }

    public List<Heading> Join(UUID player, Inventory inventory)
    {
        Snake lastSnake = null;
        for(Snake snake : snakes)
            if(snake.players.size()!=playerPerSnake)
            {
                lastSnake = snakes.get(snakes.size() - 1);
                break;
            }
        if(lastSnake==null)
        {
            lastSnake = new Snake(this);
            snakes.add(lastSnake);
        }
        Objects.requireNonNull(Bukkit.getPlayer(player)).teleport(lobbyPos);
        playerToGame.put(player,lastSnake);
        players.add(player);
        return lastSnake.Join(player,inventory);
    }

    /**
     * 修改玩家控制的蛇的方向
     */
    public void ChangeHeading(Heading heading,UUID player)
    {
        if(playerToGame.get(player)==null) return;
        Snake snake = playerToGame.get(player);
        if(snake.heading==Heading.UP && heading==Heading.DOWN) return;
        if(snake.heading==Heading.DOWN && heading==Heading.UP) return;
        if(snake.heading==Heading.LEFT && heading==Heading.RIGHT) return;
        if(snake.heading==Heading.RIGHT && heading==Heading.LEFT) return;
        playerToGame.get(player).heading = heading;
    }

    public void Leave(UUID player)
    {
        playerToGame.get(player).Leave(player);
        players.remove(player);
        Objects.requireNonNull(Bukkit.getPlayer(player)).teleport(leavePos);
    }

    public void SnakeDie()
    {
        cntAliveSnake--;
        if(cntAliveSnake==0)
            Stop();
    }

    public void Stop()
    {
        if (gameStatus != GameStatus.IN_GAME) return;
        gameStatus = GameStatus.WAITING;
        for (Food food : foods)
            food.Disable();
        for(Snake snake : snakes)
            snake.Stop();
    }

    public void Start()
    {
        //食物启用
        for (Food food : foods)
            food.Enable();
        //蛇启用
        Collections.shuffle(beginPos);
        cntAliveSnake=0;
        for(int i=0;i<snakes.size();i++)
        {
            cntAliveSnake++;
            snakes.get(i).StartGame(beginPos.get(i%beginPos.size()));
        }
        gameStatus = GameStatus.IN_GAME;
    }

    public Snake GetSnake(Coordinate pos)
    {
        for (Snake snake : snakes)
        {
            Util.Log(pos.x+" "+pos.z+" "+snake.snake.getFirst().x+" "+snake.snake.getFirst().z);
            if (snake.snakeStatus != Snake.SnakeStatus.DEAD && snake.snake.size() >= 1
                    && snake.snake.getFirst().x == pos.x && snake.snake.getFirst().z == pos.z)
                return snake;
        }
        return null;
    }
}
