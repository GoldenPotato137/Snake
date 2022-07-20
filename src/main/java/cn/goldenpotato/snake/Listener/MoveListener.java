package cn.goldenpotato.snake.Listener;

import cn.goldenpotato.snake.Util.Util;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;

public class MoveListener implements Listener
{
    @EventHandler
    public void OnMove(PlayerMoveEvent e)
    {
//        Util.Message(e.getPlayer(), Objects.requireNonNull(e.getTo()).toString());
//        Location temp = e.getFrom();
//        temp.add(1,0,0);
//        e.setTo(temp);
    }
}
