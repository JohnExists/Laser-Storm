package me.johnexists.game1.ui.uimenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import me.johnexists.game1.objects.attributes.Location;
import me.johnexists.game1.objects.attributes.Size;
import me.johnexists.game1.state.State;
import me.johnexists.game1.ui.UIElement;

@SuppressWarnings("all")
public class UIButton extends UIElement {

    private float scale, width, height;
    private String displayText;
    private Color color;
    private Runnable onClick;
    private final State workingState;

    public UIButton(State workingState) {
        super(new Location(0, 0), new Size(0, 0));
        this.scale = 1.0f;
        this.width = 235 * scale;
        this.height = 25 * scale;
        this.size = new Size(width * scale, height * scale);
        this.workingState = workingState;
        this.displayText = "";
        this.onClick = () -> {
            throw new IllegalStateException("<buttonName>.setOnClick(Runnable)");
        };
        this.color = Color.RED;
        glyphLayout.setText(font, displayText);
        registerKeys();
    }

    public void registerKeys() {
        workingState.getGameLogic().getKeyInput().registerOnMouseReleased(() -> {
            if (locationIsWithinBounds() && workingState.getGameLogic().getSelectedState().equals(workingState)) {
                this.onClick.run();
            }
        });
    }

    @Override
    public void update(float deltaTime) {
    }

    @Override
    public void render() {
        shapeRenderer.setColor(color);
        if (locationIsWithinBounds()) {
            renderFilledEdge();
            if (color != Color.WHITE) {
                font.setColor(Color.WHITE);
            } else {
                font.setColor(Color.BLACK);
            }
        } else {
            renderLineEdge();
            font.setColor(color);
        }
        renderText();
    }

    private void renderLineEdge() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        {
            shapeRenderer.rect(location.getX() - size.getWidth() / 2, location.getY() - size.getHeight() / 2,
                    size.getWidth(), size.getHeight());
        }
        shapeRenderer.end();
    }

    private void renderFilledEdge() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        {
            shapeRenderer.rect(location.getX() - size.getWidth() / 2, location.getY() - size.getHeight() / 2,
                    size.getWidth(), size.getHeight());
        }
        shapeRenderer.end();
    }

    private void renderText() {
        spriteBatch.begin();
        {
            font.getData().setScale(Size.getXSizeMultiplier() * 0.75f * scale,
                    Size.getYSizeMultiplier() * 0.75f * scale);
            font.draw(spriteBatch, displayText, location.getX() - (glyphLayout.width / 2 *
                            (Size.getXSizeMultiplier() * 0.75f * scale)),
                    location.getY() + (glyphLayout.height / 2 *
                            (Size.getYSizeMultiplier() * 0.75f * scale)));

        }
        spriteBatch.end();
    }

    public boolean locationIsWithinBounds() {
        Location mouseLoc = new Location(Gdx.input.getX(),
                Gdx.graphics.getHeight() - Gdx.input.getY());
        float width = size.getWidth() / 2, height = size.getHeight() / 2;
        return mouseLoc.getX() > this.location.getX() - width &&
                mouseLoc.getX() < this.location.getX() + width &&
                mouseLoc.getY() > this.location.getY() - height &&
                mouseLoc.getY() < this.location.getY() + height;

    }

    public void setScale(float scale) {
        this.scale = scale;
        this.width = 235 * scale;
        this.height = 25 * scale;
        this.size = new Size(width * scale, height * scale);
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
        glyphLayout.setText(font, displayText);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setOnClick(Runnable onClick) {
        this.onClick = onClick;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

}
