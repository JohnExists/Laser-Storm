package me.johnexists.game1.ui.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import me.johnexists.game1.logic.GameLogic;
import me.johnexists.game1.ui.UIElement;
import me.johnexists.game1.world.World;
import me.johnexists.game1.world.objects.GameObject;
import me.johnexists.game1.world.objects.attributes.Location;
import me.johnexists.game1.world.objects.attributes.Size;
import me.johnexists.game1.world.objects.entities.DamageableEntity;
import me.johnexists.game1.world.objects.entities.Player;
import me.johnexists.game1.world.objects.entities.enemies.Enemy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.badlogic.gdx.Gdx.gl;
import static com.badlogic.gdx.Gdx.graphics;
import static com.badlogic.gdx.graphics.GL20.*;
import static me.johnexists.game1.world.objects.attributes.Size.getXSizeMultiplier;
import static me.johnexists.game1.world.objects.attributes.Size.getYSizeMultiplier;

@SuppressWarnings({
        "MismatchedQueryAndUpdateOfCollection",
        "FieldCanBeLocal"
})
public class HUDMinimap extends UIElement {

    private final float MAP_SIZE = 96,
            MAP_RATIO_X, MAP_RATIO_Y;
    private final List<GameObject> objects;
    private final List<Location> objectLocations;
    private final World world;

    public HUDMinimap(Location location, World world) {
        super(location, new Size(0, 0));
        this.size = new Size(MAP_SIZE, MAP_SIZE);
        this.world = world;
        objects = world.getGameObjects().stream()
                .filter(gameObject -> gameObject instanceof DamageableEntity)
                .collect(Collectors.toList());
        objectLocations = new ArrayList<>();
        objects.forEach(damageableEntity ->
                objectLocations.add(new Location(0, 0)));

        MAP_RATIO_X = MAP_SIZE / World.MAP_X;
        MAP_RATIO_Y = MAP_SIZE / World.MAP_Y;
    }

    @Override
    public void update(float deltaTime) {
    }

    private Location calculatePosRatioOf(GameObject gameObject) {
        Location loc = gameObject.getLocation();
        return clampPos(loc);
    }

    private Location clampPos(Location loc) {
        return new Location(MathUtils.clamp(loc.getX() * MAP_RATIO_X * getXSizeMultiplier(), 0, World.MAP_X),
                MathUtils.clamp(loc.getY() * MAP_RATIO_Y * getYSizeMultiplier(), 0, World.MAP_Y));
    }

    @Override
    public void render() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        {
            graphics.getGL20().glEnable(GL_BLEND);
            gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.setColor(new Color(0, 0, 0, 0.45f));
            shapeRenderer.rect(location.getX(), location.getY(),
                    size.getWidth(), size.getHeight());
            renderPoints();
        }
        shapeRenderer.end();
        gl.glDisable(GL_BLEND);
        renderInfoText();
    }

    private void renderInfoText() {
        String text = String.format(" Your Kills -> %d\n Enemies Left -> %d\n Your Bits -> %s", Player.kills,
                world.getGameObjects().size() - 1, GameLogic.formatNumber(Player.bits, 0));
        GlyphLayout g = new GlyphLayout();
        g.setText(font, text,
                Color.WHITE, graphics.getWidth(), Align.center, true);

        font.getData().setScale(Size.getXSizeMultiplier() / 4f);
        spriteBatch.begin();
        {
            font.draw(spriteBatch, text, location.getX(), location.getY() - (g.height * (Size.getXSizeMultiplier() / 4f) / 2));
        }
        spriteBatch.end();
    }



    private void renderPoints() {
        objects.forEach(ob -> {
            if (((DamageableEntity) ob).isAlive()) {
                Color color = ob instanceof Enemy ?
                        ((Enemy) ob).getEnemyColour() :
                        Color.RED;
                float radius = ob.equals(world.getMainCharacter()) ?
                        5f * getXSizeMultiplier() :
                        2.5f * getXSizeMultiplier();

                shapeRenderer.setColor(color);

                shapeRenderer.circle(location.getX() + (calculatePosRatioOf(ob).getX()),
                        location.getY() + (calculatePosRatioOf(ob).getY()), radius);
            }
        });
    }
}
