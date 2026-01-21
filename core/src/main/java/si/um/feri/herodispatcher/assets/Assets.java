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

    // New hero textures
    public Texture heroAngel;
    public Texture heroMime;
    public Texture heroWhistle;

    public Assets() {
        manager = new AssetManager();
    }

    public void load() {
        font = new BitmapFont();

        // Load UI skin
        manager.load(AssetsPath.SKIN_JSON, Skin.class, new SkinLoader.SkinParameter(AssetsPath.SKIN_ATLAS));

        // Load new hero textures
        manager.load(AssetDescriptors.HERO_ANGEL);
        manager.load(AssetDescriptors.HERO_MIME);
        manager.load(AssetDescriptors.HERO_WHISTLE);

        manager.finishLoading();

        // Get loaded assets
        uiSkin = manager.get(AssetsPath.SKIN_JSON, Skin.class);
        heroAngel = manager.get(AssetDescriptors.HERO_ANGEL);
        heroMime = manager.get(AssetDescriptors.HERO_MIME);
        heroWhistle = manager.get(AssetDescriptors.HERO_WHISTLE);
    }

    public void dispose() {
        manager.dispose();
    }
}
