package cn.goldenpotato.snake.Listener;

import cn.goldenpotato.snake.Arena.SnakeGame;
import cn.goldenpotato.snake.Config.MessageManager;
import cn.goldenpotato.snake.Snake;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;

public class ChatListener implements Listener
{
    @EventHandler
    public void OnChat(AsyncPlayerChatEvent e)
    {
        Player player = e.getPlayer();
        SnakeGame game = Snake.playerToArena.get(player.getUniqueId());
        if(game==null) return;
        if(e.getMessage().startsWith("!"))
        {
            e.setMessage(e.getMessage().substring(1));
            return;
        }

        e.setCancelled(true);
        List<Player> teammate = game.GetTeammate(player.getUniqueId());
        for(Player t : teammate)
            t.sendMessage(MessageManager.msg.Util_TeamChat + "§a" +player.getDisplayName()+": §f"+e.getMessage());
    }
}
