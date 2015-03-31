package lucrasart;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class EnemyPig extends Entity { 

enum StateE { alive , dead }	
static StateE stateE;
	
public static final float WIDTH = 72; 
public static final float HEIGHT = 97; 

private Texture frame1, frame2; 
private TextureRegion dead;
private Animation animation,deadAnimation;
static float stateTime; 

private int countF=0;

public EnemyPig() {
	super(WIDTH, HEIGHT, new Rectangle());
	
	stateE = StateE.alive;
	
	stateTime = 0;
	setPosition(0, 0);
	
	frame1 = new Texture("Enemies/Jabali.png"); 
	dead = new TextureRegion(frame1);
	dead.flip(false, true);
	
	animation = new Animation(0, new TextureRegion(frame1));
	//animation = new Animation(0.5f, new TextureRegion(frame1), new TextureRegion(frame2)); // Las intoducimos en la animacion con una velocidad indicada en el primer parametro
	//animation.setPlayMode(PlayMode.LOOP);
	deadAnimation = new Animation(0, new TextureRegion(dead));
	//deadAnimation.setPlayMode(PlayMode.LOOP);
}

@Override
public void draw(SpriteBatch batch) {
	
	switch(stateE)
	{
	case alive:
		setRegion(animation.getKeyFrame(stateTime)); 
		
		break;
	case dead:
		setRegion(deadAnimation.getKeyFrame(stateTime)); 
		//batch.setColor(Color.RED);
		break;
	}
	
	
	//setRegion(animation.getKeyFrame(stateTime)); 
	super.draw(batch);
}

public void update() {
	stateTime += Gdx.graphics.getDeltaTime(); 
	
	if(Map.flag959)
	{
		stateE = StateE.dead;
		
		//stateTime += Gdx.graphics.getDeltaTime();
		
		++countF;
		
		if(countF == 10)
		{
		stateE = StateE.alive;
		
		countF=0;
		
		Map.flag959=false;
		}
		
	}
}

public void dispose() {
	frame1.dispose();
	frame2.dispose();
}
}
