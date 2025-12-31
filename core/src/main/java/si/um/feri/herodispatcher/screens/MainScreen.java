package si.um.feri.herodispatcher.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.HashMap;
import java.util.Map;

import si.um.feri.herodispatcher.HeroDispatcherGame;
import si.um.feri.herodispatcher.data.GameDataLoader;
import si.um.feri.herodispatcher.managers.CrimeSpawnManager;
import si.um.feri.herodispatcher.world.dynamic_objects.Crime;
import si.um.feri.herodispatcher.world.static_objects.CrimeDefinition;
import si.um.feri.herodispatcher.world.static_objects.CrimeLocation;

public class MainScreen implements Screen {

    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch spriteBatch;

    private Texture mapTexture; // temporary explicit declaration and initialization

    private CrimeSpawnManager crimeSpawnManager;

    private ShapeRenderer shapeRenderer;

    public MainScreen(HeroDispatcherGame heroDispatcherGame) {
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
    }


    @Override
    public void show() {
        camera.position.set(
            viewport.getWorldWidth() / 2f,
            viewport.getWorldHeight() / 2f,
            0
        );
        camera.update();
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

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        mapTexture.dispose();
    }
}
