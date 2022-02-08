package me.johnexists.game1.ui.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import me.johnexists.game1.ui.UIElement;
import me.johnexists.game1.world.objects.attributes.Location;
import me.johnexists.game1.world.objects.attributes.Size;

import static com.badlogic.gdx.Gdx.gl;
import static com.badlogic.gdx.Gdx.graphics;
import static com.badlogic.gdx.graphics.GL20.*;
import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled;

public class HUDEndScreen extends UIElement {

    private final ShapeRenderer renderBackground;
    private final GlyphLayout mainGlyphLayout, secondaryGlyphLayout;
    private final float WIDTH = graphics.getWidth(),
            HEIGHT = graphics.getHeight();
    private final boolean victory;
    private final String text;

    private int phase = 0;
    private Color selectedColour = Color.WHITE;

    public HUDEndScreen(boolean victory) {
        super(new Location(0, 0),
                new Size(graphics.getWidth(), graphics.getHeight()));
        this.victory = victory;
        renderBackground = new ShapeRenderer();
        mainGlyphLayout = new GlyphLayout();
        secondaryGlyphLayout = new GlyphLayout();

        this.text = victory ? "You Won!" : "You Died!";

        mainGlyphLayout.setText(font, text,
                Color.WHITE, graphics.getWidth(), Align.center, true);
        secondaryGlyphLayout.setText(font, "Press 'R' To Restart\nPress 'M' To Head To Main Menu",
                Color.WHITE, graphics.getWidth(), Align.center, true);
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void render() {
        float primaryTextY = HEIGHT / 2f + mainGlyphLayout.height / 2,
                secondaryTextY = HEIGHT / 2f - (mainGlyphLayout.height * 2f) - (Size.getYSizeMultiplier() * 32f);

        renderDeathScreen();
        spriteBatch.begin();
        {

            if (victory) {
                phase += 1;
                generateNewColor();
                mainGlyphLayout.setText(font, text, selectedColour,
                        graphics.getWidth(), Align.center, true);
                secondaryGlyphLayout.setText(font, "Press 'R' To Restart\nPress 'M' To Head To Main Menu",
                        selectedColour, graphics.getWidth(), Align.center, true);
            }

            // Primary Text
            font.draw(spriteBatch, mainGlyphLayout, 0, primaryTextY);

            // Secondary Text
            font.draw(spriteBatch, secondaryGlyphLayout, 0, secondaryTextY);
            font.getData().setScale(1f, 1f);
        }
        spriteBatch.end();
    }

    public void renderDeathScreen() {
        renderBackground.begin(Filled);
        {
            graphics.getGL20().glEnable(GL_BLEND);
            gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            Color backgroundColor = !victory ? new Color(1, 0, 0, 0.65f) :
                    new Color(0, 0, 0, 0.65f);
            renderBackground.setColor(backgroundColor);
            renderBackground.rect(0, 0, WIDTH, HEIGHT);
        }
        renderBackground.end();
        gl.glDisable(GL_BLEND);
    }

    @Override
    public void dispose() {
        super.dispose();
        renderBackground.dispose();
    }

    public void generateNewColor() {
        if (phase % 4 == 0) {
            selectedColour = new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1);
        }
    }
}
