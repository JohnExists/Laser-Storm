package me.johnexists.game1.world.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import me.johnexists.game1.world.objects.attributes.Size;
import me.johnexists.game1.world.World;
import me.johnexists.game1.world.objects.entities.DamageableEntity;

import java.text.DecimalFormat;

public class DamageDisplayParticle extends Particle {

    public static final int BITS_NO_VALUE = 0;

    private static final float LIFE_EXPECTANCY = 1f;

    private final DamageableEntity damageableEntity;
    private final BitmapFont font;
    private final GlyphLayout glyphLayout;
    private final StringBuilder displayText;
    private final int bits;

    public DamageDisplayParticle(float damage, int bits, DamageableEntity damageableEntity, float deltaTime) {
        super(damageableEntity.getLocation(), deltaTime);

        this.life = LIFE_EXPECTANCY;
        this.bits = bits;
        this.damageableEntity = damageableEntity;

        font = new BitmapFont(Gdx.files.internal("DefaultFont.fnt"));
        glyphLayout = new GlyphLayout();
        DecimalFormat df = new DecimalFormat("#,###");
        displayText = new StringBuilder();

        displayText.append(df.format(damage)).append(" DPS");
        if (bits != BITS_NO_VALUE) {
            displayText.append(", +1 Kill, +").append(bits).append(" Bits");
        }

    }

    @Override
    public void continueParticle() {
        life -= deltaTime;
        glyphLayout.setText(font, displayText.toString());

        spriteBatch.begin();
        {
            Color color = bits != BITS_NO_VALUE ? Color.CYAN : Color.RED;

            font.setColor(color);
            font.getData().setScale(0.5f * Size.getXSizeMultiplier(),
                    0.5f * Size.getYSizeMultiplier());

            if (damageableEntity.isAlive() || bits != BITS_NO_VALUE) {
                font.draw(spriteBatch, displayText,
                        damageableEntity.getLocation().getX(),
                        damageableEntity.getLocation().getY() + (life * Size.getYSizeMultiplier() * 16));
            }
        }
        spriteBatch.end();
    }

    public boolean isAvailable() {
        World world = getDamageableEntity().getLocation().getWorld();
        boolean anyDuplicates = world.getActiveParticles().stream()
                .filter(g -> g instanceof DamageDisplayParticle)
                .noneMatch(g -> ((DamageDisplayParticle) g).getDamageableEntity().equals(damageableEntity));
        return anyDuplicates || !damageableEntity.isAlive();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        spriteBatch.dispose();
        font.dispose();
    }

    public DamageableEntity getDamageableEntity() {
        return damageableEntity;
    }
}
