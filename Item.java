
public class Item {
        private String description;
        private ItemAction useage = new ItemAction();
        
        public Item(String desc)
        {
            description = desc;
        }
        
        /**
         * This method is called when the player enters the use command.
         * To make the Item useful you can choose to override either use(Room) or use()
         * depending on whether it needs information about a room or not. 
         * @param currentRoom
         * @return
         */
        public String use(Room currentRoom)
        {
            return useage.exec(currentRoom);
        }
        
        public void setAction(ItemAction action)
        {
            useage = action;
        }
        
        public String getDescription()
        {
            return description;
        }
}
