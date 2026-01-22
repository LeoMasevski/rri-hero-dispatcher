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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import si.um.feri.herodispatcher.HeroDispatcherGame;

public class SettingsScreen extends ScreenAdapter {
    private static final float UI_WIDTH = 1280;
    private static final float UI_HEIGHT = 720;

    private final HeroDispatcherGame game;
    private Stage stage;
    private SpriteBatch batch;

    public SettingsScreen(HeroDispatcherGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        Viewport viewport = new ExtendViewport(UI_WIDTH, UI_HEIGHT);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        // Title with shadow effect
        Table titleTable = new Table();
        titleTable.setFillParent(true);
        stage.addActor(titleTable);

        Label titleLabel = new Label("Settings", game.assets.uiSkin, "title");
        titleLabel.setFontScale(2.5f);
        titleLabel.setColor(com.badlogic.gdx.graphics.Color.WHITE);

        // Add shadow
        Label titleShadow = new Label("Settings", game.assets.uiSkin, "title");
        titleShadow.setFontScale(2.5f);
        titleShadow.setColor(0, 0, 0, 0.7f);

        com.badlogic.gdx.scenes.scene2d.ui.Stack titleStack = new com.badlogic.gdx.scenes.scene2d.ui.Stack();

        Table shadowTable = new Table();
        shadowTable.add(titleShadow).padLeft(4).padTop(4);

        Table mainTitleTable = new Table();
        mainTitleTable.add(titleLabel);

        titleStack.add(shadowTable);
        titleStack.add(mainTitleTable);

        titleTable.add(titleStack).top().expand().padTop(50).row();

        // Main Settings
        Table settingsTable = new Table();
        settingsTable.setFillParent(true);
        stage.addActor(settingsTable);

        CheckBox musicBox = new CheckBox("Music", game.assets.uiSkin);
        CheckBox sfxBox = new CheckBox("SFX", game.assets.uiSkin);

        settingsTable.add(musicBox).pad(20).row();
        settingsTable.add(sfxBox).pad(20).row();

        // Navigation
        Table navTable = new Table();
        navTable.setFillParent(true);
        navTable.bottom().left();
        navTable.padBottom(50).padLeft(50);
        stage.addActor(navTable);

        TextButton backBtn = new TextButton("Back", game.assets.uiSkin);
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });
        navTable.add(backBtn).width(150).height(50);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw background - use screen dimensions
        batch.begin();

        // Use actual screen dimensions for proper rendering
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float bgWidth = game.assets.menuBackground.getWidth();
        float bgHeight = game.assets.menuBackground.getHeight();

        // Calculate aspect ratios
        float screenAspect = screenWidth / screenHeight;
        float bgAspect = bgWidth / bgHeight;

        float scale;
        float x = 0;
        float y = 0;
        float scaledWidth;
        float scaledHeight;

        if (screenAspect > bgAspect) {
            // Screen is wider - scale to width
            scale = screenWidth / bgWidth;
            scaledWidth = screenWidth;
            scaledHeight = bgHeight * scale;
            y = (screenHeight - scaledHeight) / 2f;
        } else {
            // Screen is taller - scale to height
            scale = screenHeight / bgHeight;
            scaledHeight = screenHeight;
            scaledWidth = bgWidth * scale;
            x = (screenWidth - scaledWidth) / 2f;
        }

        // Set batch to use screen coordinates
        batch.getProjectionMatrix().setToOrtho2D(0, 0, screenWidth, screenHeight);
        batch.draw(game.assets.menuBackground, x, y, scaledWidth, scaledHeight);
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
    }
}
