package lucrasart;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import lucrasart.GameScreen;
import lucrasart.Player;

import lucrasart.GameScreen.GameState;
import lucrasart.Player.State;


public class Map { 
	
	private SavingGrannyMain main;
	private SpriteBatch batch;
	public static TiledMap map; 
	private TmxMapLoader loader; 
	private OrthogonalTiledMapRenderer renderer; 
	
	public static Player player; 
	public Sound dieSound; 
	public static Array<Rectangle> platforms; 
	public static Array<Enemy> enemies; 
	public static Rectangle start; 
	public static Rectangle goal;
	
	public static Rectangle checkPoint;
	
	public static Array<Coin> coins;
	public static Array<Life> lifes;
	
	public static boolean flag1 = false;
	
	public static boolean flag858 = false;
	public static boolean flag959 = false;
	
	
	//private float count=0;
	
	private float countcheck = 0;
	
	public Map(SavingGrannyMain main, SpriteBatch batch) {
		this.main = main;
		loader = new TmxMapLoader();
		//map = loader.load("tiledMap2.tmx");
		map = loader.load("tiledMap-Town.tmx");
		//map = loader.load("tiledMap-Forest.tmx");
		this.batch = batch;
		renderer = new OrthogonalTiledMapRenderer(map, batch);
		dieSound = Gdx.audio.newSound(Gdx.files.internal("Audio/Gag-Sound.wav"));
		
		processMapMetadata();
		
		GameScreen.map1=true;
	}

	public void draw(OrthographicCamera camera) { 
		renderer.setView(camera);
		renderer.render();
		
		update();
		
		batch.begin();
		player.draw(batch);
	
		
		for(Enemy enemy : enemies)
		{
			enemy.draw(batch);
		}
		
		for(Coin coin : coins)
		{
			coin.draw(batch);
		}
		
		for(Life life : lifes)
		{
			life.draw(batch);
		}
		
		batch.end();
	}
	
	private void update() { 
		
		
		
		player.update(platforms); 
		
		for(Coin coin : coins)
		{
			if(player.getBody().overlaps(coin.getBody())) 
			{	
				GameScreen.appleCount++;
				GameScreen.appleLabel.setText("x"+GameScreen.appleCount);
				GameScreen.scoreCount += 10;
				GameScreen.score.setText("Score "+GameScreen.scoreCount);
				
				GameScreen.coinSound.play();
				coins.removeValue(coin, false);
				
			
			}
		}
		
		
		for(Life life : lifes)
		{
			if(player.getBody().overlaps(life.getBody())) 
			{	
				GameScreen.lifeCount++;
				GameScreen.lifeLabel.setText("x"+GameScreen.lifeCount);
				
				GameScreen.scoreCount += 20;
				GameScreen.score.setText("Score "+GameScreen.scoreCount);
				
				GameScreen.lifeSound.play();
				lifes.removeValue(life, false);
				
			
			}
		}
		
		
		
		for(Enemy enemy : enemies) 
		{
			enemy.update();
			
			if(Player.state == State.fighting  )
			{
				if((player.getPosition().x + 800) == (enemy.getPosition().x))
				{
					enemies.removeValue(enemy, false);
					GameScreen.scoreCount += 30;
					GameScreen.score.setText("Score "+GameScreen.scoreCount);
					
				}
			}
			
			
			if(player.getBody().overlaps(enemy.getBody())) 
			{	
				/*else*/ if(Player.state == State.falling )//|| Player.state == State.jumping)
				{
				//	flag858=true;
					//flag959=true;
				//}
				
				//else if(Player.state == State.low)
				//{
					//++count;
					//if(count == 1)
					//{
						
					//}
					//if(count == 7)
					//{
					enemies.removeValue(enemy, false);
					//count=0;
					
					
					GameScreen.ovejaSound.play();
					GameScreen.scoreCount += 30;
					GameScreen.score.setText("Score "+GameScreen.scoreCount);
					//}
				}	
					
			
				else
				{
					//++countG;
					
					//if(countG == 2)
					//{
						GameScreen.lifeCount--;
						GameScreen.lifeLabel.setText("x"+GameScreen.lifeCount);
						if(GameScreen.checkpoint)
						{
							player.setPosition(checkPoint.x, checkPoint.y);
							dieSound.play();
						}
						else
						{	
						reset();
						//countG=0;
						}
					//}
				}
			}	
		}
		
		if(player.getBody().overlaps(goal)) { 
			main.win2 = true;
			GameScreen.finishSound.play();
			//main.win = true;
			//GameScreen.map1=false;
			//GameScreen.GameMusic1.stop();
			//GameScreen.map20=true;
		}
		
		if(player.getBody().overlaps(checkPoint)) 
		{ 
			
			countcheck += Gdx.graphics.getDeltaTime();
			//activar flag 
			GameScreen.checkpoint = true;
			
			GameScreen.checkpointSound.play();
			
			
			
			if(countcheck > 0.3f)
			{
			GameScreen.checkpointSound.stop();
			}
			//si player muere y flag activada volver a la posicion checkPoint
			
		}
		
		if(player.getPosition().y < 0)
		{
			GameScreen.lifeCount--;
			GameScreen.lifeLabel.setText("x"+GameScreen.lifeCount);
			if(GameScreen.checkpoint)
			{
				player.setPosition(checkPoint.x, checkPoint.y);
				dieSound.play();
			}
			else
			{	
			reset();
			}
		}
		
		if(GameScreen.lifeCount == 0)
		{
			main.gameOver=true;
			flag1 = true;
			GameScreen.checkpoint = false;
			GameScreen.gameState = GameState.GameOver;
		}
	}
	
	
	
	private void reset() { 
		player.setPosition(start.x, start.y);
		dieSound.play();
	}
	
	public void dispose() {
		map.dispose();
		renderer.dispose();
		player.dispose();
		for(Enemy enemy : enemies)
			enemy.dispose();
		for(Coin coin : coins)
			coin.dispose();
	}
	
	public TiledMap getMap() {
		return map;
	}

	public Player getPlayer() { 
		return player;
	}

	static void processMapMetadata() {
	//private void processMapMetadata() {
		platforms = new Array<Rectangle>(); 
		enemies = new Array<Enemy>();
		coins = new Array<Coin>();
		lifes = new Array<Life>();

		MapObjects objects = map.getLayers().get("Objects").getObjects(); 

		for (MapObject object : objects) {
			String name = object.getName();
			RectangleMapObject rectangleObject = (RectangleMapObject) object;
			Rectangle rectangle = rectangleObject.getRectangle();

			if(name.equals("PlayerStart")) 
				start = rectangle;
			if(name.equals("PlayerGoal"))
				goal = rectangle;
			
			if(name.equals("CheckPoint"))
				checkPoint = rectangle;
			
			if(name.equals("coin"))
			{
				Coin coin = new Coin();
				
				coin.setPosition(rectangle.x, rectangle.y);
				
				coins.add(coin);
			}
			
			
			if(name.equals("extra"))
			{
				Life life = new Life();
				
				life.setPosition(rectangle.x, rectangle.y);
				
				lifes.add(life);
			}
		}
		
		objects = map.getLayers().get("Physics").getObjects();
		
		for (MapObject object : objects) {
			RectangleMapObject rectangleObject = (RectangleMapObject) object;
			Rectangle rectangle = rectangleObject.getRectangle();
			platforms.add(rectangle); 
		} 
			
		
		objects = map.getLayers().get("Enemies").getObjects();
		for (MapObject object : objects) {
			RectangleMapObject rectangleObject = (RectangleMapObject) object; 
			Rectangle rectangle = rectangleObject.getRectangle();
			Enemy enemy = new Enemy ();
			enemy.setPosition(rectangle.x, rectangle.y);
			enemies.add(enemy);	
		}
		player = new Player(); 
		
		player.setPosition(start.x, start.y);
	}
}
