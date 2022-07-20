package cn.goldenpotato.snake.Listener;

import cn.goldenpotato.snake.Arena.SnakeGame;
import cn.goldenpotato.snake.Config.MessageManager;
import cn.goldenpotato.snake.Snake;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.List;
import java.util.UUID;

public class PotionSplashListener implements Listener
{
    @EventHandler
    public void OnPotionSplash(PotionSplashEvent e)
    {
        PotionMeta potion = (PotionMeta) e.getPotion().getItem().getItemMeta();
        if(potion==null) return;
        List<String> lore = potion.getLore();
        if(lore==null || lore.size()!=2) return;
        String lore1 = lore.get(1).replaceAll("§b","");
        //分割字符段，获取arena名称及玩家名称
        int dotIndex = -1;
        for(int i=0;i<lore1.length();i++)
            if(lore1.charAt(i)=='.')
            {
                dotIndex = i;
                break;
            }
        SnakeGame game = Snake.arenas.get(lore1.substring(0,dotIndex));
        UUID player = UUID.fromString(lore1.substring(dotIndex+1));
        if(game == null) return;

        if(potion.getDisplayName().equals(MessageManager.msg.Item_ChangeHeading_Up))
            game.ChangeHeading(SnakeGame.Heading.UP,player);
        else if(potion.getDisplayName().equals(MessageManager.msg.Item_ChangeHeading_Down))
            game.ChangeHeading(SnakeGame.Heading.DOWN,player);
        else if(potion.getDisplayName().equals(MessageManager.msg.Item_ChangeHeading_Left))
            game.ChangeHeading(SnakeGame.Heading.LEFT,player);
        else if(potion.getDisplayName().equals(MessageManager.msg.Item_ChangeHeading_Right))
            game.ChangeHeading(SnakeGame.Heading.RIGHT,player);
//        Util.Log(game.heading.toString());
    }
}
