package cn.goldenpotato.snake;

import cn.goldenpotato.snake.Arena.SnakeGame;
import cn.goldenpotato.snake.Command.CommandManager;
import cn.goldenpotato.snake.Config.ArenaManager;
import cn.goldenpotato.snake.Config.ConfigManager;
import cn.goldenpotato.snake.Config.MessageManager;
import cn.goldenpotato.snake.Listener.PlayerListener;
import cn.goldenpotato.snake.Listener.PotionSplashListener;
import cn.goldenpotato.snake.Listener.InvCloseListener;
import cn.goldenpotato.snake.Listener.MoveListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Objects;

public final class Snake extends JavaPlugin
{
    public static Snake instance;
    public static HashMap<String,SnakeGame> arenas;

    @Override
    public void onEnable()
    {
        instance = this;
        // Plugin startup logic
        ConfigManager.LoadConfig();
        MessageManager.LoadMessage();
        //注册命令
        Objects.requireNonNull(Bukkit.getPluginCommand("snake")).setExecutor(new CommandManager());
        CommandManager.Init();
        //注册事件
        Bukkit.getPluginManager().registerEvents(new PotionSplashListener(), this);
        Bukkit.getPluginManager().registerEvents(new InvCloseListener(), this);
        Bukkit.getPluginManager().registerEvents(new MoveListener(),this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(),this);

        arenas = new HashMap<>();

        ArenaManager.LoadArenas(arenas);
    }

    @Override
    public void onDisable()
    {
        // Plugin shutdown logic
        ArenaManager.ShutDown();
    }
}
