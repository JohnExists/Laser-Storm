package me.johnexists.game1.objects.weapons.lasers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector3;
import me.johnexists.game1.objects.attributes.*;
import me.johnexists.game1.effects.BloodParticle;
import me.johnexists.game1.effects.DamageDisplayParticle;
import me.johnexists.game1.objects.GameObject;
import me.johnexists.game1.objects.entities.DamageableEntity;
import me.johnexists.game1.objects.entities.Player;
import me.johnexists.game1.objects.weapons.generators.GeneratorConstants;

import java.util.List;
import java.util.stream.Collectors;

import static me.johnexists.game1.objects.weapons.generators.GeneratorConstants.returnModifiedDamage;

public abstract class Laser extends GameObject implements Collideable {

    protected LaserConstants laserConst;
    protected DamageableEntity wielder, target;
    protected Location loc1, loc2;
    protected final GeneratorConstants generator;

    protected float degrees, lineLength, damagePerSecond;
    protected final float THICKNESS = 10 * Size.getXSizeMultiplier();
    protected float degreesOffset;
    protected boolean isActive;


    public Laser(DamageableEntity wielder, GeneratorConstants generatorConstants) {
        super(wielder.getLocation());
        laserConst = LaserConstants.NONE;
        this.wielder = wielder;
        this.isActive = true;
        degreesOffset = 0;
        this.generator = generatorConstants;

        loc1 = wielder.getLocation();
        loc2 = null;
        viewingOrderWeight = 4;
    }

    public Laser(DamageableEntity wielder, DamageableEntity target, GeneratorConstants generatorConstants) {
        super(wielder.getLocation());
        laserConst = LaserConstants.NONE;
        this.wielder = wielder;
        this.target = target;
        this.isActive = true;
        this.generator = generatorConstants;
        degreesOffset = 0;

        loc1 = wielder.getLocation();
        loc2 = target instanceof CircleShape ? Location.getCircleLoc((CircleShape) target) :
                target.getLocation();

        viewingOrderWeight = 1;
    }

    public void update(float deltaTime) {
        if (isActive) {
            if (target == null) {
                loc2 = returnMousePointerLoc();
            } else if (target instanceof CircleShape) {
                CircleShape circleTarget = (CircleShape) target;
                loc2 = Location.getCircleLoc(circleTarget);
            }

            damagePerSecond = returnModifiedDamage(generator, wielder);
            degrees = calculateDegrees();
            lineLength = calculateLineLength();
            getCollisionList().forEach(g -> {
                onCollision(deltaTime, (DamageableEntity) g);
                checkIfAlive((DamageableEntity) g, deltaTime);
            });
        }
    }

    protected void onCollision(float deltaTime, DamageableEntity damageableEntity) {
        damageableEntity.damage(damagePerSecond * deltaTime);
        getLocation().getWorld().spawnParticle(new DamageDisplayParticle(damagePerSecond, damageableEntity, deltaTime));

    }

    private void checkIfAlive(DamageableEntity damageableEntity, float deltaTime) {
        if (!damageableEntity.isAlive()) {
            getLocation().getWorld().despawn(damageableEntity);
            getLocation().getWorld().spawnParticle(new BloodParticle(damageableEntity.getLocation(), deltaTime));
            wielder.heal(5f);
            if (wielder instanceof Player) {
                Player.kills += 1;
            }
            if (damageableEntity instanceof LaserWielder) {
                ((LaserWielder) damageableEntity).clearLaser();
            }
        }
    }


    private List<GameObject> getCollisionList() {
        return getLocation().getWorld().getGameObjects().stream()
                .filter(g -> g instanceof DamageableEntity)
                .filter(g -> !(g.equals(wielder)))
                .filter(g -> wielder.getNearbyEntities(lineLength, lineLength)
                        .contains(g))
                .filter(g -> Intersector.overlapConvexPolygons(((Collideable) g)
                        .getCollisionBounds(), getCollisionBounds()))
                .collect(Collectors.toList());
    }


    protected float calculateDegrees() {
        return (float) Math.toDegrees(MathUtils.atan2(loc2.getY() - loc1.getY(),
                loc2.getX() - loc1.getX()));
    }

    protected float calculateLineLength() {
        return (float) Math.min(Math.sqrt(Math.pow(loc2.getX() - loc1.getX(), 2) +
                Math.pow(loc2.getY() - loc1.getY(), 2)), 750 * Size.getXSizeMultiplier());
    }

    @Override
    public Polygon getCollisionBounds() {
        Polygon polygon = new Polygon(new float[]{0, 0, lineLength, 0,
                lineLength, THICKNESS, 0, THICKNESS});
        polygon.setPosition(loc1.getX(), loc1.getY());
        polygon.setRotation(degrees + degreesOffset);

        return polygon;
    }

    @Override
    public void collidesWith(Collideable collideable, Runnable runnable) {
        if (Intersector.overlapConvexPolygons(getCollisionBounds(),
                collideable.getCollisionBounds())) {
            runnable.run();
        }
    }

    public Location returnMousePointerLoc() {
        Vector3 mousePosWithoutCamera = getLocation().getWorld().getGameState().getGameCamera()
                .unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

        return new Location(mousePosWithoutCamera.x, mousePosWithoutCamera.y);
    }

    public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
        if (isActive) {
            renderLaserBody(spriteBatch, shapeRenderer);
        }
    }

    protected abstract void renderLaserBody(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer);

    protected void basicLaserBody(SpriteBatch spriteBatch, LaserConstants laserConst, float degreesOffset) {
        Location centerOfRect = new Location(loc1.getX() + size.getWidth() / 2,
                loc1.getY() + size.getHeight() / 2);

        Location renderLocation = wielder instanceof CircleShape ?
                new Location(loc1.getX() - (THICKNESS / 2), loc1.getY() - (THICKNESS / 2)) :
                new Location(centerOfRect.getX() - (THICKNESS / 2), centerOfRect.getY() - (THICKNESS / 2));

        float width = wielder instanceof CircleShape ?
                lineLength : lineLength + getSize().getWidth() / 2;

        spriteBatch.begin();
        {
            spriteBatch.draw(laserConst.displaySprite, renderLocation.getX(), renderLocation.getY(), (THICKNESS / 2),
                    (THICKNESS / 2), width, THICKNESS, 1.0f, 1.0f, degrees + degreesOffset);
        }
        spriteBatch.end();
    }


    public void enable() {
        isActive = true;
    }

    public void disable() {
        isActive = false;
    }


}
