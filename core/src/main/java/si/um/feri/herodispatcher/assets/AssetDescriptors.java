package si.um.feri.herodispatcher.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AssetDescriptors {
    public static final AssetDescriptor<Skin> SKIN_JSON = new AssetDescriptor<Skin>(AssetsPath.SKIN_JSON, Skin.class);
    public static final AssetDescriptor<Skin> SKIN_ATLAS = new AssetDescriptor<>(AssetsPath.SKIN_ATLAS, Skin.class);

    // Selection panel textures (full versions)
    public static final AssetDescriptor<Texture> HERO_ANGEL = new AssetDescriptor<>(AssetsPath.HERO_ANGEL, Texture.class);
    public static final AssetDescriptor<Texture> HERO_MIME = new AssetDescriptor<>(AssetsPath.HERO_MIME, Texture.class);
    public static final AssetDescriptor<Texture> HERO_WHISTLE = new AssetDescriptor<>(AssetsPath.HERO_WHISTLE, Texture.class);

    // Map textures (profile versions - will be cropped)
    public static final AssetDescriptor<Texture> HERO_ANGEL_MAP = new AssetDescriptor<>(AssetsPath.HERO_ANGEL_MAP, Texture.class);
    public static final AssetDescriptor<Texture> HERO_MIME_MAP = new AssetDescriptor<>(AssetsPath.HERO_MIME_MAP, Texture.class);
    public static final AssetDescriptor<Texture> HERO_WHISTLE_MAP = new AssetDescriptor<>(AssetsPath.HERO_WHISTLE_MAP, Texture.class);

    // Minigame bad_guys
    public static final AssetDescriptor<Texture> ROBBER = new AssetDescriptor<>(AssetsPath.ROBBER, Texture.class);
    public static final AssetDescriptor<Texture> CYBER = new AssetDescriptor<>(AssetsPath.CYBER, Texture.class);
    public static final AssetDescriptor<Texture> VIOLENCE = new AssetDescriptor<>(AssetsPath.VIOLENCE, Texture.class);
}
