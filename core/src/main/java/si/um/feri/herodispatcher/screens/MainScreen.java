package si.um.feri.herodispatcher.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.HashMap;
import java.util.Map;

import si.um.feri.herodispatcher.HeroDispatcherGame;
import si.um.feri.herodispatcher.data.GameDataLoader;
import si.um.feri.herodispatcher.data.dto.HeroData;
import si.um.feri.herodispatcher.managers.CrimeSpawnManager;
import si.um.feri.herodispatcher.world.dynamic_objects.Crime;
import si.um.feri.herodispatcher.world.dynamic_objects.Hero;
import si.um.feri.herodispatcher.world.static_objects.CrimeDefinition;
import si.um.feri.herodispatcher.world.static_objects.CrimeLocation;

public class MainScreen implements Screen {

    private HeroDispatcherGame game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch spriteBatch;

    private Texture mapTexture; // temporary explicit declaration and initialization

    private CrimeSpawnManager crimeSpawnManager;

    private ShapeRenderer shapeRenderer;

    // Hero selection UI
    private Stage uiStage;
    private HeroSelectionPanel heroSelectionPanel;
    private Skin buttonSkin;

    public MainScreen(HeroDispatcherGame heroDispatcherGame) {
        this.game = heroDispatcherGame;

        // TODO: use asset manager classes for handling assets
        mapTexture = new Texture(Gdx.files.internal("images/raw/central_city_map.png"));

        camera = new OrthographicCamera();

        // game world units are the same as the dimensions of the map in pixels
        viewport = new FitViewport(mapTexture.getWidth(), mapTexture.getHeight(), camera);

        spriteBatch = new SpriteBatch();

        GameDataLoader loader = new GameDataLoader();


        // TODO: maybe move conversion from MainScreen constructor
        Array<CrimeDefinition> definitionsArray = loader.loadCrimeDefinitions();
        Array<CrimeLocation> locationsArray = loader.loadCrimeLocations();

        // Convert array into a map for fast lookup
        Map<String, CrimeDefinition> definitionsMap = new HashMap<>();
        for (CrimeDefinition def : definitionsArray) {
            definitionsMap.put(def.getId(), def);
        }

        // Pass map and locations array to CrimeSpawnManager
        crimeSpawnManager = new CrimeSpawnManager(definitionsMap, locationsArray);

        shapeRenderer = new ShapeRenderer();

        // Initialize hero selection UI
        uiStage = new Stage(new ScreenViewport());
        heroSelectionPanel = new HeroSelectionPanel(uiStage, game.assets) {
            @Override
            protected void onHeroSelected(Hero hero) {
                Gdx.app.log("MainScreen", "Hero selected: " + hero.getName());
            }
        };

        createButtonSkin();
        createHeroButtons();
    }

    private void createButtonSkin() {
        buttonSkin = new Skin();

        BitmapFont font = new BitmapFont();
        font.getData().setScale(1.2f);
        buttonSkin.add("default", font);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.overFontColor = Color.WHITE;
        buttonStyle.downFontColor = Color.WHITE;

        Color btnColor = new Color(0.25f, 0.35f, 0.65f, 0.9f);
        buttonStyle.up = createButtonDrawable(btnColor);
        buttonStyle.over = createButtonDrawable(new Color(0.3f, 0.45f, 0.75f, 0.9f));
        buttonStyle.down = createButtonDrawable(new Color(0.2f, 0.3f, 0.6f, 0.9f));

        buttonSkin.add("default", buttonStyle);
    }

    private TextureRegionDrawable createButtonDrawable(Color color) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return new TextureRegionDrawable(new TextureRegion(texture));
    }

    private void createHeroButtons() {
        Table buttonTable = new Table();
        buttonTable.setFillParent(true);
        buttonTable.top().right();
        buttonTable.pad(20);

        // Single button to open hero selection
        TextButton heroesButton = new TextButton("HEROES", buttonSkin);
        heroesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                heroSelectionPanel.show();
            }
        });

        buttonTable.add(heroesButton).width(150).height(50).pad(5).row();

        uiStage.addActor(buttonTable);
    }


    @Override
    public void show() {
        camera.position.set(
            viewport.getWorldWidth() / 2f,
            viewport.getWorldHeight() / 2f,
            0
        );
        camera.update();

        Gdx.input.setInputProcessor(uiStage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);

        crimeSpawnManager.update(delta);

        spriteBatch.begin();
        spriteBatch.draw(mapTexture, 0, 0);
        spriteBatch.end();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (Crime crime : crimeSpawnManager.getActiveCrimes()) {

            // TODO: extend this classification for crime categories
            switch (crime.getDefinition().getCategory()) {
                case "minor": shapeRenderer.setColor(0, 1, 0, 1); break; // green
                case "violent": shapeRenderer.setColor(1, 0, 0, 1); break; // red
                case "cyber": shapeRenderer.setColor(0, 0, 1, 1); break; // blue
                default: shapeRenderer.setColor(1, 1, 1, 1); break; // white
            }

            shapeRenderer.circle(crime.getLocation().getX(),
                crime.getLocation().getY(),
                50);

        }

        shapeRenderer.end();

        // Draw hero selection UI
        uiStage.act(delta);
        uiStage.draw();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        uiStage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        mapTexture.dispose();
        shapeRenderer.dispose();
        uiStage.dispose();
        heroSelectionPanel.dispose();
        buttonSkin.dispose();
    }
}
