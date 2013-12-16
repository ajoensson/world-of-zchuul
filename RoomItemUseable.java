
public class RoomItemUseable extends RoomItem
{
    protected Item item;
    
    public RoomItemUseable(Item i)
    {
        item = i;
    }
    
    public String use()
    {
        return item.use(room);
    }
    
    public String look()
    {
        return item.getDescription();
    }
}
