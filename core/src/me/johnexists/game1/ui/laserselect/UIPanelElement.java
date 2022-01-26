package me.johnexists.game1.ui.laserselect;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import me.johnexists.game1.world.objects.abilities.AbilityConstants;
import me.johnexists.game1.world.objects.attributes.Location;
import me.johnexists.game1.world.objects.attributes.Size;
import me.johnexists.game1.world.objects.entities.Player;
import me.johnexists.game1.world.objects.weapons.Constant;
import me.johnexists.game1.world.objects.weapons.generators.GeneratorConstants;
import me.johnexists.game1.world.objects.weapons.lasers.LaserConstants;
import me.johnexists.game1.state.LaserSelectState;
import me.johnexists.game1.ui.UIElement;
import me.johnexists.game1.ui.uimenu.UIButton;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.Gdx.*;
import static com.badlogic.gdx.graphics.GL20.*;
import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled;
import static me.johnexists.game1.world.objects.attributes.Size.getXSizeMultiplier;
import static me.johnexists.game1.world.objects.attributes.Size.getYSizeMultiplier;

public class UIPanelElement extends UIElement {

    private final LaserSelectState laserSelectState;
    private final List<UIButton> uiElements;
    private final int MIDDLE_PANEL = 13, LOWEST_PANEL = 19;

    public UIPanelElement(LaserSelectState laserSelectState) {
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
        uiElements.forEach(ui -> ui.update(deltaTime));
        if (uiElements.stream().filter(ui -> ui instanceof PanelButton)
                .noneMatch(UIButton::locationIsWithinBounds)) {
            laserSelectState.setSelectedLaser(laserSelectState.getDefaultLaser());
        }
    }

    @Override
    public void render() {
        for (int i = 0; i < 2; i++) {
            uiElements.forEach(UIButton::render);
        }
    }

    protected Location calculateLocationRelativeToIndex(int index) {
        float xPos = 260 * getXSizeMultiplier() + (65 * getXSizeMultiplier() * ((index) % 6)), // rows
                yPos = 300 * getYSizeMultiplier() - (65 * getYSizeMultiplier() * MathUtils.floor(index / 6f)); //columns

        return new Location(xPos, yPos);
    }

    private interface PanelButton {
    }

    private class SelectButton extends UIButton implements PanelButton {

        final int index;
        final GlyphLayout glyphLayout;
        final Constant constant;
        boolean renderingText = false;

        public SelectButton(int index, Constant constant) {
            super(laserSelectState);
            setColor(Color.WHITE);
            setLocation(calculateLocationRelativeToIndex(index - 1));
            this.constant = constant;

            setOnClick(() -> {
                if (constant instanceof LaserConstants) {
                    Player.swapLaserSkin(LaserConstants.values()[index]);
                } else if (constant instanceof GeneratorConstants) {
                    Player.swapLaserGenerator(GeneratorConstants.values()[index - MIDDLE_PANEL + 1]);
                } else if (constant instanceof AbilityConstants) {
                    Player.swapAbilityConstants(AbilityConstants.values()[index - LOWEST_PANEL + 1]);
                }
            });
            this.index = index;
            this.size = new Size(50, 50);
            glyphLayout = new GlyphLayout(font, constant.toString());
        }

        @Override
        public void update(float deltaTime) {
            if (constant instanceof LaserConstants) {
                if (locationIsWithinBounds()) {
                    laserSelectState.setSelectedLaser(LaserConstants.values()[index]);
                }
            }
        }

        @Override
        public void render() {
            if (!renderingText) {
                displaySprite();
                whiteBoxIfSelected();
                renderingText = true;
            } else {
                generateTextIfHovering();
                renderingText = false;
            }
        }

        private void generateTextIfHovering() {
            float leftOrRightAdjacentX = input.getX() > graphics.getWidth() * 0.75f ?
                    -glyphLayout.width * (getXSizeMultiplier() / 3) : 0;
            if (locationIsWithinBounds()) {
                shapeRenderer.begin(Filled);
                {
                    graphics.getGL20().glEnable(GL_BLEND);
                    gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                    shapeRenderer.setColor(new Color(0,0,0,0.65f));
                    shapeRenderer.rect(input.getX() + (leftOrRightAdjacentX * 1.05f) + getXSizeMultiplier() * 5 -
                                    glyphLayout.width * getXSizeMultiplier() /3 *0.025f,
                            graphics.getHeight() - input.getY() - glyphLayout.height,
                            glyphLayout.width * getXSizeMultiplier() / 3 * 1.05f,
                            glyphLayout.height * getYSizeMultiplier() / 3 * 1.05f);
                }
                shapeRenderer.end();
                gl.glDisable(GL_BLEND);

                spriteBatch.begin();
                {
                    font.getData().setScale(getXSizeMultiplier() / 3, getYSizeMultiplier() / 3);
                    font.draw(spriteBatch, constant.toString(),
                            input.getX() + getXSizeMultiplier() * 5 + (leftOrRightAdjacentX * 1.05f),
                            graphics.getHeight() - input.getY());
                }
                spriteBatch.end();
            }

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
                if (isSelected()) {
                    shapeRenderer.rect(location.getX() - size.getWidth() / 2, location.getY() - size.getHeight() / 2,
                            size.getWidth(), size.getHeight());
                }
                shapeRenderer.end();
            }
        }

        private boolean isSelected() {
            if (constant instanceof AbilityConstants) {
                return Player.getAbilityConstants().equals(AbilityConstants.values()[index - LOWEST_PANEL + 1]);
            } else if (constant instanceof GeneratorConstants) {
                return Player.getLaserGenerator().equals(GeneratorConstants.values()[index - MIDDLE_PANEL + 1]);
            } else if (constant instanceof LaserConstants) {
                return Player.getLaserSkin().equals(LaserConstants.values()[index]);
            }
            return false;
        }

        @SuppressWarnings("unused")
        private Constant getValue() {
            if (constant instanceof LaserConstants) {
                return (LaserConstants.values()[index]);
            } else if (constant instanceof GeneratorConstants) {
                return (GeneratorConstants.values()[index - MIDDLE_PANEL + 1]);
            } else if (constant instanceof AbilityConstants) {
                return (AbilityConstants.values()[index - LOWEST_PANEL + 1]);
            }
            return null;
        }
    }
}
