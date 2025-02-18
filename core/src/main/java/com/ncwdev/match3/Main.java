package com.ncwdev.match3;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.ncwdev.ecs.World;
import com.ncwdev.match3.components.Draggable;
import com.ncwdev.match3.components.Position;
import com.ncwdev.match3.components.Renderable;
import com.ncwdev.match3.components.Selectable;
import com.ncwdev.match3.components.Swappable;
import com.ncwdev.match3.components.Tile;
import com.ncwdev.match3.components.Transform;
import com.ncwdev.match3.systems.DestructionSystem;
import com.ncwdev.match3.systems.FallingSystem;
import com.ncwdev.match3.systems.InputSystem;
import com.ncwdev.match3.systems.MatchCheckSystem;
import com.ncwdev.match3.systems.RefillSystem;
import com.ncwdev.match3.systems.RenderSystem;
import com.ncwdev.match3.systems.SwapSystem;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class Main implements ApplicationListener {
    SpriteBatch batch;
    FitViewport viewport;

    BitmapFont font;
    Vector2 touchPos;

    Sprite background;

    private World world;

    // array of cells
    private Sprite[] cells = new Sprite[GameConfig.FIELD_WIDTH * GameConfig.FIELD_HEIGHT];

    private OrthographicCamera camera;

    @Override
    public void create() {
        // Prepare your application here.
        batch = new SpriteBatch();
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);

        font = new BitmapFont();
        touchPos = new Vector2();

        background = new Sprite(new Texture(GameConfig.BACKGROUND));
        background.setSize(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);

        Utils.loadTilesTextures();

        ////////////////////////////////////////////////////////////
        // Camera setup
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);

        world = new World();

        // Add systems
        world.addSystem(new InputSystem(world, viewport));
        world.addSystem(new SwapSystem(world));
        world.addSystem(new MatchCheckSystem(world));
        world.addSystem(new DestructionSystem(world));
        world.addSystem(new FallingSystem(world));
        world.addSystem(new RefillSystem(world));
        world.addSystem(new RenderSystem(batch));

        // Initialize game board
        initializeBoard(GameConfig.FIELD_WIDTH, GameConfig.FIELD_HEIGHT);
    }

    private void initializeBoard(int width, int height) {
        Color[][] colorGrid = BoardInitializer.generateValidBoard(width, height);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                world.createEntity()
                    .add(new Position(x, y))
                    .add(new Transform(x, y)) // Initial transform matches grid position
                    .add(new Tile(colorGrid[x][y], colorGrid[x][y].hashCode()))
                    .add(new Renderable(GameConfig.ITEM_SIZE, GameConfig.ITEM_SIZE))
                    .add(new Selectable())
                    .add(new Swappable())
                    .add(new Draggable());

                Sprite cell = new Sprite(new Texture(GameConfig.CELL));
                cell.setSize(GameConfig.TILE_SIZE, GameConfig.TILE_SIZE);

                Vector2 pos = Utils.getFieldPosition(x, y);
                cell.setPosition(pos.x, pos.y);
                cells[x + y * width] = cell;
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        // Resize your application here. The parameters represent the new window size.
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        input();

        // Draw your application here.
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();
        drawField();

        float delta = Gdx.graphics.getDeltaTime();
        world.update(delta);

        batch.end();
    }

    private void drawField() {
        background.draw(batch);

        for (int i = 0; i < cells.length; i++) {
            cells[i].draw(batch);
        }
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
        font.draw(batch, "X: " + touchPos.x + ", Y: " + touchPos.y, 10, 40);
    }

    private void input() {
        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY()); // Get where the touch happened on screen
            viewport.unproject(touchPos); // Convert the units to the world units of the viewport
        }
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void dispose() {
        // Destroy application's resources here.
    }
}
