package si.um.feri.herodispatcher;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.Array;

import si.um.feri.herodispatcher.data.GameDataLoader;
import si.um.feri.herodispatcher.screens.MainScreen;
import si.um.feri.herodispatcher.world.static_objects.CrimeDefinition;
import si.um.feri.herodispatcher.world.static_objects.CrimeLocation;

public class HeroDispatcherGame extends Game {

    @Override
    public void create() {
        setScreen(new MainScreen(this));
    }
}
