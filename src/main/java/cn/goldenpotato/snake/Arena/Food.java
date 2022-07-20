package cn.goldenpotato.snake.Arena;

import cn.goldenpotato.snake.Snake;
import cn.goldenpotato.snake.Util.Coordinate;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;

public class Food
{
    public enum FoodType
    {
        UP,DOWN,LEFT,RIGHT,NORMAL
    }

    public static FoodType StringToFoodType(String type)
    {
        type = type.toUpperCase();
        switch (type)
        {
            case "UP":
                return FoodType.UP;
            case "DOWN":
                return FoodType.DOWN;
            case "LEFT":
                return FoodType.LEFT;
            case "RIGHT":
                return FoodType.RIGHT;
            default:
                return FoodType.NORMAL;
        }
    }

    public String name;
    Location location;
    public Coordinate coordinate;//提前准备，防止反复创建浪费时间
    Random rnd = new Random();
    SnakeGame game;
    public int lifeTime,birthRate;
    int tickToDie=0;
    BukkitTask task;
    public FoodType type;

    public Food(String name,Location location,SnakeGame game,int lifeTime,int birthRate,FoodType type)
    {
        this.name = name;
        this.location = location;
        coordinate = new Coordinate(location.getBlockX(),location.getBlockZ());
        this.game = game;
        this.lifeTime = lifeTime;
        this.birthRate = birthRate;
        this.type = type;
//        Util.Log("location "+location+" lifeTime"+lifeTime+" birthRate"+ birthRate);
    }

    public void Enable()
    {
        task = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                Tick();
            }
        }.runTaskTimer(Snake.instance,0,2);
    }

    public void Disable()
    {
        task.cancel();
    }

    public void Tick()
    {
        if(tickToDie>0) //该食物点当前有食物
        {
            tickToDie--;
            if(tickToDie<=0)
                Die();
            if(game.world.getBlockAt(location).getType()==Material.AIR) //已经被吃掉了
                tickToDie=0;
        }
        else //当前没有食物
        {
            if(birthRate> rnd.nextInt(1000))
                Born();
        }
    }

    void Born()
    {
        if(game.world.getBlockAt(location).getType()==Material.AIR)
        {
            if(type==FoodType.NORMAL)
                game.world.getBlockAt(location).setType(Material.BROWN_MUSHROOM_BLOCK);
            else if (type==FoodType.UP)
                game.world.getBlockAt(location).setType(Material.REDSTONE_BLOCK);
            else if(type==FoodType.DOWN)
                game.world.getBlockAt(location).setType(Material.LAPIS_BLOCK);
            else if(type==FoodType.LEFT)
                game.world.getBlockAt(location).setType(Material.EMERALD_BLOCK);
            else if(type==FoodType.RIGHT)
                game.world.getBlockAt(location).setType(Material.GOLD_BLOCK);
        }
        tickToDie = lifeTime;
    }

    void Die()
    {
        if(game.world.getBlockAt(location).getType()!=Material.MELON)
            game.world.getBlockAt(location).setType(Material.AIR);
    }
}
