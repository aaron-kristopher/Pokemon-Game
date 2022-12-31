import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Random;

public class PokemonGame {

    static Random random = new Random();
    static Scanner scanner = new Scanner(System.in);
    static boolean runGame = true;

    public static void main (String[] args) {
        
        // INTIALIZE USER POKEMON AND WILDD POKEMONS

        Pokemon userPokemon = new Pokemon(15, 80, "Pikachu", "Thunderbolt");

        Pokemon[] wildPokemonsArray = createWildPokemons();

        // GAME LOOP

        while (runGame) {
            
            PokemonDisplay.displayPokemonLogo();
            displayActionMenu();
            
            switch (getAction()) {
                
                case 1 -> move(wildPokemonsArray, userPokemon);

                case 2 -> rest(userPokemon);

                case 3 -> close();
                    
            }
        }
        PokemonDisplay.displayPokemonLogo();

    }

    // MAIN MENU ACTION METHODS

    public static void move(Pokemon[] wildPokemonsArray, Pokemon userPokemon) {

        if (isEncounterSuccessful()) {

            boolean isAlive = inEncounter(getWildPokemon(wildPokemonsArray), userPokemon, true);
            
            if (!isAlive) {
                System.out.printf("%s fainted!\nYou have whited out...\n", userPokemon);
                System.out.printf("%s was brought to the PokeCenter and healed...\n", userPokemon);
                System.out.println();
            }
        }

        else System.out.println("Nothing happened!");
    }

    public static void rest(Pokemon userPokemon) {

        if (userPokemon.hp != 100) {
            System.out.printf("%s was healed!\n\n", userPokemon);
            userPokemon.healPokemon();
        }

        else System.out.printf("%s already has full HP!\n\n", userPokemon);

    }

    public static void close() {
        runGame = false;
        System.out.println("Closing Program");
    }

    // GAME SETUP METHODS

    public static Pokemon[] createWildPokemons() {
        
        final int MAX_LEVEL = 10;
        final int MIN_LEVEL = 5;

        final int MAX_HP = 100;
        final int MIN_HP = 5;

        Pokemon[] wildPokemonsArray = new Pokemon[3];
        String[] wildPokemonNames = {"Gengar", "Eevee", "Jinx"};

        for (int i = 0; i < wildPokemonsArray.length; i++) {

            String name = wildPokemonNames[random.nextInt((wildPokemonNames.length - 1))];
            int level = (random.nextInt(MAX_LEVEL - MIN_LEVEL) + MIN_LEVEL);
            int hp = (random.nextInt(MAX_HP - MIN_HP) + MIN_HP);

            Pokemon pokemon = new Pokemon(level, hp, name, "Quick Attack");

            wildPokemonsArray[i] = pokemon;
        }

        return wildPokemonsArray;
    }

    public static void displayEncounter(Pokemon pokemon) {
        
        PokemonDisplay.getASCIIdisplay(pokemon);
        System.out.println(pokemon);
        System.out.printf("Level: %d",pokemon.getLevel());
        System.out.printf("\nHP: %d\n", pokemon.hp);
        System.out.println();
    }

    public static boolean isActionSuccessful(int max, int min, int successNumber) {
        
        int chosenActionRate = random.nextInt(((max + 1) - min) + min);
        return (chosenActionRate == successNumber);
    }

    public static boolean isEncounterSuccessful() {
        return isActionSuccessful(3, 1, 1);
    }

    public static boolean isCatchSuccessful() {
        return isActionSuccessful(3, 1, 1);
    }

    public static boolean isEscapeSuccessful() {
        return isActionSuccessful(2, 1, 1);
    }
    
    public static Pokemon getWildPokemon (Pokemon[] wildPokemonsArray) {

        // RANDOMLY CHOOSE WILD POKEMON FROM WILD POKEMON ARRAY

        Pokemon wildPokemon = wildPokemonsArray[((wildPokemonsArray.length - 1) - 0)];
        return wildPokemon;
    }

    // MENU METHODS

    public static void displayActionMenu() {
        System.out.println("Choose an action:");
        System.out.println("[1] Move");
        System.out.println("[2] Rest");
        System.out.println("[3] Quit Game");
        System.out.println();
    }

    public static void displayEncounterMenu() {
        System.out.println("Choose an action:");
        System.out.println("[1] Attack");
        System.out.println("[2] Run");
        System.out.println("[3] Catch");
        System.out.println();
    }
    
    public static int getAction() {
        
        int action;

        try {
            action = scanner.nextInt();
        }
        catch (InputMismatchException ime)
        {
            System.out.println("INVALID INPUT!");
            System.out.println();
            scanner.nextLine();
            System.out.println();
            
            return getAction();
        }
        return action;
    }
    
    // RECURSIVE ENCOUNTER METHOD

    public static boolean inEncounter(Pokemon wildPokemon, Pokemon userPokemon, boolean isStartEncounter) {

        // CHECKS IF IT IS THE START OF THE ENCOUNTER
        // PRINTS THE BEGINNING ENCOUNTER PROMPT

        if (isStartEncounter) {
            System.out.printf("\nA wild %s appeared!\n\n", wildPokemon);
            displayEncounter(wildPokemon);
            displayEncounter(userPokemon);
        }

        // CHECKS IF USER POKEMON IS STILL ALIVE
        // OTHERWISE ENDS RECURSION AND ENDS ENCOUNTER LOOP

        if (userPokemon.getHP() <= 0) return false;



        return switch (getAction()) {
            
            case 1 -> attack(userPokemon, wildPokemon);

            case 2 -> escape(userPokemon, wildPokemon);

            case 3 -> catchPokemon(userPokemon, wildPokemon);

            default -> true;
        
        };
    }

    // POKEMON ENCOUNTER ACTION METHODS

    public static boolean attack(Pokemon userPokemon, Pokemon wildPokemon) {
        
        // USER POKEMON ATTACKS

        System.out.printf("%s took %d points of damage!\n", wildPokemon, wildPokemon.takeDamage(userPokemon.attack()));
        displayEncounter(wildPokemon);
        System.out.println();

        // CHECKS IF WILD POKEMON FAINTED

        if (wildPokemon.getHP() <= 0) {
            System.out.printf("%s has fainted!\n", wildPokemon);
            return true;
        }

        // WILD POKEMON ATTACKS

        System.out.printf("%s took %d points of damage!\n", userPokemon, userPokemon.takeDamage(wildPokemon.attack()));
        displayEncounter(userPokemon);
        System.out.println();
        
        return inEncounter(wildPokemon, userPokemon, false);
    }

    public static boolean escape(Pokemon userPokemon, Pokemon wildPokemon) {

        if (isEscapeSuccessful()) {
            System.out.println("You have successfully escaped!\n");
            return true;
            }

            // WILD POKEMON ATTACKS IF ESCAPE IS UNSUCCESSFUL
            
            System.out.println("You have failed to escape!");
            System.out.println();

            System.out.printf("%s took %d points of damage!\n", userPokemon, userPokemon.takeDamage(wildPokemon.attack()));
            displayEncounter(userPokemon);
            System.out.println();

            return inEncounter(wildPokemon, userPokemon, false);
    }

    public static boolean catchPokemon(Pokemon userPokemon, Pokemon wildPokemon) {

        // THROWS POKEBALL

        System.out.println("You threw a pokeball!");
        System.out.println();

        if (isCatchSuccessful()) {
            System.out.printf("You have successfully caught %s\n", wildPokemon);
            PokemonDisplay.displayASCIIcaught();
            return true;
        }
        
        // WILD POKEMON ATTACKS IF CATCH IS UNSUCCESSFUL

        System.out.printf("%s escaped the ball!\n", wildPokemon);
        displayEncounter(wildPokemon);
        
        System.out.printf("%s took %d points of damage!\n", userPokemon, userPokemon.takeDamage(wildPokemon.attack()));
        displayEncounter(userPokemon);
        

        return inEncounter(wildPokemon, userPokemon, false);
    }
}

