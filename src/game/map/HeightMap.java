package game.map;

import game.map.Block.BlockType;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.newdawn.slick.opengl.Texture;

/*CLASS used to generate a voxel heightmap from a given texture
 * 
 */
public class HeightMap {
	// Length of a cube
	static final int CUBE_LENGTH = 2;
	// the 3D array of blocks
	private Block[][][] Blocks;
	// Handles for storing the vertex data of the whole 3D array of blocks
	private int VBOVertexHandle;
	private int VBOColorHandle;
	private int VBONormalHandle;
	private int VBOTextureHandle;
	// The position of the start corner of the chunk
	private int StartX, StartY, StartZ;
	// Draw mode for swapping to wireframe, solid by default
	private int DrawMode = GL11.GL_QUADS;
	// How many blocks the chunk stretches in the x,y,z directions; defaults to
	// zero
	//private FloatBuffer VertexPositionData,VertexColorData,VertexNormalData;
	private int BlocksToRender=0;

	// RENDERS the data stored in the handles
	public void Render() {
		GL11.glPushMatrix();
		// Binds the buffers
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOVertexHandle);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0L);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOColorHandle);
		GL11.glColorPointer(3, GL11.GL_FLOAT, 0, 0L);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBONormalHandle);
		GL11.glNormalPointer(GL11.GL_FLOAT, 0, 0L);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOTextureHandle);
		GL11.glTexCoordPointer(2,GL11.GL_FLOAT, 0, 0L);
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

	// No need to update anything in the chunk, stays blank
	public void Update() {

	}

	/*
	 * Could possibly be used to disable/enable lighting for all heightmaps
	 * public static void EnableLighting(boolean enabled){
	 * GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
	 * GL11.glEnable(GL11.GL_COLOR_MATERIAL); GL11.glEnable(GL11.GL_LIGHTING);
	 * GL11.glEnable(GL11.GL_LIGHT0);
	 * 
	 * }
	 */

	// CONSTRUCTOR creates a new heightmap with data from a texture.
	public HeightMap(int startX, int startY, int startZ, Texture texture) {
		StartX = startX;
		StartY = startY;
		StartZ = startZ;
		BuildHeightMap(texture);
		System.out.println("Built new map");

	}
	public void ChangeHeightMap(int startX, int startY, int startZ, Texture texture) {
		StartX = startX;
		StartY = startY;
		StartZ = startZ;
		BuildHeightMap(texture);

	}

	// Divisor to compress the maximum height of 256 downwards, creates smoother
	// heightmaps but loses data
	// as integer division is performed
	private int HeightMapHeightDivisor = 2;
	public void RebuildHeightMap(Texture t,int divisor){
		HeightMapHeightDivisor=divisor;
		BuildHeightMap(t);
	}
	// CREATES the actual block data from a texture
	private void BuildHeightMap(Texture texture) {
		// XSize=0;
		// YSize=0;
		// ZSize=0;
		byte[] bytes = texture.getTextureData();
		int width = texture.getImageWidth();
		int height = texture.getImageHeight();
		int blockHeight = 0;

		// Sets the bounds of the Block chunk according to the image width,
		// height, and the constraints on the length
		Blocks = new Block[width][height][256 / HeightMapHeightDivisor];
		int blockCount = 0;
		try {
			for (int x = 0; x < width; x++) {

				for (int y = 0; y < height; y++) {
					blockHeight = GetHeight(bytes[(y * width
							* (texture.hasAlpha() ? 4 : 3) + x
							* (texture.hasAlpha() ? 4 : 3))]);

					for (int z = 0; z < 256 / HeightMapHeightDivisor; z++) {
						if (z <= blockHeight) {
							if (z <= (256 / HeightMapHeightDivisor) / 10) {
								Blocks[width-x-1][y][z] = new Block(
										BlockType.BlockType_Water);
							} else if (z <= (256 / HeightMapHeightDivisor) / 7) {
								Blocks[width-x-1][y][z] = new Block(
										BlockType.BlockType_Dirt);
							} else {
								Blocks[width-x-1][y][z] = new Block(
										BlockType.BlockType_Grass);
							}
							// blockCount++;

						} else {
							Blocks[width-x-1][y][z] = new Block(
									BlockType.BlockType_Default);
							Blocks[width-x-1][y][z].setVisible(false);
						}
					}

				}

			}

		} catch (Exception e) {
			System.out.println("ERROR: Failed to construct heightmap data");
		}
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				for (int z = 0; z < 256 / HeightMapHeightDivisor; z++) {
					if (Blocks[x][y][z].isVisible()) {
						if (y > 0 && y < height - 1 && z > 0
								&& z < 256 / HeightMapHeightDivisor - 1
								&& x > 0 && x < width - 1) {
							if (Blocks[x][y - 1][z].GetID() != 0
									&& Blocks[x][y + 1][z].GetID() != 0
									&& Blocks[x + 1][y][z].GetID() != 0
									&& Blocks[x - 1][y][z].GetID() != 0
									&& Blocks[x][y][z + 1].GetID() != 0
									&& Blocks[x][y][z - 1].GetID() != 0) {
								Blocks[x][y][z].setVisible(false);
							} else {
								blockCount++;
							}

						} else {
							blockCount++;
						}
					}
				}
			}
		}

		// Calls for the handle data to be created/recreated
		//try{
			RebuildCustomMesh(StartX, StartY, StartZ, width, height,
					256 / HeightMapHeightDivisor, blockCount);
		//}catch(Exception e){
			System.out.println("Building map");
		//}
		
		BlocksToRender=blockCount;
	}

	// SWITCHES the draw mode to what is essentially wireframe mode
	public void RenderMode_Wireframe() {
		DrawMode = GL11.GL_LINE_LOOP;
	}

	// SWITCHES the draw mode to what is essentially draw solid mode
	public void RenderMode_Solid() {
		DrawMode = GL11.GL_QUADS;
	}

	// RETURNS the height (brightness) from greyscale pixel data. Data ranges
	// between -125 -> 125 where -1 is white and
	// 0 is black, if the value is below 0 it's added to 256 so that white
	// becomes 255 and black remains 0 to create a
	// constant scale, it then returns a height that is scaled by the divisor
	private int GetHeight(int i) {
		int returned = i;
		if (returned < 0) {
			returned = 256 + i;

		}
		returned /= HeightMapHeightDivisor;
		return returned;
	}

	// GENERATES the handles, so that the cube can be rendered
	private void RebuildCustomMesh(float startX, float startY, float startZ,
			int xSize, int ySize, int zSize, int visibleBlocks) {
		// VBOColorHandle = 0;
		// VBOVertexHandle = 0;
		// VBONormalHandle = 0;
		VBOColorHandle = GL15.glGenBuffers();
		VBOVertexHandle = GL15.glGenBuffers();
		VBONormalHandle = GL15.glGenBuffers();
		VBOTextureHandle = GL15.glGenBuffers();
		
		FloatBuffer VertexPositionData = BufferUtils
				.createFloatBuffer(visibleBlocks * 6 * 12);
		FloatBuffer VertexColorData = BufferUtils
				.createFloatBuffer(visibleBlocks * 6 * 12);
		FloatBuffer VertexNormalData = BufferUtils
				.createFloatBuffer(visibleBlocks * 6 * 12);
		FloatBuffer VertexTextureData = BufferUtils
				.createFloatBuffer(visibleBlocks * 6 * 12);
		/*
		 * if(VertexPositionData==null){
			VertexPositionData = BufferUtils
					.createFloatBuffer(visibleBlocks * 6 * 12);
			VertexColorData = BufferUtils
					.createFloatBuffer(visibleBlocks * 6 * 12);
			VertexNormalData = BufferUtils
					.createFloatBuffer(visibleBlocks * 6 * 12);
		}else{
			VertexPositionData.clear();
			VertexColorData.clear();
			VertexNormalData.clear();
		}
		 */
		//VertexPositionData.
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
						VertexTextureData.put(GetCubeTextureCoords(Blocks[(int) (x - startX)][(int) (y - startY)][(int) (z - startZ)].GetID()));

					}
				}
			}

		}

		// Prepare to write handle data
		VertexColorData.flip();
		VertexPositionData.flip();
		VertexNormalData.flip();
		VertexTextureData.flip();
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
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOTextureHandle);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, VertexTextureData,
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
	private float[] GetCubeTextureCoords(int id) {
		float[] cubeTexCoords = new float[8*6];
		// Takes a single point's color and returns a new array with the data of
		// an entire cube's points (duplicates the
		// single point)
		for (int i = 0; i < 6; i++) {
			cubeTexCoords[i*8] = GetTexCoordIDX(id, false);
			cubeTexCoords[i*8+1] = GetTexCoordIDY(id, false);
			
			cubeTexCoords[i*8+2] = GetTexCoordIDX(id, true);
			cubeTexCoords[i*8+3] = GetTexCoordIDY(id, false);
			
			cubeTexCoords[i*8+4] = GetTexCoordIDX(id, true);
			cubeTexCoords[i*8+5] = GetTexCoordIDY(id, true);
			
			cubeTexCoords[i*8+6] = GetTexCoordIDX(id, false);
			cubeTexCoords[i*8+7] = GetTexCoordIDY(id, true);
		}
		return cubeTexCoords;
	}
	
	private float GetTexCoordIDX(int id, boolean far){
		//return (far?0.5f:0.25f);
		switch (id) {
		//case 0:
			// AIR
			//return 0;
		case 1:
			// GRASS
			return (far?0.25f:0f);

		case 2:
			// DIRT
			return (far?0.25f:0f);
		case 3:
			// WATER
			return (far?0.5f:0.25f);
		}
		return 0;
	}
	private float GetTexCoordIDY(int id, boolean far){
		//return (far?0.25f:0f);
		switch (id) {
		//case 0:
			// AIR
			//return 0;
		case 1:
			// GRASS
			return (far?0.25f:0f);

		case 2:
			// DIRT
			return (far?0.5f:0.25f);
		case 3:
			// WATER
			return (far?0.5f:0.25f);
		}
		return 0;
		//
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
