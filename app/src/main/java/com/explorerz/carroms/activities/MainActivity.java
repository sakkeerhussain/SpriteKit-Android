package com.explorerz.carroms.activities;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.explorerz.carroms.R;
import com.explorerz.carroms.game.Game;
import com.explorerz.carroms.spritekit.Sprite;
import com.explorerz.carroms.spritekit.SpriteKit;
import com.explorerz.carroms.spritekit.Texture;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private SpriteKit spriteKit;
    private ArrayList<Sprite> coinSprites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FrameLayout parentLayout = (FrameLayout) findViewById(R.id.parentLayout);
        setupSpriteKit(parentLayout);

        Game game = new Game();
    }

    private void setupSpriteKit(ViewGroup parentLayout) {
        SpriteKit.UIConfigs.setHeight(1000);
        SpriteKit.UIConfigs.setwidth(1000);
        SpriteKit.UIConfigs.setTransparencyAlpha(0.5f);
        spriteKit = new SpriteKit(this, parentLayout);
        spriteKit.setOnTouchListener(new SpriteKit.OnTouchEventListener() {
            @Override
            public boolean onTouch(MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_DOWN){
                    Log.d("Touch", "DOWN, point: x="+e.getX()+", y="+e.getY());
                    return true;
                }else if (e.getAction() == MotionEvent.ACTION_MOVE){
                    Log.d("Touch", "MOVE, point: x="+e.getX()+", y="+e.getY());
                    return true;
                }else if (e.getAction() == MotionEvent.ACTION_UP){
                    Log.d("Touch", "UP, point: x="+e.getX()+", y="+e.getY());
                    return true;
                }
                return false;
            }
        });
        Texture whiteCoinTexture = new Texture(R.drawable.coin_white);
        Texture blckCoinTexture = new Texture(R.drawable.coin_black);

        Sprite bgSprite = new Sprite(spriteKit, R.drawable.carrom_board);
        bgSprite.setPosition(new PointF(500,500));
        bgSprite.setSize(1000, 1000);
        spriteKit.addSprite(bgSprite);

        coinSprites = new ArrayList<>();
        Sprite redSprite = new Sprite(spriteKit, R.drawable.coin_red);
        redSprite.setPosition(new PointF(500,500));
        redSprite.setSize(24);
        coinSprites.add(redSprite);
        spriteKit.addSprite(redSprite);

        for (int i = 0; i<9; i++) {
            Sprite whiteSprite = new Sprite(spriteKit, whiteCoinTexture);
            whiteSprite.setSize(24);
            coinSprites.add(whiteSprite);
            spriteKit.addSprite(whiteSprite);
        }

        for (int i = 0; i<9; i++) {
            Sprite blckSprite = new Sprite(spriteKit, blckCoinTexture);
            blckSprite.setSize(24);
            coinSprites.add(blckSprite);
            spriteKit.addSprite(blckSprite);
        }
        shuffleCoinPosition();
    }

    private void shuffleCoinPosition() {
        Random rand = new Random();
        for (Sprite whiteSprite:coinSprites) {
            float whiteDx = rand.nextInt(20)-10;
            float whiteDy = rand.nextInt(20)-10;
            float whiteX = whiteSprite.getPosition().x + whiteDx;
            float whiteY = whiteSprite.getPosition().y + whiteDy;
            if (whiteX > 800 || whiteX < 100 || whiteY > 800 || whiteY < 100){
                whiteY = rand.nextInt(800)+100;
                whiteX = rand.nextInt(800)+100;
            }
            whiteSprite.setPosition(new PointF(whiteX, whiteY));
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                shuffleCoinPosition();
            }
        }, 500);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        spriteKit.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        spriteKit.onResume();
    }
}
