package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;

import java.util.Random;

import static com.badlogic.gdx.math.MathUtils.random;

public class MyGdxGame extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	Texture tiles;
	TextureRegion down, up, right, left, stand, sprite, upWalk, downWalk, aTree;
	float x, y, xVel, yVel;
    String direction;
    Animation walk, walkUp, walkDown;
    float currentSpeed;
    boolean wasRight;
    float totalTime;
    Texture tileImg;
    TiledMap tiledMap;
    OrthographicCamera camera;
    OrthogonalTiledMapRenderer tiledMapRenderer;



	static final int WIDTH = 16;
	static final int HEIGHT = 16;
	static final int DRAW_WIDTH = WIDTH * 4;
	static final int DRAW_HEGHT = HEIGHT * 4;
	static final float MAX_VELOCITY = 400;
    static final float VELOCITY_BOOST = 400;
	static final float FRICTION = 0.90f;



	
	@Override
	public void create () {
		batch = new SpriteBatch();
		tiles = new Texture("tiles.png");
		TextureRegion[][] grid = TextureRegion.split(tiles, 16, 16);
		down = grid[6][0];
        downWalk = new TextureRegion(down);
        downWalk.flip(true, false);
		up = grid[6][1];
		right = grid[6][3];
        stand = grid[6][2];
		left = new TextureRegion(right);
		left.flip(true, false);
		aTree = new TextureRegion(tiles, 0, 8, 16, 16 );
        walk = new Animation(0.1f, grid[6][2], grid[6][3]);
        upWalk = new TextureRegion(up);
        upWalk.flip(true, false);
        walkUp = new Animation(0.1f, up, upWalk);
        walkDown = new Animation(0.1f, down, downWalk);

        tileImg = new Texture("image.png");
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false,w,h);
        camera.update();
        tiledMap = new TmxMapLoader().load("tileset.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        Gdx.input.setInputProcessor(this);
		

    }

	@Override
	public void render () {
        Gdx.gl.glClearColor(0.137255f, 0.556863f, 0.137255f, 0.5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.position.x = x;
        camera.position.y = y;
        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        batch.setProjectionMatrix(camera.combined);



        totalTime += Gdx.graphics.getDeltaTime();

        direction = "stand";



        move();
        //wraparound();




		batch.begin();


		if (direction.equals("right")){
			sprite = walk.getKeyFrame(totalTime, true);
			batch.draw(sprite, x, y, DRAW_WIDTH, DRAW_HEGHT);

		}
		else if (direction.equals("left")){
            sprite = walk.getKeyFrame(totalTime, true);
            batch.draw(sprite, x + DRAW_WIDTH, y, DRAW_WIDTH * -1, DRAW_HEGHT);
        }
        else if (direction.equals("up")){
            sprite = walkUp.getKeyFrame(totalTime, true);
            batch.draw(sprite, x, y, DRAW_WIDTH, DRAW_HEGHT);
        }
        else if (direction.equals("down")){
            sprite = walkDown.getKeyFrame(totalTime, true);
            batch.draw(sprite, x, y, DRAW_WIDTH, DRAW_HEGHT);
        }
        else{
            sprite = stand;
            if (wasRight){
                batch.draw(sprite, x, y, DRAW_WIDTH, DRAW_HEGHT);
            }
            else if (!wasRight){
                batch.draw(sprite, x + DRAW_WIDTH, y, DRAW_WIDTH * -1, DRAW_HEGHT);
            }
        }




        batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		tiles.dispose();
	}

	public float decelerate(float velocity){
        velocity *= FRICTION;
        if (Math.abs(velocity) < 70){
            velocity = 0;
        }
        return velocity;


    }

	public void move(){
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            speedBoost();
        }else {
            currentSpeed = MAX_VELOCITY;

        }



        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            xVel = currentSpeed;
            direction = "right";
            wasRight = true;

        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            xVel = currentSpeed * -1;
            direction = "left";
            wasRight = false;
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            yVel = currentSpeed;
            direction = "up";
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            yVel = currentSpeed * -1;
            direction = "down";
        }

        x += xVel * Gdx.graphics.getDeltaTime();
        y += yVel * Gdx.graphics.getDeltaTime();

        xVel = decelerate(xVel);
        yVel = decelerate(yVel);

	}

	public void speedBoost(){
            currentSpeed = MAX_VELOCITY + VELOCITY_BOOST;
    }
    public void wraparound(){
        if (x > Gdx.graphics.getWidth()){
            x = 0;
        }
        else if(x < 0){
            x = Gdx.graphics.getWidth();
        }
        if (y > Gdx.graphics.getHeight()){
            y = 0;
        }
        else if(y < 0){
            y = Gdx.graphics.getHeight();
        }

    }
    public int getRandom(){
		Random rand = new Random();
		int random =  rand.nextInt((Gdx.graphics.getWidth() - 15) + 1) + 15;
		return random;

	}
    @Override public boolean keyDown(int keycode) {
        return false;
    }




    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return true;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
