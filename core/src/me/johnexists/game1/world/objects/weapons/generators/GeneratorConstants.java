package me.johnexists.game1.world.objects.weapons.generators;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import me.johnexists.game1.logic.GameLogic;
import me.johnexists.game1.world.objects.entities.DamageableEntity;
import me.johnexists.game1.world.objects.entities.Player;
import me.johnexists.game1.world.objects.entities.enemies.Enemy;
import me.johnexists.game1.world.objects.weapons.Constant;

import static com.badlogic.gdx.Gdx.files;

@SuppressWarnings("all")
public enum GeneratorConstants implements Constant {
    NONE(-1, "<--->",
            true, 0),
    MINI(0,
            "Small Generator\n" +
                    "Damage Per Second: 185",
            true, 0),
    MAX(1,
            "Large Generator\n" +
                    "Damage Per Second: 365",
            false, 35000),
    QUANTITY_SCALAR(2,
            "Quantity Scalar\n" +
                    "Damage Per Second: Amount Of\n" +
                    "Kills x Scalar x 5",
            false, 80000),
    QUANTITY_MAX_SCALAR(3,
            "Maximum Quantity Scalar\n" +
                    "Damage Per Second: Amount Of\n" +
                    "Kills x Scalar x 7.5",
            false, 180000),
    HEALTH_UPSCALAR(4,
            "Health Modification Scalar\n" +
                    "Damage Per Second: Scalar x 12.5\n" +
                    "Health Per Second: Scalar x 0.025",
            false, 120000),
    HYPER_CHARGED(5,
            "Hyper Charged Generator\n" +
                    "Damage Per Second: 1600",
            false, 200500);

    public Sprite displaySprite;
    public String displayMeta;
    public boolean unlocked;
    private int price;
    private final Texture texture;


    GeneratorConstants(int positionNumber, String displayMeta, boolean unlocked, int price) {
        this.price = price;
        this.unlocked = unlocked;
        this.displayMeta = displayMeta;
        this.texture = new Texture(files.internal("generator_assets.png"));
        displaySprite = new Sprite(new TextureRegion(texture), positionNumber * 32,
                0, 32, 32);
    }


    public static float returnModifiedDamage(GeneratorConstants generatorConstants, DamageableEntity generatorOwner) {
        float scalar = generatorOwner instanceof Player ?
                generatorOwner.getScalar() * 2.5f :
                generatorOwner.getScalar() * 0.41f;
        float kills = generatorOwner instanceof Player ?
                Player.kills * 250f + 1:
                1;

        return switch (generatorConstants) {
            case NONE -> 0.0F;
            case MINI -> 285;
            case MAX -> 465F;
            case QUANTITY_SCALAR -> 5f * scalar + kills;
            case QUANTITY_MAX_SCALAR -> (15f * scalar) + kills;
            case HEALTH_UPSCALAR -> (30f * scalar);
            case HYPER_CHARGED -> 2500f;
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
    public boolean isUnlocked() {
        return unlocked;
    }

    @Override
    public void dispose() {
        texture.dispose();
    }

    @Override
    public String toString() {
        if (unlocked) {
            return displayMeta + (Player.getLaserGenerator().equals(this) ? "\n>> In Use! <<" : "\n>> Click To Use! <<");
        }
        return displayMeta + "\nCost: " + GameLogic.formatNumber(getCost(), 0) + " Bits" + "\n>> Locked <<";
    }

    @Override
    public void unlock() {
        unlocked = true;
    }

    @Override
    public int getCost() {
        return price;
    }
}
