import java.util.Set;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.08.10
 */

public class Room 
{
    private String description;
    private HashMap<String, Room> exits = new HashMap<String, Room>();        // stores exits of this room.
    
    // The key holds the exit which is blocked. The value is the error message 
    private HashMap<Room, String> blockage = new HashMap<Room, String>();
    private HashMap<String, RoomItem> items = new HashMap<String, RoomItem>();
    private boolean entered = false;
    private RoomAction firstEnter = new RoomAction();
    private RoomAction everyEnter = new RoomAction();

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String description) 
    {
        this.description = description;
    }

    /**
     * @return The short description of the room
     * (the one that was defined in the constructor).
     */
    public String getShortDescription()
    {
        return description;
    }

    /**
     * Return a description of the room in the form:
     *     You are in the kitchen.
     *     Exits: north west
     * @return A long description of this room
     */
    public String getLongDescription()
    {
        return "You are " + description + ".\n" + getItemString() + getExitString();
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * @return Details of the room's exits.
     */
    private String getExitString()
    {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }
    
    private String getItemString()
    {
        if (items.size() == 0) return "";
        
        String returnString = "Items: ";
        for (String s : items.keySet()) {
            returnString += s + " ";
        }
        return returnString + "\n";
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }

    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor) 
    {
        exits.put(direction, neighbor);
    }
    
    /**
     * Returns the "error message" if the room is blocked, otherwise null
     * @param blocked
     */
    public String getBlock(Room blocked)
    {
        return blockage.get(blocked);
    }
    
    /**
     * Adds a blockage to the given room. Trying to access it will display
     * the given message. 
     * @param blocked
     * @param message
     */
    public void setBlock(Room blocked, String message)
    {
        blockage.put(blocked, message);
    }
    
    /**
     * Removes the blockage from the given room
     * @param blocked
     */
    public void unBlock(Room blocked)
    {
        blockage.remove(blocked);
    }
    
    /**
     * enter is called every time the player enters the room and will in turn
     * call firstEnter only once and everyEnter whenever the player enter the
     * room.
     */
    public void enter()
    {
        if (!entered) {
            firstEnter.exec();
            entered = true;
        }
        everyEnter.exec();
    }
    
    /**
     * Binds an action to the enter event. If onlyFirst is true, the action
     * will only be executed the first time the player enters the room
     * else it will happen every time the player enters the room.
     * @param onlyFirst
     * @param action
     */
    public void setEnterAction(boolean onlyFirst, RoomAction action)
    {
        if (onlyFirst) {
            firstEnter = action;
        } else {
            everyEnter = action;
        }
    }
    
    public void addItem(String name, RoomItem item)
    {
        items.put(name, item);
        item.setRoom(this);
    }
    
    public boolean hasItem(String name)
    {
        return items.containsKey(name);
    }
    
    public void useItem(String itemname)
    {
        if (!items.containsKey(itemname)) {
            System.out.println("There is no such item.");
        }
        
        System.out.println(items.get(itemname).use());
    }
    
    public Item takeItem(String itemname)
    {
        if (!items.containsKey(itemname)) {
            return null;
        }
        
        Item it = items.get(itemname).take();
        if (it != null) {
            items.remove(itemname);
        }
        return it;
    }
    
    public String lookItem(String itemname)
    {
        if(!items.containsKey(itemname)) {
            return null;
        }
        
        return items.get(itemname).look();
    }
}

