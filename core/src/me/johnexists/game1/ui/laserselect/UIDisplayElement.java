package me.johnexists.game1.ui.laserselect;

import com.badlogic.gdx.graphics.Color;
import me.johnexists.game1.world.objects.attributes.Location;
import me.johnexists.game1.world.objects.attributes.Size;
import me.johnexists.game1.state.UpgradeSelectState;
import me.johnexists.game1.ui.UIElement;

import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled;
import static me.johnexists.game1.world.objects.attributes.Size.getXSizeMultiplier;
import static me.johnexists.game1.world.objects.attributes.Size.getYSizeMultiplier;

public class UIDisplayElement extends UIElement {

    private final UpgradeSelectState upgradeSelectState;
    private final Location playerLocation, enemyLocation;
    private final float THICKNESS = 10 * getXSizeMultiplier();

    public UIDisplayElement(UpgradeSelectState upgradeSelectState) {
        super(new Location(0, 0), new Size(0, 0));
        this.upgradeSelectState = upgradeSelectState;
        playerLocation = new Location(50 * getXSizeMultiplier(), 100 * getYSizeMultiplier());
        enemyLocation = new Location(150 * getXSizeMultiplier(), 275 * getYSizeMultiplier());
    }

    @Override
    public void update(float deltaTime) {
    }

    @Override
    public void render() {
        spriteBatch.begin();
        {
            spriteBatch.draw(upgradeSelectState.getSelectedLaser().displaySprite, playerLocation.getX(), playerLocation.getY(),
                    THICKNESS / 2, THICKNESS / 2, 185f * getXSizeMultiplier(), THICKNESS,
                    1.0f, 1.0f, 60.26f);
        }
        spriteBatch.end();

        shapeRenderer.begin(Filled);
        {
            shapeRenderer.setColor(new Color(136/255f,0,0,1));
            shapeRenderer.circle(playerLocation.getX(), playerLocation.getY(), 25 * getXSizeMultiplier());
            shapeRenderer.setColor(Color.BLUE);
            shapeRenderer.rect(enemyLocation.getX(), enemyLocation.getY(), 150, 150);

        }
        shapeRenderer.end();
    }

}
