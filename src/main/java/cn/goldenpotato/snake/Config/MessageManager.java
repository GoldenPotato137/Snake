package cn.goldenpotato.snake.Config;

import cn.goldenpotato.snake.Snake;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MessageManager
{
    public static Message msg;
    public static void LoadMessage()
    {
        msg = new Message();
        Snake.instance.saveResource("message_"+ConfigManager.config.language+".yml",true);
        File messageFile = new File(Snake.instance.getDataFolder(), "message_"+ConfigManager.config.language+".yml");
        FileConfiguration messageReader = YamlConfiguration.loadConfiguration(messageFile);

        msg.Util_WrongNum = messageReader.getString("Util_WrongNum");
        msg.Util_WrongCommand = messageReader.getString("Util_WrongCommand");
        msg.Util_NoPermission = messageReader.getString("Util_NoPermission");
        msg.Util_ArenaNotFound = messageReader.getString("Util_ArenaNotFound");
        msg.Util_TeamChat = messageReader.getString("Util_TeamChat");

        msg.Item_ChangeHeading_Up = messageReader.getString("Item_ChangeHeading_Up");
        msg.Item_ChangeHeading_Down = messageReader.getString("Item_ChangeHeading_Down");
        msg.Item_ChangeHeading_Left = messageReader.getString("Item_ChangeHeading_Left");
        msg.Item_ChangeHeading_Right = messageReader.getString("Item_ChangeHeading_Right");
        msg.Item_ChangeHeading_Lore = messageReader.getString("Item_ChangeHeading_Lore");
        msg.Item_Leave = messageReader.getString("Item_Leave");
        msg.Item_Leave_Lore = messageReader.getString("Item_Leave_Lore");

        msg.Game_CountDown = messageReader.getString("Game_CountDown");
        msg.Game_Loss = messageReader.getString("Game_Loss");
        msg.Game_Victory = messageReader.getString("Game_Victory");

        msg.Command_CommandManager_NoCommandInConsole = messageReader.getString("Command_CommandManager_NoCommandInConsole");
        msg.SubCommand_Create_Usage = messageReader.getString("SubCommand_Create_Usage");
        msg.SubCommand_Create_ArenaExist = messageReader.getString("SubCommand_Create_ArenaExist");
        msg.SubCommand_Create_WrongPlayerNum = messageReader.getString("SubCommand_Create_WrongPlayerNum");
        msg.SubCommand_Game_Usage = messageReader.getString("SubCommand_Game_Usage");
        msg.SubCommand_Game_Help_1 = messageReader.getString("SubCommand_Game_Help_1");
        msg.SubCommand_Game_Help_2 = messageReader.getString("SubCommand_Game_Help_2");
        msg.SubCommand_Game_Help_3 = messageReader.getString("SubCommand_Game_Help_3");
        msg.SubCommand_Game_Help_4 = messageReader.getString("SubCommand_Game_Help_4");
        msg.SubCommand_Help_Usage = messageReader.getString("SubCommand_Help_Usage");
        msg.SubCommand_Help_NoSuchCommand = messageReader.getString("SubCommand_Help_NoSuchCommand");
        msg.SubCommand_Join_Usage = messageReader.getString("SubCommand_Join_Usage");
        msg.SubCommand_Join_AlreadyInArena = messageReader.getString("SubCommand_Join_AlreadyInArena");
        msg.SubCommand_Join_Full = messageReader.getString("SubCommand_Join_Full");
        msg.SubCommand_Join_ArenaNotExist = messageReader.getString("SubCommand_Join_ArenaNotExist");
        msg.SubCommand_Leave_Usage = messageReader.getString("SubCommand_Leave_Usage");
        msg.SubCommand_Leave_NotInGame = messageReader.getString("SubCommand_Leave_NotInGame");
        msg.SubCommand_Leave_Success = messageReader.getString("SubCommand_Leave_Success");
        msg.SubCommand_Reload_Usage = messageReader.getString("SubCommand_Reload_Usage");
        msg.SubCommand_Save_Usage = messageReader.getString("SubCommand_Save_Usage");
        msg.SubCommand_Save_Success = messageReader.getString("SubCommand_Save_Success");
        msg.SubCommand_Set_Usage = messageReader.getString("SubCommand_Set_Usage");
        msg.SubCommand_Set_Usage2 = messageReader.getString("SubCommand_Set_Usage2");
        msg.SubCommand_Set_Usage3 = messageReader.getString("SubCommand_Set_Usage3");
        msg.SubCommand_Set_Usage4 = messageReader.getString("SubCommand_Set_Usage4");
        msg.SubCommand_Set_ArenaNotExist = messageReader.getString("SubCommand_Set_ArenaNotExist");
        msg.SubCommand_Set_FoodWrongName = messageReader.getString("SubCommand_Set_FoodWrongName");
        msg.SubCommand_Set_FoodAlreadyExist = messageReader.getString("SubCommand_Set_FoodAlreadyExist");
        msg.SubCommand_Set_FoodSuccess = messageReader.getString("SubCommand_Set_FoodSuccess");
        msg.SubCommand_Set_BeginPosSuccess = messageReader.getString("SubCommand_Set_BeginPosSuccess");
        msg.SubCommand_Set_LobbyPosSuccess = messageReader.getString("SubCommand_Set_LobbyPosSuccess");
        msg.SubCommand_Set_LeavePosSuccess = messageReader.getString("SubCommand_Set_LeavePosSuccess");
        msg.SubCommand_Start_Usage = messageReader.getString("SubCommand_Start_Usage");
        msg.SubCommand_Start_ArenaNotExist = messageReader.getString("SubCommand_Start_ArenaNotExist");
        msg.SubCommand_Start_AlreadyStart = messageReader.getString("SubCommand_Start_AlreadyStart");
        msg.SubCommand_Status_Usage = messageReader.getString("SubCommand_Status_Usage");
        msg.SubCommand_Stop_Usage = messageReader.getString("SubCommand_Stop_Usage");
        msg.SubCommand_Stop_ArenaNotExist = messageReader.getString("SubCommand_Stop_ArenaNotExist");
        msg.SubCommand_Stop_NotInGame = messageReader.getString("SubCommand_Stop_NotInGame");

        msg.SnakeGame_ArenaName = messageReader.getString("SnakeGame_ArenaName");
        msg.SnakeGame_SnakeNum = messageReader.getString("SnakeGame_SnakeNum");
        msg.SnakeGame_MinSnakeNum = messageReader.getString("SnakeGame_MinSnakeNum");
        msg.SnakeGame_PlayerPerSnake = messageReader.getString("SnakeGame_PlayerPerSnake");
        msg.SnakeGame_FoodNum = messageReader.getString("SnakeGame_FoodNum");
        msg.SnakeGame_SpawnNum = messageReader.getString("SnakeGame_SpawnNum");
        msg.SnakeGame_GameStatus = messageReader.getString("SnakeGame_GameStatus");
        msg.SnakeGame_InGame = messageReader.getString("SnakeGame_InGame");
        msg.SnakeGame_Waiting = messageReader.getString("SnakeGame_Waiting");
        msg.SnakeGame_PlayerNum = messageReader.getString("SnakeGame_PlayerNum");
        msg.SnakeGame_InitialSpeed = messageReader.getString("SnakeGame_InitialSpeed");
        msg.SnakeGame_CountDown = messageReader.getString("SnakeGame_CountDown");
        msg.SnakeGame_WaitingTitle = messageReader.getString("SnakeGame_WaitingTitle");
        msg.SnakeGame_VictoryConditionLength = messageReader.getString("SnakeGame_VictoryConditionLength");
        msg.SnakeGame_VictoryConditionSnake = messageReader.getString("SnakeGame_VictoryConditionSnake");
        msg.Snake_Welcome = messageReader.getString("Snake_Welcome");
        msg.Snake_Welcome_Control = messageReader.getString("Snake_Welcome_Control");
        msg.Snake_Welcome_Teammate = messageReader.getString("Snake_Welcome_Teammate");
        msg.Snake_Welcome_Help = messageReader.getString("Snake_Welcome_Help");
        msg.Snake_Welcome_Chat = messageReader.getString("Snake_Welcome_Chat");
        msg.Snake_Status = messageReader.getString("Snake_Status");
        msg.Snake_Status_Dead = messageReader.getString("Snake_Status_Dead");
        msg.Snake_Status_Alive = messageReader.getString("Snake_Status_Alive");
        msg.Snake_Player = messageReader.getString("Snake_Player");
        msg.Snake_Facing_Up = messageReader.getString("Snake_Facing_Up");
        msg.Snake_Facing_Down = messageReader.getString("Snake_Facing_Down");
        msg.Snake_Facing_Left = messageReader.getString("Snake_Facing_Left");
        msg.Snake_Facing_Right = messageReader.getString("Snake_Facing_Right");
        msg.Snake_Facing_Null = messageReader.getString("Snake_Facing_Null");
    }
}
