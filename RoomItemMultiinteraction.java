
public class RoomItemMultiinteraction extends RoomItem
{
    private Room itemRoom;
    private Game game;
    
    public RoomItemMultiinteraction(Room newRoom, Game g)
    {
        itemRoom = newRoom;
        game = g;
    }
    
    public void setRoom(Room r)
    {
        super.setRoom(r);
        itemRoom.setExit("back", r);
    }
    
    public String look()
    {
        return game.goRoom(itemRoom);
    }
}
