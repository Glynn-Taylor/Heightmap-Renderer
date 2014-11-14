package game.states;

import game.gui.GUI_Layer;
import game.states.Game.GameState;
import game.util.FileBrowser;

import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
public class State_HEIGHTMAP_MENU extends State {
	
	private int MouseLastX;
	private int MouseLastY;
	private GUI_Layer GUI= new GUI_Layer();
	float RebuildTimer = 1;
	Texture BackGroundImage;
	Texture HeightMap1;
	Texture HeightMap2;
	Texture HeightMap3;
	
	@Override
	protected void Init() {
		
		try {
			 GL11.glEnable(GL11.GL_TEXTURE_2D);
			 Texture button1 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Buttons/HMButton1.png"), false, GL11.GL_NEAREST);
			 GUI.AddButton(460, 154, button1.getImageWidth(), button1.getImageHeight(), button1);
			 Texture button2 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Buttons/HMButton2.png"), false, GL11.GL_NEAREST);
			 GUI.AddButton(460, 214, button2.getImageWidth(), button2.getImageHeight(), button2);
			 Texture button3 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Buttons/HMButton3.png"), false, GL11.GL_NEAREST);
			 GUI.AddButton(460, 274, button3.getImageWidth(), button3.getImageHeight(), button3);
			 Texture button4 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Buttons/HMButton4.png"), false, GL11.GL_NEAREST);
			 GUI.AddButton(460, 334, button4.getImageWidth(), button4.getImageHeight(), button4);
			 Texture button5 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Buttons/Back.png"), false, GL11.GL_NEAREST);
			 GUI.AddButton(460, 394, button5.getImageWidth(), button5.getImageHeight(), button5);
			BackGroundImage = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Menus/HMMenu.png"), false, GL11.GL_NEAREST);
			HeightMap1 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/images/Heightmap1.png"));
			HeightMap2 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/images/Heightmap2.png"));
			HeightMap3 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/images/Heightmap3.png"));
		} catch (IOException e) {
			
			System.out.println("ERROR: Failed to load image @ Menu");
			e.printStackTrace();
		}
		
	}

	@Override
	protected void Update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void ProcessInput() {
	
		if(MouseLastX!=Mouse.getX()){
			MouseLastX=Mouse.getX();
			//System.out.println(Integer.toString(MouseLastX)+":"+Integer.toString(MouseLastY));
		}
		if(MouseLastY!=Mouse.getY()){
			MouseLastY=Mouse.getY();
			//System.out.println(Integer.toString(MouseLastX)+":"+Integer.toString(MouseLastY));
		}
		GUI.ProcessInput(MouseLastX, MouseLastY, Mouse.isButtonDown(0));
		if(GUI.isButtonDown(0)){
			FileBrowser fb = new FileBrowser();
			try {
				 GL11.glEnable(GL11.GL_TEXTURE_2D);
				String path = fb.GetImage();
				if(path!=null){
					Texture HeightMap = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(path));
					State_HEIGHTMAP sh=null;
					sh = (State_HEIGHTMAP)(GameState.State_Heightmap.GetState());
					sh.LoadMapWithTexture(HeightMap);
					fb.DestroyMe();
					GameState.SwitchToState(GameState.State_Heightmap_Menu, GameState.State_Heightmap);
				}
				//System.out.println(fb.GetImage());
			} catch (IOException e) {
				
				System.out.println("ERROR: Failed to load image @ Filebrowser");
				e.printStackTrace();
			}	
			
			//fb.DestroyMe();
		}
		if(GUI.isButtonDown(1)){
			State_HEIGHTMAP sh=null;
			sh = (State_HEIGHTMAP)(GameState.State_Heightmap.GetState());
			sh.LoadMapWithTexture(HeightMap1);
			GameState.SwitchToState(GameState.State_Heightmap_Menu, GameState.State_Heightmap);
		}
		if(GUI.isButtonDown(2)){
			State_HEIGHTMAP sh=null;
			sh = (State_HEIGHTMAP)(GameState.State_Heightmap.GetState());
			sh.LoadMapWithTexture(HeightMap2);
			GameState.SwitchToState(GameState.State_Heightmap_Menu, GameState.State_Heightmap);
		}
		if(GUI.isButtonDown(3)){
			State_HEIGHTMAP sh=null;
			sh = (State_HEIGHTMAP)(GameState.State_Heightmap.GetState());
			sh.LoadMapWithTexture(HeightMap3);
			GameState.SwitchToState(GameState.State_Heightmap_Menu, GameState.State_Heightmap);
		}
		if(GUI.isButtonDown(4)){
			GameState.SwitchToState(GameState.State_Heightmap_Menu, GameState.State_Menu);
		}
	}
	
	
	private void DrawBackground(){
		BackGroundImage.bind();
		GL11.glBegin(GL11.GL_QUADS);
	
			GL11.glTexCoord2f(0,0);
			GL11.glVertex3f(0,0,0);
	
			GL11.glTexCoord2f(0,1);
			GL11.glVertex3f(0,BackGroundImage.getTextureHeight(),0);
		
			GL11.glTexCoord2f(1,1);
			GL11.glVertex3f(BackGroundImage.getTextureWidth(),BackGroundImage.getTextureHeight(),0);
		
			GL11.glTexCoord2f(1,0);
			GL11.glVertex3f(BackGroundImage.getTextureWidth(),0,0);
	
		GL11.glEnd();
	}
	
	protected void Render() {
		OrthoMode();
		ModelMode();
		DrawBackground();
		GUI.Render();
		ProjectionMode();
		ModelMode();
		
	}

	@Override
	public void Unload() {
		// TODO Auto-generated method stub
		
	}

}
