package me.johnexists.game1.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import me.johnexists.game1.objects.attributes.Location;
import me.johnexists.game1.logic.GameLogic;
import me.johnexists.game1.ui.UIElement;
import me.johnexists.game1.ui.uimenu.UIButton;

import java.util.List;
import java.util.Optional;

import static com.badlogic.gdx.Gdx.graphics;
import static java.util.List.of;
import static me.johnexists.game1.objects.attributes.Size.getXSizeMultiplier;

@SuppressWarnings("all")
public class MainMenuState extends State {

    private final Texture mainMenuBackground;
    private final SpriteBatch spriteBatch;
    private final List<UIElement> mainMenuElements;
    private UIButton play, exit, laserSelect;

    public MainMenuState(GameLogic gameLogic) {
        super(gameLogic);
        spriteBatch = new SpriteBatch();
        mainMenuBackground = new Texture(Gdx.files.internal("MainMenu.png"));

        play = new UIButton(this);
        play.setLocation(new Location(graphics.getWidth() / 2f - (35 * getXSizeMultiplier()), graphics.getHeight() / 2f));
        play.setDisplayText("Play");
        play.setOnClick(() -> gameLogic.setSelectedState(Optional.of(new GameState(gameLogic))));


        laserSelect = new UIButton(this);
        laserSelect.setLocation(new Location(graphics.getWidth() / 2f - (35 * getXSizeMultiplier()), graphics.getHeight() / 2f - 150));
        laserSelect.setDisplayText("Customize Laser");
        laserSelect.setOnClick(() -> gameLogic.setSelectedState(Optional.of(new LaserSelectState(gameLogic))));

        exit = new UIButton(this);
        exit.setLocation(new Location(graphics.getWidth() / 2f - (35 * getXSizeMultiplier()), graphics.getHeight() / 2f - 300));
        exit.setDisplayText("Exit");
        exit.setOnClick(() -> System.exit(-1));

        this.mainMenuElements = of(play, exit, laserSelect);

    }

    @Override
    public void render() {
        Gdx.gl.glLineWidth(getXSizeMultiplier());
        spriteBatch.begin();
        {
            spriteBatch.draw(new TextureRegion(mainMenuBackground), 0f, 0f, 0f, 0f, graphics.getWidth(),
                    graphics.getHeight(), 1.0f, 1.0f, 0);
        }
        spriteBatch.end();
        mainMenuElements.forEach(UIElement::render);
    }

    @Override
    public void update(float deltaTime) {
        mainMenuElements.forEach(uiElement -> uiElement.update(deltaTime));
    }

    @Override
    public void dispose() {
        mainMenuBackground.dispose();
        mainMenuElements.forEach(UIElement::dispose);
    }
}
