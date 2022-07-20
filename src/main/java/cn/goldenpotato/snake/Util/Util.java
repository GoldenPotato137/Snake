package cn.goldenpotato.snake.Util;

import cn.goldenpotato.snake.Config.ConfigManager;
import cn.goldenpotato.snake.Snake;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class Util
{
    public enum SoundType
    {
        FOOD_SOUND, LOSS_SOUND, COUNT_DOWN , COUNT_DOWN2
    }

    public static void PlaySound(SoundType soundType, List<UUID> players)
    {
        for(UUID uuid : players)
        {
            Player player = Bukkit.getPlayer(uuid);
            if(player==null) continue;
            if (soundType == SoundType.FOOD_SOUND && ConfigManager.config.playEatSound)
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            else if (soundType == SoundType.LOSS_SOUND && ConfigManager.config.playLossSound)
                player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
            else if(soundType==SoundType.COUNT_DOWN && ConfigManager.config.playCountDownSound)
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            else if(soundType==SoundType.COUNT_DOWN2 && ConfigManager.config.playCountDownSound)
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 1);
        }
    }

    public static void Message(CommandSender player, String s)
    {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
    }

    public static void Title(Player player, String s,int stay)
    {
        player.sendTitle(ChatColor.translateAlternateColorCodes('&', s), "", 0, stay, 0);
    }

    public static void Log(String s)
    {
        Snake.instance.getLogger().info(ChatColor.translateAlternateColorCodes('&', s));
    }
}
