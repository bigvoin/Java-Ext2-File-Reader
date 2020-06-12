package directorypackage;
import java.io.IOException;
 
public class Ext2File {

	//declaring variables
	private long position;
	private Volume volume;
	private Inode inode;
	public static long size;
	//private Directory dir;
	

	/**
	 * This method creates an array of bytes from the volume.
	 * @param vol The Volume the file resides in
	 * @param path The path that leads to the file
	 */
	public Ext2File(Volume volume, String path) {
		this.volume = volume;
		Inode current = volume.getInode(2);
		if(path.equals(""))
		{
			inode = current;
		}
		if(path.charAt(0)=='/')//check if the first character is / and remove it from the string
		{
			path = path.substring(1);
		}
		String[] names = path.split("/");
		Directory dir = new Directory(volume.getInode(2),volume);
		for (int i = 0; i < names.length; i++) {
			FileInfo[] files = dir.getFileInfo(); // Get the information for all the files from the directory
			for (int j = 0; j < files.length; j++) { // Check all files in the directory
				if(files[j].getName().equals(names[i])) { // Check if we found a filename match
					if(i < names.length - 1) { // If we're not at the destination file
						dir = new Directory(volume.getInode(files[j].getInodePtr()), volume); // Switch directory
					} else{ 
						current = volume.getInode(files[j].getInodePtr());//returns the current inode
						inode=current;
					}
					
				}
			}
		}
		inode=current;
				
	}

	/**
	 * Method that creates an array of bytes from the file, given start byte and length.
	 * @param startByte The byte to start reading on
	 * @param length The amount of bytes to read
	 * @return The array of read bytes from the file
	 */
	public byte[] read(long startByte, long length) {
		long newLength = length - startByte;
		seek(startByte);
		return read(newLength);
	}

	/**
	 * Method creating  an array of bytes from the file at the current pointer position, given the length
	 * @param length The amount of bytes to read
	 * @return The array of read bytes from the file
	 */
	public byte[] read(long length) {
		long current = position + length; 
		try{
		if(current >= inode.getFileSize() && (position <=0) && (length < 0)) // check for the limits of the file
		{
			throw new IOException("Error, Outside of the file bounds, the file size is: "+inode.getFileSize());
		}
	}catch(IOException e)
	{
		e.printStackTrace();
	}
			size = inode.getFileSize();
			long c = (inode.getFileSize() - position < length) ? (inode.getFileSize() - position) : length;
			c = c > size ? size : c; // if c > size c=size else c =c 
			byte[] bytes;
			int temp = (int) (position - position+length);
		byte[] b = new byte[temp];
		for (int i = 0; i < temp; i++) { //Assemble array of bytes
			if(position + i < volume.getDirectLimit()) { //Call relevant function for the current byte requested in the file
				b[i]= volume.getBlockFromDirectData(position + i, inode);
			} else if(position < (volume.getIndirectLimit() + volume.getDirectLimit())) {
				b[i]= volume.getBlockFromIndirect(position, inode);
			} else if(position < (volume.getDoubleIndirectLimit() + volume.getIndirectLimit() + volume.getDirectLimit())) {
				b[i]= volume.getBlockFromDoubleIndirect(position, inode);
			} else if(position < (volume.getDoubleIndirectLimit() + volume.getDoubleIndirectLimit() + volume.getIndirectLimit() + volume.getDirectLimit())) {
				b[i]= volume.getBlockFromTripleIndirect(position, inode);
			} else{
				System.out.println("Error!, the byte is too large");
			}
		}
		bytes = b;



			position += c;
			return bytes;
		
	}

	/**
	 * Method tha is placing a pointer at the specificed place in the file
	 * @param place The byte to place the pointer on
	 */
	public void seek(long place) {
		position = place;
	}

	/**
	* Returns the current position
	* @return Pointer position
	*/
	public long position() {
		return position;
	}

	/**
	* Returns the size of the file
	* @return Filesize
	*/
	public long size() {
		return inode.getFileSize();
	}

	/**
	* Returns the Inode of the file
	* @return The file's Inode
	*/
	public Inode getInode() {
		return inode;
	}
	
}