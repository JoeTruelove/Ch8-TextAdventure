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
 * @author  Joseph Truelove
 * @version 2018.11.05
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
        
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
        Room cementEnt, oldCement, graveYard, brokenLab, oldGrave, oldHouse, oldSmoke, stars, supplyHouse, supplyBase, hidden, kings;
      
        // create the rooms
        cementEnt = new Room("outside the main entrance of an old Cementary");
        oldCement = new Room("in a old looking Cementary");
        graveYard = new Room("in the grave yard");
        brokenLab = new Room("in front of a broken down lab");
        oldGrave = new Room("in front of a grave that says 'J_s__h Tr__L__e'");
        oldHouse = new Room("inside of a spooky house");
        oldSmoke = new Room("in front of a casket of a dead smoker holding a lighter");
        stars = new Room("a window that has a view of all of the beautiful stars");
        supplyHouse = new Room("in a empty supply house");
        supplyBase = new Room("in a room with a bookcase and a shovel");
        hidden = new Room("behind the bookcase, with a stair case leading up");
        kings = new Room("in front of a skeleton sitting in a throne with a crown on");
        // initialise room exits
        

        cementEnt.setExit("north", oldCement);
        oldCement.setExit("south", cementEnt);
        oldCement.setExit("north", graveYard);
        oldCement.setExit("west", oldHouse);
        oldCement.setExit("east", supplyHouse);
        
        graveYard.setExit("south", oldCement);
        graveYard.setExit("north", brokenLab);
        graveYard.setExit("west", oldGrave);
        brokenLab.setExit("south", graveYard);
        oldGrave.setExit("east", graveYard);
        
        oldHouse.setExit("east", oldCement);
        oldHouse.setExit("west", oldSmoke);
        oldHouse.setExit("south", stars);
        oldSmoke.setExit("east", oldHouse);
        stars.setExit("north", oldHouse);
        
        supplyHouse.setExit("west", oldCement);
        supplyHouse.setExit("down", supplyBase);
        supplyBase.setExit("up", supplyHouse);
        supplyBase.setExit("east", hidden);
        hidden.setExit("west", supplyBase);
        hidden.setExit("up", kings);
        kings.setExit("down", hidden);
        

        currentRoom = cementEnt;  // start game outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
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
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
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
            case LOOK:
                look();
                break;
            case COMMANDS:
                commands();
                break;
            case WHISTLE:
                whistle();
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
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
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
            System.out.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
    
    /**
     * "look" was entered. Gets the long description for the user
     */
    private void look()
    {
        System.out.println(currentRoom.getLongDescription());
    }
    
    /**
     * Prints out list of commands
     */
    private void commands()
    {
        System.out.println("Type: help, go, look, whistle or quit");
    }
    
    /**
     * allows player to whistle.
     */
    private void whistle()
    {
        System.out.println("You whistle but no noise comes out");
    }
}
