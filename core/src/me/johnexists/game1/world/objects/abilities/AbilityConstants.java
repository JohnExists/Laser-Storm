package me.johnexists.game1.world.objects.abilities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import me.johnexists.game1.world.objects.weapons.Constant;

import static com.badlogic.gdx.Gdx.files;

@SuppressWarnings("SpellCheckingInspection")
public enum AbilityConstants implements Constant {

    NONE(-1, "<--->"),
    REPULSOR(0,
            "Repulsor\n" +
                    "Shoves all nearby enemies\n" +
                    "in a certain radius"),
    DERANGER(1,
            "Deranger\n" +
                    "Individually damages\n" +
                    "all nearby enemies");

    public Sprite displaySprite;
    public String displayMeta;

    AbilityConstants(int positionNumber, String displayMeta) {
        this.displayMeta = displayMeta;
        Texture texture = new Texture(files.internal("ability_icon_assets.png"));
        displaySprite = new Sprite(new TextureRegion(texture), positionNumber * 16,
                0,  16, 16);
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
