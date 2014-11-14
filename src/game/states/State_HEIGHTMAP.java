package game.states;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import game.gui.GUI_Layer;
import game.map.Chunk;
import game.map.HeightMap;
import game.states.Game.GameState;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
public class State_HEIGHTMAP extends State {
	
	private HeightMap chunky;
	private float PX=50, PY=120, PZ;
	private float PYaw,PPitch;
	private int MouseLastX;
	private int MouseLastY;
	private final float MouseSpeed=0.1f;
	float RebuildTimer = 1;
	Texture texture;
	Texture testTexture;
	private GUI_Layer GUI= new GUI_Layer();
	
	@Override
	protected void Init() {
		
		//chunky.BuildHeightMap(texture);
		try {
			//texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/images/Heightmap1.png"));
			//if(texture!=null){
			 Texture button5 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Buttons/Back.png"), false, GL11.GL_NEAREST);
			 GUI.AddButton(540, 530, button5.getImageWidth(), button5.getImageHeight(), button5);
			 Texture buttonUP = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Buttons/Up.png"), false, GL11.GL_NEAREST);
			 GUI.AddButton(700, 30, buttonUP.getImageWidth(), buttonUP.getImageHeight(), buttonUP);	
			 Texture buttonDOWN = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Buttons/Down.png"), false, GL11.GL_NEAREST);
			 GUI.AddButton(700, 94, buttonDOWN.getImageWidth(), buttonDOWN.getImageHeight(), buttonDOWN);	
			 testTexture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Textures/TestTexture.png"), false, GL11.GL_NEAREST);
			//}
		} catch (IOException e) {
			
			System.out.println("Fuck");
			e.printStackTrace();
		}
		if(chunky==null){
			//chunky = new HeightMap(0, 0, 0,texture);
			
		}
		
		// THIS CODE DISPLAYS A <1000 pixel heightmap's data in a formatted grid (no alpha)
		/*byte[] bytes = texture.getTextureData();
		String s = "";
		for(int y = 0; y<texture.getImageHeight();y++){
			s = "";
			for(int x = 0; x<texture.getImageWidth();x++){
				s+=(bytes[(y*texture.getImageWidth()*3+x*3)]>=0?" ":"");
				s+=(Integer.toString(bytes[(y*texture.getImageWidth()*3+x*3)]).length()==2?" ":"");
				s+=Integer.toString(bytes[(y*texture.getImageWidth()*3+x*3)]);
				s+=(Integer.toString(bytes[(y*texture.getImageWidth()*3+x*3)]).length()==1?" ":"");
				if(Integer.toString(bytes[(y*texture.getImageWidth()*3+x*3)]).length()==3&&bytes[(y*texture.getImageWidth()*3+x*3)]<0)
					s+=" ";
			}
			System.out.println(s);
		}
		*/
		
	}
	public void LoadMapWithTexture(Texture t){
		/*if(chunky==null){
			chunky = new HeightMap(0, 0, 0,t);
		}else{
			chunky.ChangeHeightMap(0, 0, 0,t);
		}*/
		
		chunky = new HeightMap(0, 0, 0,t);
		PX=50;
		PY=120;
		PZ=-128;
		texture=t;
		//chunky.BuildHeightMap(texture);
		//chunky.BuildHeightMap(texture);
	}

	
	private boolean HasCentered=false;
	protected void Update() {
		if(!HasCentered){
			PYaw=3f;
			PPitch=-20f;
			HasCentered=true;
		}
		
	}

	@Override
	protected void ProcessInput() {
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			PY--;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_F1)) {
			chunky.RenderMode_Wireframe();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_F2)) {
			chunky.RenderMode_Solid();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			PY++;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			PX++;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			PX--;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
			PZ--;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			PZ++;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_T)) {
			System.out.println(PYaw);
			System.out.println(PPitch);
			System.out.println(PX);
			System.out.println(PY);
			
		}
	
		
		if(MouseLastX!=Mouse.getX()){
			PYaw-=((float)(MouseLastX-Mouse.getX()))*MouseSpeed;
			MouseLastX=Mouse.getX();
			
		}
		if(MouseLastY!=Mouse.getY()){
			PPitch+=((float)(MouseLastY-Mouse.getY()))*MouseSpeed;
			MouseLastY=Mouse.getY();
		}
		GUI.ProcessInput(MouseLastX, MouseLastY, Mouse.isButtonDown(0));
		if(GUI.isButtonDown(0)){
			GameState.SwitchToState(GameState.State_Heightmap, GameState.State_Heightmap_Menu);
		}
		if(GUI.isButtonDown(1)){
			HeightDivisor--;
			if(HeightDivisor<2){
				HeightDivisor=2;
			}else{
				chunky.RebuildHeightMap(texture, HeightDivisor);
			}
			
		}
		if(GUI.isButtonDown(2)){
			HeightDivisor++;
			if(HeightDivisor>128){
				HeightDivisor=128;
			}else{
				chunky.RebuildHeightMap(texture, HeightDivisor);
			}
		}
		
	}
	private int HeightDivisor=2;
	@Override
	protected void Render() {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		OrthoMode();
		ModelMode();
		
		GUI.Render();
		ProjectionMode();
		ModelMode();
		//GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glRotatef(PYaw, 0f, 1.0f, 0f);
		GL11.glRotatef(PPitch, 1f, 0f, 0f);
		
		GL11.glTranslatef(-30f + PX, -40f + PY, -160f+PZ); // Move Right
	
		GL11.glRotatef(45f, 0.4f, 1.0f, 0.1f);
		GL11.glRotatef(45f, 0f, 1.0f, 0f);
		// GL11.glRotatef(RebuildTimer, 1f, 1.0f, 1f);
		//RebuildTimer += 1;
		
		//if (RebuildTimer % 60 == 0)
			//chunky.RebuildMesh(0, 0, 0);
		GL11.glDisable(GL11.GL_COLOR_MATERIAL);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		testTexture.bind();
		chunky.Render();
		
	}

	@Override
	public void Unload() {
		// TODO Auto-generated method stub
		
	}

}
