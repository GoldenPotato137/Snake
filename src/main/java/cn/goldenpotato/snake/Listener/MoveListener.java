package cn.goldenpotato.snake.Listener;

import cn.goldenpotato.snake.Arena.SnakeGame;
import cn.goldenpotato.snake.Snake;
import cn.goldenpotato.snake.Util.Util;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener
{
    @EventHandler
    public void OnMove(PlayerMoveEvent e)
    {
        SnakeGame game = Snake.playerToArena.get(e.getPlayer().getUniqueId());
        if(game==null) return;
        float rotation = (e.getPlayer().getLocation().getYaw()% 360 + 360)%360 ;
        game.OnRawChange(e.getPlayer().getUniqueId(),rotation);
    }
}
