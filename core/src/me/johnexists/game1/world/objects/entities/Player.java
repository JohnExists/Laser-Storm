package me.johnexists.game1.world.objects.entities;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import me.johnexists.game1.world.objects.abilities.Ability;
import me.johnexists.game1.world.objects.abilities.AbilityConstants;
import me.johnexists.game1.world.objects.abilities.Deranger;
import me.johnexists.game1.world.objects.abilities.Repulsor;
import me.johnexists.game1.logic.GameLogic;
import me.johnexists.game1.world.objects.attributes.CircleShape;
import me.johnexists.game1.world.objects.attributes.LaserWielder;
import me.johnexists.game1.world.objects.attributes.Location;
import me.johnexists.game1.world.objects.weapons.generators.GeneratorConstants;
import me.johnexists.game1.world.objects.weapons.lasers.BasicLaser;
import me.johnexists.game1.world.objects.weapons.lasers.Laser;
import me.johnexists.game1.world.objects.weapons.lasers.LaserConstants;
import me.johnexists.game1.state.State;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled;

public class Player extends DamageableEntity implements CircleShape, LaserWielder {

    public static LaserConstants laserSkin = LaserConstants.RAINBOW;
    public static GeneratorConstants laserGenerator = GeneratorConstants.MINI;
    public static AbilityConstants abilityConstants = AbilityConstants.DERANGER;
    public static float playerScalar = 115, passiveHealPerSecond = 3.5f;
    public static int kills = 0;

    private final GameLogic gameLogic;
    private final float DISTANCE_PER_SECOND = 405, //255
            RADIUS = ((getSize().getWidth() + getSize().getHeight()) / 2) / 2;

    private Laser laser;
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<Ability> currentAbility;

    public Player(State state, Location location) {
        super(location);
        this.gameLogic = state.getGameLogic();
        this.laser = new BasicLaser(this, 0, Player.laserGenerator);
        this.scalar = playerScalar;

//        health =  MAX_OVERFLOW_HEALTH;

        currentAbility = Optional.empty();
        viewingOrderWeight = 5;

        gameLogic.getKeyInput().registerOnKeyReleased(Input.Keys.Q, () -> {
            if (currentAbility.isEmpty()) {
                currentAbility = switch (Player.abilityConstants) {
                    case NONE -> Optional.empty();
                    case REPULSOR -> Optional.of(new Repulsor(this));
                    case DERANGER -> Optional.of(new Deranger(this));
                };
            }
        });
    }

    @Override
    public void update(float deltaTime) {
        System.out.println(getScalar());
        AtomicReference<Boolean> isSprinting = new AtomicReference<>();
        float speed;

        isSprinting.set(false);
        gameLogic.getKeyInput().isPressed(Input.Keys.SHIFT_LEFT, () -> isSprinting.set(true));

        speed = isSprinting.get() ? 155f : 0f;
        float finalSpeed = speed;

        gameLogic.getKeyInput().isPressed(Input.Keys.W,
                () -> velocity.add(0, DISTANCE_PER_SECOND + finalSpeed));

        gameLogic.getKeyInput().isPressed(Input.Keys.S,
                () -> velocity.add(0, -DISTANCE_PER_SECOND - finalSpeed));

        gameLogic.getKeyInput().isPressed(Input.Keys.D,
                () -> velocity.add(DISTANCE_PER_SECOND + finalSpeed, 0));

        gameLogic.getKeyInput().isPressed(Input.Keys.A,
                () -> velocity.add(-DISTANCE_PER_SECOND - finalSpeed, 0));

        currentAbility.ifPresent(ability -> {
            if (ability.isDone()) {
                setImmortal(false);
                currentAbility = Optional.empty();
            }
        });
        currentAbility.ifPresent(ability -> ability.update(deltaTime));
        super.update(deltaTime);

        heal(passiveHealPerSecond * deltaTime);

        // TODO Laser Stamina - Scale With Damage - 40% Stamina = 40% Damage
    }

    @Override
    public Polygon getCollisionBounds() {
        Polygon polygon = new Polygon(new float[]{0, 0, size.getWidth(), 0,
                size.getWidth(), size.getHeight(), 0, size.getHeight()});
        //Set Bounds To Bottom Left Instead Of Center

        polygon.setPosition(location.getX() - RADIUS, location.getY() - RADIUS);

        return polygon;
    }

    @Override
    public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
//        shapeRenderer.setProjectionMatrix(getLocation().getWorld().getGameState().getGameCamera().combined);
        currentAbility.ifPresent(ability -> ability.render(shapeRenderer, spriteBatch));
        shapeRenderer.begin(Filled);
        {
            shapeRenderer.setColor(Color.BLUE);
            shapeRenderer.circle(location.getX(), location.getY(), RADIUS);
        }
        shapeRenderer.end();

    }

    @Override
    public Laser getLaser() {
        return laser;
    }

    @Override
    public void clearLaser() {
        laser = null;
    }

    public static LaserConstants getLaserSkin() {
        return laserSkin;
    }

    public static void swapLaserSkin(LaserConstants laserSkin) {
        if (Player.laserSkin.equals(laserSkin)) {
            Player.laserSkin = LaserConstants.NONE;
        } else {
            Player.laserSkin = laserSkin;
        }
    }

    public static GeneratorConstants getLaserGenerator() {
        return laserGenerator;
    }

    public static void swapLaserGenerator(GeneratorConstants laserGenerator) {
        if (Player.laserGenerator.equals(laserGenerator)) {
            Player.laserGenerator = GeneratorConstants.NONE;
        } else {
            Player.laserGenerator = laserGenerator;

        }
    }

    public static AbilityConstants getAbilityConstants() {
        return abilityConstants;
    }

    public static void swapAbilityConstants(AbilityConstants abilityConstants) {
        if (Player.abilityConstants.equals(abilityConstants)) {
            Player.abilityConstants = AbilityConstants.NONE;
        } else {
            Player.abilityConstants = abilityConstants;

        }
    }

}
