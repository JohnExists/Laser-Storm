package me.johnexists.game1.ui.laserselect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import me.johnexists.game1.world.objects.attributes.Location;
import me.johnexists.game1.world.objects.attributes.Size;
import me.johnexists.game1.world.objects.entities.Player;
import me.johnexists.game1.world.objects.weapons.lasers.LaserConstants;
import me.johnexists.game1.state.LaserSelectState;
import me.johnexists.game1.ui.UIElement;
import me.johnexists.game1.ui.uimenu.UIButton;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.math.MathUtils.floor;
import static me.johnexists.game1.world.objects.attributes.Size.getXSizeMultiplier;
import static me.johnexists.game1.world.objects.attributes.Size.getYSizeMultiplier;

@Deprecated
public class UITopPanelElement extends UIElement {

    private final LaserSelectState laserSelectState;
    private final List<UIButton> uiElements;

    public UITopPanelElement(LaserSelectState laserSelectState) {
        super(new Location(0, 0), new Size(0, 0));
        this.laserSelectState = laserSelectState;
        uiElements = new ArrayList<>();

        for (int i = 1; i < 12; i++) {
            uiElements.add(new SelectLaserButton(i));
        }
    }

    @Override
    public void update(float deltaTime) {
        uiElements.forEach(ui -> ui.update(deltaTime));
        if (uiElements.stream().filter(ui -> ui instanceof SelectLaserButton)
                .noneMatch(UIButton::locationIsWithinBounds)) {
            laserSelectState.setSelectedLaser(laserSelectState.getDefaultLaser());
        }
    }

    @Override
    public void render() {
        uiElements.forEach(UIButton::render);
    }

    protected Location calculateLocationRelativeToIndex(int index) {
        float xPos = 260 * getXSizeMultiplier() + (65 * getXSizeMultiplier() * ((index) % 6)), // rows
                yPos = 300 * getYSizeMultiplier() - (65 * getYSizeMultiplier() * floor(index / 6f)); //columns

        return new Location(xPos, yPos);
    }

    private class SelectLaserButton extends UIButton {

        final int index;

        public SelectLaserButton(int index) {
            super(laserSelectState);
            setColor(Color.WHITE);
            setLocation(calculateLocationRelativeToIndex(index - 1));
            setOnClick(() -> Player.swapLaserSkin(LaserConstants.values()[index]));
            this.index = index;
            this.size = new Size(50, 50);
        }

        @Override
        public void update(float deltaTime) {
            if (locationIsWithinBounds()) {
                laserSelectState.setSelectedLaser(LaserConstants.values()[index]);
            }
        }

        @Override
        public void render() {
            displaySprite();
            whiteBoxIfSelected();
            generateTextIfHovering();

        }

        private void generateTextIfHovering() {
            spriteBatch.begin();
            {
                if (locationIsWithinBounds()) {
                    font.draw(spriteBatch, LaserConstants.values()[index].toString(),
                            Gdx.input.getX() - glyphLayout.width,
                            Gdx.graphics.getHeight() - Gdx.input.getY() - glyphLayout.height);
                }
            }
            spriteBatch.end();
        }

        private void displaySprite() {
            spriteBatch.begin();
            {
                spriteBatch.draw(new TextureRegion(LaserConstants.values()[index].displaySprite),
                        location.getX() - size.getWidth() / 2, location.getY() - size.getHeight() / 2,
                        size.getWidth(), size.getHeight());


            }
            spriteBatch.end();
        }

        private void whiteBoxIfSelected() {
            if (Player.getLaserSkin().equals(LaserConstants.values()[index])) {
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                {
                    shapeRenderer.setColor(Color.WHITE);
                    shapeRenderer.rect(location.getX() - size.getWidth() / 2, location.getY() - size.getHeight() / 2,
                            size.getWidth(), size.getHeight());

                }
                shapeRenderer.end();
            }
        }
    }
}
