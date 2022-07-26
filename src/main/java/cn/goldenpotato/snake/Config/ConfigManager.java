package cn.goldenpotato.snake.Config;

import cn.goldenpotato.snake.Snake;
import cn.goldenpotato.snake.Util.Util;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager
{
    static public Config config;
    static boolean init = false;
    static FileConfiguration configuration;

    static void Init()
    {
        Snake.instance.saveDefaultConfig();
        config = new Config();
        init = true;
    }

    static public void LoadConfig()
    {
        if (!init) Init();
        Snake.instance.reloadConfig();
        configuration = Snake.instance.getConfig();
        //Language
        config.language = configuration.getString("Language", "en-WW");
        Util.Log("Using locale: " + config.language);
        //Command
        config.globalLossCommand = configuration.getString("Command.GlobalLossCommand","[null]");
        config.globalWinCommand = configuration.getString("Command.GlobalWinCommand","[null]");
        //Sound
        config.playEatSound = configuration.getBoolean("Sound.PlayEatSound", true);
        config.playLossSound = configuration.getBoolean("Sound.PlayLossSound", true);
        config.playCountDownSound = configuration.getBoolean("Sound.PlayCountDownSound", true);
        //Game
        config.maxMoveTools = configuration.getInt("Game.MaxMoveTools", 30);
        config.toolsPerFood = configuration.getInt("Game.ToolsPerFood", 5);
        config.initialTools = configuration.getInt("Game.InitialTools", 10);
    }
}
