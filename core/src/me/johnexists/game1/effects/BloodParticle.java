package me.johnexists.game1.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import me.johnexists.game1.objects.attributes.Location;
import me.johnexists.game1.objects.attributes.Size;

public class BloodParticle extends Particle {

    private static final int AMOUNT_OF_PARTICLES = 25;
    private static final float LIFE_EXPECTANCY = 2.5f;
    private final Location[] bloodLocations;

    public BloodParticle(Location location, float deltaTime) {
        super(location, deltaTime);
        bloodLocations = new Location[AMOUNT_OF_PARTICLES];
        life = LIFE_EXPECTANCY;
        for (int i = 0; i < AMOUNT_OF_PARTICLES; i++) {
            bloodLocations[i] = new Location(MathUtils.random(-1f, 1f), MathUtils.random(-1f, 1f));
        }
    }

    @Override
    public void continueParticle() {
        life -= deltaTime;
        float lifeLeft = LIFE_EXPECTANCY - life;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        {
            Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            for (int i = 0; i < AMOUNT_OF_PARTICLES; i++) {
                float distanceFromCenterX = (lifeLeft * 2) * bloodLocations[i].getX() * 45,
                        distanceFromCenterY = (lifeLeft * 2) * bloodLocations[i].getY() * 45 - (lifeLeft * 16);
                float radius = (life * 6) * Size.getXSizeMultiplier();
                Size bloodSize = new Size(distanceFromCenterX, distanceFromCenterY);

                shapeRenderer.setColor(new Color(187 / 255f, 10 / 255f, 30 / 255f, life - 1 / LIFE_EXPECTANCY - 1));
                shapeRenderer.circle(location.getX() + bloodSize.getWidth(),
                        location.getY() + bloodSize.getHeight(), radius);
            }
        }
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);


    }

    @Override
    public boolean isAvailable() {
        return true;
    }
}
