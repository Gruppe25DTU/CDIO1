package view;

import java.util.HashMap;
import java.util.Map;

import view.TUIController.Command;

public class Menu {
	
	

	private Map<String, Command> commandMap;
	private String menuMsg;
	
	public Menu(String msg)
	{
		this.menuMsg = msg;
		commandMap = new HashMap<String, Command>();
	}
	
	public void addCommand(Command newCommand)
	{
		commandMap.put(newCommand.trigger, newCommand);
	}
	
	public void removeCommand(String trigger)
	{
		commandMap.remove(trigger);
	}
	
	public Command get(String trigger)
	{
		return commandMap.get(trigger);
	}
	
	public void setMsg(String msg)
	{
		this.menuMsg = msg;
	}
	
	public String toString()
	{
		String result = menuMsg+"\n";
		for(String key : commandMap.keySet())
			result += "["+key+"]"+ " : "+commandMap.get(key)+"\n";
		return result;
	}
	
	
}
