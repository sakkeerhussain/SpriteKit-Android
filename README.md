## SpriteKit-Android
Android 2D image UI rendering tool using open GL technology

1. Developers can add a view group in their ui an pass a reference of that on initialising sprite kit. 

```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FrameLayout parentLayout = (FrameLayout) findViewById(R.id.parentLayout);
        setupSpriteKit(parentLayout);
    }
```


2. Create Texture object.

Texture represents a particular image, Can be created by passing reference of a drawable resource id.
Eg: 
```java
Texture whiteCoinTexture = new Texture(R.drawable.coin_white);
```


3. Create Sprite object.

Sprite reoresents an object in the UI, and user can updated positio and size dynamically by keeping reference of the same.
Eg: 
```java
    Sprite bgSprite = new Sprite(spriteKit, R.drawable.carrom_board);
    Sprite whiteSprite = new Sprite(spriteKit, whiteCoinTexture);
```
    
Size and position of sprite can be updated by its reference.
Eg: 
```java
    bgSprite.setPosition(new PointF(500,500));  //sentting center point of sprite
    bgSprite.setSize(1000, 1000);               //using width and height
    bgSprite.setSize(500);                      //using radius
```

Sprite should add to sprite kit before view created. by calling spriteKit.addSprite() methord.
Eg: 
```java
spriteKit.addSprite(bgSprite);
```



A carom board UI example is added below with coins moving randomly.
==================================================================
```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FrameLayout parentLayout = (FrameLayout) findViewById(R.id.parentLayout);
        setupSpriteKit(parentLayout);
    }

    private void setupSpriteKit(ViewGroup parentLayout) {
        spriteKit = new SpriteKit(this, parentLayout);
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
```

## Dependency

Add jitpack.io to your root build.gradle, eg:

```groovy
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```

then add the dependency to your project build.gradle:

```groovy
dependencies {
    compile 'com.github.sakkeerhussain:SpriteKit-Android:1.0'
}
```
You can find the latest version in the releases tab above: https://github.com/sakkeerhussain/SpriteKit-Android/releases

More options at jitpack.io: https://jitpack.io/#sakkeerhussain/SpriteKit-Android

## Licence

Full licence here: https://github.com/sakkeerhussain/SpriteKit-Android/blob/master/LICENSE

In short:

> A permissive license whose main conditions require preservation of copyright and license notices. Contributors provide an express grant of patent rights. Licensed works, modifications, and larger works may be distributed under different terms and without source code.


