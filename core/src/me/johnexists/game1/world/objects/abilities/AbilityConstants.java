package me.johnexists.game1.abilities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import me.johnexists.game1.world.objects.weapons.Constant;

import static com.badlogic.gdx.Gdx.files;

@SuppressWarnings("SpellCheckingInspection")
public enum AbilityConstants implements Constant {

    NONE(-1),
    REPULSOR(0),
    DERANGER(1);

    public Sprite displaySprite;

    AbilityConstants(int positionNumber) {
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
}
