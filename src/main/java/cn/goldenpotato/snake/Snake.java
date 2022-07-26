package cn.goldenpotato.snake;

import cn.goldenpotato.snake.Arena.SnakeGame;
import cn.goldenpotato.snake.Command.CommandManager;
import cn.goldenpotato.snake.Config.ArenaManager;
import cn.goldenpotato.snake.Config.ConfigManager;
import cn.goldenpotato.snake.Config.MessageManager;
import cn.goldenpotato.snake.Listener.PlayerListener;
import cn.goldenpotato.snake.Listener.PotionSplashListener;
import cn.goldenpotato.snake.Listener.ChatListener;
import cn.goldenpotato.snake.Listener.MoveListener;
import cn.goldenpotato.snake.Metrics.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public final class Snake extends JavaPlugin
{
    public static Snake instance;
    public static HashMap<String,SnakeGame> arenas;
    public static HashMap<UUID,SnakeGame> playerToArena;

    @Override
    public void onEnable()
    {
        int pluginId = 15913;
        new Metrics(this, pluginId);

        instance = this;
        // Plugin startup logic
        ConfigManager.LoadConfig();
        MessageManager.LoadMessage();
        //注册命令
        Objects.requireNonNull(Bukkit.getPluginCommand("snake")).setExecutor(new CommandManager());
        CommandManager.Init();
        //注册事件
        Bukkit.getPluginManager().registerEvents(new PotionSplashListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new MoveListener(),this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(),this);

        arenas = new HashMap<>();
        playerToArena = new HashMap<>();

        ArenaManager.LoadArenas(arenas);
    }

    @Override
    public void onDisable()
    {
        // Plugin shutdown logic
        ArenaManager.ShutDown();
    }
}
