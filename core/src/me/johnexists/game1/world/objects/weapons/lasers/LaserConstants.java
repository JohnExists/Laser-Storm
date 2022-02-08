package me.johnexists.game1.world.objects.weapons.lasers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import me.johnexists.game1.logic.GameLogic;
import me.johnexists.game1.world.objects.entities.Player;
import me.johnexists.game1.world.objects.weapons.Constant;

import static com.badlogic.gdx.Gdx.files;

@SuppressWarnings("all")
public enum LaserConstants implements Constant {
    NONE(-1, "<--->", true, 0),
    RED(0, "Red", true, 0),
    BLUE(1, "Blue", true, 0),
    GREEN(2, "Green", true, 0),
    PINK(3, "Pink", true, 0),
    PURPLE(4, "Purple", true, 0),
    RAINBOW(5, "Rainbow", false, 25000),
    MAGMA(6, "Magma", false, 25000),
    GOLD(7, "Gold", false, 25000),
    DARK(8, "Dark Side", false, 25000),
    BOLT(9, "Lightning Bolt", false, 40000),
    DARK_RAINBOW(10, "Dark Rainbow", false, 40000),
    RANDOM(MathUtils.random(0, 5), "", false, 0);

    public Sprite displaySprite;
    private String displayMeta;
    public boolean unlocked;
    private int price;
    private final Texture texture;

    LaserConstants(int positionNumber, String displayMeta, boolean unlocked, int price) {
        this.displayMeta = displayMeta;
        this.price = price;
        this.unlocked = unlocked;
        this.texture = new Texture(files.internal("laser_assets_complete.png"));
        displaySprite = new Sprite(new TextureRegion(texture), 0,
                positionNumber * 10, 4000, 10);
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
    public void dispose() {
        texture.dispose();
    }

    @Override
    public String toString() {
        if (unlocked) {
            return displayMeta + "\nReskins your laser"
                    + (Player.getLaserSkin().equals(this) ? "\n>> In Use! <<" : "\n>> Click To Use! <<");
        }
        return displayMeta + "\nReskins your laser"
                + "\nCost: " + GameLogic.formatNumber(getCost(), 0) + " Bits" + "\n>> Locked <<";
    }


    @Override
    public boolean isUnlocked() {
        return unlocked;
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
