package me.johnexists.game1.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import me.johnexists.game1.world.objects.attributes.Location;
import me.johnexists.game1.world.objects.attributes.Size;

public abstract class UIElement {

    protected Location location;
    protected Size size;
    protected BitmapFont font;
    protected GlyphLayout glyphLayout;
    protected ShapeRenderer shapeRenderer;
    protected SpriteBatch spriteBatch;

    public UIElement(Location location, Size size) {
        this.location = location;
        this.size = size;
        font = new BitmapFont(Gdx.files.internal("DefaultFont.fnt"));
        glyphLayout = new GlyphLayout();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        spriteBatch = new SpriteBatch();
    }

    public abstract void update(float deltaTime);
    public abstract void render();

    public void dispose() {
        shapeRenderer.dispose();
        spriteBatch.dispose();
        font.dispose();
    }

    public GlyphLayout getGlyphLayout() {
        return glyphLayout;
    }
}
