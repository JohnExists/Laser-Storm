package me.johnexists.game1.objects.weapons.lasers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import me.johnexists.game1.objects.entities.DamageableEntity;
import me.johnexists.game1.objects.weapons.Constant;
import me.johnexists.game1.objects.weapons.generators.GeneratorConstants;

import static com.badlogic.gdx.Gdx.files;

@SuppressWarnings("all")
public enum LaserConstants {
    NONE (-1),
    RED (0),
    BLUE (1),
    GREEN (2),
    PINK (3),
    PURPLE(4),
    RAINBOW(5),
    MAGMA(6),
    GOLD(7),
    DARK(8),
    BOLT(9),
    DARK_RAINBOW(10),
    RANDOM(MathUtils.random(0, 5));

    public Sprite displaySprite;

    LaserConstants(int positionNumber) {
        Texture texture = new Texture(files.internal("laser_assets_complete.png"));
        displaySprite = new Sprite(new TextureRegion(texture), 0,
                positionNumber * 10, 4000, 10);
    }

}
