package si.um.feri.herodispatcher;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;

import si.um.feri.herodispatcher.assets.Assets;
import si.um.feri.herodispatcher.data.GameDataLoader;
import si.um.feri.herodispatcher.screens.MainScreen;
import si.um.feri.herodispatcher.screens.MenuScreen;
import si.um.feri.herodispatcher.world.static_objects.CrimeDefinition;
import si.um.feri.herodispatcher.world.static_objects.CrimeLocation;

public class HeroDispatcherGame extends Game {
    public Assets assets;

    @Override
    public void create() {
        assets = new Assets();
        assets.load();

        // For testing comment this and ...
        setScreen(new MenuScreen(this));

        // use this
//        setScreen(new MainScreen(this));
    }

    @Override
    public void dispose() {
        assets.dispose();
    }
}
