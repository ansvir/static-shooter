package org.itique.ss.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

public class EnemyActor extends Actor implements Disposable {

    private static final int COLS_ANIM = 4;
    private static final int ROWS_ANIM = 2;
    private float health;
    private TextureRegion texture;
    private long timeline;
    private EnemyStatus status;
    private EnemyAction action;
    private int bullets;
    private Animation<TextureRegion> animationWalk;
    private Animation<TextureRegion> animationShoot;
    private Animation<TextureRegion> animationDown;
    private Animation<TextureRegion> animationStand;
    private Direction direction;
    private float stateTime;
    private Texture animSheet;
    private long dieTime;
    private Sound shootSound;

    public EnemyActor(float x, float y, float health) {
        super.setX(x);
        super.setY(y);
        this.direction = Direction.DOWN;
        this.animSheet = new Texture(Gdx.files.internal("enemy.png"));
        super.setSize((float) animSheet.getWidth() / (COLS_ANIM * 8),
                (float) animSheet.getHeight() / (ROWS_ANIM * 8));
        this.health = health;
        this.status = EnemyStatus.ALIVE;
        this.action = EnemyAction.WALKING;
        this.bullets = 30 * 3;
        TextureRegion[][] tmp = TextureRegion.split(animSheet,
                animSheet.getWidth() / COLS_ANIM,
                animSheet.getHeight() / ROWS_ANIM);
        TextureRegion[] walkRegions = new TextureRegion[3];
        TextureRegion[] shootRegions = new TextureRegion[1];
        TextureRegion[] downRegions = new TextureRegion[3];
        TextureRegion[] standRegions = new TextureRegion[1];
        walkRegions[0] = tmp[0][0];
        walkRegions[1] = tmp[0][1];
        walkRegions[2] = tmp[0][2];
        shootRegions[0] = tmp[0][3];
        downRegions[0] = tmp[1][0];
        downRegions[1] = tmp[1][1];
        downRegions[2] = tmp[1][2];
        standRegions[0] = tmp[0][0];
        this.animationWalk = new Animation<>(0.5f, walkRegions);
        this.animationShoot = new Animation<>(0.025f, shootRegions);
        this.animationDown = new Animation<>(2f, downRegions);
        this.animationStand = new Animation<>(2f, standRegions);
        this.stateTime = 0f;
        this.texture = animationStand.getKeyFrame(this.stateTime, true);
        setZIndex(1);
        this.shootSound = Gdx.audio.newSound(Gdx.files.internal("shoot.mp3"));
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public void hurt() {
        this.health -= 1.0 * this.stateTime;
        if (this.health <= 0.0f) {
            this.status = EnemyStatus.DEAD;
            this.dieTime = System.currentTimeMillis();
            this.direction = Direction.STOP;
            this.action = EnemyAction.WAITING;
        }
    }

    public void shoot() {
        this.bullets -= 3;
        this.texture.setRegion(animationShoot.getKeyFrame(this.stateTime, false));
        this.shootSound.play();
        this.action = EnemyAction.SHOOTING;
        this.direction = Direction.STOP;
    }

    public void walk() {
        this.texture.setRegion(animationWalk.getKeyFrame(this.stateTime, true));
        this.action = EnemyAction.WALKING;
        if (hasBullets()) {
            this.direction = Direction.DOWN;
            setY(getY() - 0.4f);
            setSize(getWidth() + 0.15f, getHeight() + 0.3f);
        } else {
            if ((getWidth() - 0.15f <= 0.0f || getHeight() - 0.3f <= 0.0f)
                    || getY() + 0.4f > getStage().getViewport().getScreenHeight() / 2.6f) {
                this.status = EnemyStatus.RUN;
                this.dieTime = System.currentTimeMillis();
            } else {
                this.direction = Direction.UP;
                setY(getY() + 0.4f);
                setSize(getWidth() - 0.15f, getHeight() - 0.3f);
            }
        }
    }

    public void stop() {
        this.texture.setRegion(animationStand.getKeyFrame(this.stateTime, false));
        this.direction = Direction.STOP;
        this.action = EnemyAction.WAITING;
    }

    public void down() {
        this.texture.setRegion(animationDown.getKeyFrame(this.stateTime, false));
        this.direction = Direction.STOP;
        this.shootSound.stop();
        this.action = EnemyAction.WAITING;
    }

    public boolean invalidate() {
        return System.currentTimeMillis() - this.dieTime >= 10000L;
    }

    public boolean hasBullets() {
        return this.bullets > 0;
    }

    public TextureRegion getTexture() {
        return texture;
    }

    public void setTexture(TextureRegion texture) {
        this.texture = texture;
    }

    public long getTimeline() {
        return timeline;
    }

    public void setTimeline(long timeline) {
        this.timeline = timeline;
    }

    public EnemyStatus getStatus() {
        return status;
    }

    public void setStatus(EnemyStatus status) {
        this.status = status;
    }

    public int getBullets() {
        return bullets;
    }

    public void setBullets(int bullets) {
        this.bullets = bullets;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public EnemyAction getAction() {
        return action;
    }

    public void setAction(EnemyAction action) {
        this.action = action;
    }

    public float getStateTime() {
        return stateTime;
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(this.texture, getX(), getY(), getWidth(), getHeight());
    }


    @Override
    public void dispose() {
        this.animSheet.dispose();
        this.shootSound.dispose();
    }
}
