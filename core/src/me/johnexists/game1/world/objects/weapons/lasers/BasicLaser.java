package me.johnexists.game1.world.objects.weapons.lasers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import me.johnexists.game1.world.objects.entities.DamageableEntity;
import me.johnexists.game1.world.objects.entities.Player;
import me.johnexists.game1.world.objects.weapons.generators.GeneratorConstants;

public class BasicLaser extends Laser {

    final LaserConstants laserConst;

    public BasicLaser(DamageableEntity wielder, float degreesOffset, GeneratorConstants generatorConstants) {
        super(wielder, generatorConstants);
        laserConst = Player.getLaserSkin();
        this.degreesOffset = degreesOffset;
    }

    public BasicLaser(DamageableEntity wielder, DamageableEntity target, GeneratorConstants generatorConstants) {
        super(wielder, target, generatorConstants);
        laserConst = LaserConstants.values()[MathUtils.random(1, 12)];
    }

    @Override
    public void renderLaserBody(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
        basicLaserBody(spriteBatch, laserConst, degreesOffset);
    }


}
