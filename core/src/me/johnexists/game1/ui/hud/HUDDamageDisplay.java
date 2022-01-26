package me.johnexists.game1.ui.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import me.johnexists.game1.world.objects.attributes.Size;
import me.johnexists.game1.world.objects.GameObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Deprecated
@SuppressWarnings("unused")
public class HUDDamageDisplay {

    private List<HUDDamageDisplayElement> damageDisplays;


    public HUDDamageDisplay() {
        damageDisplays = new ArrayList<>();
    }

    public void addHealthIndicator(float damage, GameObject gameObject) {
        if (!checkDuplicatesOf(gameObject)) {
            damageDisplays.add(new HUDDamageDisplayElement(damage, gameObject));
        }
    }

    public boolean checkDuplicatesOf(GameObject gameObject) {
        return damageDisplays.stream().anyMatch(displayElement ->
                displayElement.getGameObject().equals(gameObject));
    }

    public void update(float deltaTime) {
        clear0TickElements();
        damageDisplays.forEach(d -> d.update(deltaTime));
    }

    public void render(SpriteBatch spriteBatch) {
        damageDisplays.forEach(d -> d.render(spriteBatch));
    }

    private void clear0TickElements() {
        damageDisplays = damageDisplays.stream()
                .filter(displayElement -> displayElement.getTickLeft() > 0)
                .collect(Collectors.toList());
    }

    private static class HUDDamageDisplayElement {

        private int tickLeft;
        private final float damage;
        private final GameObject gameObject;
        private final BitmapFont font;
        private final GlyphLayout glyphLayout;
        private final DecimalFormat df;

        public HUDDamageDisplayElement(float damage, GameObject gameObject) {
            font = new BitmapFont(Gdx.files.internal("DefaultFont.fnt"));
            this.tickLeft = 60;
            this.damage = damage;
            this.gameObject = gameObject;
            glyphLayout = new GlyphLayout();
            df = new DecimalFormat("#,###");
        }

        public void update(float dt) {
            tickLeft -= dt;
            glyphLayout.setText(font, df.format(damage) + " DPS");
        }

        public void render(SpriteBatch spriteBatch) {
            spriteBatch.begin();
            {
                font.setColor(Color.RED);
                font.getData().setScale(0.5f * Size.getXSizeMultiplier(),
                        0.5f * Size.getYSizeMultiplier());
                font.draw(spriteBatch, df.format(damage) + " DPS",
                        gameObject.getLocation().getX(),
                        gameObject.getLocation().getY() + tickLeft);
            }
            spriteBatch.end();
        }


        public GameObject getGameObject() {
            return gameObject;
        }

        public int getTickLeft() {
            return tickLeft;
        }
    }
}
