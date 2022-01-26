package me.johnexists.game1.ui.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import me.johnexists.game1.world.objects.attributes.Location;
import me.johnexists.game1.world.objects.attributes.Size;
import me.johnexists.game1.ui.UIElement;

import static com.badlogic.gdx.Gdx.gl;
import static com.badlogic.gdx.Gdx.graphics;
import static com.badlogic.gdx.graphics.GL20.*;
import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled;

public class HUDDeath extends UIElement {

    private final ShapeRenderer renderBackground;
    private final GlyphLayout mainGlyphLayout, secondaryGlyphLayout;
    private final float WIDTH = graphics.getWidth(),
            HEIGHT = graphics.getHeight();

    public HUDDeath() {
        super(new Location(0, 0),
                new Size(graphics.getWidth(), graphics.getHeight()));
        renderBackground = new ShapeRenderer();
        mainGlyphLayout = new GlyphLayout();
        secondaryGlyphLayout = new GlyphLayout();

        mainGlyphLayout.setText(font, "You Died!",
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
            renderBackground.setColor(new Color(1, 0, 0, 0.65f));
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
}
