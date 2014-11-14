package game.map;

public class Block {
	/*CLASS containing information about a block, the type is a enum type that is stored when the block
	 * is created using it's constructor. Only the minimum amount of data (ID) is stored, as the block class
	 * needs to be instantiated many times, as to reduce the amount of memory a block takes up.
	 * */

	//Stores which type of block the object is
	private BlockType Type;
	private boolean Visible=true;
	public enum BlockType {
		BlockType_Default(0),
		BlockType_Grass(1),
		BlockType_Dirt(2),
		BlockType_Water(3), 
		BlockType_Stone(4),
		BlockType_Wood(5),
		BlockType_Sand(6),
		BlockType_NumTypes(7);
		private int BlockID;
		BlockType(int i) {
			BlockID=i;
		}
		public int GetID(){
			return BlockID;
		}
	}
	//CONSTRUCTOR sets type
	public Block(BlockType type){
		Type= type;
	}

	
	//RETURNS the block ID, behaviour and properties of the ID are stored elsewhere
	public int GetID(){
		return Type.GetID();
	}


	public boolean isVisible() {
		return Visible;
	}


	public void setVisible(boolean visible) {
		Visible = visible;
	}
}
