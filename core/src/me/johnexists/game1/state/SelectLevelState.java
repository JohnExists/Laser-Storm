package me.johnexists.game1.state;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import me.johnexists.game1.logic.GameLogic;
import me.johnexists.game1.ui.UIElement;
import me.johnexists.game1.ui.uimenu.UIButton;
import me.johnexists.game1.world.objects.attributes.Location;
import me.johnexists.game1.world.objects.attributes.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.badlogic.gdx.Gdx.gl;
import static com.badlogic.gdx.Gdx.graphics;
import static me.johnexists.game1.world.objects.attributes.Size.getXSizeMultiplier;
import static me.johnexists.game1.world.objects.attributes.Size.getYSizeMultiplier;

public class SelectLevelState extends State {

    private final List<UIElement> uiElements;
    private final ShapeRenderer shapeRenderer;

    public SelectLevelState(GameLogic gameLogic) {
        super(gameLogic);
        uiElements = new ArrayList<>();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        List<SelectButton> selectButtons = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            selectButtons.add(new SelectButton(this, i + 1));
        }

        UIButton back = new UIButton(this);
        back.setLocation(new Location(85 * getXSizeMultiplier(), 25 * getYSizeMultiplier()));
        back.setColor(Color.WHITE);
        back.setScale(0.8f);
        back.setDisplayText("Back");
        back.setOnClick(() -> gameLogic.setSelectedState(Optional.of(new MainMenuState(gameLogic))));

        uiElements.addAll(selectButtons);
        uiElements.add(back);
    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.BLACK);
        renderBackground();
        uiElements.forEach(UIElement::render);

    }

    private void renderBackground() {
        final float GAP = 50 * getXSizeMultiplier();

        shapeRenderer.begin();
        {
            shapeRenderer.setColor(GameLogic.cycle());
            gl.glLineWidth(getXSizeMultiplier());
            for (int i = 0; i < graphics.getHeight(); i += GAP) {
                shapeRenderer.line(0, i, graphics.getWidth(), i);
            }
            for (int i = 0; i < graphics.getWidth(); i += GAP) {
                shapeRenderer.line(i, 0, i, graphics.getHeight());
            }
        }
        shapeRenderer.end();
    }

    @Override
    public void update(float deltaTime) {
        uiElements.forEach(uiElement -> uiElement.update(deltaTime));
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }

    private static class SelectButton extends UIButton {


        public SelectButton(State workingState, int level) {
            super(workingState);
            this.size = new Size(50, 50);
            setLocation(new Location((50 * getXSizeMultiplier()) + (70 * (level - 1) * getXSizeMultiplier()), graphics.getHeight() - 150));
            setDisplayText(String.valueOf(level));
            setScale(getXSizeMultiplier());
            setColor(Color.GRAY);
            setOnClick(() -> {
                System.gc();
                workingState.getGameLogic().setSelectedState(Optional.of(new GameState(workingState.getGameLogic(), level)));
            });
        }

        @Override
        public void render() {
            shapeRenderer.setColor(Color.GRAY);
            if (locationIsWithinBounds()) {
                renderFilledEdge();
                font.setColor(Color.BLACK);
            } else {
                renderFilledEdge();
                font.setColor(Color.DARK_GRAY);
            }
            renderText();
        }

        @Override
        public void setScale(float scale) {
            this.size = new Size(15 * scale, 15 * scale);
        }
    }
}
