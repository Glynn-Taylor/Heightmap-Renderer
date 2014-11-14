package game.map;

import java.nio.FloatBuffer;
import java.util.Random;

import org.lwjgl.BufferUtils;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.newdawn.slick.opengl.Texture;

import game.map.Block.BlockType;
//NEEDS COMMENTING IF USED IN THE FINAL PROJECT.
public class Chunk {
	//How many blocks the chunk stretches in the x,y,z directions
	static final int CHUNK_SIZE = 32;
	//Length of a cube
	static final int CUBE_LENGTH = 2;
	//the 3D array of blocks
	private Block[][][] Blocks;
	//Handles for storing the vertex data of the whole 3D array of blocks
	private int VBOVertexHandle;
	private int VBOColorHandle;
	private int VBONormalHandle;
	//The position of the start corner of the chunk
	private int StartX, StartY, StartZ;
	//Used for random generation
	private Random r;
	//Draw mode for swapping to wireframe, solid by default
	private int DrawMode=GL11.GL_QUADS;
	private int BlocksToRender=0;
	//RENDERS the data stored in the handles
	public void Render() {
		GL11.glPushMatrix();
		// Binds the buffers
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOVertexHandle);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0L);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOColorHandle);
		GL11.glColorPointer(3, GL11.GL_FLOAT, 0, 0L);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBONormalHandle);
		GL11.glNormalPointer(GL11.GL_FLOAT, 0, 0L);
		// Rotates the map so that it's facing the player, rotates in yaw 270
		// degrees and roll 180
		GL11.glRotatef(270f, 0f, 1.0f, 0f);
		GL11.glRotatef(180f, 0f, 0f, 1f);
		// Draws the buffers for all of the points in them (which is the same as
		// XSize*YSize*ZSize * 24,
		// as there are 4(points on a face)*6(faces) points in every cube(24))
		GL11.glDrawArrays(DrawMode, 0, BlocksToRender * 24);
		// Rotates the map back to the initial position
		GL11.glRotatef(-180f, 0f, 0f, 1f);
		GL11.glRotatef(-270f, 0f, 1.0f, 0f);
		GL11.glPopMatrix();
	}
	
	//No need to update anything in the chunk, stays blank
	public void Update() {

	}
	public static void EnableLighting(boolean enabled){
		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glEnable(GL11.GL_LIGHTING);
	    GL11.glEnable(GL11.GL_LIGHT0);
	    
	}
	//CONSTRUCTOR creates a 
	public Chunk(int startX, int startY, int startZ) {
		r= new Random();
		Blocks = new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];
		for (int x = 0; x < CHUNK_SIZE; x++) {
			for (int y = 0; y < CHUNK_SIZE; y++) {
				for (int z = 0; z < CHUNK_SIZE; z++) {
					if(r.nextFloat()>0.7f){
						Blocks[x][y][z] = new Block(BlockType.BlockType_Grass);
					}else if(r.nextFloat()>0.4f){
						Blocks[x][y][z] = new Block(BlockType.BlockType_Dirt);
					}else if(r.nextFloat()>0.2f){
						Blocks[x][y][z] = new Block(BlockType.BlockType_Water);
					}else{
						Blocks[x][y][z] = new Block(BlockType.BlockType_Default);
					}
				}
			}
		}
		
		StartX = startX;
		StartY = startY;
		StartZ = startZ;
		RebuildMesh(startX, startY, startZ);
	}
	
	public void RenderMode_Wireframe(){
		DrawMode=GL11.GL_LINE_LOOP;
	}
	public void RenderMode_Solid(){
		DrawMode=GL11.GL_QUADS;
	}
	

	public void RebuildMesh(float startX, float startY, float startZ) {
		VBOColorHandle = GL15.glGenBuffers();
		VBOVertexHandle = GL15.glGenBuffers();
		VBONormalHandle = GL15.glGenBuffers();
		long time = System.nanoTime();
		FloatBuffer VertexPositionData = BufferUtils
				.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
		FloatBuffer VertexColorData = BufferUtils.createFloatBuffer((CHUNK_SIZE
				* CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
		FloatBuffer VertexNormalData = BufferUtils.createFloatBuffer((CHUNK_SIZE
				* CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
		System.out.print((CHUNK_SIZE ^ 3) * 6 * 12);
		for (float x = 0; x < CHUNK_SIZE; x +=1) {
			for (float y = 0; y < CHUNK_SIZE; y += 1) {
				for (float z = 0; z < CHUNK_SIZE; z += 1) {
					
					
					if(Blocks[(int) x][(int) y][(int) z].GetID()!=0){
					
					VertexPositionData.put(CreateCube((float) startX + x
							* CUBE_LENGTH, (float) startY + y * CUBE_LENGTH,
							(float) startZ + z * CUBE_LENGTH));
					 VertexColorData.put(CreateCubeVertexCol(GetCubeColor(Blocks[(int)
					 (x-startX)][(int) (y-startY)][(int) (z-startZ)])));
					 VertexNormalData.put(GetNormalVector());
					
					}
					
				}
			}
			
		}
		System.out.println((System.nanoTime()-time));
		
		// System.out.print(""+x+" "+y+" "+z);
		VertexColorData.flip();
		VertexPositionData.flip();
		VertexNormalData.flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOVertexHandle);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, VertexPositionData,
				GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOColorHandle);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, VertexColorData,
				GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBONormalHandle);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, VertexNormalData,
				GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

	}
	

	private void RebuildCustomMesh(float startX, float startY, float startZ,
			int xSize, int ySize, int zSize, int visibleBlocks) {
		// VBOColorHandle = 0;
		// VBOVertexHandle = 0;
		// VBONormalHandle = 0;
		VBOColorHandle = GL15.glGenBuffers();
		VBOVertexHandle = GL15.glGenBuffers();
		VBONormalHandle = GL15.glGenBuffers();

		FloatBuffer VertexPositionData = BufferUtils
				.createFloatBuffer(visibleBlocks * 6 * 12);
		FloatBuffer VertexColorData = BufferUtils
				.createFloatBuffer(visibleBlocks * 6 * 12);
		FloatBuffer VertexNormalData = BufferUtils
				.createFloatBuffer(visibleBlocks * 6 * 12);

		// Iterates through the blocks 3D array

		for (int x = 0; x < xSize; x++) {
			for (int y = 0; y < ySize; y++) {
				for (int z = 0; z < zSize; z++) {

					// If the block is not of type default (ID!=0)
					if (Blocks[(int) x][(int) y][(int) z].isVisible()) {

						VertexPositionData.put(CreateCube((float) startX + x
								* CUBE_LENGTH,
								(float) startY + y * CUBE_LENGTH,
								(float) startZ + z * CUBE_LENGTH));
						// Gets a repeated color value dependent on the block ID
						VertexColorData
								.put(CreateCubeVertexCol(GetCubeColor(Blocks[(int) (x - startX)][(int) (y - startY)][(int) (z - startZ)])));
						VertexNormalData.put(GetNormalVector());

					}
				}
			}

		}

		// Prepare to write handle data
		VertexColorData.flip();
		VertexPositionData.flip();
		VertexNormalData.flip();
		// Write to the handles
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOVertexHandle);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, VertexPositionData,
				GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOColorHandle);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, VertexColorData,
				GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBONormalHandle);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, VertexNormalData,
				GL15.GL_STATIC_DRAW);
		// Unbinds the buffer
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

	}

	// RETURNS color data for a cube
	private float[] CreateCubeVertexCol(float[] CubeColorArray) {
		float[] cubeColors = new float[CubeColorArray.length * 4 * 6];
		// Takes a single point's color and returns a new array with the data of
		// an entire cube's points (duplicates the
		// single point)
		for (int i = 0; i < cubeColors.length; i++) {
			cubeColors[i] = CubeColorArray[i % CubeColorArray.length];
		}
		return cubeColors;
	}

	// RETURNS position data for a cube
	public static float[] CreateCube(float x, float y, float z) {
		int offset = CUBE_LENGTH / 2;
		return new float[] {
				// BOTTOM QUAD(DOWN=+Y)
				x + offset, y + offset,
				z,
				x - offset,
				y + offset,
				z,
				x - offset,
				y + offset,
				z - CUBE_LENGTH,
				x + offset,
				y + offset,
				z - CUBE_LENGTH,
				// TOP!
				x + offset, y - offset, z - CUBE_LENGTH, x - offset,
				y - offset,
				z - CUBE_LENGTH,
				x - offset,
				y - offset,
				z,
				x + offset,
				y - offset,
				z,
				// FRONT QUAD
				x + offset, y + offset, z - CUBE_LENGTH, x - offset,
				y + offset, z - CUBE_LENGTH, x - offset,
				y - offset,
				z - CUBE_LENGTH,
				x + offset,
				y - offset,
				z - CUBE_LENGTH,
				// BACK QUAD
				x + offset, y - offset, z, x - offset, y - offset, z,
				x - offset, y + offset, z,
				x + offset,
				y + offset,
				z,
				// LEFT QUAD
				x - offset, y + offset, z - CUBE_LENGTH, x - offset,
				y + offset, z, x - offset, y - offset, z, x - offset,
				y - offset,
				z - CUBE_LENGTH,
				// RIGHT QUAD
				x + offset, y + offset, z, x + offset, y + offset,
				z - CUBE_LENGTH, x + offset, y - offset, z - CUBE_LENGTH,
				x + offset, y - offset, z };

	}

	// RETURNS color data for a point dependent on the type (ID) of a block
	private float[] GetCubeColor(Block block) {
		switch (block.GetID()) {
		case 0:
			// AIR
			return new float[] { 0.5f, 0.5f, 0.5f };
		case 1:
			// GRASS
			return new float[] { 0, 1, 0 };

		case 2:
			// DIRT
			return new float[] { 1, 0.5f, 0 };
		case 3:
			// WATER
			return new float[] { 0, 0f, 1f };
		}
		// WHITE (ID not linked to color)
		return new float[] { 0, 0, 0 };
	}

	// RETURNS the normal data for a cube
	private float[] GetNormalVector() {
		return new float[] {
				// BOTTOM
				0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0,
				// TOP
				0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0,
				// FRONT
				0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1,
				// BOTTOM
				0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1,
				// LEFT QUAD
				1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0,
				// RIGHT QUAD
				-1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0, };
	}
}
