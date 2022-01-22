package me.johnexists.game1.objects.weapons.generators;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import me.johnexists.game1.objects.attributes.Size;
import me.johnexists.game1.objects.entities.DamageableEntity;

import static com.badlogic.gdx.Gdx.files;

@SuppressWarnings("all")
public enum GeneratorConstants {
    NONE (-1),
    MINI (0),
    MAX (1),
    QUANTITY_SCALAR (2),
    QUANTITY_MAX_SCALAR(3),
    HEALTH_UPSCALAR(4),
    HYPER_CHARGED(5);

    public Sprite displaySprite;

    GeneratorConstants(int positionNumber) {
        Texture texture = new Texture(files.internal("generator_assets.png"));
        displaySprite = new Sprite(new TextureRegion(texture), positionNumber * 32,
                0,  32, 32);
    }


    public static float returnModifiedDamage(GeneratorConstants generatorConstants, DamageableEntity damageableEntity) {
        float nearbyEntities = damageableEntity.getNearbyEntities(1280 * Size.getXSizeMultiplier(),
                        1280 * Size.getYSizeMultiplier())
                .size();
        float scalar = damageableEntity.getScalar();
        if(generatorConstants.equals(GeneratorConstants.HEALTH_UPSCALAR)) {
            damageableEntity.heal(0.0025f * scalar);
        }

        return switch (generatorConstants) {
            case NONE -> 0.0F;
            case MINI -> 85;
            case MAX -> 165F;
            case QUANTITY_SCALAR -> nearbyEntities * 5f * scalar;
            case QUANTITY_MAX_SCALAR -> nearbyEntities * 7.5f * scalar;
            case HEALTH_UPSCALAR -> 12.5f * scalar;
            case HYPER_CHARGED -> 800f;
        };

    }
}
