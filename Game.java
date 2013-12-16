import java.util.HashMap;
import java.util.Random;

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.08.10
 */

public class Game 
{
    public static void main(String[] args)
    {
        new Game().play();
    }
 
    private final int GAMEHOURS = 2;
    
    private Parser parser;
    private Room currentRoom;
    private HashMap<String, Item> inventory = new HashMap<String, Item>();
    
    private Clock clock = new Clock(GAMEHOURS);
    private int playerSpeed = 1;
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        final Room outside, lowerCorridor, upperCorridor, canteen, kitchen, office, auditorium, classroom, cellar, physlab, chemlab;
        final Item shovel, desk, key, leverBook, uselessBook, transporter, flask, bottle, boiler, red, blue;
        final RoomItem bookcase;
      
        // initialize rooms
        // bottom floor
        outside = new Room("outside the main entrance of the high school");
        lowerCorridor = new Room("in the bottom floor corridor");
        auditorium = new Room("in the auditorium AND THERE'S THE BOMB! It is a new\nkind. If you are going to do this, this is the time. With \ntrembling cogs you have to make a decision: which button to \npress. This mighty decision is put on your robot shoulders");
        canteen = new Room("in the school canteen");
        kitchen = new Room("in the canteen kitchen");
        classroom = new Room("in the classroom for the tenth graders");
        chemlab = new Room("in the chemistry laboratory");
        // second floor
        upperCorridor = new Room("in the second floor corridor");
        office = new Room("in the principal's office. There is a big desk with many drawers");
        physlab = new Room("in the physics laboratory");
        // cellar
        cellar = new Room("in a damp cellar");
        
        
        // initialize room exits
        outside.setExit("inside", lowerCorridor);

        lowerCorridor.setExit("up", upperCorridor);
        lowerCorridor.setExit("down", cellar);
        lowerCorridor.setExit("outside", outside);
        lowerCorridor.setExit("classroom", classroom);
        lowerCorridor.setExit("canteen", canteen);
        lowerCorridor.setExit("auditorium", auditorium);
        lowerCorridor.setExit("lab", physlab);
        
        classroom.setExit("corridor", lowerCorridor);
        
        auditorium.setExit("corridor", lowerCorridor);
        
        canteen.setExit("corridor", lowerCorridor);
        canteen.setExit("kitchen", kitchen);
        
        kitchen.setExit("back", canteen);
        
        physlab.setExit("corridor", lowerCorridor);

        upperCorridor.setExit("down", lowerCorridor);
        upperCorridor.setExit("office", office);
        upperCorridor.setExit("lab", chemlab);
        
        office.setExit("corridor", upperCorridor);
        
        chemlab.setExit("corridor", upperCorridor);
        
        cellar.setExit("up", lowerCorridor);
        
        // initialize room blockages
        upperCorridor.setBlock(office, "There are big amounts of rubble blocking the door. The\n" +
                        "Ogre Power grunts really didn't want anyone to get in here");
        lowerCorridor.setBlock(auditorium, "The Ogre Power grunts have locked this door and put \nogre goo all over it. There is no way a key can enter here.");
        lowerCorridor.setBlock(cellar, "The cellar door is locked. The janitor takes his job \nseriously");
        
        // initialize items
        shovel = new Item("A big shovel with soup dried in. Looks like gulasch.");
        desk = new Item("The principal's mighty desk. There are very many drawers \nin it.");
        key = new Item("The label on this key says \"audotorium\". What could that \nmean?");
        leverBook = new Item("It is just a book. Or is it?");
        uselessBook = new Item("It is a book that has seen the best of its days");
        transporter = new Item("A machine able to bend both time and space");
        flask = new Item("A flask containing something blue. You think it is liquid.");
        bottle = new Item("A bottle containing bubbling acid. You wonder what it will \ndo to a gooey lock.");
        boiler = new Item("An item used to boil and heat stuff. Occasionally eggs.");
        red = new Item("It is a red button. But is it the right one?");
        blue = new Item("It is a blue button. But is it the right one?");
        
        
        // puts all room specific items in their respective room
        kitchen.addItem("shovel", new RoomItemTakeable(shovel));
        
        chemlab.addItem("flask", new RoomItemUseable(flask));
        chemlab.addItem("boiler", new RoomItemUseable(boiler));
        chemlab.addItem("bottle", new RoomItemTakeable(bottle));
        
        office.addItem("desk", new RoomItemUseable(desk));
        
        physlab.addItem("transporter", new RoomItemUseable(transporter));
        
        Room bookcaseRoom = new Room("standing in front of the bookcase");
        bookcaseRoom.addItem("brownbook", new RoomItemUseable(uselessBook));
        bookcaseRoom.addItem("bulkybook", new RoomItemUseable(leverBook));
        bookcaseRoom.addItem("tinybook", new RoomItemUseable(uselessBook));
        bookcase = new RoomItemMultiinteraction(bookcaseRoom, this);
        classroom.addItem("bookcase", bookcase);
        
        auditorium.addItem("blue", new RoomItemUseable(blue));
        auditorium.addItem("red", new RoomItemUseable(red));
        
        // initialize actions
        kitchen.setEnterAction(true, new RoomAction() {
            public void exec() {
                System.out.println("You feel sorry for the kids having to eat food made in");
                System.out.println("this filthy room. You see a shovel that seem to have been");
                System.out.println("used to stir the soup pot.");
            }
        });
        cellar.setEnterAction(true, new RoomAction() {
            public void exec() {
                System.out.println("A spring sounds and a the floor beneath you opens.");
                System.out.println("You don't scream, because you're a robot. After");
                System.out.println("recovering a little you find that the fall hurt");
                System.out.println("your hydraulics. The time it takes for you to move");
                System.out.println("between rooms is doubled.");
                System.out.println("You decide to unlock the door to the cellar from");
                System.out.println("the inside");
                playerSpeed *= 2;
                clock.passTime(5);
                lowerCorridor.unBlock(cellar);
            }
        });
        shovel.setAction(new ItemAction() {
            public String exec(Room r) {
                if (r == upperCorridor) {
                    clock.passTime(5);
                    r.unBlock(office);
                    inventory.remove("shovel");
                    return "You spend five minutes clearing the rubble, but the office\nis now accesible";
                }
                return super.exec(r);
            }
        });
        desk.setAction(new ItemAction() {
            private boolean emptied = false;
            public String exec(Room r) {
                if (emptied) return "There is nothing more to find here.";
                
                emptied = true;
                inventory.put("key", key);
                return "While digging through the drawers you found a key \n" +
                        "with a label on. It might come handy.";
            }
        });
        key.setAction(new ItemAction() {
            public String exec(Room r) {
                if (r == lowerCorridor) {
                    return "The ogre goo makes it impossible to unlock the door";
                }
                return super.exec(r);
            }
        });
        final ItemAction newKeyAction = new ItemAction() {
            public String exec(Room r) {
                if (r == lowerCorridor) {
                    lowerCorridor.unBlock(auditorium);
                    inventory.remove("key");
                    return "Without the ogre goo barring your way, you unlock the \ndoor to the auditorium";
                }
                return super.exec(r);
            }
        };
        leverBook.setAction(new ItemAction() {
            public String exec(Room r) {
                leverBook.setAction(new ItemAction());
                return goRoom(cellar);
            }
        });
        transporter.setAction(new ItemAction() {
            private Random rand = new Random();
            private Room[] locations = { physlab, office, classroom, lowerCorridor, outside, upperCorridor, canteen, kitchen };
            public String exec(Room r) {
                int timechange = rand.nextInt(40) - 10;
                Room target = locations[rand.nextInt(locations.length)];
                
                System.out.println("The bending of spacetime makes it tingle in your robot belly.");
                clock.passTime(timechange);
                return goRoom(target);
            }
        });
        bottle.setAction(new ItemAction() {
            public String exec(Room r) {
                return "The bubbling makes you too afraid to use the acid";
            }
        });
        final ItemAction newBottleAction = new ItemAction() {
            public String exec(Room r) {
                if (r == lowerCorridor) {
                    clock.passTime(10);
                    lowerCorridor.setBlock(auditorium, "There is no goo, but it is still locked");
                    key.setAction(newKeyAction);
                    inventory.remove("bottle");
                    return "The acid burns all the goo away, but it does take its time";
                }
                return "You created a little hole in the floor. Nice!";
            }
        };
        boiler.setAction(new ItemAction() {
            public String exec(Room r) {
                playerSpeed += 2;
                boiler.setAction(new ItemAction());
                bottle.setAction(newBottleAction);
                return "You try to make the acid less dangerous through boiling it.\nIt is a massive failure. At least now you know things \nwon't get worse and you are ready to use the acid. You hurt your \nfoot, however, and it will now take you 2 more minutes to \nmove between rooms";
            }
        });
        red.setAction(new ItemAction() {
            public String exec(Room r) {
                throw new GameOver("Silence. Is time standing still or is it just \nthe timer on the bomb? You wink your \ncameralid and realise it is all over. It is \nover! We are free! FREE!");
            }
        });
        blue.setAction(new ItemAction() {
            public String exec(Room r) {
                throw new GameOver("The story ends, you wake up in your bed and believe \nwhatever you want to believe.");
            }
        });
        
        
        currentRoom = outside;  // start game outside
        
        
        inventory.put("clock", new Item("Your CPU's clock. It runs at one Whoppahertz and is made \nby Swatch") {
            public String use(Room r) {
                return "You have " + clock.getHours() + " hours and " + clock.getMinutes() + " minutes to go.";
            }
        });
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        try {
            boolean finished = false;
            while (! finished) {
                Command command = parser.getCommand();
                finished = processCommand(command);
            }
        } catch (GameOver go) {
            System.out.println(go.getMessage());
            System.out.println();
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new adventure game.");
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
        System.out.println("The year is 2059. You are a bomb diffusal robot with the");
        System.out.println("mission of saving a small town high school from being");
        System.out.println("blown up by the feared bandit gang known as \"Ogre Power\".");
        System.out.println("The bomb is about to go off in " + GAMEHOURS + " hours.");
        System.out.println("Good luck, compadre.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) {
            case UNKNOWN:
                System.out.println("I don't know what you mean...");
                break;

            case HELP:
                printHelp();
                break;

            case GO:
                goRoom(command);
                break;

            case QUIT:
                wantToQuit = quit(command);
                break;
                
            case INVENTORY:
                printInventory();
                break;
                
            case USE:
                useItem(command);
                break;
                
            case LOOK:
                lookItem(command);
                break;
                
            case TAKE:
                takeItem(command);
                break;
        }
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to go in one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door that way!");
        }
        else {
            clock.passTime(playerSpeed);
            
            String blocked = currentRoom.getBlock(nextRoom);
            if (blocked == null) {
                System.out.println(goRoom(nextRoom));
            }
            else {
                System.out.println(blocked);
            }
        }
    }
    
    public String goRoom(Room nextRoom)
    {
        currentRoom = nextRoom;
        currentRoom.enter();
        return currentRoom.getLongDescription();
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if (command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
    
    /**
     * Prints the name of every item in the inventory.
     */
    private void printInventory()
    {
        System.out.println("Your inventory contains the following items:");
        for (String key : inventory.keySet()) {
            System.out.print(key + " ");
        }
        System.out.println();
    }
    
    /**
     * Uses the given item
     */
    private void useItem(Command cmd)
    {
        if (cmd.getSecondWord() != null) {
            Item item = inventory.get(cmd.getSecondWord());
            if (item != null) {
                System.out.println(item.use(currentRoom));
            } else {
                currentRoom.useItem(cmd.getSecondWord());
                clock.passTime(1);
            }
        }
        else {
            System.out.println("Which item would you like to use?");
        }
    }
    
    /**
     * Looks at the given item
     */
    private void lookItem(Command cmd)
    {
        if(cmd.getSecondWord() == null) {
            System.out.println("What would you like to look at?");
            return;
        }
        
        Item item = inventory.get(cmd.getSecondWord());
        if (item != null) {
            System.out.println(item.getDescription());
        } else {
            String str = currentRoom.lookItem(cmd.getSecondWord());
            if (str == null) {
                System.out.println("There is no such item");
            } else {
                System.out.println(str);
                clock.passTime(1);
            }
        }
    }
    
    /**
     * Takes the item passed as command
     */
    private void takeItem(Command cmd)
    {
        if (!currentRoom.hasItem(cmd.getSecondWord())) {
            System.out.println("There is no such item in this room.");
            return;
        }
        
        Item it = currentRoom.takeItem(cmd.getSecondWord());
        if (it == null) {
            System.out.println("You should not pick this item up.");
        } else {
            inventory.put(cmd.getSecondWord(), it);
            System.out.println("You now carry a " + cmd.getSecondWord());
        }
        clock.passTime(1);
    }
}
