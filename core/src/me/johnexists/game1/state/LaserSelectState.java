package me.johnexists.game1.state;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import me.johnexists.game1.objects.attributes.Location;
import me.johnexists.game1.logic.GameLogic;
import me.johnexists.game1.objects.entities.Player;
import me.johnexists.game1.objects.weapons.lasers.LaserConstants;
import me.johnexists.game1.ui.UIElement;
import me.johnexists.game1.ui.laserselect.UIBottomPanelElement;
import me.johnexists.game1.ui.laserselect.UIDisplayElement;
import me.johnexists.game1.ui.laserselect.UITopPanelElement;
import me.johnexists.game1.ui.uimenu.UIButton;

import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.of;
import static me.johnexists.game1.objects.attributes.Size.getXSizeMultiplier;
import static me.johnexists.game1.objects.attributes.Size.getYSizeMultiplier;

@SuppressWarnings("all")
public class LaserSelectState extends State {

    private final SpriteBatch spriteBatch;
    private final ShapeRenderer shapeRenderer;
    private final List<UIElement> uiElements;
    private final float RIGHT_PANEL_WIDTH = 400 * getXSizeMultiplier(),
            RIGHT_PANEL_HEIGHT = 325 * getYSizeMultiplier();

    private LaserConstants defaultLaser = Player.getLaserSkin(),
            selectedLaser;

    public LaserSelectState(GameLogic gameLogic) {
        super(gameLogic);
        selectedLaser = LaserConstants.RAINBOW;

        uiElements = new ArrayList<>();
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        UIButton back = new UIButton(this);
        back.setLocation(new Location(115 * getXSizeMultiplier(), 25 * getYSizeMultiplier()));
        back.setColor(Color.WHITE);
        back.setScale(0.90f);
        back.setDisplayText("Back");
        back.setOnClick(() -> gameLogic.setSelectedState(of(new MainMenuState(gameLogic))));

        uiElements.add(back);
        uiElements.add(new UIDisplayElement(this));
        uiElements.add(new UITopPanelElement(this));
        uiElements.add(new UIBottomPanelElement(this));
    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.BLACK);
        uiElements.forEach(UIElement::render);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        {
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.rect(225 * getXSizeMultiplier(), 15 * getXSizeMultiplier(),
                    RIGHT_PANEL_WIDTH, RIGHT_PANEL_HEIGHT);
        }
        shapeRenderer.end();
    }

    @Override
    public void update(float deltaTime) {
        uiElements.forEach(uiElement -> uiElement.update(deltaTime));
        defaultLaser = Player.getLaserSkin();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        spriteBatch.dispose();
    }

    public LaserConstants getDefaultLaser() {
        return defaultLaser;
    }

    public LaserConstants getSelectedLaser() {
        return selectedLaser;
    }

    public void setSelectedLaser(LaserConstants selectedLaser) {
        this.selectedLaser = selectedLaser;
    }

}
