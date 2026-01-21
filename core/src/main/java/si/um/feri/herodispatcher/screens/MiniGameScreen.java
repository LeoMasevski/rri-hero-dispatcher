package si.um.feri.herodispatcher.screens;

import si.um.feri.herodispatcher.HeroDispatcherGame;
import si.um.feri.herodispatcher.minigame.FallingObject;
import si.um.feri.herodispatcher.minigame.MiniGameResult;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import si.um.feri.herodispatcher.minigame.PlayerDodger;
import java.util.function.Consumer;

public class MiniGameScreen implements Screen {
    private static final float WIDTH = 800f;
    private static final float HEIGHT = 400f;

    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;
    private Skin skin;
    private Table rootTable;
    private float timeLeft = 20f;
    private int score = 0;
    private Consumer<MiniGameResult> onFinish;
    private Texture backgroundTexture;
    private PlayerDodger player;
    private Array<FallingObject> obstacles = new Array<>();
    private float spawnTimer = 0f;
    private Label timeLabel;
    private Label scoreLabel;
    private Label targetLabel;
    private int requiredScore;
    private Texture heroTexture;
    private String crimeCategory;


    public MiniGameScreen(Skin skin, Texture heroTexture, String crimeCategory, Consumer<MiniGameResult> onFinish) {
        this.skin = skin;
        this.heroTexture = heroTexture;
        this.crimeCategory = crimeCategory;
        this.requiredScore = calculateRequiredScore(crimeCategory);
        this.onFinish = onFinish;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(WIDTH, HEIGHT, camera);
        stage = new Stage(viewport);

        Gdx.input.setInputProcessor(stage);

        buildUI();
        createHUD();
        createPlayer();
    }

    private void buildUI() {
        rootTable = new Table();
        rootTable.setFillParent(true);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 0.85f);
        pixmap.fill();

        backgroundTexture = new Texture(pixmap);
        pixmap.dispose();

        rootTable.setBackground(
            new TextureRegionDrawable(new TextureRegion(backgroundTexture))
        );

        BitmapFont font = new BitmapFont();
        LabelStyle labelStyle = new LabelStyle(font, com.badlogic.gdx.graphics.Color.WHITE);

        Label title = new Label("CRIME IN PROGRESS", labelStyle);
        Label info = new Label("Capture all the suspects!", labelStyle);

        rootTable.top().padTop(20);
        rootTable.add(title).padBottom(10).row();
        rootTable.add(info).padBottom(20).row();

        stage.addActor(rootTable);
    }

    private void createHUD() {
        BitmapFont font = new BitmapFont();
        LabelStyle style = new Label.LabelStyle(font, com.badlogic.gdx.graphics.Color.WHITE);

        timeLabel = new Label("TIME: 20", style);
        scoreLabel = new Label("SCORE: 0", style);
        targetLabel = new Label("TARGET: " + requiredScore, style);

        Table hudTable = new Table();
        hudTable.setFillParent(true);

        hudTable.top().left();
        hudTable.padTop(15);
        hudTable.padLeft(20);

        hudTable.add(timeLabel)
            .left()
            .padBottom(6)
            .row();

        hudTable.add(targetLabel)
            .left()
            .padBottom(6)
            .row();

        hudTable.add(scoreLabel)
            .left();

        stage.addActor(hudTable);
    }

    private void createPlayer() {
        player = new PlayerDodger(heroTexture);
        player.setPosition(
            WIDTH / 2f - player.getWidth() / 2f,
            40
        );
        stage.addActor(player);
    }

    private void handleInput(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.moveLeft(delta);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.moveRight(delta);
        }

        if (player.getX() < 0) {
            player.setX(0);
        }

        if (player.getX() + player.getWidth() > WIDTH) {
            player.setX(WIDTH - player.getWidth());
        }
    }

    private Texture getBadGuyTexture() {
        switch (crimeCategory) {
            case "violent":
                return Gdx.app.getApplicationListener()
                    instanceof HeroDispatcherGame ? ((HeroDispatcherGame) Gdx.app.getApplicationListener()).assets.violentCriminal : null;

                case "cyber":
                return ((HeroDispatcherGame) Gdx.app.getApplicationListener()).assets.cyberCriminal;

            case "minor":
                return ((HeroDispatcherGame) Gdx.app.getApplicationListener()).assets.robber;

            default:
                return ((HeroDispatcherGame) Gdx.app.getApplicationListener()).assets.robber;
        }
    }

    private void spawnObstacle() {
        Texture badGuyTexture = getBadGuyTexture();
        FallingObject obj = new FallingObject(badGuyTexture);

        float x = MathUtils.random(0, WIDTH - obj.getWidth());
        obj.setPosition(x, HEIGHT);

        obstacles.add(obj);
        stage.addActor(obj);
    }

    private boolean isColliding(PlayerDodger player, FallingObject obj) {
        Rectangle playerRect = new Rectangle(
            player.getX(),
            player.getY(),
            player.getWidth(),
            player.getHeight()
        );

        Rectangle objRect = new Rectangle(
            obj.getX(),
            obj.getY(),
            obj.getWidth(),
            obj.getHeight()
        );

        return playerRect.overlaps(objRect);
    }

    private void updateObstacles(float delta) {
        for (int i = obstacles.size - 1; i >= 0; i--) {
            FallingObject obj = obstacles.get(i);
            obj.update(delta);

            if (isColliding(player, obj)) {
                score += 10;
                obj.remove();
                obstacles.removeIndex(i);
                System.out.println("HIT!");
                continue;
            }

            if (obj.isOutOfScreen()) {
                obj.remove();
                obstacles.removeIndex(i);
            }
        }
    }

    private int calculateRequiredScore(String category) {
        switch (category) {
            case "minor":
                return 40;
            case "violent":
                return 60;
            case "cyber":
                return 80;
            case "high_risk":
                return 100;
            default:
                return 50;
        }
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    private void update(float delta) {
        handleInput(delta);

        spawnTimer += delta;
        if (spawnTimer > 1f) {
            spawnObstacle();
            spawnTimer = 0f;
        }

        updateObstacles(delta);

        timeLeft -= delta;
        if (timeLeft <= 0f) {
            timeLeft = 0f;
            finishMiniGame();
        }

        timeLabel.setText("TIME: " + Math.max(0, (int) timeLeft));
        scoreLabel.setText("SCORE: " + score);
    }

    private void finishMiniGame() {
        if (onFinish != null) {
            MiniGameResult result =
                score >= requiredScore
                    ? MiniGameResult.SUCCESS
                    : MiniGameResult.FAILED;

            onFinish.accept(result);
        }
    }

    public int getScore() {
        return score;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }

    @Override
    public void dispose() {
        stage.dispose();
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
    }
}
