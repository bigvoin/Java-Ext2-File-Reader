package directorypackage;
import java.io.*;
import java.nio.*;

public class Volume {

	//Declaing Variables
	private String volumeName;
	private SuperBlock superBlock;
	private final long blockSize;
	private final long inodeSize;
	/**
	 * Constructor that will create a volume from th given path
	 * @param filename path for the filesystem
	 */
	public Volume(String pathName) { 
		volumeName = pathName;
		superBlock = new SuperBlock(getBytes(1024, 2048)); // Initializing the superblock and the bytes
		byte[] d = new byte[1024];
		ByteBuffer buff = ByteBuffer.wrap(d);
		buff.order(ByteOrder.LITTLE_ENDIAN);

		blockSize = 1024 * (long)Math.pow(2, superBlock.getBlockSize()); // calculating the exact blocksize and the inodesize
		inodeSize = 0xFFFFFFFF & (long)superBlock.getInodeSize();
	
	}

	/**
	 * Method that is returning bytes from start and end location
	 * @param start The byte to start reading on
	 * @param end The byte to finish reading on
	 * @return The array of read bytes
	 */
	public byte[] getBytes(long start, long end) {
		byte[] bytes;
		try {
			final int length = (int) (end - start); 
			bytes = new byte[length];
			RandomAccessFile file = new RandomAccessFile(volumeName, "r");
			file.seek(start);
			for(int i = 0; i < length; i++) { // reading the bytes one by one with this loop
			bytes[i] = file.readByte(); 
		}

		} catch (IOException e) { // otherwise throw an exception
			System.out.println("IO Exception:");
			bytes = new byte[10];
			bytes[1] = 0; 
		}
		return bytes;
	}
	
	/**
	 * Get method that Returns the SuperBlock currently in use on the volume
	 * @return The volume's SuperBlock instance
	 */
	public SuperBlock getSuperBlock() {
		return superBlock;
	}
	
	/**
	 * Get method that Returns the blocksize
	 * @return the blocksize of the volume
	 */
	public long getBlockSize()
	{
		return blockSize;
	}

	/**
	 * Get method that Returns the BootOffset
	 * @return the exact BootOffset in long
	 */
	public long getBootOffset()
	{
		return 1024L;
	}

	/**
	 * Get method that that Returns the BlockDescriptorSize 
	 * @return the exact value of 32L in long
	 */
	public long getBlockDescribtorSize()
	{
		return 32L;
	}

	/**
	 * Get method that Returns the inodesize
	 * @return the inode size of this volume
	 */
	public long getInodeSize()
	{
		return inodeSize;
	}

	/**
	 * Get method that Returns the byte location of a block on the volume, in bytes
	 * @param block A pointer
	 * @return The location of a block
	 */
	
	public long getBlockLocation(int block) {
		return block * blockSize;
	}

	/**
	 * Get method that Returns Block Group Descriptor for a given Block Group
	 * @param blockGroup The Block Group of the descriptor
	 * @return The GroupDescriptor of the given Block Group
	 */
	
	public GroupDescriptor getDescriptor(int blockGroup) {
		long position = 1024L + blockSize + (32 * blockGroup); //from the Descriptor Table
		long temp = position + 32L;
		GroupDescriptor groupDescriptor = new GroupDescriptor(getBytes(position, temp));
		return groupDescriptor; 
	}

	/**
	 * Get method that Returns a given Inode, given the pointer index of it
	 * @param id The index for the Inode
	 * @return The Inode matching the idex
	 */
	
	public Inode getInode(int idex) {
		int blockGroup = idex / superBlock.getGroupInodes();
		idex = idex % superBlock.getGroupInodes();
		long location = getBlockLocation(getDescriptor(blockGroup).getInodeTablePointer()) + ((idex - 1) * inodeSize);
		Inode inode = new Inode(getBytes(location, location+inodeSize));
		return inode;
	}

	/**
	 * Get method that Returns the Root directory of this filesystem
	 * @return the inode with index 2
	 */
	public Inode getRootDirectory()
	{
		Inode temp = getInode(2);
		return temp;
	}

	/**
	 * Get method that Returns the direct  limit
	 * @return the limit
	 */
	 public long getDirectLimit()
	 {
		 return blockSize*12;
	 }
	 
	 /**
	  * Get method that Returns the Indirect  limit
	  * @return the limit
	  */
	 public long getIndirectLimit()
	 {
		 return blockSize*(blockSize/4);
	 }

	 /**
	  * Get method that Returns the Double indirect limit
	  * @return the limit
	  */
	 public long getDoubleIndirectLimit()
	 {
		return blockSize*(blockSize/4)*blockSize;
	 }
	 
	 /**
	  * Get method that Returns the Triple indirect limit
	  * @return limit
	  */
	 public long getTripleIndirectLimit()
	 {
		 return blockSize*(blockSize/4)*blockSize*blockSize;
	 }

	 
	 /**
	  * Get method that Returns a single byte from the direct pointer 
	  * @param position of the pointer 
	  * @param inode the inode of the volume
	  * @return a single byte
	  */
 	public byte getBlockFromDirectData(long position, Inode inode) {
 		int block = (int)(position/blockSize);
 		int pointer = inode.getBlockPointers()[block];

 		if(pointer == 0) {
 			return 0;
 		}

		 //Calculation and read - return
 		long newLocation = getBlockLocation(inode.getBlockPointers()[block]) + (position % blockSize); 
 		return getBytes(newLocation, newLocation+1)[0]; 
 	}


	  /**
	   * Get method that Returns a pointer, this method takes a pointer to indirect tab
	   * @param pointer to the indirect inode table 
	   * @param block a block pointer in the table
	   * @return the pointer
	   */
 	public int getBlockFromIndirect(int pointer, long block) { 
 		long pointerBlock = getBlockLocation(pointer);
 		long pointLocation = pointerBlock + (block * 4);
		 ByteBuffer buf = ByteBuffer.wrap(getBytes(pointLocation, pointLocation+4));
		 buf.order(ByteOrder.LITTLE_ENDIAN);
		 int blockLocation = buf.getInt();

 		return blockLocation;
 	}

	 /**
	  * Get method that Returns a byte from the inode table
	  * @param position position
	  * @param inode and an inode
	  * @return the value from the indirect table in byte
	  */
 	public byte getBlockFromIndirect(long position, Inode inode) {
 		position = position - blockSize * 12; // Offset from Direct
 		int directBlockPointer = getBlockFromIndirect(inode.getIndirectPointer(), position / blockSize);

		 //Calculation for the pointer location and the block location then reading the bytes and returning value in byte
 		long block = getBlockLocation(directBlockPointer); 
 		long byteLocation = block + (position % blockSize); 
 		return getBytes(byteLocation, byteLocation+1)[0];
 	}

	 
	 /**
	  * Get method that Returns a byte from the double indirect indoe table
	  * @param position position
	  * @param inode inode
	  * @return the value from the double indirect table in byte
	  */
 	public byte getBlockFromDoubleIndirect(long position, Inode inode) {
 		position = position - (blockSize * 12 + blockSize*(blockSize/4)); // Offset from Indirect

		 //Calculation for the indirect table and the direct block
 		int indirectBlockPointer = getBlockFromIndirect(inode.getDoubleIndirectPointer(), position / blockSize*(blockSize/4));
 		long location = position % blockSize*(blockSize/4); 
 		int directBlockPointer = getBlockFromIndirect(indirectBlockPointer, location / blockSize); 


 		Long newBlock = getBlockLocation(directBlockPointer);

 		long byteLocation = newBlock + (position % blockSize);
 		return getBytes(byteLocation, byteLocation+1)[0]; // reading and returning the value in byte
 	}
	 
	 /**
	  * Get method that Returns a byte from the triple indirect inode table
	  * @param position position
	  * @param inode inode 
	  * @return the value from the triple indirect table in byte
	  */
  	public byte getBlockFromTripleIndirect(long position, Inode inode) {
  		position = position - (blockSize * 12 + blockSize*(blockSize/4) + blockSize*(blockSize/4)*blockSize); // Offset from Double Indirect

		  // Calculation for the double indirect table and the indirect table and geting the direct block from the inode
  		int doubleIndPointer = getBlockFromIndirect(inode.getTripleIndirectPointer(), position / blockSize*(blockSize/4)*blockSize);
  		long first = position % blockSize*(blockSize/4)*blockSize;
 		int indirectBlockPtr = getBlockFromIndirect(doubleIndPointer, first / blockSize*(blockSize/4));
 		long second = first % blockSize*(blockSize/4); // Calculate for indirect table

 		int directBlockPointer = getBlockFromIndirect(indirectBlockPtr, second / blockSize); 

 		Long block = getBlockLocation(directBlockPointer);

 		long byteLocation = block + (position % blockSize);
 		return getBytes(byteLocation, byteLocation+1)[0]; // reading and returning the value in byte
	 }
	 
	 	 
	 
}