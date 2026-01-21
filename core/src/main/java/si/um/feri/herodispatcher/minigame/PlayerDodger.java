package si.um.feri.herodispatcher.minigame;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class PlayerDodger extends Image {
    private static final float SPEED = 360f;

    public PlayerDodger(Texture texture) {
        super(texture);
        setSize(50, 50);
    }

    private static Texture createTexture() {
        Pixmap p = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        p.setColor(0, 1, 0, 1);
        p.fill();
        Texture t = new Texture(p);
        p.dispose();
        return t;
    }

    public void moveLeft(float delta) {
        moveBy(-SPEED * delta, 0);
    }

    public void moveRight(float delta) {
        moveBy(SPEED * delta, 0);
    }
}
