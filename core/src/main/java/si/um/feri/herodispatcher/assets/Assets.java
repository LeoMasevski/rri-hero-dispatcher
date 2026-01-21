package si.um.feri.herodispatcher.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {
    private AssetManager manager;
    public Skin uiSkin;
    public BitmapFont font;

    // Hero textures for selection panel (full versions)
    public Texture heroAngel;
    public Texture heroMime;
    public Texture heroWhistle;

    // Hero textures for map display (cropped + bordered)
    public Texture heroAngelMap;
    public Texture heroMimeMap;
    public Texture heroWhistleMap;

    public Assets() {
        manager = new AssetManager();
    }

    public void load() {
        font = new BitmapFont();

        // Load UI skin
        manager.load(AssetsPath.SKIN_JSON, Skin.class, new SkinLoader.SkinParameter(AssetsPath.SKIN_ATLAS));

        // Load hero textures for selection panel
        manager.load(AssetDescriptors.HERO_ANGEL);
        manager.load(AssetDescriptors.HERO_MIME);
        manager.load(AssetDescriptors.HERO_WHISTLE);

        // Load profile textures for map
        manager.load(AssetDescriptors.HERO_ANGEL_MAP);
        manager.load(AssetDescriptors.HERO_MIME_MAP);
        manager.load(AssetDescriptors.HERO_WHISTLE_MAP);

        manager.finishLoading();

        // Get loaded assets
        uiSkin = manager.get(AssetsPath.SKIN_JSON, Skin.class);

        heroAngel = manager.get(AssetDescriptors.HERO_ANGEL);
        heroMime = manager.get(AssetDescriptors.HERO_MIME);
        heroWhistle = manager.get(AssetDescriptors.HERO_WHISTLE);

        // Get profile textures, crop to center, and add border
        Texture angelProfile = manager.get(AssetDescriptors.HERO_ANGEL_MAP);
        Texture mimeProfile = manager.get(AssetDescriptors.HERO_MIME_MAP);
        Texture whistleProfile = manager.get(AssetDescriptors.HERO_WHISTLE_MAP);

        heroAngelMap = cropAndBorder(angelProfile, 200, 3);
        heroMimeMap = cropAndBorder(mimeProfile, 200, 3);
        heroWhistleMap = cropAndBorder(whistleProfile, 200, 3);

        Gdx.app.log("Assets", "All textures cropped and bordered successfully");
    }

    /**
     * Crops texture to center of mass, then adds a black border
     */
    private Texture cropAndBorder(Texture original, int targetSize, int borderWidth) {
        if (!original.getTextureData().isPrepared()) {
            original.getTextureData().prepare();
        }

        Pixmap originalPixmap = original.getTextureData().consumePixmap();
        int width = originalPixmap.getWidth();
        int height = originalPixmap.getHeight();

        Gdx.app.log("Assets", "Processing texture: " + width + "x" + height);

        // STEP 1: Find center of mass
        int minX = width, maxX = 0, minY = height, maxY = 0;
        long sumX = 0, sumY = 0, count = 0;
        boolean foundContent = false;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = originalPixmap.getPixel(x, y);

                int r = (pixel >> 24) & 0xFF;
                int g = (pixel >> 16) & 0xFF;
                int b = (pixel >> 8) & 0xFF;
                int a = pixel & 0xFF;

                boolean isTooLightGray = (r > 200 && g > 200 && b > 200);
                boolean isTooDark = (r < 30 && g < 30 && b < 30);
                boolean isTransparent = (a < 10);

                if (!isTransparent && !isTooDark && !isTooLightGray) {
                    foundContent = true;
                    if (x < minX) minX = x;
                    if (x > maxX) maxX = x;
                    if (y < minY) minY = y;
                    if (y > maxY) maxY = y;

                    sumX += x;
                    sumY += y;
                    count++;
                }
            }
        }

        if (!foundContent || count == 0) {
            Gdx.app.log("Assets", "Warning: No visible content found");
            minX = 0;
            maxX = width - 1;
            minY = 0;
            maxY = height - 1;
            sumX = width / 2;
            sumY = height / 2;
            count = 1;
        }

        int centerX = (int)(sumX / count);
        int centerY = (int)(sumY / count);
        int contentWidth = maxX - minX + 1;
        int contentHeight = maxY - minY + 1;

        Gdx.app.log("Assets", "Center of mass: " + centerX + "," + centerY);

        // STEP 2: Crop around center of mass
        int cropSize = Math.max(contentWidth, contentHeight);
        cropSize = (int)(cropSize * 1.2f); // 20% padding

        int cropX = centerX - cropSize / 2;
        int cropY = centerY - cropSize / 2;

        if (cropX < 0) cropX = 0;
        if (cropY < 0) cropY = 0;
        if (cropX + cropSize > width) cropX = width - cropSize;
        if (cropY + cropSize > height) cropY = height - cropSize;
        if (cropSize > width) cropSize = width;
        if (cropSize > height) cropSize = height;

        // STEP 3: Create final texture with border
        int innerSize = targetSize - (borderWidth * 2);
        Pixmap finalPixmap = new Pixmap(targetSize, targetSize, Pixmap.Format.RGBA8888);

        // Fill with black border
        finalPixmap.setColor(0, 0, 0, 1);
        finalPixmap.fill();

        // Draw cropped content in center (leaving border)
        finalPixmap.drawPixmap(originalPixmap,
            cropX, cropY, cropSize, cropSize,           // source (cropped region)
            borderWidth, borderWidth, innerSize, innerSize);  // destination (inside border)

        Texture result = new Texture(finalPixmap);
        finalPixmap.dispose();
        originalPixmap.dispose();

        Gdx.app.log("Assets", "Created bordered texture: " + targetSize + "x" + targetSize);
        return result;
    }

    public void dispose() {
        manager.dispose();

        // Dispose manually created textures
        if (heroAngelMap != null) heroAngelMap.dispose();
        if (heroMimeMap != null) heroMimeMap.dispose();
        if (heroWhistleMap != null) heroWhistleMap.dispose();
    }
}
