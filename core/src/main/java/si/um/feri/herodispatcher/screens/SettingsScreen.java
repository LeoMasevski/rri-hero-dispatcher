package si.um.feri.herodispatcher.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import si.um.feri.herodispatcher.HeroDispatcherGame;

public class SettingsScreen extends ScreenAdapter {
    private static final float UI_WIDTH = 1280;
    private static final float UI_HEIGHT = 720;
    private final HeroDispatcherGame game;
    private Stage stage;

    public SettingsScreen(HeroDispatcherGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        Viewport viewport = new ExtendViewport(UI_WIDTH, UI_HEIGHT);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        // Title
        Table titleTable = new Table();
        titleTable.setFillParent(true);
        stage.addActor(titleTable);

        Label title = new Label("Settings", game.assets.uiSkin);
        title.setFontScale(2f);
        titleTable.add(title).top().expand().padTop(50).row();

        // Main Settings
        Table settingsTabel = new Table();
        settingsTabel.setFillParent(true);
        stage.addActor(settingsTabel);

        CheckBox musicBox = new CheckBox("Music", game.assets.uiSkin);
        CheckBox sfxBox = new CheckBox("SFX", game.assets.uiSkin);

        settingsTabel.add(musicBox).pad(20).row();
        settingsTabel.add(sfxBox).pad(20).row();

        // Navigation
        Table navTable = new Table();
        navTable.setFillParent(true);
        navTable.bottom().left();
        navTable.padTop(70);
        stage.addActor(navTable);

        TextButton backBtn = new TextButton("Back", game.assets.uiSkin);
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });
        navTable.add(backBtn);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}

