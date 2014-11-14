package game.gui;

import java.util.ArrayList;

import org.newdawn.slick.opengl.Texture;

public class GUI_Layer {
	private ArrayList<GUI_Button> Buttons = new ArrayList<GUI_Button>();
	private boolean[] ButtonClicked;
	private static long LastClickTime;
	private final long RegisterClickDelay =500;
	public void Render(){
		for(int i =0; i <Buttons.size();i++){
			Buttons.get(i).Render();
		}
		
	}
	public void ProcessInput(int mouseX, int mouseY, boolean mouseDown){
		for(int i =0; i <Buttons.size();i++){
			if(Buttons.get(i).InsideButton(mouseX, mouseY)&&mouseDown&&System.currentTimeMillis()-LastClickTime>RegisterClickDelay){
				ButtonClicked[i]=true;
				LastClickTime=System.currentTimeMillis();
			}else{
				ButtonClicked[i]=false;
			}
		}
	}
	public void AddButton (int startX, int startY, int width, int height, Texture t){
		Buttons.add(new GUI_Button(startX, startY, width, height, t));
		ButtonClicked= new boolean[Buttons.size()];
	}
	public boolean isButtonDown(int i){
		if(ButtonClicked[i]==true){
			//ButtonClicked[i]=false;
			return true;
		}
		return false;
	}
}
