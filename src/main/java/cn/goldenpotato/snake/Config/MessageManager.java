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

        msg.Item_ChangeHeading_Up = messageReader.getString("Item_ChangeHeading_Up");
        msg.Item_ChangeHeading_Down = messageReader.getString("Item_ChangeHeading_Down");
        msg.Item_ChangeHeading_Left = messageReader.getString("Item_ChangeHeading_Left");
        msg.Item_ChangeHeading_Right = messageReader.getString("Item_ChangeHeading_Right");
        msg.Item_ChangeHeading_Lore = messageReader.getString("Item_ChangeHeading_Lore");
        msg.Item_Leave = messageReader.getString("Item_Leave");
        msg.Item_Leave_Lore = messageReader.getString("Item_Leave_Lore");

        msg.Game_CountDown = messageReader.getString("Game_CountDown");
        msg.Game_Loss = messageReader.getString("Game_Loss");

        msg.Command_CommandManager_NoCommandInConsole = messageReader.getString("Command_CommandManager_NoCommandInConsole");
        msg.SubCommand_Create_Usage = messageReader.getString("SubCommand_Create_Usage");
        msg.SubCommand_Create_ArenaExist = messageReader.getString("SubCommand_Create_ArenaExist");
        msg.SubCommand_Create_WrongPlayerNum = messageReader.getString("SubCommand_Create_WrongPlayerNum");
        msg.SubCommand_Join_Usage = messageReader.getString("SubCommand_Join_Usage");
        msg.SubCommand_Join_AlreadyInArena = messageReader.getString("SubCommand_Join_AlreadyInArena");
        msg.SubCommand_Join_Full = messageReader.getString("SubCommand_Join_Full");
        msg.SubCommand_Join_ArenaNotExist = messageReader.getString("SubCommand_Join_ArenaNotExist");
        msg.SubCommand_Leave_Usage = messageReader.getString("SubCommand_Leave_Usage");
        msg.SubCommand_Leave_NotInGame = messageReader.getString("SubCommand_Leave_NotInGame");
        msg.SubCommand_Leave_Success = messageReader.getString("SubCommand_Leave_Success");
        msg.SubCommand_Save_Success = messageReader.getString("SubCommand_Save_Success");
        msg.SubCommand_Set_Usage = messageReader.getString("SubCommand_Set_Usage");
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
        msg.SubCommand_Stop_Usage = messageReader.getString("SubCommand_Stop_Usage");
        msg.SubCommand_Stop_ArenaNotExist = messageReader.getString("SubCommand_Stop_ArenaNotExist");
        msg.SubCommand_Stop_NotInGame = messageReader.getString("SubCommand_Stop_NotInGame");
    }
}
