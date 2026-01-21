package si.um.feri.herodispatcher.minigame;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class FallingObject extends Image {
    private static final float SPEED = 250f;

    public FallingObject(Texture texture) {
        super(texture);
        setSize(40, 40);
    }

    private static Texture createTexture() {
        Pixmap p = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        p.setColor(1, 0, 0, 1);
        p.fill();
        Texture t = new Texture(p);
        p.dispose();
        return t;
    }

    public void update(float delta) {
        moveBy(0, -SPEED * delta);
    }

    public boolean isOutOfScreen() {
        return getY() + getHeight() < 0;
    }
}
