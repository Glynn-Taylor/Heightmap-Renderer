package game.states;


import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.Color;

public abstract class State {
	protected boolean Active = false;
	protected boolean Initialised = false;
	public boolean LaunchMe=false;

	protected abstract void Init();
	public void Launch(){
		LaunchMe=true;
	}
	public void Toggle(boolean active) {
		Active = active;
		if (Active) {
			if (!Initialised){
				Init();
				Initialised=true;
			}
			Start();
		}
		LaunchMe=false;
	}

	protected void Start() {
		while(Active&&!Display.isCloseRequested()){
			ProcessInput();
			Update();
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT
					| GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glLoadIdentity();
			Render();
			//System.out.println("Running");
			Display.update();
			Display.sync(60);
		}

	}

	protected abstract void Update();

	protected abstract void ProcessInput();

	protected abstract void Render();

	public abstract void Unload();
	private int Width=Game.Width,Height=Game.Height;
	 protected void OrthoMode()
	    {
		 GL11.glDisable(GL11.GL_LIGHTING);
	        GL11.glMatrixMode(GL11.GL_PROJECTION);
	        GL11.glLoadIdentity();
	        //Clipping planes, 0->1 for x, 0->1 for Y, -1->1 for z
	        GL11.glOrtho(0,Width,Height,0,-1,1);
	        Color.white.bind();
	    }
	 protected void ProjectionMode()
	    {
		 	GL11.glEnable(GL11.GL_LIGHTING);
	        GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();

			GLU.gluPerspective(45.0f, (float) Display.getWidth()
					/ (float) Display.getHeight(), 0.1f, 900.0f);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GLU.gluLookAt(0, 20, 50, 0, -2, -100, 0, -1, 0);
	    }
	 protected void ModelMode(){
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
	 }
}
