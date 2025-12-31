package si.um.feri.herodispatcher.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import si.um.feri.herodispatcher.HeroDispatcherGame;

public class MenuScreen extends ScreenAdapter {
    private static final float UI_WIDTH = 1280;
    private static final float UI_HEIGHT = 720;
    private final HeroDispatcherGame game;
    private Stage stage;

    public MenuScreen(HeroDispatcherGame game) {
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

        Label titleLabel = new Label("DISPATCH HEROES", game.assets.uiSkin);
        titleLabel.setFontScale(2f);

        titleTable.add(titleLabel).top().expand().padTop(50).row();

        // Nav
        Table navTable = new Table();
        navTable.setFillParent(true);
        navTable.padTop(300);
        stage.addActor(navTable);


        TextButton startBtn = new TextButton("Start game", game.assets.uiSkin);
        startBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainScreen(game));
            }
        });

        TextButton settingsBtn = new TextButton("Settings", game.assets.uiSkin);
        settingsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game));
            }
        });

        TextButton exitBtn = new TextButton("Exit", game.assets.uiSkin);
        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent even, float x, float y) {
                Gdx.app.exit();
            }
        });

        navTable.add(startBtn).width(200).pad(10);
        navTable.row();
        navTable.add(settingsBtn).width(200).pad(10);
        navTable.row();
        navTable.add(exitBtn).width(200).pad(10);
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

    public void dispose() {
        stage.dispose();
    }
}

