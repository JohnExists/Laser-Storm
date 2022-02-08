package me.johnexists.game1.world.objects.abilities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import me.johnexists.game1.logic.GameLogic;
import me.johnexists.game1.world.objects.entities.Player;
import me.johnexists.game1.world.objects.weapons.Constant;
import org.w3c.dom.Text;

import static com.badlogic.gdx.Gdx.files;

@SuppressWarnings("SpellCheckingInspection")
public enum AbilityConstants implements Constant {

    NONE(-1, "<--->", false, 0),
    TRILASER(0,
            "Tri-Laser\n" +
                    "Adds 2 additionial lasers\n" +
                    "to the player", false, 20000),
    PENTALASER(1,
            "Penta-Laser\n" +
                    "Adds 4 additionial lasers\n" +
                    "to the player", false, 35000),
    REPULSOR(2,
            "Repulsor\n" +
                    "Shoves all nearby enemies\n" +
                    "in a certain radius", false, 30000),
    DERANGER(3,
            "Deranger\n" +
                    "Individually damages\n" +
                    "all nearby enemies", false, 60000);

    public Sprite displaySprite;
    public String displayMeta;
    private final Texture texture;
    private boolean unlocked;
    private final int price;

    AbilityConstants(int positionNumber, String displayMeta, boolean unlocked, int price) {
        this.price = price;
        this.unlocked = unlocked;
        this.displayMeta = displayMeta;
        this.texture = new Texture(files.internal("ability_icon_assets.png"));
        displaySprite = new Sprite(new TextureRegion(texture), positionNumber * 16,
                0, 16, 16);
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
            return displayMeta + (Player.getAbilityConstants().equals(this) ? "\n>> In Use! <<" : "\n>> Click To Use! <<");
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
