package directorypackage;
import java.util.*;

public class FileInfo {

	private String filename;
	private int inodePointer;
	private Inode node;

	/**
	 * Provides a file information
	 * @param inode The inode of the file
	 * @param name The name of the file
	 * @param ptr Pointer to the inode of the file
	 */
	public FileInfo(Inode inode, String name, int pointer) {
		node = inode;
		filename = name;
		inodePointer = pointer;
	}

	/**
	 * Get method that returns the permissions of he file
	 * @return The filemode
	 */
	public String getMode() {
		return node.getFileMode();
	}

	/**
	 * Get method that returns the amount of hard links to the file
	 * @return Hard link count
	 */
	public int getLinks() {
		return node.getHardLinks();
	}


	/**
	 * Get method that returns the ID of the owner of the file
	 * @return Owner ID
	 */
	public String getOwnerID() {
		return node.getUserID();
	}

	/**
	 * Get method that returns the ID of the gorup of the file
	 * @return Group ID
	 */
	public String getGroupID() {
		return node.getGroupID();
	}

	/**
	 * Get method that returns the size of the file in bytes
	 * @return Filesize
	 */
	public long getSize() {
		return node.getFileSize();
	}

	/**
	 * Get method that returns the date the file was modified
	 * @return Last Modified Time
	 */
	public Date getModified() {
		return node.getLastModified();
	}

	/**
	 * Get method that returns the name of the file
	 * @return Filename
	 */
	public String getName() {
		return filename;
	}

	/**
	 * Get method that returns the pointer to the Inode of the file 
	 * @return Inode pointer
	 */
	public int getInodePtr() {
		return inodePointer;
	}
}