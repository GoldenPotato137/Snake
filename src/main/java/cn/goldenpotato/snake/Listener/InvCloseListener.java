package cn.goldenpotato.snake.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InvCloseListener implements Listener
{
    @EventHandler
    public void OnClose(InventoryCloseEvent e)
    {
//        if(!e.getView().getTitle().equals("Snake"))
//            return;
//
//        SnakeGame game = Snake.GameMap.get(e.getPlayer().getUniqueId());
//        if(game==null)
//            return;
//        game.Leave(e.getPlayer().getUniqueId(),e.getInventory());
//        Snake.GameMap.remove(e.getPlayer().getUniqueId());
    }
}
