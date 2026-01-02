package si.um.feri.herodispatcher.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {
    private AssetManager manager;
    public Skin uiSkin;
    public BitmapFont font;

    // Hero textures
    public Texture heroSpiderman;
    public Texture heroCatwoman;
    public Texture heroDeadpool;

    public Assets() {
        manager = new AssetManager();
    }

    public void load() {
        font = new BitmapFont();

        // Load UI skin
        manager.load(AssetsPath.SKIN_JSON, Skin.class, new SkinLoader.SkinParameter(AssetsPath.SKIN_ATLAS));

        // Load hero textures
        manager.load(AssetDescriptors.HERO_SPIDERMAN);
        manager.load(AssetDescriptors.HERO_CATWOMAN);
        manager.load(AssetDescriptors.HERO_DEADPOOL);

        manager.finishLoading();

        // Get loaded assets
        uiSkin = manager.get(AssetsPath.SKIN_JSON, Skin.class);
        heroSpiderman = manager.get(AssetDescriptors.HERO_SPIDERMAN);
        heroCatwoman = manager.get(AssetDescriptors.HERO_CATWOMAN);
        heroDeadpool = manager.get(AssetDescriptors.HERO_DEADPOOL);
    }

    public void dispose() {
        manager.dispose();
    }
}
