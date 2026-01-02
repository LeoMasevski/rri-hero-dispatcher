package si.um.feri.herodispatcher.data.dto;

import com.badlogic.gdx.utils.Array;
import si.um.feri.herodispatcher.world.dynamic_objects.Hero;

public class HeroData {

    public static Array<Hero> getAllHeroes() {
        Array<Hero> heroes = new Array<>();

        // Spider-Man - strong in agility
        heroes.add(new Hero(
            "Spider-Man",
            "Superhero",
            23,
            "Web-shooters",
            "Bitten by radioactive spider. Great power comes with great responsibility. Lives in New York City and fights crime with spider abilities.",
            4, // strength
            5, // intelligence (Peter Parker is a genius)
            5, // agility
            "images/heroes/spiderman.jpg"
        ));

        // Catwoman - most agile
        heroes.add(new Hero(
            "Catwoman",
            "Anti-hero",
            32,
            "Whip, Claws",
            "Master thief from Gotham City. Skilled acrobat and martial artist. Complex relationship with Batman.",
            3, // strength
            4, // intelligence
            5, // agility (most agile!)
            "images/heroes/catwoman.jpg"
        ));

        // Deadpool - regeneration, strong fighter
        heroes.add(new Hero(
            "Deadpool",
            "Mercenary",
            35,
            "Katanas, Guns",
            "Regenerative healing factor. Breaks the fourth wall. Extremely skilled mercenary with dark humor and unpredictable fighting style.",
            5, // strength (regeneration = high stamina/strength)
            3, // intelligence (a bit confused)
            4, // agility
            "images/heroes/deadpool.jpg"
        ));

        return heroes;
    }
}
