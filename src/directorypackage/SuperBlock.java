package directorypackage;
import java.nio.*;

public class SuperBlock {
	//declaring the variables approriate for the superblock
	private short magicNumber;
	private int inodes;
	private int blocks;
	private int blockSize;
	private int groupBlocks;
	private int groupInodes;
	private int inodeSize;
	private String volumeName;
	private ByteBuffer buff;

	/**
	 * Constructor, that will create and store the information of the superblock represented by bytes
	 * @param bytes Block of 1024 bytes
	 */
	public SuperBlock(byte[] bytes) {
		buff = ByteBuffer.wrap(bytes);
		 buff.order(ByteOrder.LITTLE_ENDIAN);
		
		 //Initializing the variables
		 magicNumber = buff.getShort(56);
		 inodes = buff.getInt(0);
		 blocks = buff.getInt(4);
		 blockSize = buff.getInt(24);
		 groupBlocks = buff.getInt(32);
		 groupInodes = buff.getInt(40);
		 inodeSize = buff.getInt(88);

		 //Intializing the volume name
		 byte[] char_bytes = new byte[16];

		 for(int i = 0; i< 16; i++) {
			 //Get the characters 1 by 1
			 char_bytes[i]=buff.get(120 + i);
		 }
 
		 //converting the bytes that contain the name to a String
		 volumeName = new String(char_bytes);
 
	}

	

	/**
	 * Get method that Returns the magic number of the SuperBlock
	 * @return Magic number
	 */
	public short getMagicNumber() {
		return magicNumber;
	}

	/**
	 *	Get method that Returns the total number of inodes in the volume
	 * @return Inode count
	 */
	public int getInodes() {
		return inodes;
	}

	/**
	 *  Get method that Returns the total number of blocks in the volume
	 * @return Block count
	 */
	public int getBlocks() {
		return blocks;
	}

	/**
	 * Get method that Returns the size of the blocks in the volume.
	 * @return Size n. Where Block size is 1024 * 2^n
	 */
	public int getBlockSize() {
		return blockSize; 
	}

	/**
	 * Get method that Returns the amount of blocks in each block group
	 * @return Block group blocks
	 */
	public int getGroupBlocks() {
		return groupBlocks;
	}

	/**
	 * Get method that Returns the amount of inodes in each block group
	 * @return Block group inodes
	 */
	public int getGroupInodes() {
		return groupInodes;
	}

	/**
	 *  Get method that Returns the size of inodes in bytes
	 * @return Block group blocks
	 */
	public int getInodeSize() {
		return inodeSize;
	}

	/**
	 * Get method that Returns the label for the volume
	 * @return Volume Label
	 */
	public String getVolumeLabel() {
		return volumeName;
	}
}