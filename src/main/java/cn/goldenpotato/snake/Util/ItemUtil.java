package cn.goldenpotato.snake.Util;

import cn.goldenpotato.snake.Config.MessageManager;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemUtil
{
    public ItemStack UpItem, DownItem, LeftItem, RightItem;

    public void SetItem(String arenaName, UUID upPlayer, UUID downPlayer, UUID leftPlayer, UUID rightPlayer)
    {
        UpItem = new ItemStack(Material.SPLASH_POTION, 10);
        DownItem = new ItemStack(Material.SPLASH_POTION, 10);
        LeftItem = new ItemStack(Material.SPLASH_POTION, 10);
        RightItem = new ItemStack(Material.SPLASH_POTION, 10);
        PotionMeta meta = (PotionMeta) UpItem.getItemMeta();
        assert meta != null;
        List<String> lore = new ArrayList<>();
        lore.add(MessageManager.msg.Item_ChangeHeading_Lore);
        lore.add("");
        if(upPlayer!=null) //upPlayer
        {
            lore.set(1,"§b" + arenaName + "." + upPlayer);
            meta.setLore(lore);
            meta.setDisplayName(MessageManager.msg.Item_ChangeHeading_Up);
            meta.setColor(Color.RED);
            UpItem.setItemMeta(meta);
        }
        if(downPlayer!=null) //downPlayer
        {
            lore.set(1, "§b" + arenaName + "." + downPlayer);
            meta.setLore(lore);
            meta.setDisplayName(MessageManager.msg.Item_ChangeHeading_Down);
            meta.setColor(Color.BLUE);
            DownItem.setItemMeta(meta);
        }
        if(leftPlayer!=null) //leftPlayer
        {
            lore.set(1, "§b" + arenaName + "." + leftPlayer);
            meta.setLore(lore);
            meta.setDisplayName(MessageManager.msg.Item_ChangeHeading_Left);
            meta.setColor(Color.GREEN);
            LeftItem.setItemMeta(meta);
        }
        if(rightPlayer!=null) //rightPlayer
        {
            lore.set(1, "§b" + arenaName + "." + rightPlayer);
            meta.setLore(lore);
            meta.setDisplayName(MessageManager.msg.Item_ChangeHeading_Right);
            meta.setColor(Color.YELLOW);
            RightItem.setItemMeta(meta);
        }
    }

    public static ItemStack GetItem(ItemStack item,int amount)
    {
        ItemStack newItem = item.clone();
        newItem.setAmount(amount);
        return newItem;
    }
}
