
public class RoomItemTakeable extends RoomItemUseable
{
    public RoomItemTakeable(Item it)
    {
        super(it);
    }
    
    public Item take()
    {
        return item;
    }
}
