package si.um.feri.herodispatcher.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AssetDescriptors {
    public static final AssetDescriptor<Skin> SKIN_JSON = new AssetDescriptor<Skin>(AssetsPath.SKIN_JSON, Skin.class);
    public static final AssetDescriptor<Skin> SKIN_ATLAS = new AssetDescriptor<>(AssetsPath.SKIN_ATLAS, Skin.class);

    public static final AssetDescriptor<Texture> HERO_SPIDERMAN = new AssetDescriptor<>(AssetsPath.HERO_SPIDERMAN, Texture.class);
    public static final AssetDescriptor<Texture> HERO_CATWOMAN = new AssetDescriptor<>(AssetsPath.HERO_CATWOMAN, Texture.class);
    public static final AssetDescriptor<Texture> HERO_DEADPOOL = new AssetDescriptor<>(AssetsPath.HERO_DEADPOOL, Texture.class);
}
