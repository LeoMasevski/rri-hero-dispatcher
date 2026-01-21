package si.um.feri.herodispatcher.data.dto;

import com.badlogic.gdx.utils.Array;
import si.um.feri.herodispatcher.world.dynamic_objects.Hero;

public class HeroData {

    public static Array<Hero> getAllHeroes() {
        Array<Hero> heroes = new Array<>();

        // Angel - balanced hero with wings
        heroes.add(new Hero(
            "Angel",
            "Mutant",
            28,
            "Wings, Aerial Combat",
            "Mutant with large feathered wings granting flight. Enhanced strength and agility in aerial combat. Former X-Men member known for nobility and honor.",
            4, // strength
            4, // intelligence
            5, // agility - highest (flight!)
            "angel"
        ));

        // Mime - intelligence focused
        heroes.add(new Hero(
            "Mime",
            "Psychic",
            30,
            "Psychic Barriers",
            "Creates invisible psychic force fields and barriers. Master of silent combat and stealth. Uses mime techniques to confuse and trap enemies.",
            3, // strength
            5, // intelligence - highest (psychic powers)
            4, // agility
            "mime"
        ));

        // Whistle - strength focused
        heroes.add(new Hero(
            "Whistle",
            "Vigilante",
            35,
            "Sonic Whistle, Enhanced Reflexes",
            "Uses powerful sonic whistles to disorient enemies and signal for help. Expert driver and street fighter. Protects the city's highways and streets.",
            5, // strength - highest (physical combat)
            3, // intelligence
            4, // agility
            "whistle"
        ));

        return heroes;
    }
}
