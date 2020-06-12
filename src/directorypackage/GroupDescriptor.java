package directorypackage;
import java.nio.*;

public class GroupDescriptor {
	private int blockBitmapPointer;
	private int inodeBitmapPointer;
	private int inodeTablePointer;
	private short freeBlocks;
	private short freeInodes;
	private short usedDirectories;
	private ByteBuffer buff;

	/**
	 * Constructor Providing 32 bytes groupdesciptor
	 * @param bytes Array of bytes that make up the Group Descriptor
	 */
	public GroupDescriptor(byte[] bytes) {
		buff = ByteBuffer.wrap(bytes);
		buff.order(ByteOrder.LITTLE_ENDIAN);
		
		//initializing the varialbes with the approriate values
		blockBitmapPointer = buff.getInt(0);
		inodeBitmapPointer = buff.getInt(4);
		inodeTablePointer = buff.getInt(8);
		freeBlocks = buff.getShort(12);
		freeInodes = buff.getShort(14);
		usedDirectories = buff.getShort(16);
	}

	/**
	 * Get method that returns the pointer to the block group's Block bitmap table
	 * @return Block Bitmap Table Pointer
	 */
	public int getBlockBitmapPointer() {
		return blockBitmapPointer;
	}

	/**
	 * Get method that Returns the pointer to the block group's Inode bitmap table
	 * @return Inode Bitmap Table Pointer
	 */
	public int getInodeBitmapPointer() {
		return inodeBitmapPointer;
	}

	/**
	 *  Get method that Returns the pointer to the block group's Inode Table
	 * @return Inode Table Pointer
	 */
	public int getInodeTablePointer() {
		return inodeTablePointer;
	}

	/**
	 * Get method that Returns the amount of free blocks in the group
	 * @return Free Blocks
	 */
	public short getFreeBlocks() {
		return freeBlocks;
	}

	/**
	 *  Get method that Returns the amount of free Inodes in the group
	 * @return Free Inodes
	 */
	public short getFreeInodes() {
		return freeInodes;
	}

	/**
	 * Get method that Returns the amount of used directories in the group
	 * @return Used Directories
	 */
	public short getUsedDirectories() {
		return usedDirectories;
	}
}