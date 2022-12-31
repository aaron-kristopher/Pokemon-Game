import java.util.Random;

public class Pokemon {

    int level;
    int hp;
    String type;
    String name;
    String skill;
    
    public Pokemon(int level, int hp, String name, String skill) {

        this.level = level;
        this.hp = hp;
        this.name = name;       
        this.skill = skill; 
    }

    public int getHP() {
        return hp;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return name;
    }

    public int attack() {

        System.out.printf("%s used %s!\n", name, skill);
        System.out.println();
        return getAttackDamage();
    }

    private int getAttackDamage() {

        Random random = new Random();

        int min = 5;
        int max = 20;

        return random.nextInt((max - min) + min);

    }

    public int takeDamage(int damage) {

        hp -= damage;
        return damage;
    }

    public boolean isAlive() {
        return (hp != 0);
    }

    public void healPokemon() {
        hp = 100;
    }
}