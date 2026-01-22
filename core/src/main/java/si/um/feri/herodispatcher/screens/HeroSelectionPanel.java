package si.um.feri.herodispatcher.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
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

public class HeroSelectionPanel implements Disposable {

    private Stage stage;
    private Skin skin;
    private Assets assets;
    private Array<Hero> heroes;
    private int currentHeroIndex = 0;

    private Table rootTable;
    private Image heroImage;
    private Label nameLabel;
    private Label typeLabel, ageLabel, weaponLabel;
    private Label strengthNumber, intelligenceNumber, agilityNumber;
    private Label infoText;

    private boolean isVisible = false;

    private final Color BG_COLOR = new Color(0.1f, 0.1f, 0.15f, 0.92f);
    private final Color CARD_BG = new Color(1, 1, 1, 0.98f);

    private Array<Texture> createdTextures = new Array<>();

    public HeroSelectionPanel(Stage stage, Assets assets) {
        this.stage = stage;
        this.assets = assets;
        this.skin = assets.uiSkin;
        this.heroes = HeroData.getAllHeroes();

        createUI();
        hide();
    }

    private TextureRegionDrawable createColorDrawable(Color color) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        createdTextures.add(texture);
        pixmap.dispose();
        return new TextureRegionDrawable(new TextureRegion(texture));
    }

    private void createUI() {
        Table background = new Table();
        background.setFillParent(true);
        background.setBackground(createColorDrawable(BG_COLOR));

        Table panel = new Table();
        panel.setBackground(createColorDrawable(CARD_BG));
        panel.pad(25);

        Table headerTable = new Table();

        Label titleLabel = new Label("SELECT HERO", skin);
        titleLabel.setFontScale(2.0f);
        headerTable.add(titleLabel).expandX().left().padBottom(15);

        TextButton closeButton = new TextButton("X", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }
        });
        headerTable.add(closeButton).width(50).height(50).right().padBottom(15);

        panel.add(headerTable).width(700).row();

        Table contentTable = new Table();

        Table leftTable = new Table();

        typeLabel = new Label("", skin);
        typeLabel.setFontScale(0.9f);
        leftTable.add(typeLabel).left().padBottom(8).row();

        Container<Image> imageContainer = new Container<>();
        heroImage = new Image();
        imageContainer.setActor(heroImage);
        imageContainer.setBackground(createColorDrawable(Color.WHITE));
        leftTable.add(imageContainer).size(280, 280).pad(8).row();

        Table selectorTable = new Table();

        TextButton leftArrow = new TextButton("<", skin);
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

        nameLabel = new Label("", skin);
        nameLabel.setFontScale(1.3f); // Make hero name bigger
        selectorTable.add(nameLabel).expandX().center();

        TextButton rightArrow = new TextButton(">", skin);
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

        Table basicInfoTable = new Table();
        basicInfoTable.setBackground(createColorDrawable(new Color(0.98f, 0.98f, 0.99f, 1)));
        basicInfoTable.pad(12);
        basicInfoTable.defaults().left().padTop(5).padBottom(5);

        ageLabel = new Label("", skin);
        weaponLabel = new Label("", skin);

        basicInfoTable.add(ageLabel).row();
        basicInfoTable.add(weaponLabel).row();

        leftTable.add(basicInfoTable).width(300).padTop(10);

        contentTable.add(leftTable).top().left().padRight(20);

        Table rightTable = new Table();

        Table statsCard = new Table();
        statsCard.setBackground(createColorDrawable(new Color(0.98f, 0.98f, 0.99f, 1)));
        statsCard.pad(15);

        Label statsTitle = new Label("STATS", skin);
        statsTitle.setFontScale(1.5f);
        statsCard.add(statsTitle).left().padBottom(12).colspan(2).row();

        Label strengthLabel = new Label("STR:", skin);
        strengthLabel.setFontScale(1.1f);
        statsCard.add(strengthLabel).left().padRight(20).padTop(8);

        strengthNumber = new Label("", skin);
        strengthNumber.setFontScale(1.8f);
        statsCard.add(strengthNumber).left().padTop(8).row();

        Label intelligenceLabel = new Label("INT:", skin);
        intelligenceLabel.setFontScale(1.1f);
        statsCard.add(intelligenceLabel).left().padRight(20).padTop(8);

        intelligenceNumber = new Label("", skin);
        intelligenceNumber.setFontScale(1.8f);
        statsCard.add(intelligenceNumber).left().padTop(8).row();

        Label agilityLabel = new Label("AGI:", skin);
        agilityLabel.setFontScale(1.1f);
        statsCard.add(agilityLabel).left().padRight(20).padTop(8);

        agilityNumber = new Label("", skin);
        agilityNumber.setFontScale(1.8f);
        statsCard.add(agilityNumber).left().padTop(8).row();

        rightTable.add(statsCard).width(360).left().padBottom(15).row();

        Table infoCard = new Table();
        infoCard.setBackground(createColorDrawable(new Color(0.98f, 0.98f, 0.99f, 1)));
        infoCard.pad(15);

        Label infoTitle = new Label("Info", skin);
        infoTitle.setFontScale(1.2f);

        infoText = new Label("", skin);
        infoText.setWrap(true);

        infoCard.add(infoTitle).top().left().row();
        infoCard.add(infoText).width(330).top().left().padTop(8);

        rightTable.add(infoCard).width(360).left().top().expand();

        contentTable.add(rightTable).top().left();

        panel.add(contentTable).row();

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

        Texture heroTexture = getHeroTexture(currentHeroIndex);
        if (heroTexture != null) {
            Gdx.app.log("HeroPanel", "Texture loaded successfully for " + hero.getName());
            heroImage.setDrawable(new TextureRegionDrawable(new TextureRegion(heroTexture)));
        } else {
            Gdx.app.log("HeroPanel", "WARNING: Texture is NULL for " + hero.getName());
        }

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

    // override this method to handle hero selection
    protected void onHeroSelected(Hero hero) {
        // to be overridden by MainScreen or use listener pattern
    }

    @Override
    public void dispose() {

        for (Texture tex : createdTextures) {
            tex.dispose();
        }
    }
}
