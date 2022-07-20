package cn.goldenpotato.snake.Arena;

import cn.goldenpotato.snake.Config.MessageManager;
import cn.goldenpotato.snake.Util.Coordinate;
import cn.goldenpotato.snake.Util.ItemUtil;
import cn.goldenpotato.snake.Util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class Snake
{
    public enum SnakeStatus
    {
        ALIVE, DEAD, STOP
    }

    SnakeGame game;
    public SnakeStatus snakeStatus = SnakeStatus.DEAD;
    public SnakeGame.Heading heading;
    public int maxPlayer;
    int ticks = 0, coolDownTick;
    int length;
    public UUID playerUp, playerDown, playerLeft, playerRight;
    public World world;
    public Coordinate beginPos;
    HashMap<UUID, Inventory> inventories = new HashMap<>();
    HashMap<UUID, ItemStack[]> inventoriesBackup = new HashMap<>();
    public List<UUID> players = new ArrayList<>();
    public int y;
    Deque<Coordinate> snake;
    BukkitTask task;
    ItemUtil item = new ItemUtil();

    public Snake(SnakeGame game)
    {
        this.game = game;
        maxPlayer = game.playerPerSnake;
        world = game.world;
        y = game.y;
    }

    public List<SnakeGame.Heading> Join(UUID player, Inventory inventory)
    {
        //寄存物品并清空背包
        inventories.put(player, inventory);
        inventoriesBackup.put(player, inventory.getContents());
        inventory.clear();
        players.add(player);
        List<SnakeGame.Heading> result = new ArrayList<>();
        if (maxPlayer == 1)
        {
            result.add(SnakeGame.Heading.UP);
            result.add(SnakeGame.Heading.DOWN);
            result.add(SnakeGame.Heading.LEFT);
            result.add(SnakeGame.Heading.RIGHT);
            playerUp = playerDown = playerLeft = playerRight = player;
        }
        else if (maxPlayer == 2)
        {
            if (playerUp == null) //分配上下方向
            {
                result.add(SnakeGame.Heading.UP);
                result.add(SnakeGame.Heading.DOWN);
                playerUp = playerDown = player;
            }
            else //分配左右方向
            {
                result.add(SnakeGame.Heading.LEFT);
                result.add(SnakeGame.Heading.RIGHT);
                playerLeft = playerRight = player;
            }
        }
        else
        {
            if (playerUp == null)
            {
                result.add(SnakeGame.Heading.UP);
                playerUp = player;
            }
            else if (playerDown == null)
            {
                result.add(SnakeGame.Heading.DOWN);
                playerDown = player;
            }
            else if (playerLeft == null)
            {
                result.add(SnakeGame.Heading.LEFT);
                playerLeft = player;
            }
            else if (playerRight == null)
            {
                result.add(SnakeGame.Heading.RIGHT);
                playerRight = player;
            }
        }
        item.SetItem(game.name, playerUp, playerDown, playerLeft, playerRight);
        StandBy(player);
        return result;
    }

    void StandBy(UUID player)
    {
        Inventory inv = inventories.get(player);
        inv.clear();
        ItemStack leaveItem = new ItemStack(Material.NETHER_STAR);
        ItemMeta leaveMeta = leaveItem.getItemMeta();
        if (leaveMeta == null) return;
        leaveMeta.setDisplayName(MessageManager.msg.Item_Leave);
        leaveMeta.setLore(Collections.singletonList(MessageManager.msg.Item_Leave_Lore));
        leaveItem.setItemMeta(leaveMeta);
        inv.setItem(8, leaveItem);
    }

    public void Leave(UUID player)
    {
        if (snakeStatus != SnakeStatus.DEAD)
            Stop();
        if (!players.contains(player)) return;
        if (playerUp == player) playerUp = null;
        if (playerDown == player) playerDown = null;
        if (playerLeft == player) playerLeft = null;
        if (playerRight == player) playerRight = null;
        players.remove(player);
        //恢复物品
        inventories.get(player).clear();
        inventories.get(player).setContents(inventoriesBackup.get(player));
        inventories.remove(player);
        inventoriesBackup.remove(player);
    }

    public void Stop()
    {
        if (snakeStatus == SnakeStatus.DEAD) return;
        for (UUID player : players)
            Util.Title(Objects.requireNonNull(Bukkit.getPlayer(player)), MessageManager.msg.Game_Loss, 30);
        snakeStatus = SnakeStatus.DEAD;
        game.SnakeDie();
        task.cancel();
        for (UUID player : players)
            StandBy(player);
        //清理蛇
        for (Coordinate c : snake)
            world.getBlockAt(c.x, y, c.z).setType(Material.AIR);
    }

    /**
     * 启用这条蛇
     */
    void StartGame(Coordinate beginPos)
    {
        this.beginPos = beginPos;
        //修改游戏状态
        snakeStatus = SnakeStatus.ALIVE;
        ticks = 0;
        for (UUID player : players)
        {
            Util.Title(Objects.requireNonNull(cn.goldenpotato.snake.Snake.instance.getServer().getPlayer(player)), MessageManager.msg.Game_CountDown, 100);
            Objects.requireNonNull(Bukkit.getPlayer(player)).teleport(new Location(world, beginPos.x, y, beginPos.z));
        }
        new BukkitRunnable()
        {
            int sec = 0;

            @Override
            public void run()
            {
                if (sec <= 6)
                    Util.PlaySound(Util.SoundType.COUNT_DOWN, players);
                else
                    Util.PlaySound(Util.SoundType.COUNT_DOWN2, players);
                sec++;
                for (Inventory inv : inventories.values())
                    inv.setItem(4, new ItemStack(Material.CLOCK, 10 - sec));
                if (sec == 7 && players.size() == maxPlayer)
                    NewGame();
                else if (sec == 10)
                    this.cancel();
            }
        }.runTaskTimer(cn.goldenpotato.snake.Snake.instance, 0, 20);
    }

    public void NewGame()
    {
        //蛇
        snake = new LinkedList<>();
        snake.add(beginPos);
        world.getBlockAt(beginPos.x, y, beginPos.z).setType(Material.PUMPKIN);
        length = 1;
        heading = SnakeGame.Heading.UP;
        //发放物品
        if (playerUp != null)
            inventories.get(playerUp).addItem(ItemUtil.GetItem(item.UpItem, 30));
        if (playerDown != null)
            inventories.get(playerDown).addItem(ItemUtil.GetItem(item.DownItem, 30));
        if (playerLeft != null)
            inventories.get(playerLeft).addItem(ItemUtil.GetItem(item.LeftItem, 30));
        if (playerRight != null)
            inventories.get(playerRight).addItem(ItemUtil.GetItem(item.RightItem, 30));
        //倒计时开始timer
        coolDownTick = 10 - maxPlayer;
        task = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                Tick();
            }
        }.runTaskTimer(cn.goldenpotato.snake.Snake.instance, 60, 2);
        //倒计时
        new BukkitRunnable()
        {
            int sec = 0;

            @Override
            public void run()
            {
                if (sec == 0)
                    for (UUID player : players)
                        Util.Title(Objects.requireNonNull(cn.goldenpotato.snake.Snake.instance.getServer().getPlayer(player)), "§c&l3", 20);
                else if (sec == 1)
                    for (UUID player : players)
                        Util.Title(Objects.requireNonNull(cn.goldenpotato.snake.Snake.instance.getServer().getPlayer(player)), "§c&l2", 20);
                else if (sec == 2)
                    for (UUID player : players)
                        Util.Title(Objects.requireNonNull(cn.goldenpotato.snake.Snake.instance.getServer().getPlayer(player)), "§c&l1", 20);
                else if (sec == 3)
                {
                    for (UUID player : players)
                        Util.Title(Objects.requireNonNull(cn.goldenpotato.snake.Snake.instance.getServer().getPlayer(player)), "§c&lGo!", 10);
                    this.cancel();
                }
                sec++;
            }
        }.runTaskTimer(cn.goldenpotato.snake.Snake.instance, 0, 20);
    }

    public void Move()
    {
//        Util.Log(heading.toString());
        if (snake.isEmpty()) return;
        Coordinate nextPosition = new Coordinate(snake.getLast().x, snake.getLast().z);
        if (heading == SnakeGame.Heading.UP)
            nextPosition.x--;
        else if (heading == SnakeGame.Heading.DOWN)
            nextPosition.x++;
        else if (heading == SnakeGame.Heading.LEFT)
            nextPosition.z++;
        else
            nextPosition.z--;
        //蛇尾向前移动一步
        Coordinate tail = snake.removeFirst();
        world.getBlockAt(tail.x, y, tail.z).setType(Material.AIR);
        //判断前方是否合法
        if (world.getBlockAt(nextPosition.x, y, nextPosition.z).getType() == Material.BROWN_MUSHROOM_BLOCK)//吃到食物
        {
            length++;
            if (length % 5 == 0 && coolDownTick > 5)
                coolDownTick--;
            world.getBlockAt(tail.x, y, tail.z).setType(Material.MELON);
            snake.addFirst(tail);
            Util.PlaySound(Util.SoundType.FOOD_SOUND, players);
        }
        else if (world.getBlockAt(nextPosition.x, y, nextPosition.z).getType() == Material.REDSTONE_BLOCK) //奖励：上
        {
            inventories.get(playerUp).addItem(ItemUtil.GetItem(item.UpItem, 10));
            Util.PlaySound(Util.SoundType.FOOD_SOUND, players);
        }
        else if (world.getBlockAt(nextPosition.x, y, nextPosition.z).getType() == Material.LAPIS_BLOCK) //奖励：下
        {
            inventories.get(playerDown).addItem(ItemUtil.GetItem(item.DownItem, 10));
            Util.PlaySound(Util.SoundType.FOOD_SOUND, players);
        }
        else if (world.getBlockAt(nextPosition.x, y, nextPosition.z).getType() == Material.EMERALD_BLOCK) //奖励：左
        {
            inventories.get(playerLeft).addItem(ItemUtil.GetItem(item.LeftItem, 10));
            Util.PlaySound(Util.SoundType.FOOD_SOUND, players);
        }
        else if (world.getBlockAt(nextPosition.x, y, nextPosition.z).getType() == Material.GOLD_BLOCK) //奖励：右
        {
            inventories.get(playerRight).addItem(ItemUtil.GetItem(item.RightItem, 10));
            Util.PlaySound(Util.SoundType.FOOD_SOUND, players);
        }
        else if (world.getBlockAt(nextPosition.x, y, nextPosition.z).getType() == Material.LIME_CONCRETE && nextPosition!=tail) //吃到别的蛇的尾部
        {
            Snake target = game.GetSnake(nextPosition);
            assert target!=null;
            target.Stop();
            while(!target.snake.isEmpty())
                snake.addLast(target.snake.removeFirst());
            for (Coordinate c : snake)
                world.getBlockAt(c.x, y, c.z).setType(Material.MELON);
            world.getBlockAt(snake.getFirst().x, y, snake.getFirst().z).setType(Material.LIME_CONCRETE);
            world.getBlockAt(snake.getLast().x, y, snake.getLast().z).setType(Material.PUMPKIN);
            return;
        }
        else if (world.getBlockAt(nextPosition.x, y, nextPosition.z).getType() != Material.AIR || //吃到自己或碰墙
                world.getBlockAt(nextPosition.x, y - 1, nextPosition.z).getType() == Material.AIR || //前方无地面
                world.getBlockAt(nextPosition.x, y - 1, nextPosition.z).getType() == Material.WATER || //前方无地面
                world.getBlockAt(nextPosition.x, y - 1, nextPosition.z).getType() == Material.LAVA) //前方无地面
        {
            Util.PlaySound(Util.SoundType.LOSS_SOUND, players);
            if (snake.size() < 2)
                Stop();
            else //扣长度&&加速
            {
                Coordinate pos = snake.removeLast();
                world.getBlockAt(pos.x, y, pos.z).setType(Material.AIR);
                world.getBlockAt(snake.getLast().x, y, snake.getLast().z).setType(Material.PUMPKIN);
                length -= 2;
                coolDownTick = Math.max(coolDownTick - 1, 2);
                snakeStatus = SnakeStatus.STOP;
                new BukkitRunnable()
                {
                    int sec = 0;

                    @Override
                    public void run()
                    {
                        if (sec == 3)
                        {
                            for (UUID player : players)
                                Util.Title(Objects.requireNonNull(cn.goldenpotato.snake.Snake.instance.getServer().getPlayer(player)), "§c&lGo!", 10);
                            snakeStatus = SnakeStatus.ALIVE;
                            this.cancel();
                        }
                        else
                            for (UUID player : players)
                                Util.Title(Objects.requireNonNull(cn.goldenpotato.snake.Snake.instance.getServer().getPlayer(player)), "§c&l" + (3 - sec), 20);
                        sec++;
                    }
                }.runTaskTimer(cn.goldenpotato.snake.Snake.instance, 0, 20);
            }
            return;
        }
        //将曾经的蛇头换回西瓜
        if (snake.size() >= 2)
            world.getBlockAt(snake.getLast().x, y, snake.getLast().z).setType(Material.MELON);
        //蛇首向前移动一步
        snake.addLast(nextPosition);
        world.getBlockAt(nextPosition.x, y, nextPosition.z).setType(Material.PUMPKIN);
        //蛇尾方块为黄绿混凝土
        if (snake.size() >= 2)
            world.getBlockAt(snake.getFirst().x, y, snake.getFirst().z).setType(Material.LIME_CONCRETE);
    }

    public void Tick()
    {
        if (snakeStatus != SnakeStatus.ALIVE) return;
        ticks++;
        if (ticks % coolDownTick == 0)
            Move();
    }
}