package cn.goldenpotato.snake.Util;

import cn.goldenpotato.snake.Arena.SnakeGame;
import cn.goldenpotato.snake.Config.ConfigManager;
import cn.goldenpotato.snake.Config.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ItemUtil
{
    public ItemStack upItem, downItem, leftItem, rightItem;

    public void SetItem(String arenaName, UUID upPlayer, UUID downPlayer, UUID leftPlayer, UUID rightPlayer)
    {
        upItem = new ItemStack(Material.SPLASH_POTION, 10);
        downItem = new ItemStack(Material.SPLASH_POTION, 10);
        leftItem = new ItemStack(Material.SPLASH_POTION, 10);
        rightItem = new ItemStack(Material.SPLASH_POTION, 10);
        PotionMeta meta = (PotionMeta) upItem.getItemMeta();
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
            upItem.setItemMeta(meta);
        }
        if(downPlayer!=null) //downPlayer
        {
            lore.set(1, "§b" + arenaName + "." + downPlayer);
            meta.setLore(lore);
            meta.setDisplayName(MessageManager.msg.Item_ChangeHeading_Down);
            meta.setColor(Color.BLUE);
            downItem.setItemMeta(meta);
        }
        if(leftPlayer!=null) //leftPlayer
        {
            lore.set(1, "§b" + arenaName + "." + leftPlayer);
            meta.setLore(lore);
            meta.setDisplayName(MessageManager.msg.Item_ChangeHeading_Left);
            meta.setColor(Color.GREEN);
            leftItem.setItemMeta(meta);
        }
        if(rightPlayer!=null) //rightPlayer
        {
            lore.set(1, "§b" + arenaName + "." + rightPlayer);
            meta.setLore(lore);
            meta.setDisplayName(MessageManager.msg.Item_ChangeHeading_Right);
            meta.setColor(Color.YELLOW);
            rightItem.setItemMeta(meta);
        }
    }

    public static ItemStack GetItem(ItemStack item,int amount)
    {
        ItemStack newItem = item.clone();
        newItem.setAmount(amount);
        return newItem;
    }

    public void GiveMoveTools(UUID uuid, SnakeGame.Heading heading)
    {
        ItemStack targetItem;
        if(heading== SnakeGame.Heading.UP)
            targetItem = upItem;
        else if(heading== SnakeGame.Heading.DOWN)
            targetItem = downItem;
        else if(heading== SnakeGame.Heading.LEFT)
            targetItem = leftItem;
        else
            targetItem = rightItem;
        Player player = Bukkit.getPlayer(uuid);
        if(player==null) return;
        ItemStack[] target = player.getInventory().getContents();
        for(ItemStack item : target)
            if(Objects.requireNonNull(item.getItemMeta()).getDisplayName().equals(Objects.requireNonNull(targetItem.getItemMeta()).getDisplayName()))
            {
                item.setAmount(Math.min(item.getAmount()+ConfigManager.config.toolsPerFood, ConfigManager.config.maxMoveTools));
                return;
            }
    }
}
