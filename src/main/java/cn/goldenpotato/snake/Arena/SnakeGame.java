package cn.goldenpotato.snake.Arena;

import cn.goldenpotato.snake.Config.ArenaManager;
import cn.goldenpotato.snake.Config.ConfigManager;
import cn.goldenpotato.snake.Config.MessageManager;
import cn.goldenpotato.snake.Util.Coordinate;
import cn.goldenpotato.snake.Util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

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

    public String name; //场地名字
    public int maxSnake, minSnake, maxPlayer, playerPerSnake; //最大蛇数，最小蛇数，最大玩家数，每蛇玩家数
    public int initialSpeed = 10; //初始蛇运动间隔时间
    public World world; //场地所在世界
    public List<Food> foods = new ArrayList<>();
    public List<Snake> snakes = new ArrayList<>();
    public int cntAliveSnake, cntPlayedSnake; // 存活的蛇数量，开始游戏的蛇数量
    public HashMap<UUID, Snake> playerToGame = new HashMap<>();
    public List<Coordinate> beginPos = new ArrayList<>();
    public Location lobbyPos,leavePos; //大厅位置，离开位置
    public List<UUID> players = new ArrayList<>();
    public int y; //场地y轴高度
    public GameStatus gameStatus = GameStatus.WAITING; //游戏状态
    public int victoryCondition,victory; //胜利条件(0:曾到达最长长度，1:坚持到最后若干条蛇)及胜利具体值
    public String winCommand,lossCommand;
    private BossBar bossBar;

    void Init()
    {
        maxPlayer = maxSnake * playerPerSnake;
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                CheckStart();
            }
        }.runTaskTimer(cn.goldenpotato.snake.Snake.instance, 0, 60);
        bossBar = Bukkit.createBossBar("", BarColor.RED, BarStyle.SOLID);
        if(winCommand.equals("SameAsGlobal")) winCommand = ConfigManager.config.globalWinCommand;
        if(lossCommand.equals("SameAsGlobal")) lossCommand = ConfigManager.config.globalLossCommand;
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
        minSnake = in.getInt("minSnake", 1);
        playerPerSnake = in.getInt("playerPerSnake", 1);
        world = Bukkit.getWorld(Objects.requireNonNull(in.getString("world", "world")));
        initialSpeed = in.getInt("initialSpeed", 10);
        //victory
        String victoryConditionStr = in.getString("victoryCondition", "length");
        if(Objects.equals(victoryConditionStr, "length"))
            victoryCondition = 0;
        else
            victoryCondition = 1;
        if(victoryCondition==0)
            victory = in.getInt("victory", 10);
        else
            victory = in.getInt("victory", 1);
        y = in.getInt("y", 64);
        //command
        winCommand = in.getString("winCommand", "SameAsGlobal");
        lossCommand = in.getString("lossCommand", "SameAsGlobal");
        //beginPos
        in = ArenaManager.arenaReader.getConfigurationSection("ArenaList." + name + ".beginPos");
        if (in != null)
            for (String no : in.getKeys(false))
                beginPos.add(new Coordinate(in.getInt(no + ".x"), in.getInt(no + ".z")));
        //lobbyPos
        in = ArenaManager.arenaReader.getConfigurationSection("ArenaList." + name + ".lobbyPos");
        lobbyPos = new Location(world, 0, 64, 0);
        if (in != null)
            lobbyPos = new Location(world, in.getInt("x", 0), in.getInt("y", 64), in.getInt("z", 0));
        //leavePos
        in = ArenaManager.arenaReader.getConfigurationSection("ArenaList." + name + ".leavePos");
        leavePos = new Location(world, 0, 64, 0);
        if (in != null)
            leavePos = new Location(world, in.getInt("x", 0), in.getInt("y", 64), in.getInt("z", 0));
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

    public SnakeGame(String name, int minSnake, int maxSnake, int playerPerSnake, Location location)
    {
        this.name = name;
        this.victoryCondition = 0;
        this.victory = 10;
        this.minSnake = minSnake;
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
        cn.goldenpotato.snake.Snake.playerToArena.put(player, this);
        bossBar.addPlayer(Objects.requireNonNull(Bukkit.getPlayer(player)));
        Snake lastSnake = null;
        for (Snake snake : snakes)
            if (snake.players.size() != playerPerSnake)
            {
                lastSnake = snake;
                break;
            }
        if (lastSnake == null)
        {
            lastSnake = new Snake(this);
            snakes.add(lastSnake);
        }
        Objects.requireNonNull(Bukkit.getPlayer(player)).teleport(lobbyPos);
        playerToGame.put(player, lastSnake);
        players.add(player);
        CheckStart();
        return lastSnake.Join(player, inventory);
    }

    private boolean cancelCountDown;
    private boolean countDownStarted;

    private void CheckStart() //检查游戏是否可以开始了并修改bossBar条
    {
        int cntReady = 0;
        for (Snake snake : snakes)
            if (snake.players.size() == playerPerSnake)
                cntReady++;
        String title = MessageManager.msg.SnakeGame_WaitingTitle;
        title = title.replace("<MinSnakeNum>", String.valueOf(minSnake));
        title += "(" + cntReady + "/" + maxSnake + ")";
        bossBar.setTitle(title);
        bossBar.setProgress((float) cntReady / maxSnake);
        if (cntReady >= minSnake && gameStatus == GameStatus.WAITING)
        {
            cancelCountDown = false;
            CountDownStart();
        }
        else
            CancelCountDown();
    }

    private void CountDownStart()
    {
        if (countDownStarted) return;
        countDownStarted = true;
        for (UUID player : players)
            Util.Title(Objects.requireNonNull(Bukkit.getPlayer(player)), MessageManager.msg.SnakeGame_CountDown, 30);
        new BukkitRunnable()
        {
            int sec = 30;

            @Override
            public void run()
            {
                for (UUID player : players)
                {
                    if (Bukkit.getPlayer(player) == null) continue;
                    Objects.requireNonNull(Bukkit.getPlayer(player)).getInventory().setItem(4, new ItemStack(Material.CLOCK, sec));
                }
                if (sec == 10)
                    Start();
                if (cancelCountDown || sec <= 10)
                {
                    CancelCountDown();
                    cancel();
                }
                sec--;
            }
        }.runTaskTimer(cn.goldenpotato.snake.Snake.instance, 0, 20);
    }

    private void CancelCountDown()
    {
        cancelCountDown = true;
        countDownStarted = false;
    }

    /**
     * 修改玩家控制的蛇的方向
     */
    public void ChangeHeading(Heading heading, UUID player)
    {
        if (playerToGame.get(player) == null) return;
        Snake snake = playerToGame.get(player);
        if (snake.heading == Heading.UP && heading == Heading.DOWN) return;
        if (snake.heading == Heading.DOWN && heading == Heading.UP) return;
        if (snake.heading == Heading.LEFT && heading == Heading.RIGHT) return;
        if (snake.heading == Heading.RIGHT && heading == Heading.LEFT) return;
        playerToGame.get(player).heading = heading;
    }

    public void Leave(UUID player)
    {
        playerToGame.get(player).Leave(player);
        cn.goldenpotato.snake.Snake.playerToArena.remove(player);
        if (playerToGame.get(player).players.size() == 0)
            snakes.remove(playerToGame.get(player));
        playerToGame.remove(player);
        players.remove(player);
        Objects.requireNonNull(Bukkit.getPlayer(player)).teleport(leavePos);
        bossBar.removePlayer(Objects.requireNonNull(Bukkit.getPlayer(player)));
    }

    public void SnakeDie()
    {
        cntAliveSnake--;
        if(victoryCondition==1 && cntAliveSnake==victory)
        {
            for (Snake snake : snakes)
                snake.Victory();
            cntAliveSnake=0;
        }
        if (cntAliveSnake == 0)
            Stop();
    }

    public void Stop()
    {
        if (gameStatus != GameStatus.IN_GAME) return;
        gameStatus = GameStatus.WAITING;
        for (Food food : foods)
            food.Disable();
        for (Snake snake : snakes)
            snake.Loss();
        //恢复bossBar显示
        for (UUID player : players)
            bossBar.addPlayer(Objects.requireNonNull(Bukkit.getPlayer(player)));
    }

    public void Start()
    {
        if (gameStatus != GameStatus.WAITING) return;
        cancelCountDown = true;
        //取消bossBar显示
        bossBar.removeAll();
        //食物启用
        for (Food food : foods)
            food.Enable();
        //蛇启用
        Collections.shuffle(beginPos);
        cntAliveSnake = 0;
        for (int i = 0; i < snakes.size(); i++)
            if (snakes.get(i).players.size() == playerPerSnake)
            {
                cntAliveSnake++;
                snakes.get(i).StartGame(beginPos.get(i % beginPos.size()));
            }
        cntPlayedSnake = cntAliveSnake;
        gameStatus = GameStatus.IN_GAME;
    }

    public Snake GetSnake(Coordinate pos)
    {
        for (Snake snake : snakes)
        {
            Util.Log(pos.x + " " + pos.z + " " + snake.snake.getFirst().x + " " + snake.snake.getFirst().z);
            if (snake.snakeStatus != Snake.SnakeStatus.DEAD && snake.snake.size() >= 1
                    && snake.snake.getFirst().x == pos.x && snake.snake.getFirst().z == pos.z)
                return snake;
        }
        return null;
    }

    /**
     * 输出游戏信息
     */
    public void PrintStatus(Player player)
    {
        Util.Message(player, "§a=========================");
        Util.Message(player, MessageManager.msg.SnakeGame_ArenaName + name);
        String winCondition = MessageManager.msg.SnakeGame_VictoryConditionLength;
        if(victoryCondition == 1)
            winCondition = MessageManager.msg.SnakeGame_VictoryConditionSnake;
        winCondition = winCondition.replace("<num>", String.valueOf(victory));
        Util.Message(player, winCondition);
        Util.Message(player, MessageManager.msg.SnakeGame_SnakeNum + maxSnake);
        Util.Message(player, MessageManager.msg.SnakeGame_MinSnakeNum + minSnake);
        Util.Message(player, MessageManager.msg.SnakeGame_PlayerPerSnake + playerPerSnake);
        Util.Message(player, MessageManager.msg.SnakeGame_FoodNum + foods.size());
        Util.Message(player, MessageManager.msg.SnakeGame_SpawnNum + beginPos.size());
        Util.Message(player, MessageManager.msg.SnakeGame_InitialSpeed + initialSpeed);
        if (gameStatus == GameStatus.IN_GAME)
            Util.Message(player, MessageManager.msg.SnakeGame_GameStatus + MessageManager.msg.SnakeGame_InGame + " (" + cntAliveSnake + ")");
        else
            Util.Message(player, MessageManager.msg.SnakeGame_GameStatus + MessageManager.msg.SnakeGame_Waiting);
        for (Snake snake : snakes)
            snake.PrintStatus(player);
        Util.Message(player, "§a=========================");
    }

    public void OnRawChange(UUID player, float raw)
    {
        if (playerToGame.get(player) == null) return;
        playerToGame.get(player).OnRawChange(player, raw);
    }
}
