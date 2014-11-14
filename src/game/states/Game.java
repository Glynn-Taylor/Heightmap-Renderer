package game.states;


import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;

import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import org.lwjgl.util.glu.GLU;

/*CLASS used as a launcher for the program, handles basic initialisation and swapping states
 * 
 */
public class Game {
	public static final int Width=800,Height=600;
	//The current state, launches the default state on startup
	public static GameState CurrentState = GameState.State_Menu;
	//Ambient lighting intensity (1 is fullbright)
	private final float AmbientLight = 0.5f;
	
	//CALLED after the program starts
	public void Start() {
		try {
			CreateWindow();
			InitGL();

			Run();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	DisplayMode displayMode;

	//CREATES the window and sets basic properties
	private void CreateWindow() throws Exception {
		Display.setFullscreen(false);
		DisplayMode d[] = Display.getAvailableDisplayModes();
		//Loops through possible displays until the one with the right dimensions is found
		for (int i = 0; i < d.length; i++) {
			if (d[i].getWidth() == Width && d[i].getHeight() == Height
					&& d[i].getBitsPerPixel() == 32) {
				displayMode = d[i];
				break;
			}
		}
		//Prints out available resolutions
		/*for (int i = 0; i < d.length; i++) {
			//System.out.println(Integer.toString(d[i].getWidth())+":"+Integer.toString(d[i].getHeight()));
		}*/
		Display.setDisplayMode(displayMode);
		Display.setTitle("EPQ Voxel engine");
		Display.setVSyncEnabled(true);
		Display.create();
	}

	//INITIALISES different OpenGL modes/features and sets up lighting + camera mode
	private void InitGL() {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL11.glClearDepth(1.0);
		GL11.glEnable(GL11.GL_BLEND);
	    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glEnable(GL11.GL_LIGHTING);
	    GL11.glEnable(GL11.GL_LIGHT0);
	    
	    GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, CreateFloatBuffer(1.0f, 1.0f, 1.0f, 1.0f));
	    GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, 64f);

	    GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION,CreateFloatBuffer(2f, 40f, 2f, 0f));
	    GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, CreateFloatBuffer(1f, 1f, 1f, 1f));
	    GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, CreateFloatBuffer(AmbientLight, AmbientLight, AmbientLight, 1.0f));

	    GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(45.0f, (float) displayMode.getWidth()
				/ (float) displayMode.getHeight(), 0.1f, 300.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
	
	}
	//LOOP: The base level loop, handles a state being swapped, if the state stops without swapping the program ends.
	//Destroys sound and display before killing the process.
	private void Run(){
		
		CurrentState.GetState().Launch();
		
		
		
		while (CurrentState.GetState().LaunchMe) {
			try {
			
				if(CurrentState.GetState().LaunchMe){
					CurrentState.GetState().Toggle(true);
				}
			} catch (Exception e) {
				System.out.println(e.getLocalizedMessage());
			}
		}
		AL.destroy();
		Display.destroy();
		System.exit(0);
		
	}
	
	

	//CALLED on startup, instantiates the program and starts it.
	public static void main(String[] args) throws LWJGLException {
		Game r = new Game();
		r.Start();
	}
	//STORES each state with a specific ID and allows for swapping between two states using SwitchToState
	public enum GameState {
		State_Heightmap(0, new State_HEIGHTMAP()),
		State_Heightmap_Menu(1, new State_HEIGHTMAP_MENU()),
		State_Menu(2, new State_MENU());
		private int StateID;
		private State GameState;
		GameState(int i, State state) {
			StateID = i;
			GameState = state;
		}

		public int GetID() {
			return StateID;
		}
		public static void SwitchToState(GameState oldState, GameState newState){
			oldState.GetState().Toggle(false);
			newState.GetState().Launch();
			Game.CurrentState=newState;
		}
		public State GetState(){
			return GameState;
		}
	}
	//Returns a new floatbuffer that is fully configured.
	public FloatBuffer CreateFloatBuffer(float a, float b, float c, float d) {
	    float[] data = new float[]{a,b,c,d};
	    FloatBuffer fb = BufferUtils.createFloatBuffer(data.length);
	    fb.put(data);
	    fb.flip();
	    return fb;
	  }	
}
