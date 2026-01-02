package si.um.feri.herodispatcher.world.dynamic_objects;

public class Hero {
    private String name;
    private String type;
    private int age;
    private String weapon;
    private String infoFacts;

    // Stats (0-5) - only 3 stats
    private int strength;
    private int intelligence;
    private int agility;

    private String heroId; // ID for asset lookup (e.g. "spiderman", "catwoman", "deadpool")

    public Hero(String name, String type, int age, String weapon, String infoFacts,
                int strength, int intelligence, int agility, String heroId) {
        this.name = name;
        this.type = type;
        this.age = age;
        this.weapon = weapon;
        this.infoFacts = infoFacts;
        this.strength = strength;
        this.intelligence = intelligence;
        this.agility = agility;
        this.heroId = heroId;
    }

    // Getters
    public String getName() { return name; }
    public String getType() { return type; }
    public int getAge() { return age; }
    public String getWeapon() { return weapon; }
    public String getInfoFacts() { return infoFacts; }
    public int getStrength() { return strength; }
    public int getIntelligence() { return intelligence; }
    public int getAgility() { return agility; }
    public String getHeroId() { return heroId; }

    // Keep old method for backward compatibility
    public String getImagePath() {
        return "images/heroes/" + heroId + ".jpg";
    }
}
