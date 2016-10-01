package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture tiles;
	TextureRegion down, up, right, left, sprite;
	float x, y, xVel, yVel;
    String direction;
    float currentSpeed;


	static final int WIDTH = 16;
	static final int HEIGHT = 16;
	static final int DRAW_WIDTH = WIDTH * 4;
	static final int DRAW_HEGHT = HEIGHT * 4;
	static final float MAX_VELOCITY = 80;
    static final float VELOCITY_BOOST = 90;
	static final float FRICTION = 0.80f;



	
	@Override
	public void create () {
		batch = new SpriteBatch();
		tiles = new Texture("tiles.png");
		TextureRegion[][] grid = TextureRegion.split(tiles, 16, 16);
		down = grid[6][0];
		up = grid[6][1];
		right = grid[6][3];
		left = new TextureRegion(right);
		left.flip(true, false);


	}

	@Override
	public void render () {
        direction = "right";

		move();
		Gdx.gl.glClearColor(0.137255f, 0.556863f , 0.137255f, 0.5f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		if (direction.equals("right")){
			sprite = right;
			batch.draw(sprite, x, y, DRAW_WIDTH, DRAW_HEGHT);
		}
		else if (direction.equals("left")){
            sprite = left;
            batch.draw(sprite, x, y, DRAW_WIDTH, DRAW_HEGHT);
        }
        if (direction.equals("up")){
        sprite = up;
        batch.draw(sprite, x, y, DRAW_WIDTH, DRAW_HEGHT);
        }
        else if (direction.equals("down")){
            sprite = down;
            batch.draw(sprite, x, y, DRAW_WIDTH, DRAW_HEGHT);
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
        if (Math.abs(velocity) < 60){
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
            xVel += currentSpeed;
            direction = "right";

        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            xVel += currentSpeed * -1;
            direction = "left";
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            yVel += currentSpeed;
            direction = "up";
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            yVel += currentSpeed * -1;
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
}
