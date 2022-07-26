package cn.goldenpotato.snake.Listener;

import cn.goldenpotato.snake.Arena.SnakeGame;
import cn.goldenpotato.snake.Command.SubCommands.Leave;
import cn.goldenpotato.snake.Config.MessageManager;
import cn.goldenpotato.snake.Snake;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerListener implements Listener
{
    @EventHandler
    public void OnQuit(PlayerQuitEvent e)
    {
        Leave leaveCommand = new Leave();
        leaveCommand.onCommand(e.getPlayer(), new String[]{});
    }

    @EventHandler
    public void OnPlayerUse(PlayerInteractEvent e)
    {
        if(e.getItem()==null) return;
        ItemMeta itemMeta = e.getItem().getItemMeta();
        if(itemMeta==null) return;
        if(itemMeta.getDisplayName().equals(MessageManager.msg.Item_Leave))
        {
            Leave leaveCommand = new Leave();
            leaveCommand.onCommand(e.getPlayer(), new String[]{});
        }
    }

    @EventHandler
    public void OnDropItem(PlayerDropItemEvent e)
    {
        SnakeGame game = Snake.playerToArena.get(e.getPlayer().getUniqueId());
        if(game==null) return;
        e.setCancelled(true);
    }
}
