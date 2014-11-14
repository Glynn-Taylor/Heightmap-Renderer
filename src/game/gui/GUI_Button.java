package game.gui;

import game.states.Game;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

public class GUI_Button {
	int StartX, StartY, Width, Height,ScreenWidth,ScreenHeight;
	int ScreenX,ScreenY,ScreenButtonWidth,ScreenButtonHeight;
	Texture texture;
	private final float MouseOverMultiplier = 1.1f;
	private boolean MouseInsideMe;
	public void Render(){
		//System.out.println(StartX);
		//System.out.println(StartX+Width);
		texture.bind();
		GL11.glBegin(GL11.GL_QUADS);
	
			GL11.glTexCoord2f(0,0);
			GL11.glVertex3f(ScreenX,ScreenY,0);
	
			GL11.glTexCoord2f(0,1);
			GL11.glVertex3f(ScreenX,ScreenY+ScreenButtonHeight*(MouseInsideMe?MouseOverMultiplier:1),0);
		
			GL11.glTexCoord2f(1,1);
			GL11.glVertex3f(ScreenX+ScreenButtonWidth*(MouseInsideMe?MouseOverMultiplier:1),ScreenY+ScreenButtonHeight*(MouseInsideMe?MouseOverMultiplier:1),0);
		
			GL11.glTexCoord2f(1,0);
			GL11.glVertex3f(ScreenX+ScreenButtonWidth*(MouseInsideMe?MouseOverMultiplier:1),ScreenY,0);
	
		GL11.glEnd();
	
		
	}
	public GUI_Button(int startX, int startY, int width, int height, Texture t){
		int displayWidth = NextPowerOf2(Game.Width);
		int displayHeight = NextPowerOf2(Game.Height);
		int gameWidth = Game.Width;
		int gameHeight = Game.Height;
		//System.out.println(startX);
		//System.out.println(gameWidth);
		//System.out.println(displayWidth);
		ScreenX=startX;
		ScreenY=startY;
		ScreenButtonWidth=width;
		ScreenButtonHeight=height;
		StartX=(int) (((float)startX/(float)gameWidth)*(float)displayWidth);
		StartY=(int) (((float)startY/(float)gameHeight)*(float)displayHeight);
		Width = (int) (((float)width/(float)gameWidth)*(float)displayWidth);
		Height=(int) (((float)height/(float)gameHeight)*(float)displayHeight);
		ScreenWidth=gameWidth;
		ScreenHeight=gameHeight;
		texture=t;
		
	}
	public int NextPowerOf2(int num){
		int returnNumber=1;
		while(returnNumber<num){
			returnNumber*=2;
		}
		return returnNumber;
		
	}
	public boolean InsideButton(int mouseX, int mouseY){
		if(mouseX>ScreenX&&mouseX<ScreenX+ScreenButtonWidth){
			if(ScreenHeight-mouseY>ScreenY&&ScreenHeight-mouseY<ScreenY+ScreenButtonHeight){
				MouseInsideMe=true;
				return true;
			}
			
		}
		MouseInsideMe=false;
		return false;
	}
}
