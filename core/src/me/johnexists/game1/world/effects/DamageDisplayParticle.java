package me.johnexists.game1.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import me.johnexists.game1.world.objects.attributes.Size;
import me.johnexists.game1.world.objects.GameObject;
import me.johnexists.game1.world.World;

import java.text.DecimalFormat;

public class DamageDisplayParticle extends Particle {

    private static final float LIFE_EXPECTANCY = 1f;

    private final GameObject gameObject;
    private final BitmapFont font;
    private final GlyphLayout glyphLayout;
    private final DecimalFormat df;
    private final float damage;

    public DamageDisplayParticle(float damage, GameObject gameObject, float deltaTime) {
        super(gameObject.getLocation(), deltaTime);
        font = new BitmapFont(Gdx.files.internal("DefaultFont.fnt"));
        this.life = LIFE_EXPECTANCY;
        this.damage = damage;
        this.gameObject = gameObject;
        glyphLayout = new GlyphLayout();
        df = new DecimalFormat("#,###");
    }

    @Override
    public void continueParticle() {
        life -= deltaTime;
        glyphLayout.setText(font, df.format(damage) + " DPS");

        spriteBatch.begin();
        {
            font.setColor(Color.RED);
            font.getData().setScale(0.5f * Size.getXSizeMultiplier(),
                    0.5f * Size.getYSizeMultiplier());
            font.draw(spriteBatch, df.format(damage) + " DPS",
                    gameObject.getLocation().getX(),
                    gameObject.getLocation().getY() + life);
        }
        spriteBatch.end();
    }

    public boolean isAvailable() {
        World world = getGameObject().getLocation().getWorld();
        return world.getActiveParticles().stream()
                .filter(g -> g instanceof DamageDisplayParticle)
                .noneMatch(g -> ((DamageDisplayParticle) g).getGameObject().equals(gameObject));
    }

    public GameObject getGameObject() {
        return gameObject;
    }
}
