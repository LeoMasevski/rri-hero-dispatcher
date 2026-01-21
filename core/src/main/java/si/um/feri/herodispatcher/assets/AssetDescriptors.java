package si.um.feri.herodispatcher.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AssetDescriptors {
    public static final AssetDescriptor<Skin> SKIN_JSON = new AssetDescriptor<Skin>(AssetsPath.SKIN_JSON, Skin.class);
    public static final AssetDescriptor<Skin> SKIN_ATLAS = new AssetDescriptor<>(AssetsPath.SKIN_ATLAS, Skin.class);

    // New hero textures
    public static final AssetDescriptor<Texture> HERO_ANGEL = new AssetDescriptor<>(AssetsPath.HERO_ANGEL, Texture.class);
    public static final AssetDescriptor<Texture> HERO_MIME = new AssetDescriptor<>(AssetsPath.HERO_MIME, Texture.class);
    public static final AssetDescriptor<Texture> HERO_WHISTLE = new AssetDescriptor<>(AssetsPath.HERO_WHISTLE, Texture.class);
}
