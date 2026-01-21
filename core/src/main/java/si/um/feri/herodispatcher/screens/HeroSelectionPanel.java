package si.um.feri.herodispatcher.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import si.um.feri.herodispatcher.assets.Assets;
import si.um.feri.herodispatcher.data.dto.HeroData;
import si.um.feri.herodispatcher.world.dynamic_objects.Hero;

/**
 * Hero Selection Panel - overlay UI that appears on MainScreen
 * Shows hero details with carousel navigation
 */
public class HeroSelectionPanel implements Disposable {

    private Stage stage;
    private Skin skin;
    private Assets assets;
    private Array<Hero> heroes;
    private int currentHeroIndex = 0;

    // UI Elements
    private Table rootTable;
    private Image heroImage;
    private Label nameLabel;
    private Label typeLabel, ageLabel, weaponLabel;
    private Label strengthNumber, intelligenceNumber, agilityNumber;
    private Label infoText;

    private boolean isVisible = false;

    // Colors
    private final Color PRIMARY_COLOR = new Color(0.25f, 0.35f, 0.65f, 1);
    private final Color ACCENT_COLOR = new Color(0.95f, 0.5f, 0.2f, 1);
    private final Color BG_COLOR = new Color(0.1f, 0.1f, 0.15f, 0.92f); // Dark semi-transparent
    private final Color CARD_BG = new Color(1, 1, 1, 0.98f);
    private final Color TEXT_COLOR = new Color(0.15f, 0.15f, 0.15f, 1);

    // Track textures for disposal
    private Array<Texture> createdTextures = new Array<>();

    public HeroSelectionPanel(Stage stage, Assets assets) {
        this.stage = stage;
        this.assets = assets;
        this.heroes = HeroData.getAllHeroes();

        createSkin();
        createUI();
        hide(); // Start hidden
    }

    private void createSkin() {
        skin = new Skin();

        // Fonts
        BitmapFont normalFont = new BitmapFont();
        normalFont.getData().setScale(1.0f);
        skin.add("normal", normalFont);

        BitmapFont boldFont = new BitmapFont();
        boldFont.getData().setScale(1.2f);
        skin.add("bold", boldFont);

        BitmapFont titleFont = new BitmapFont();
        titleFont.getData().setScale(1.6f);
        skin.add("title-font", titleFont);

        BitmapFont statFont = new BitmapFont();
        statFont.getData().setScale(2.2f);
        skin.add("stat-font", statFont);

        BitmapFont arrowFont = new BitmapFont();
        arrowFont.getData().setScale(2.5f);
        skin.add("arrow", arrowFont);

        // Label styles
        Label.LabelStyle normalStyle = new Label.LabelStyle();
        normalStyle.font = normalFont;
        normalStyle.fontColor = TEXT_COLOR;
        skin.add("default", normalStyle);

        Label.LabelStyle boldStyle = new Label.LabelStyle();
        boldStyle.font = boldFont;
        boldStyle.fontColor = TEXT_COLOR;
        skin.add("bold", boldStyle);

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = titleFont;
        titleStyle.fontColor = PRIMARY_COLOR;
        skin.add("title", titleStyle);

        Label.LabelStyle statStyle = new Label.LabelStyle();
        statStyle.font = statFont;
        statStyle.fontColor = ACCENT_COLOR;
        skin.add("stat", statStyle);

        Label.LabelStyle typeStyle = new Label.LabelStyle();
        typeStyle.font = normalFont;
        typeStyle.fontColor = new Color(0.5f, 0.5f, 0.5f, 1);
        skin.add("type", typeStyle);

        Label.LabelStyle arrowStyle = new Label.LabelStyle();
        arrowStyle.font = arrowFont;
        arrowStyle.fontColor = PRIMARY_COLOR;
        skin.add("arrow", arrowStyle);

        // Button styles
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = boldFont;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.overFontColor = Color.WHITE;
        buttonStyle.downFontColor = Color.WHITE;

        buttonStyle.up = createButtonDrawable(PRIMARY_COLOR);
        buttonStyle.over = createButtonDrawable(new Color(0.3f, 0.45f, 0.75f, 1));
        buttonStyle.down = createButtonDrawable(new Color(0.2f, 0.3f, 0.6f, 1));

        skin.add("default", buttonStyle);

        // Arrow button style
        TextButton.TextButtonStyle arrowButtonStyle = new TextButton.TextButtonStyle();
        arrowButtonStyle.font = arrowFont;
        arrowButtonStyle.fontColor = PRIMARY_COLOR;
        arrowButtonStyle.overFontColor = new Color(0.4f, 0.55f, 0.85f, 1);
        arrowButtonStyle.downFontColor = new Color(0.2f, 0.3f, 0.6f, 1);
        skin.add("arrow-button", arrowButtonStyle);

        // Close button style (red)
        TextButton.TextButtonStyle closeButtonStyle = new TextButton.TextButtonStyle();
        closeButtonStyle.font = boldFont;
        closeButtonStyle.fontColor = Color.WHITE;
        closeButtonStyle.overFontColor = Color.WHITE;
        closeButtonStyle.downFontColor = Color.WHITE;

        closeButtonStyle.up = createButtonDrawable(new Color(0.85f, 0.2f, 0.2f, 1));
        closeButtonStyle.over = createButtonDrawable(new Color(0.95f, 0.3f, 0.3f, 1));
        closeButtonStyle.down = createButtonDrawable(new Color(0.75f, 0.15f, 0.15f, 1));

        skin.add("close", closeButtonStyle);
    }

    private TextureRegionDrawable createButtonDrawable(Color color) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        createdTextures.add(texture); // Track for disposal
        pixmap.dispose();
        return new TextureRegionDrawable(new TextureRegion(texture));
    }

    private TextureRegionDrawable createColorDrawable(Color color) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        createdTextures.add(texture); // Track for disposal
        pixmap.dispose();
        return new TextureRegionDrawable(new TextureRegion(texture));
    }

    private void createUI() {
        // Background overlay (semi-transparent dark)
        Table background = new Table();
        background.setFillParent(true);
        background.setBackground(createColorDrawable(BG_COLOR));

        // Content panel (centered)
        Table panel = new Table();
        panel.setBackground(createColorDrawable(CARD_BG));
        panel.pad(25);

        // Title with close button
        Table headerTable = new Table();

        Label titleLabel = new Label("SELECT HERO", skin, "title");
        headerTable.add(titleLabel).expandX().left().padBottom(15);

        TextButton closeButton = new TextButton("X", skin, "close");
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }
        });
        headerTable.add(closeButton).width(50).height(50).right().padBottom(15);

        panel.add(headerTable).width(700).row();

        // Content area
        Table contentTable = new Table();

        // Left side - Hero image
        Table leftTable = new Table();

        typeLabel = new Label("", skin, "type");
        leftTable.add(typeLabel).left().padBottom(8).row();

        Container<Image> imageContainer = new Container<>();
        heroImage = new Image();
        imageContainer.setActor(heroImage);
        imageContainer.setBackground(createColorDrawable(Color.WHITE));
        leftTable.add(imageContainer).size(280, 280).pad(8).row();

        // Hero selector with arrows
        Table selectorTable = new Table();

        TextButton leftArrow = new TextButton("<", skin, "arrow-button");
        leftArrow.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentHeroIndex--;
                if (currentHeroIndex < 0) {
                    currentHeroIndex = heroes.size - 1;
                }
                updateHeroDisplay();
            }
        });
        selectorTable.add(leftArrow).width(60).padRight(20);

        nameLabel = new Label("", skin, "bold");
        selectorTable.add(nameLabel).expandX().center();

        TextButton rightArrow = new TextButton(">", skin, "arrow-button");
        rightArrow.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentHeroIndex++;
                if (currentHeroIndex >= heroes.size) {
                    currentHeroIndex = 0;
                }
                updateHeroDisplay();
            }
        });
        selectorTable.add(rightArrow).width(60).padLeft(20);

        leftTable.add(selectorTable).width(300).padTop(10).row();

        // Basic info
        Table basicInfoTable = new Table();
        basicInfoTable.setBackground(createColorDrawable(new Color(0.98f, 0.98f, 0.99f, 1)));
        basicInfoTable.pad(12);
        basicInfoTable.defaults().left().padTop(5).padBottom(5);

        ageLabel = new Label("", skin, "default");
        weaponLabel = new Label("", skin, "default");

        basicInfoTable.add(ageLabel).row();
        basicInfoTable.add(weaponLabel).row();

        leftTable.add(basicInfoTable).width(300).padTop(10);

        contentTable.add(leftTable).top().left().padRight(20);

        // Right side - Stats and info
        Table rightTable = new Table();

        // Stats
        Table statsCard = new Table();
        statsCard.setBackground(createColorDrawable(new Color(0.98f, 0.98f, 0.99f, 1)));
        statsCard.pad(15);

        Label statsTitle = new Label("STATS", skin, "title");
        statsCard.add(statsTitle).left().padBottom(12).colspan(2).row();

        Label strengthLabel = new Label("STR:", skin, "bold");
        statsCard.add(strengthLabel).left().padRight(20).padTop(8);
        strengthNumber = new Label("", skin, "stat");
        statsCard.add(strengthNumber).left().padTop(8).row();

        Label intelligenceLabel = new Label("INT:", skin, "bold");
        statsCard.add(intelligenceLabel).left().padRight(20).padTop(8);
        intelligenceNumber = new Label("", skin, "stat");
        statsCard.add(intelligenceNumber).left().padTop(8).row();

        Label agilityLabel = new Label("AGI:", skin, "bold");
        statsCard.add(agilityLabel).left().padRight(20).padTop(8);
        agilityNumber = new Label("", skin, "stat");
        statsCard.add(agilityNumber).left().padTop(8).row();

        rightTable.add(statsCard).width(360).left().padBottom(15).row();

        // Info section
        Table infoCard = new Table();
        infoCard.setBackground(createColorDrawable(new Color(0.98f, 0.98f, 0.99f, 1)));
        infoCard.pad(15);

        Label infoTitle = new Label("Info", skin, "bold");
        infoText = new Label("", skin, "default");
        infoText.setWrap(true);

        infoCard.add(infoTitle).top().left().row();
        infoCard.add(infoText).width(330).top().left().padTop(8);

        rightTable.add(infoCard).width(360).left().top().expand();

        contentTable.add(rightTable).top().left();

        panel.add(contentTable).row();

        // Select button at bottom
        TextButton selectButton = new TextButton("Select Hero", skin);
        selectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onHeroSelected(heroes.get(currentHeroIndex));
                hide();
            }
        });
        panel.add(selectButton).width(300).height(50).padTop(20);

        background.add(panel).center();

        rootTable = background;
        stage.addActor(rootTable);

        updateHeroDisplay();
    }

    private void updateHeroDisplay() {
        Hero hero = heroes.get(currentHeroIndex);

        Gdx.app.log("HeroPanel", "Updating display for: " + hero.getName());

        // Get texture
        Texture heroTexture = getHeroTexture(currentHeroIndex);
        if (heroTexture != null) {
            Gdx.app.log("HeroPanel", "Texture loaded successfully for " + hero.getName());
            heroImage.setDrawable(new TextureRegionDrawable(new TextureRegion(heroTexture)));
        } else {
            Gdx.app.log("HeroPanel", "WARNING: Texture is NULL for " + hero.getName());
        }

        // Update labels
        typeLabel.setText("Type: " + hero.getType());
        nameLabel.setText(hero.getName());
        ageLabel.setText("Age: " + hero.getAge());
        weaponLabel.setText("Weapon: " + hero.getWeapon());

        strengthNumber.setText(String.valueOf(hero.getStrength()));
        intelligenceNumber.setText(String.valueOf(hero.getIntelligence()));
        agilityNumber.setText(String.valueOf(hero.getAgility()));

        infoText.setText(hero.getInfoFacts());
    }

    private Texture getHeroTexture(int index) {
        if (index >= heroes.size) return null;

        // Get heroId directly (no parsing needed since it's just "angel", "mime", "whistle")
        String heroId = heroes.get(index).getHeroId();

        Gdx.app.log("HeroPanel", "Loading texture for heroId: " + heroId);

        switch (heroId) {
            case "angel": return assets.heroAngel;
            case "mime": return assets.heroMime;
            case "whistle": return assets.heroWhistle;
            default:
                Gdx.app.log("HeroPanel", "Unknown heroId: " + heroId);
                return null;
        }
    }

    public void show() {
        isVisible = true;
        rootTable.setVisible(true);
        updateHeroDisplay();
    }

    public void hide() {
        isVisible = false;
        rootTable.setVisible(false);
    }

    public boolean isVisible() {
        return isVisible;
    }

    public Hero getSelectedHero() {
        return heroes.get(currentHeroIndex);
    }

    // Override this method to handle hero selection
    protected void onHeroSelected(Hero hero) {
        // To be overridden by MainScreen or use listener pattern
    }

    @Override
    public void dispose() {
        skin.dispose();
        // Dispose created textures
        for (Texture tex : createdTextures) {
            tex.dispose();
        }
    }
}
