import directorypackage.*;
public class Driver {
	/**
	 * Main method printing the final results of the assigmnent
	 * @param args
	 */
	public static void main(String[] args) {
		
		Volume vol = new Volume("ext2fs");
		Ext2File file = new Ext2File(vol, "two-cities");  // printing the content of the two-cities file
		byte buf[ ] = file.read(0L, file.size());
		System.out.format ("%s\n", new String(buf));
		
		/*
		//Testing the dumHexBytes method for debugging, The first block and the second block
			Helper.dumpHexBytes(file.read(0L, 1024L)); //first block
			Helper.dumpHexBytes(file.read(file.size() - 1024L, file.size())); //Second block
		
		// Printing the content of the SuperBlock and the GroupDescriptor
			Helper.printSuperBlock(vol.getSuperBlock()); // content for SuperBlock
			Helper.printGroupDescriptor(vol.getDescriptor(2)); // content of the GroupDescriptor

		//Printing the Root directory of the filesystem
			Directory dir = new Directory(vol.getRootDirectory(), vol);
			Helper.list(dir.getFileInfo());
		
		//Content of the file in a path:  /deep/down/in/the/filesystem/there/lived/a/file
			Volume vol = new Volume("ext2fs");
			Ext2File file = new Ext2File(vol, " /deep/down/in/the/filesystem/there/lived/a/file");
			byte buf[ ] = file.read(0L, file.size());
			System.out.format ("%s\n", new String(buf));

			
		//Content of the directory big-dir
			Directory dir = new Directory(vol, "big-dir");
			Helper.list(dir.getFileInfo());
		
		//Content of the /files directory containing large files
			Directory dir = new Directory(vol, "/files");
			Helper.list(dir.getFileInfo());

		// Content of the small and large direct files 
			Volume vol = new Volume("ext2fs");
			Ext2File file = new Ext2File(vol, "/files/dir-s");
			byte buf[ ] = file.read(0L, file.size());
			System.out.format ("%s\n", new String(buf));

			Volume vol = new Volume("ext2fs");
			Ext2File file = new Ext2File(vol, "/files/dir-e");
			byte buf[ ] = file.read(0L, file.size());
			System.out.format ("%s\n", new String(buf));
			
			Volume vol = new Volume("ext2fs");
			Ext2File file = new Ext2File(vol, "/files/ind-s");
			byte buf[ ] = file.read(0L, file.size());
        	System.out.format ("%s\n", new String(buf));
		*/
	}
}