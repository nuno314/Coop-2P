package com.mygdx.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.Escape;
import com.mygdx.handlers.ResourceHandler;
import com.mygdx.network.EventListener;
import com.mygdx.sprites.Steven;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ConnectScreen implements Screen {

    private Socket socket;

    private Escape game;
    private Viewport viewport;
    private OrthographicCamera camera;
    private Skin skin, textSkin;
    private SpriteBatch batch;
//
    private Stage stage;
    private Table root;

    public static TextField usernameLabel;
    public static TextButton connectButton;

    public static String player, teammate;
    public static int order;
    public static ArrayList<Object> players;

    public ConnectScreen(final Escape game) {
        this.game = game;
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(Escape.WIDTH, Escape.HEIGHT, camera);
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
        stage = new Stage(viewport, batch);
        skin = new Skin(Gdx.files.internal("skin/screen.json"), new TextureAtlas("skin/screen.pack"));
        textSkin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        order = 0;
        players = new ArrayList<>();
        root = new Table();
        usernameLabel = new TextField("Username", textSkin);
        connectButton = new TextButton("Connect", textSkin);
        connectButton.addListener(new ClickListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EventListener.connectSocket();
                            EventListener.configSocketEvents();
                        } catch (Exception e) {
                                e.printStackTrace();
                            }
                    }
                });

                return super.touchDown(event, x, y, pointer, button);
            }
        });

//
//        setToDefault();
    }

    public void setToDefault() {
        this.root.clear();
        this.root.add(this.usernameLabel).width(250).padTop(25).row();
        this.root.add(this.connectButton).size(250, 50 ).padTop(100 ).row();
//        this.root.add(this.errorLabel).padTop(50);
    }



    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        root.setBackground(skin.getDrawable("background"));
        root.setFillParent(true);
        root.top();
        setToDefault();
        stage.addActor(root);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
        stage.act(delta);


    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
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
        skin.dispose();
    }

}
