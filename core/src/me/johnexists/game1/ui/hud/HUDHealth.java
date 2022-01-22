package me.johnexists.game1.ui.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import me.johnexists.game1.objects.attributes.Damageable;
import me.johnexists.game1.objects.attributes.Location;
import me.johnexists.game1.objects.attributes.Size;
import me.johnexists.game1.objects.entities.DamageableEntity;
import me.johnexists.game1.ui.UIElement;

import static com.badlogic.gdx.Gdx.gl;
import static com.badlogic.gdx.Gdx.graphics;
import static com.badlogic.gdx.graphics.GL20.*;
import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled;

public class HUDHealth extends UIElement implements Damageable {

    private float health;
    private final DamageableEntity damageableEntity;
    private final GlyphLayout textLayout;
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
        shapeRenderer.begin(Filled);
        {
            // Draws background
            graphics.getGL20().glEnable(GL_BLEND);
            gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.setColor(new Color(26 / 255f, 34 / 255f, 39 / 255f, 0.45f));
            shapeRenderer.circle(location.getX(), location.getY(), RADIUS);

            // Draws foreground
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.arc(location.getX(), location.getY(), RADIUS, 90, calculateNonOverflowRotation());

            if (health > MAX_HEALTH) {
                shapeRenderer.setColor(Color.CYAN);
                shapeRenderer.arc(location.getX(), location.getY(), RADIUS, 90, calculateOverflowRotation());

            }

            // Draws Text Display Background
            shapeRenderer.setColor(new Color(26 / 255f, 34 / 255f, 39 / 255f, 1f));
            shapeRenderer.circle(location.getX(), location.getY(), RADIUS * 0.85f);
        }
        shapeRenderer.end();
        gl.glDisable(GL_BLEND);

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

    private float calculateNonOverflowRotation() {
        float healthPercentage = health / MAX_HEALTH;
        return healthPercentage * 360;
    }

    private float calculateOverflowRotation() {
        float healthPercentage = (health - MAX_HEALTH) / (MAX_OVERFLOW_HEALTH - MAX_HEALTH);
        return healthPercentage * 360;
    }

    @Override
    public void damage(float damage) {

    }

    @Override
    public void heal(float heal) {

    }
}
