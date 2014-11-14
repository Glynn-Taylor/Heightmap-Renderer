package game.states;

import game.gui.GUI_Layer;
import game.states.Game.GameState;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
public class State_MENU extends State {
	
	private int MouseLastX;
	private int MouseLastY;
	private GUI_Layer GUI= new GUI_Layer();
	float RebuildTimer = 1;
	Texture BackGroundImage;
	
	@Override
	protected void Init() {
		
		try {
			 GL11.glEnable(GL11.GL_TEXTURE_2D);
			 Texture button1 = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Buttons/Button1.png"), false, GL11.GL_NEAREST);
			 GUI.AddButton(460, 154, button1.getImageWidth(), button1.getImageHeight(), button1);
			BackGroundImage = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Menus/Menu.png"), false, GL11.GL_NEAREST);
			TextureImpl.unbind();
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
			GameState.SwitchToState(GameState.State_Menu, GameState.State_Heightmap_Menu);
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
