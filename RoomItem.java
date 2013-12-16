public abstract class RoomItem
{
    protected Room room;
    
    public void setRoom(Room r)
    {
        room = r;
    }
    
    public Item take()
    {
        return null;
    }
    
    public String use()
    {
        return "Nothing happened";
    }
    
    public String look()
    {
        return "There's not much to see";
    }
}
