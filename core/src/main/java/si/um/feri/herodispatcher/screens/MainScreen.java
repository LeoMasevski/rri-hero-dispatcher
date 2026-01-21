package si.um.feri.herodispatcher.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
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
import si.um.feri.herodispatcher.managers.CrimeSpawnManager;
import si.um.feri.herodispatcher.managers.PathfindingManager;
import si.um.feri.herodispatcher.world.dynamic_objects.Crime;
import si.um.feri.herodispatcher.world.dynamic_objects.Hero;
import si.um.feri.herodispatcher.world.static_objects.CrimeDefinition;
import si.um.feri.herodispatcher.world.static_objects.CrimeLocation;
import si.um.feri.herodispatcher.world.static_objects.PathGraph;
import si.um.feri.herodispatcher.world.static_objects.PathNode;

public class MainScreen implements Screen {

    // ---------- Constants ----------
    private static final float CRIME_RADIUS = 50f;
    private static final float HERO_RADIUS = 70f;

    // ---------- Core ----------
    private final HeroDispatcherGame game;

    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;

    private Texture mapTexture;

    // ---------- Systems ----------
    private CrimeSpawnManager crimeSpawnManager;
    private PathfindingManager pathfindingManager;

    // ---------- World ----------
    private Hero hero;

    // ---------- UI ----------
    private Stage uiStage;
    private HeroSelectionPanel heroSelectionPanel;
    private Skin buttonSkin;

    public MainScreen(HeroDispatcherGame heroDispatcherGame) {
        this.game = heroDispatcherGame;

        initRendering();
        initWorldAndSystems();
        initUI();
    }

    // =========================================================
    // Init
    // =========================================================

    private void initRendering() {
        mapTexture = new Texture(Gdx.files.internal("images/raw/central_city_map.png"));

        camera = new OrthographicCamera();
        viewport = new FitViewport(mapTexture.getWidth(), mapTexture.getHeight(), camera);

        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
    }

    private void initWorldAndSystems() {
        GameDataLoader loader = new GameDataLoader();

        // Crimes
        Array<CrimeDefinition> definitionsArray = loader.loadCrimeDefinitions();
        Array<CrimeLocation> locationsArray = loader.loadCrimeLocations();
        crimeSpawnManager = new CrimeSpawnManager(toCrimeDefinitionMap(definitionsArray), locationsArray);

        // Pathfinding
        Array<PathNode> pathNodes = loader.loadPathGraph();
        PathGraph pathGraph = new PathGraph(toPathNodeMap(pathNodes));
        pathfindingManager = new PathfindingManager(pathGraph);

        // Hero - starting with Angel
        PathNode startNode = pathNodes.first();
        hero = new Hero(
            "Angel",
            "Mutant",
            28,
            "Wings, Aerial Combat",
            "Mutant with large feathered wings granting flight. Enhanced strength and agility in aerial combat.",
            4, 4, 5,
            "angel",
            startNode.getPosition().x,
            startNode.getPosition().y
        );
    }

    private void initUI() {
        uiStage = new Stage(new ScreenViewport());

        heroSelectionPanel = new HeroSelectionPanel(uiStage, game.assets) {
            @Override
            protected void onHeroSelected(Hero selectedHero) {
                Gdx.app.log("MainScreen", "Hero selected: " + selectedHero.getName());

                // Replace active hero with selected one
                PathNode currentNode = pathfindingManager.getGraph().findClosestNode(hero.getPosition());
                hero = new Hero(
                    selectedHero.getName(),
                    selectedHero.getType(),
                    selectedHero.getAge(),
                    selectedHero.getWeapon(),
                    selectedHero.getInfoFacts(),
                    selectedHero.getStrength(),
                    selectedHero.getIntelligence(),
                    selectedHero.getAgility(),
                    selectedHero.getHeroId(),
                    currentNode.getPosition().x,
                    currentNode.getPosition().y
                );
            }
        };

        createButtonSkin();
        createHeroButtons();
    }

    private Map<String, CrimeDefinition> toCrimeDefinitionMap(Array<CrimeDefinition> definitions) {
        Map<String, CrimeDefinition> map = new HashMap<>();
        for (CrimeDefinition def : definitions) {
            map.put(def.getId(), def);
        }
        return map;
    }

    private Map<String, PathNode> toPathNodeMap(Array<PathNode> pathNodes) {
        Map<String, PathNode> map = new HashMap<>();
        for (PathNode node : pathNodes) {
            map.put(node.getId(), node);
        }
        return map;
    }

    // =========================================================
    // UI setup
    // =========================================================

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

    // =========================================================
    // Screen lifecycle
    // =========================================================

    @Override
    public void show() {
        camera.position.set(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f, 0f);
        camera.update();

        // UI + world clicks
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(uiStage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        update(delta);
        draw();
        handleInput();

        uiStage.act(delta);
        uiStage.draw();
    }

    private void update(float delta) {
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);

        crimeSpawnManager.update(delta);
        hero.update(delta);
    }

    private void draw() {
        drawMap();
        drawCrimes();
        drawPlannedPath();
        drawHero();
    }

    private void drawMap() {
        spriteBatch.begin();
        spriteBatch.draw(mapTexture, 0, 0);
        spriteBatch.end();
    }

    private void drawCrimes() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (Crime crime : crimeSpawnManager.getActiveCrimes()) {
            shapeRenderer.setColor(getCrimeColor(crime));
            shapeRenderer.circle(crime.getLocation().getX(), crime.getLocation().getY(), CRIME_RADIUS);
        }

        shapeRenderer.end();
    }

    private Color getCrimeColor(Crime crime) {
        switch (crime.getDefinition().getCategory()) {
            case "minor": return new Color(0, 1, 0, 1);
            case "violent": return new Color(1, 0, 0, 1);
            case "cyber": return new Color(0, 0, 1, 1);
            default: return new Color(1, 1, 1, 1);
        }
    }

    private void drawPlannedPath() {
        Array<PathNode> path = hero.getCurrentPath();
        if (path == null || path.size < 1) return;

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0, 1, 1, 1); // cyan

        int startIdx = Math.min(hero.getCurrentPathIndex(), Math.max(path.size - 1, 0));
        Vector2 last = hero.getPosition();

        for (int i = startIdx; i < path.size; i++) {
            Vector2 next = path.get(i).getPosition();
            shapeRenderer.line(last, next);
            last = next;
        }

        shapeRenderer.end();
    }

    private void drawHero() {
        spriteBatch.begin();

        Texture heroTexture = getHeroTexture(hero.getHeroId());
        if (heroTexture != null) {
            float size = HERO_RADIUS * 2;
            spriteBatch.draw(heroTexture,
                hero.getPosition().x - HERO_RADIUS,
                hero.getPosition().y - HERO_RADIUS,
                size, size);
        } else {
            // Fallback to circle if texture not found
            spriteBatch.end();
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(1, 1, 0, 1); // yellow
            shapeRenderer.circle(hero.getPosition().x, hero.getPosition().y, HERO_RADIUS);
            shapeRenderer.end();
            return;
        }

        spriteBatch.end();
    }

    private Texture getHeroTexture(String heroId) {
        switch (heroId) {
            case "angel": return game.assets.heroAngel;
            case "mime": return game.assets.heroMime;
            case "whistle": return game.assets.heroWhistle;
            default: return null;
        }
    }

    private void handleInput() {
        if (!Gdx.input.justTouched()) return;

        Vector2 clickPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(clickPos);

        Crime clickedCrime = findClickedCrime(clickPos);
        if (clickedCrime == null) return;

        Vector2 crimePos = new Vector2(clickedCrime.getLocation().getX(), clickedCrime.getLocation().getY());

        Array<PathNode> path = pathfindingManager.requestPath(hero.getPosition(), crimePos);

        Gdx.app.log("PATH", "Crime clicked: " + clickedCrime.getLocation().getName()
            + " crimePos=" + crimePos
            + " pathSize=" + (path == null ? 0 : path.size));

        if (path != null && path.size > 0) {
            Gdx.app.log("PATH", "Start node: " + path.first().getId() + " -> Goal node: " + path.peek().getId());
        }

        hero.assignCrime(clickedCrime);
        hero.setPath(path, crimePos);
    }

    private Crime findClickedCrime(Vector2 clickPos) {
        for (Crime crime : crimeSpawnManager.getActiveCrimes()) {
            Vector2 crimePos = new Vector2(crime.getLocation().getX(), crime.getLocation().getY());
            if (clickPos.dst(crimePos) < CRIME_RADIUS) {
                return crime;
            }
        }
        return null;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        uiStage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

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
