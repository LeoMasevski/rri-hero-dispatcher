package si.um.feri.herodispatcher.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {
    private AssetManager manager;
    public Skin uiSkin;
    public BitmapFont font;

    public Assets() {
        manager = new AssetManager();
    }

    public void load() {
        font = new BitmapFont();

        manager.load(AssetsPath.SKIN_JSON, Skin.class, new SkinLoader.SkinParameter(AssetsPath.SKIN_ATLAS));
        manager.finishLoading();
        uiSkin = manager.get(AssetsPath.SKIN_JSON, Skin.class);
    }

    public void dispose() {
        manager.dispose();
    }
}
