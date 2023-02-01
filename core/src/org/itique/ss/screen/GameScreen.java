package org.itique.ss.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.itique.ss.actor.EnemyAction;
import org.itique.ss.actor.EnemyActor;
import org.itique.ss.actor.EnemyStatus;
import org.itique.ss.actor.HeroActor;
import org.itique.ss.map.Cell;
import org.itique.ss.map.DefaultMap;
import org.itique.ss.map.object.EnemyObject;

import java.util.Arrays;
import java.util.Random;

public class GameScreen implements Screen {

    private static final int ENEMY_WIDTH = 56;
    private static final int ENEMY_HEIGHT = 128;
    private static final int VISIBLE_ANGLE = 480;

    private Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Texture background;
    private float yRange;
    private Game game;
    private long previousSpawn;
    private Music backgroundMusic;
    private HeroActor hero;
    private Cell[][] tiledMap;
    private Sound shootSoundTwo;
    private Skin skin;
    private TextureAtlas atlas;
    private Container<Table> mainContainer;
    private Label killedEnemiesLabel;

    public GameScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        this.backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("war_background.mp3"));
        this.backgroundMusic.setLooping(true);
        this.backgroundMusic.play();
        shootSoundTwo = Gdx.audio.newSound(Gdx.files.internal("shoot2.mp3"));
        previousSpawn = System.currentTimeMillis();
        camera = new OrthographicCamera();
        viewport = new FillViewport(640, 480, camera);
        batch = new SpriteBatch();
        atlas = new TextureAtlas("skins/default/default.atlas");
        skin = new Skin(Gdx.files.internal("skins/default/default.json"), atlas);
        stage = new Stage(viewport, batch);
        yRange = stage.getViewport().getScreenHeight() / 2.6f;
        background = new Texture("field.png");
        tiledMap = DefaultMap.getMap();
        hero = new HeroActor(tiledMap.length / 2, tiledMap[0].length / 2, 0);
        this.stage.addActor(hero);
        this.stage.addActor(spawnEnemy());
        this.stage.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button == Input.Buttons.LEFT) {
                    shootSoundTwo.play();
                }
                return true;
            }
        });
        killedEnemiesLabel = new Label("0", skin);
        Table table = new Table();
        table.add(killedEnemiesLabel).top().left().pad(20);
        mainContainer = new Container<>(table);
        stage.addActor(mainContainer);
    }

    @Override
    public void render(float delta) {
        Gdx.input.setInputProcessor(stage);
        ScreenUtils.clear(Color.BLACK);
        batch.begin();
        batch.draw(background, 0, 0);
        batch.end();
        updateMotion(delta);
        int cursorY = Gdx.app.getInput().getY();
        if (!(cursorY <= stage.getViewport().getScreenHeight() - hero.getHeight())) {
            hero.setY(hero.getHeight() / 4f - cursorY);
        }
        hero.setX(Gdx.app.getInput().getX() + 60);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        camera.update();
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        stage.getActors().forEach(a -> {
            if (a instanceof EnemyActor) {
                ((EnemyActor) a).dispose();
            }
        });
        stage.dispose();
        backgroundMusic.dispose();
        hero.dispose();
        skin.dispose();
        atlas.dispose();
    }

    private EnemyActor spawnEnemy() {
        Random random = new Random();
        EnemyActor actor = new EnemyActor(random.nextInt(VISIBLE_ANGLE - 10) + 10,
                yRange, 200.0f);
        int xSpawn = random.nextInt(tiledMap.length);
        int ySpawn = random.nextInt(tiledMap[0].length);
        Arrays.stream(tiledMap).forEach(c -> Arrays.stream(c).forEach(c2 -> {
            if (c2.getX() == xSpawn && c2.getY() == ySpawn) {
                EnemyObject enemy = new EnemyObject(xSpawn, ySpawn, (int) actor.getX());
                c2.setObject(enemy);
            }
        }));
        return actor;
    }

    private void updateMotion(float delta) {
        long time = System.currentTimeMillis();
        if (time - previousSpawn >= 6000L) {
            previousSpawn = System.currentTimeMillis();
            EnemyActor enemyActor = spawnEnemy();
            enemyActor.setTimeline(time);
            stage.addActor(enemyActor);
        }
        Array<Actor> actorsToRemove = new Array<>();
        stage.getActors().forEach(a -> {
            if (a instanceof EnemyActor) {
                ((EnemyActor) a).setStateTime(((EnemyActor) a).getStateTime() + delta);
                if (((EnemyActor) a).getStatus() != EnemyStatus.DEAD) {
                    Random random = new Random();
                    long actionTime = random.nextInt(3000) + 2000L;
                    if (time - ((EnemyActor) a).getTimeline() <= actionTime
                    && ((EnemyActor) a).getAction() == EnemyAction.WAITING) {
                        ((EnemyActor) a).walk();
                    } else if (time - ((EnemyActor) a).getTimeline() > actionTime
                    && ((EnemyActor) a).getAction() == EnemyAction.WALKING) {
                        ((EnemyActor) a).shoot();
                        int probability = random.nextInt(101);
                        if (probability >= 50) {
                            this.hero.setHealth(this.hero.getHealth() - 1.0f);
                        }
                    } else if (time - ((EnemyActor) a).getTimeline() >= actionTime + 2000L
                    && ((EnemyActor) a).getAction() == EnemyAction.SHOOTING) {
                        ((EnemyActor) a).stop();
                    } else if (time - ((EnemyActor) a).getTimeline() > actionTime + 500L
                    && ((EnemyActor) a).getAction() == EnemyAction.WAITING) {
                        ((EnemyActor) a).setTimeline(time);
                    }
                    a.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            ((EnemyActor) a).hurt();
                        }
                    });
                }
            }
        });
        stage.getActors().forEach(a -> {
            if (a instanceof EnemyActor) {
                if (((EnemyActor) a).getStatus() == EnemyStatus.DEAD) {
                    ((EnemyActor) a).down();
                    this.killedEnemiesLabel.setText(Integer.parseInt(this.killedEnemiesLabel.getText().toString()) + 1);
                    if (((EnemyActor) a).invalidate()) {
                        actorsToRemove.add(a);
                        ((EnemyActor) a).dispose();
                    }
                } else if (((EnemyActor) a).getStatus() == EnemyStatus.RUN) {
                    if (((EnemyActor) a).invalidate()) {
                        actorsToRemove.add(a);
                        ((EnemyActor) a).dispose();
                    }
                }
            }
        });
        stage.getActors().removeAll(actorsToRemove, true);
    }

}
