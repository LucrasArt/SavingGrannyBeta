package lucrasart;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class GameScreen extends AbstractScreen { 
	enum GameState {Start, Running, GameOver, Finish}
	
	public static GameState gameState;
	
	public static Music GameMusic1; 
	public static Music GameMusic2;
	
	private Map map;
	private Map2 map2;
	private Texture continueEsp, continueEng;
	private Texture lose;
	
	private Stage stage;
	private Skin skin;
	private Table table;
	private Texture apple;
	private Texture life;
	public static Label appleLabel, lifeLabel, score, time;
	public static int appleCount=0, lifeCount=3, scoreCount=0; 
	public static float timeCount= 0;

	
	public static Sound coinSound; 
	public static Sound lifeSound;
	public static Sound checkpointSound;
	public static Sound ovejaSound;
	public static Sound finishSound;
	public static Sound pigSound;
	
	private float count,count2 = 0;
	
	public static boolean checkpoint = false;
	public static boolean checkpoint2 = false;
	
	public static boolean map1 = false;
	public static boolean map20 = false;
	
	public GameScreen(SavingGrannyMain main) {
		super(main);
		
		gameState = GameState.Start;
		
		
		map = new Map(main, batch);
		map2 = new Map2(main, batch);
		
		continueEsp = new Texture("Scenes/continuara.png");
		continueEng = new Texture("Scenes/continued.png");
		//lose = new Texture (Gdx.files.internal("Dead.png"));
		lose = new Texture (Gdx.files.internal("Scenes/gameOver.png"));
		
		coinSound = Gdx.audio.newSound(Gdx.files.internal("Audio/coin.wav"));
		lifeSound = Gdx.audio.newSound(Gdx.files.internal("Audio/powerup-success.wav"));
		checkpointSound = Gdx.audio.newSound(Gdx.files.internal("Audio/bell.wav"));
		//ovejaSound = Gdx.audio.newSound(Gdx.files.internal("Audio/oveja.wav"));
		ovejaSound = Gdx.audio.newSound(Gdx.files.internal("Audio/baaaa.mp3"));
		//pigSound = Gdx.audio.newSound(Gdx.files.internal("Pig-Oinking.wav"));
		pigSound = Gdx.audio.newSound(Gdx.files.internal("Audio/pigfarm2.wav"));
		finishSound = Gdx.audio.newSound(Gdx.files.internal("Audio/aplauso2.wav"));
		
		apple = new Texture("Items/manzana.png");
		life = new Texture("Items/heart.png");
		Image manzana = new Image(apple); 
		Image vida = new Image(life); 

		skin = new Skin (Gdx.files.internal("skin/uiskin.json"));
		
		appleLabel = new Label ("x"+appleCount,skin);
		lifeLabel = new Label ("x"+lifeCount,skin);
		score = new Label ("Score "+scoreCount,skin);
		time = new Label ("Time "+timeCount,skin);
		
		stage = new Stage();
		
		table = new Table();
		table.setPosition(500, Gdx.graphics.getHeight()*0.9f);
		
		stage.addActor(table);
		
		Gdx.input.setInputProcessor(stage);
		
		table.add(vida).top().left().width(30).height(30);
		table.add(lifeLabel).top().left().padRight(800);
		table.add(score).top().right();
		table.row();
		table.add(manzana).top().left().width(30).height(30);
		table.add(appleLabel).top().left();
		table.add(time).top().right();
		
		
		
		
	}
	
	@Override
	public void show(){
		SavingGrannyMain.GameMusic.stop();
		if(map1)
		{
		GameScreen.GameMusic1 = Gdx.audio.newMusic(Gdx.files.internal("Audio/Green_Hills.mp3"));
		//GameMusic1 = Gdx.audio.newMusic(Gdx.files.internal("Audio/Tavern.mp3"));
		GameMusic1.play();
		GameMusic1.setLooping(true);
		}
		/*else*/
		 if(GameScreen.map20)
			{
				GameScreen.GameMusic2 = Gdx.audio.newMusic(Gdx.files.internal("Audio/Green_Hills.mp3"));
				GameScreen.GameMusic2.play();
				GameScreen.GameMusic2.setLooping(true);
			}
			
		 
	}
	
	
	
	@Override
	public void render(float delta) { 
		Gdx.gl.glClearColor(0, 0, 0, 1);  
		                                    
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		
		switch(gameState)
		{
		case Start:
			start();
			break;
		case Running:
			running();
			break;
		case GameOver:
			GameOver();
			break;
		case Finish:	
			finish();
			break;
		}
		
	}
	
	@Override
	public void dispose() { 
		map.dispose();
	}
	
	
	void start()
	{
		timeCount = 0;
		time.setText("Time "+GameScreen.timeCount);
		
		appleCount = 0;
		appleLabel.setText("x"+GameScreen.appleCount);
		scoreCount = 0;
		score.setText("Score "+GameScreen.scoreCount);
		lifeCount = 3;
		lifeLabel.setText("x"+GameScreen.lifeCount);
		
		 if(Map.flag1)
		 {
			 Map.processMapMetadata();
			 Map.flag1=false;
		 }
		if(Map2.flag1)
		{
			Map2.processMapMetadata();
			Map2.flag1=false;
		}
		
		gameState = GameState.Running;
	}
	
	
	void GameOver()
	{
		
		batch.begin();
		
			batch.draw(lose, camera.position.x - lose.getWidth() * 0.5f, camera.position.y - lose.getHeight() * 0.5f, 
					lose.getWidth(), lose.getHeight()); 
		
			
			
		batch.end();
				
		count += Gdx.graphics.getDeltaTime();
		
				//if(Gdx.input.isKeyPressed(Keys.ANY_KEY))
				if(count > 1)
				{
					gameState = GameState.Start;	
				}
		
	}
	
	void running()
	{
		//Arreglarlo
		//timeCount += Gdx.graphics.getDeltaTime();
		//time.setText("Time "+GameScreen.timeCount);
		
		
		
		if(main.win)
		{
			//fin juego
			gameState = GameState.Finish;	
			
		}
		 if(main.win2)
		{
			//fin fase 1 
			updateCameraWin2();
			map2.draw(camera);
			stage.draw();
		}
		else
		{ 
			
			updateCamera();
			map.draw(camera);
				//atajo para iniciar en la fase 2
				//updateCameraWin2();
				//map2.draw(camera);
			
			stage.draw();
			
		}
		
		if(Gdx.input.isKeyPressed(Keys.ESCAPE))
		{ 
			if(Cover.flagLanguageEsp)
			{
				main.setScreen(new PantallaMenuEsp(main));
			}
			else if(Cover.flagLanguageEng)
			{
				main.setScreen(new PantallaMenu(main));
			}
			
		}	
		
	}
	
	
	void finish()
	{
		
		
		
		updateCameraWin();
		
		main.win=false;
		main.win2=false;
		
		count2 += Gdx.graphics.getDeltaTime();
		if(count2 > 2)
		{

			
			main.setScreen(new PantallaMenu(main));
			
			updateCamera();
			map.draw(camera);
			stage.draw();
			 
			gameState = GameState.Start;
		}
	}
	
	private void updateCamera() { 
		
		
		
		camera.position.x = map.getPlayer().getCenterPosition().x; 
		camera.position.y = map.getPlayer().getCenterPosition().y;

		TiledMapTileLayer layer = (TiledMapTileLayer)map.getMap().getLayers().get(0); 

		float cameraMinX = viewport.getWorldWidth() * 0.5f; 
		float cameraMinY = viewport.getWorldHeight() * 0.5f; 
		float cameraMaxX = layer.getWidth() * layer.getTileWidth() - cameraMinX; 
		float cameraMaxY = layer.getHeight() * layer.getTileHeight() - cameraMinY; 

		camera.position.x = MathUtils.clamp(camera.position.x, cameraMinX, cameraMaxX); 
		camera.position.y= MathUtils.clamp(camera.position.y, cameraMinY, cameraMaxY);
                                                                                       
		camera.update();
	}
	
	private void updateCameraWin() {
		batch.begin();
		
			
		if(Cover.flagLanguageEsp)
		{
			batch.draw(continueEsp, camera.position.x - continueEsp.getWidth() * 0.5f, camera.position.y - continueEsp.getHeight() * 0.5f, 
					continueEsp.getWidth(), continueEsp.getHeight()); 
		}
		else if(Cover.flagLanguageEng)
		{
			batch.draw(continueEng, camera.position.x - continueEng.getWidth() * 0.5f, camera.position.y - continueEng.getHeight() * 0.5f, 
					continueEng.getWidth(), continueEng.getHeight()); 
		}
		
		
		
			
		
		batch.end();
		
	}
	
	private void updateCameraWin2() {
		// TODO Auto-generated method stub
		
		
		
		camera.position.x = map2.getPlayer().getCenterPosition().x; 
		camera.position.y = map2.getPlayer().getCenterPosition().y;

		TiledMapTileLayer layer = (TiledMapTileLayer)map2.getMap().getLayers().get(0); 

		float cameraMinX = viewport.getWorldWidth() * 0.5f; 
		float cameraMinY = viewport.getWorldHeight() * 0.5f; 
		float cameraMaxX = layer.getWidth() * layer.getTileWidth() - cameraMinX; 
		float cameraMaxY = layer.getHeight() * layer.getTileHeight() - cameraMinY; 

		camera.position.x = MathUtils.clamp(camera.position.x, cameraMinX, cameraMaxX); 
		camera.position.y= MathUtils.clamp(camera.position.y, cameraMinY, cameraMaxY); 
                                                                                       
		camera.update();
	}
}
