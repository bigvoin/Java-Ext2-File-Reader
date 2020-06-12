package directorypackage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Directory {
	private Volume vol;
	private Inode inode;
	//private ByteBuffer buff;
	
	/**
	 * Constructor , which create a new directory 
	 * with given inode and volume
	 * @param i The inode of the  new directory
	 * @param v The volume that directory have
	 */
	public Directory(Inode i, Volume v) {
		inode = i;
		vol = v;
		
	}

	/**
	 * Constructor for directory with volume and a path
	 * @param v The volume that this directory has
	 * @param path The file path that leads to this directory
	 */
	public Directory(Volume v, String path) {
		vol = v;
		Inode current = vol.getInode(2);
		if(path.equals(""))
		{
			inode = current;
		}
		if(path.charAt(0)=='/')
		{
			path = path.substring(1);
		}
		String[] names = path.split("/");
		Directory dir = new Directory(vol.getInode(2),vol);
		for (int i = 0; i < names.length; i++) {
			FileInfo[] files = dir.getFileInfo(); // Get the information for all the files from the current direcotry
			for (int j = 0; j < files.length; j++) { // Check all files in the directory by the lenght of the directoy
				if(files[j].getName().equals(names[i])) { // Check if we found a filename match
					if(i < names.length - 1) { 
						dir = new Directory(vol.getInode(files[j].getInodePtr()), vol); // create a new directory
					} else{ 
						current = vol.getInode(files[j].getInodePtr());// in this case we get the inode of the directory
						inode=current;
					}
					
				}
			}
		}
		inode=current;

		
	}

	/**
	 * method, which returns the length of the amount of files in array
	 * 
	 * @return Infromation about all the files in the directory
	 */
	public FileInfo[] getFileInfo() {
		int length;

		int temp = 0;
		for(int count = 0;count < inode.getFileSize();) { //Follow the pointers and increment the counter
			short point = toShort(readFromFile(inode, count+4, count+6)); 
			temp++;
			count = count + point;
		}
		length = temp;
	
		FileInfo[] files = new FileInfo[length];

		int count = 0;
		for(int i = 0; i < length; i++) {
			ByteBuffer buf = ByteBuffer.wrap(readFromFile(inode, count, count+4));
			buf.order(ByteOrder.LITTLE_ENDIAN);
			buf.getInt();
			int fileInodePtr = toInt(readFromFile(inode, count, count+4));
			Inode fileInode = vol.getInode(fileInodePtr);
			String fileName = "";
			byte nameLength = readFromFile(inode, count+6, count+7)[0];// implement readFromFile method and getBytes method from 
			//volume here GL
			for (int j = 0; j < nameLength; j++) { // Reading in the name of the file one character at a time
				fileName = fileName + Character.toString((char) (readFromFile(inode, count+8+j, count+9+j)[0] & 0xFF));
			}
			
			files[i] = new FileInfo(fileInode, fileName, fileInodePtr);
			short point = toShort(readFromFile(inode, count+4, count+6));
			count = count + point;
		}
		return files;
		
	}

	/**
	 * creates a Bytebuffer with bytes and order it in Little endian format
	 * @param b the bytes
	 * @return the buffer
	 */
	public ByteBuffer create(byte[] b)
	{
		ByteBuffer buff = ByteBuffer.wrap(b);
		buff.order(ByteOrder.LITTLE_ENDIAN);
		return buff;
	}
	/**
	 * transfer the bytes into short value
	 * @param b the current bytes
	 * @return the new value in short
	 */
	public short toShort(byte[] b)
	{
		return create(b).getShort();
	}

	/**
	 * Transfer the bytes into integer value
	 * @param b
	 * @return
	 */
	public int toInt(byte[] b)
	{
		return create(b).getInt();
	}

	/**
	 * This method will return array of bytes with given inode start value and end value 
	 * @param inode the node of the file
	 * @param start the start of the file 
	 * @param end  the end of the file when reading
	 * @return 	the bytes 
	 */
	public byte[] readFromFile(Inode inode, long start, long end) {
		int length = (int) (end - start);
		byte[] bytes = new byte[length]; 
		for (int i = 0; i < length; i++) { // goes trough the array of bytes
			if(start + i < vol.getDirectLimit()) { //Call relevant methods, for direc/indirect/doubleindirect and tripleindirect datas
				bytes[i]= vol.getBlockFromDirectData(start + i, inode);
			} else if(start < (vol.getIndirectLimit() + vol.getDirectLimit())) {
				bytes[i]= vol.getBlockFromIndirect(start, inode);
			} else if(start < (vol.getDoubleIndirectLimit() + vol.getIndirectLimit() + vol.getDirectLimit())) {
				bytes[i]= vol.getBlockFromDoubleIndirect(start, inode);
			} else if(start < (vol.getDoubleIndirectLimit() + vol.getDoubleIndirectLimit() + vol.getIndirectLimit() + vol.getDirectLimit())) {
				bytes[i]= vol.getBlockFromTripleIndirect(start, inode);
			} else{
				bytes[0] = 0;
				System.out.println("Error, the byte is too large");
				return bytes;
			}
		}
		return bytes;
	}
} 