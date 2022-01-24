package me.johnexists.game1.ui.laserselect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import me.johnexists.game1.abilities.AbilityConstants;
import me.johnexists.game1.objects.attributes.Location;
import me.johnexists.game1.objects.attributes.Size;
import me.johnexists.game1.objects.entities.Player;
import me.johnexists.game1.objects.weapons.Constant;
import me.johnexists.game1.objects.weapons.generators.GeneratorConstants;
import me.johnexists.game1.objects.weapons.lasers.LaserConstants;
import me.johnexists.game1.state.LaserSelectState;
import me.johnexists.game1.ui.UIElement;
import me.johnexists.game1.ui.uimenu.UIButton;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.math.MathUtils.floor;
import static me.johnexists.game1.objects.attributes.Size.getXSizeMultiplier;
import static me.johnexists.game1.objects.attributes.Size.getYSizeMultiplier;

public class UIMiddlePanelElement extends UIElement {

    private final LaserSelectState laserSelectState;
    private final List<UIButton> uiElements;
    private final int MIDDLE_PANEL = 13;
    private final int LOWEST_PANEL = 19;

    public UIMiddlePanelElement(LaserSelectState laserSelectState) {
        super(new Location(0, 0), new Size(0, 0));
        this.laserSelectState = laserSelectState;
        uiElements = new ArrayList<>();

        int laserConstLength = LaserConstants.values().length - 1;
        int genConstLength = GeneratorConstants.values().length - 1;
        int abilityConstLength = AbilityConstants.values().length - 1;

        for (int i = 1; i < laserConstLength; i++) {
            uiElements.add(new SelectButton(i, LaserConstants.values()[i]));
        }

        for (int i = MIDDLE_PANEL; i < MIDDLE_PANEL + genConstLength; i++) {
            uiElements.add(new SelectButton(i, GeneratorConstants.values()[i - MIDDLE_PANEL + 1]));
        }

        for (int i = LOWEST_PANEL; i < LOWEST_PANEL + abilityConstLength; i++) {
            uiElements.add(new SelectButton(i, AbilityConstants.values()[i - LOWEST_PANEL + 1]));
        }
    }

    @Override
    public void update(float deltaTime) {
    }

    @Override
    public void render() {
        uiElements.forEach(UIButton::render);
    }

    protected Location calculateLocationRelativeToIndex(int index) {
//        float xPos = 260 * getXSizeMultiplier() + (65 * getXSizeMultiplier() * ((index) % 6)), // rows
//                yPos = 150 * getYSizeMultiplier() - (65 * getYSizeMultiplier() * floor(index / 6f)); //columns
//
//        return new Location(xPos, yPos);
        float xPos = 260 * getXSizeMultiplier() + (65 * getXSizeMultiplier() * ((index) % 6)), // rows
                yPos = 300 * getYSizeMultiplier() - (65 * getYSizeMultiplier() * floor(index / 6f)); //columns

        return new Location(xPos, yPos);
    }

    private class SelectButton extends UIButton {

        final int index;
        final GlyphLayout glyphLayout;
        final Constant constant;

        public SelectButton(int index, Constant constant) {
            super(laserSelectState);
            setColor(Color.WHITE);
            setLocation(calculateLocationRelativeToIndex(index - 1));
            this.constant = constant;

            setOnClick(() -> {
                if (constant instanceof LaserConstants) {
                    Player.setLaserSkin(LaserConstants.values()[index]);
                } else if (constant instanceof GeneratorConstants) {
                    Player.setLaserGenerator(GeneratorConstants.values()[index - MIDDLE_PANEL]);
                } else if (constant instanceof AbilityConstants) {
                    Player.setAbilityConstants(AbilityConstants.values()[index - LOWEST_PANEL]);
                }
            });
            this.index = index;
            this.size = new Size(50, 50);
            glyphLayout = new GlyphLayout(font, constant.toString());
        }

        @Override
        public void update(float deltaTime) {
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
                    font.draw(spriteBatch, constant.toString(),
                            Gdx.input.getX() - glyphLayout.width,
                            Gdx.graphics.getHeight() - Gdx.input.getY() - glyphLayout.height);
                }
            }
            spriteBatch.end();
        }

        private void displaySprite() {
            spriteBatch.begin();
            {
                spriteBatch.draw(new TextureRegion(constant.getSprite()),
                        location.getX() - size.getWidth() / 2, location.getY() - size.getHeight() / 2,
                        size.getWidth(), size.getHeight());

            }
            spriteBatch.end();
        }

        private void whiteBoxIfSelected() {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            {
                shapeRenderer.setColor(Color.WHITE);
                if (constant instanceof AbilityConstants) {
                    if (Player.getAbilityConstants().equals(AbilityConstants.values()[index - LOWEST_PANEL])) {
                        shapeRenderer.rect(location.getX() - size.getWidth() / 2, location.getY() - size.getHeight() / 2,
                                size.getWidth(), size.getHeight());

                    }
                } else if (constant instanceof GeneratorConstants) {
                    if (Player.getLaserGenerator().equals(GeneratorConstants.values()[index - MIDDLE_PANEL])) {
                        shapeRenderer.rect(location.getX() - size.getWidth() / 2, location.getY() - size.getHeight() / 2,
                                size.getWidth(), size.getHeight());

                    }

                } else if (constant instanceof LaserConstants) {
                    if (Player.getLaserSkin().equals(LaserConstants.values()[index])) {
                        shapeRenderer.rect(location.getX() - size.getWidth() / 2, location.getY() - size.getHeight() / 2,
                                size.getWidth(), size.getHeight());
                    }
                }
                shapeRenderer.end();
            }
        }
    }
}
