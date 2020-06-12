package directorypackage;
import java.nio.*;
import java.util.*;

public class Inode {
	//The data of the inode in the right order
	private short fileMode;
	private short userID;
	private long fileSize;
	private int lastAccess;
	private int creationTime;
	private int lastModified;
	private int deletedTime; 
	private short groupID;
	private short hardLinks;
	private int[] blockPointers;
	private int blockPointersNumber = 12; // each inode points to 12 direct data blocks
	private int indirectPointer;
	private int doubleIndirectPointer;
	private int tripleIndirectPointer;
	private int filesizeupper;
	private int filesizelower;
	private ByteBuffer buff;

	static final int IFSCK = 0xC000;  // Socket file mode
    static final int IFLNK = 0xA000;  // Symbolic Link file mode
    static final int IFREG = 0x8000;  // Regular File file mode
    static final int IFBLK = 0x6000;  // Block Device file mode
    static final int IFDIR = 0x4000;  // Directory file mode
    static final int IFCHR = 0x2000;  // Character Device file mode
    static final int IFIFO = 0x1000;  // FIFO file mode
    static final int ISUID = 0x0800;  // Set process User ID file mode
    static final int ISGID = 0x0400;  // Set process Group ID file mode
    static final int ISVTX = 0x0200;  // Sticky bit file mode
    static final int IRUSR = 0x0100;  // User readBytes file mode
    static final int IWUSR = 0x0080;  // User write file mode
    static final int IXUSR = 0x0040;  // User execute file mode
    static final int IRGRP = 0x0020;  // Group readBytes file mode
    static final int IWGRP = 0x0010;  // Group write file mode
    static final int IXGRP = 0x0008;  // Group execute file mode
    static final int IROTH = 0x0004;  // Others readBytes file mode
    static final int IWOTH = 0x0002;  // Others write file mode
    static final int IXOTH = 0x0001;  // Others execute file mode

	/**
	 * Constructor, creating an inode
	 * @param bytes Array of bytes containing the inode data.
	 */
	public Inode(byte[] bytes) {//128 bytes
		buff = ByteBuffer.wrap(bytes);
		buff.order(ByteOrder.LITTLE_ENDIAN);
		blockPointers = new int[blockPointersNumber];
		fileMode = buff.getShort(0);
	
		userID = buff.getShort(2);

		filesizeupper = buff.getInt(4);
		filesizelower = buff.getInt(108);

		byte[] sizeArray = {bytes[4], bytes[5], bytes[6], bytes[7],  bytes[108], bytes[109], bytes[110], bytes[111]};
		

		ByteBuffer size = ByteBuffer.wrap(sizeArray);
		size.order(ByteOrder.LITTLE_ENDIAN);
		fileSize = size.getLong();// the full size is the combined result of the upper and lower filesize
		//initializing the variables
		lastAccess = buff.getInt(8);
		creationTime = buff.getInt(12);
		lastModified = buff.getInt(16);
		deletedTime = buff.getInt(20);

		groupID = buff.getShort(24);

		hardLinks = buff.getShort(26);

		for(int i = 0; i < blockPointersNumber; i++) {
			blockPointers[i] = buff.getInt(40+(i*4));
		}

		indirectPointer = buff.getInt(88);
		doubleIndirectPointer = buff.getInt(92);
		tripleIndirectPointer = buff.getInt(96);
		
	}

	/**
	 * Method that is returning the right permissions for the inode
	 * @return the permission in string
	 */
	public String getFileMode()
	{
		String permissions = "";

        if(((int)fileMode & IFSCK) == IFSCK)
            permissions = "socket";
        else if(((int)fileMode & IFLNK) == IFLNK)
            permissions = "symbolic link";
        else if(((int)fileMode & IFREG) == IFREG)
            permissions = "-";
        else if(((int)fileMode & IFBLK) == IFBLK)
            permissions = "block device";
        else if(((int)fileMode & IFDIR) == IFDIR)
            permissions = "d";
        else if(((int)fileMode & IFCHR) == IFCHR)
            permissions = "c";
        else if(((int)fileMode & IFIFO) == IFIFO)
            permissions = "fifo";

        //USER GROUP PERMISSIONS read write execute 
        permissions += ((int)fileMode & IRUSR) == IRUSR ? "r" : "-";
        permissions += ((int)fileMode & IWUSR) == IWUSR ? "w" : "-";
        permissions += ((int)fileMode & IXUSR) == IXUSR ? "x" : "-";

        //GROUP PERMISSIONS read write execute
        permissions += ((int)fileMode & IRGRP) == IRGRP ? "r" : "-";
        permissions += ((int)fileMode & IWGRP) == IWGRP ? "w" : "-";
        permissions += ((int)fileMode & IXGRP) == IXGRP ? "x" : "-";

        //OTHER GROUP PERMISSIONS read write execute sticky bit
        permissions += ((int)fileMode & IROTH) == IROTH ? "r" : "-";
        permissions += ((int)fileMode & IWOTH) == IWOTH ? "w" : "-";
        permissions += ((int)fileMode & IXOTH) == IXOTH ? "x" : "-";
        permissions += ((int)fileMode & ISVTX) == ISVTX ? "t" : "";

        return permissions;
	}

	/**
	 * Get method that Returns the User ID
	 * @return User ID
	 */
	public String getUserID() {
		return (userID == 0 ? "root" : userID == 1000 ? "user" : "unknown");
	}

	/**
	 * Get method that Returns the size of the Inode's file
	 * @return File Size
	 */
	public long getFileSize() {
		return fileSize;
	}

	/**
	 * Get method that Returns date of the last access of the file
	 * @return Last Access time
	 */
	public int getLastAccess() {
		return lastAccess;
	}

	/**
	 * Get method that Returns the time the file was created
	 * @return Creation Time
	 */
	public int getCreationTime() {
		return creationTime;
	}

	/**
	 * Get method that Returns the time the file was last modified
	 * @return Last Modified Time
	 */
	public Date getLastModified() {
		return new Date((long)lastModified*1000);
	}

	/**
	 * Get method that Returns the time the file was deleted
	 * @return Deletion Time
	 */
	public int getDeletedTime() {
		return deletedTime;
	}

	/**
	 * Get method that Returns the Group ID
	 * @return Group ID
	 */
	public String getGroupID() {
		return	(groupID == 0 ? "root" : groupID == 1000 ? "group" : "unknown");
	}

	/**
	 * Get method that Returns the amount of hard links to this file
	 * @return Hard Link Count
	 */
	public short getHardLinks() {
		return hardLinks;
	}

	/**
	 *Get method that Returns an array of the first 12 direct block pointers
	 * @return Direct Block Pointers
	 */
	public int[] getBlockPointers() {
		return blockPointers;
	}

	/**
	 * Get method that Returns a block pointer to an indirect block
	 * @return Indirect block pointer
	 */
	public int getIndirectPointer() {
		return indirectPointer;
	}

	/**
	 * Get method that Returns a block pointer to a double indirect block
	 * @return Double indirect block pointer
	 */
	public int getDoubleIndirectPointer() {
		return doubleIndirectPointer;
	}

	/**
	 * Get method that Returns a block pointer to a triple indirect block
	 * @return Triple indirect block pointer
	 */
	public int getTripleIndirectPointer() {
		return tripleIndirectPointer;
	}

}