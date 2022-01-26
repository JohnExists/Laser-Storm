package me.johnexists.game1.objects.weapons.lasers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import me.johnexists.game1.objects.entities.DamageableEntity;
import me.johnexists.game1.objects.weapons.generators.GeneratorConstants;

import java.util.ArrayList;
import java.util.List;

public class MultipleLasers extends Laser{

    private final List<BasicLaser> basicLasers;

    public MultipleLasers(DamageableEntity wielder, GeneratorConstants generatorConstants) {
        super(wielder, generatorConstants);
        basicLasers = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            basicLasers.add(new BasicLaser(wielder, (i - 1) * 9f, generatorConstants));
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        basicLasers.forEach(b -> b.update(deltaTime));
    }

    @Override
    protected void renderLaserBody(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
        basicLasers.forEach(b -> b.renderLaserBody(spriteBatch, shapeRenderer));
    }
}
