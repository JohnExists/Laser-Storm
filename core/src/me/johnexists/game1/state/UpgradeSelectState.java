package me.johnexists.game1.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import me.johnexists.game1.logic.GameLogic;
import me.johnexists.game1.ui.UIElement;
import me.johnexists.game1.ui.laserselect.UIDisplayElement;
import me.johnexists.game1.ui.laserselect.UIPanelElement;
import me.johnexists.game1.ui.uimenu.UIButton;
import me.johnexists.game1.world.objects.attributes.Location;
import me.johnexists.game1.world.objects.entities.Player;
import me.johnexists.game1.world.objects.weapons.lasers.LaserConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static me.johnexists.game1.world.objects.attributes.Size.getXSizeMultiplier;
import static me.johnexists.game1.world.objects.attributes.Size.getYSizeMultiplier;

public class LaserSelectState extends State {

    private final SpriteBatch spriteBatch;
    private final ShapeRenderer shapeRenderer;
    private final List<UIElement> uiElements;
    private final float RIGHT_PANEL_WIDTH = 400 * getXSizeMultiplier(),
            RIGHT_PANEL_HEIGHT = 325 * getYSizeMultiplier();
    private final int NOT_PURCHASING = 0, FAILED_PURCHASE = 1, SUCCESSFUL_PURCHASE = 2;
    protected BitmapFont font;

    private LaserConstants defaultLaser = Player.getLaserSkin(),
            selectedLaser;
    private final UIButton scalarButton;
    private int purchaseDisplay = NOT_PURCHASING;

    public LaserSelectState(GameLogic gameLogic) {
        super(gameLogic);
        selectedLaser = LaserConstants.RAINBOW;
        font = new BitmapFont(Gdx.files.internal("DefaultFont.fnt"));

        uiElements = new ArrayList<>();
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        UIButton back = new UIButton(this);
        back.setLocation(new Location(115 * getXSizeMultiplier(), 25 * getYSizeMultiplier()));
        back.setColor(Color.WHITE);
        back.setScale(0.90f);
        back.setDisplayText("Back");
        back.setOnClick(() -> gameLogic.setSelectedState(Optional.of(new MainMenuState(gameLogic))));

        scalarButton = new UIButton(this);
        scalarButton.setLocation(new Location(115 * getXSizeMultiplier(), 55 * getYSizeMultiplier()));
        scalarButton.setColor(Color.WHITE);
        scalarButton.setScale(0.90f);
        scalarButton.setDisplayText("Scalar: " + MathUtils.round(Player.playerScalar));
        scalarButton.setOnClick(() -> {
            if (Player.bits > returnScalarCost()) {
                scalarButton.modifyDisplayText("Scalar: " + MathUtils.round(Player.playerScalar));
                Player.bits -= returnScalarCost();
                Player.playerScalar += 1;
                purchaseDisplay = SUCCESSFUL_PURCHASE;
            } else {
                purchaseDisplay = FAILED_PURCHASE;
            }
        });

        uiElements.add(back);
        uiElements.add(scalarButton);
        uiElements.add(new UIDisplayElement(this));
        uiElements.add(new UIPanelElement(this));
    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.BLACK);
        uiElements.forEach(UIElement::render);

        spriteBatch.begin();
        {
            font.getData().setScale(getXSizeMultiplier() / 1.45f);
            font.draw(spriteBatch, MathUtils.round(Player.bits) + " Bits", 235 * getXSizeMultiplier(), 45 * getYSizeMultiplier());
        }
        spriteBatch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        {
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.rect(225 * getXSizeMultiplier(), 15 * getYSizeMultiplier(),
                    RIGHT_PANEL_WIDTH, RIGHT_PANEL_HEIGHT);
        }
        shapeRenderer.end();
    }

    @Override
    public void update(float deltaTime) {
        uiElements.forEach(uiElement -> uiElement.update(deltaTime));
        defaultLaser = Player.getLaserSkin();
        if (scalarButton.locationIsWithinBounds()) {
            String displayText = switch (purchaseDisplay) {
                case NOT_PURCHASING -> String.format("-%d Bits >> %d", returnScalarCost(),
                        MathUtils.round(Player.playerScalar + 1));
                case FAILED_PURCHASE -> "Purchase Failure";
                case SUCCESSFUL_PURCHASE -> "Purchase Success!";
                default -> throw new IllegalStateException("Unexpected value: " + purchaseDisplay);
            };
            scalarButton.modifyDisplayText(displayText);
        } else {
            scalarButton.modifyDisplayText("Scalar: " + MathUtils.round(Player.playerScalar));
            purchaseDisplay = NOT_PURCHASING;
        }
    }

    public int returnScalarCost() {
        return MathUtils.round(Player.playerScalar * 5.75f);
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
