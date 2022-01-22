package me.johnexists.game1.ui.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import me.johnexists.game1.objects.attributes.Damageable;
import me.johnexists.game1.objects.attributes.Location;
import me.johnexists.game1.objects.attributes.Size;
import me.johnexists.game1.objects.entities.DamageableEntity;
import me.johnexists.game1.ui.UIElement;

public class HUDHealth extends UIElement implements Damageable {

    private float health;
    private DamageableEntity damageableEntity;
    private GlyphLayout textLayout;
    private final float RADIUS = 45 * Size.getXSizeMultiplier();


    public HUDHealth(Location location, Size size, DamageableEntity damageableEntity) {
        super(location, size);
        this.health = damageableEntity.getHealth();
        this.damageableEntity = damageableEntity;
        textLayout = new GlyphLayout();
    }

    @Override
    public void update(float deltaTime) {
        health = damageableEntity.getHealth();
        textLayout.setText(font, String.valueOf(Math.round(health)));
    }

    @Override
    public void render() {
        createGraphics();
        createText();

    }

    private void createGraphics() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        {
            // Draws background
            Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.setColor(new Color(26 / 255f, 34 / 255f, 39 / 255f, 0.45f));
            shapeRenderer.circle(location.getX(), location.getY(), RADIUS);

            // Draws foreground
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.arc(location.getX(), location.getY(), RADIUS, 90, calculateDegrees());

            // Draws Text Display Background
            shapeRenderer.setColor(new Color(26 / 255f, 34 / 255f, 39 / 255f, 1f));
            shapeRenderer.circle(location.getX(), location.getY(), RADIUS * 0.85f);
        }
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

    }

    private void createText() {
        // Draws Text Display
        spriteBatch.begin();
        {
            font.getData().setScale(Size.getXSizeMultiplier(), Size.getYSizeMultiplier());
            font.draw(spriteBatch, String.valueOf(Math.round(health)),
                    location.getX() - textLayout.width / 2, location.getY() + textLayout.height / 2);
        }
        spriteBatch.end();
    }

    private float calculateDegrees() {
        float healthPercentage = health / MAX_HEALTH;
        return healthPercentage * 360;
    }

    @Override
    public void damage(float damage) {

    }

    @Override
    public void heal(float heal) {

    }
}
