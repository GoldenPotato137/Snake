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
        Util.Log("Using language: " + config.language);
        //Command
        config.lossCommand = configuration.getString("Command.LossCommand");
        if (config.lossCommand != null && config.lossCommand.equals("[null]"))
            config.lossCommand = null;
        config.winCommand = configuration.getString("Command.WinCommand");
        if (config.winCommand != null && config.winCommand.equals("[null]"))
            config.winCommand = null;
        //Sound
        config.playEatSound = configuration.getBoolean("Sound.PlayEatSound", true);
        config.playLossSound = configuration.getBoolean("Sound.PlayLossSound", true);
        config.playCountDownSound = configuration.getBoolean("Sound.PlayCountDownSound", true);
        //Game
        config.foodPercentage = configuration.getDouble("Game.FoodPercentage", 0.05);
    }
}
