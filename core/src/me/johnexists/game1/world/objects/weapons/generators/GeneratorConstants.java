package me.johnexists.game1.world.objects.weapons.generators;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import me.johnexists.game1.world.objects.attributes.Size;
import me.johnexists.game1.world.objects.entities.DamageableEntity;
import me.johnexists.game1.world.objects.entities.Player;
import me.johnexists.game1.world.objects.weapons.Constant;

import static com.badlogic.gdx.Gdx.files;

@SuppressWarnings("all")
public enum GeneratorConstants implements Constant {
    NONE(-1, "<--->"),
    MINI(0,
            "Small Generator\n" +
                    "Damage Per Second: 185"),
    MAX(1,
            "Large Generator\n" +
                    "Damage Per Second: 365"),
    QUANTITY_SCALAR(2,
            "Quantity Scalar\n" +
                    "Damage Per Second: Amount Of\n" +
                    "Kills x Scalar x 5"),
    QUANTITY_MAX_SCALAR(3,
            "Quantity Maximum Scalar\n" +
                    "Damage Per Second: Amount Of\n" +
                    "Kills x Scalar x 7.5"),
    HEALTH_UPSCALAR(4,
            "Health Modification Scalar\n" +
                    "Damage Per Second: Scalar x 12.5\n" +
                    "Health Per Second: Scalar x 0.025"),
    HYPER_CHARGED(5,
            "Hyper Charged Generator\n" +
                    "Damage Per Second: 1600");

    public Sprite displaySprite;
    public String displayMeta;

    GeneratorConstants(int positionNumber, String displayMeta) {
        this.displayMeta = displayMeta;
        Texture texture = new Texture(files.internal("generator_assets.png"));
        displaySprite = new Sprite(new TextureRegion(texture), positionNumber * 32,
                0, 32, 32);
    }


    public static float returnModifiedDamage(GeneratorConstants generatorConstants, DamageableEntity generatorOwner) {
        float nearbyEntities = generatorOwner.getNearbyEntities(1280 * Size.getXSizeMultiplier(),
                        1280 * Size.getYSizeMultiplier())
                .size();
        float scalar = generatorOwner.getScalar();
        float kills = generatorOwner instanceof Player ?
                Player.kills + 1 :
                1;
        if (generatorConstants.equals(GeneratorConstants.HEALTH_UPSCALAR)) {
            generatorOwner.overflowHeal(0.025f * scalar);
        }



        return switch (generatorConstants) {
            case NONE -> 0.0F;
            case MINI -> 185;
            case MAX -> 365F;
            case QUANTITY_SCALAR -> kills * 5f * scalar;
            case QUANTITY_MAX_SCALAR -> kills * 7.5f * scalar;
            case HEALTH_UPSCALAR -> 12.5f * scalar;
            case HYPER_CHARGED -> 1600f;
        };

    }

    @Override
    public Constant[] returnList() {
        return values();
    }

    @Override
    public Sprite getSprite() {
        return displaySprite;
    }

    @Override
    public String toString() {
        return displayMeta;
    }
}
