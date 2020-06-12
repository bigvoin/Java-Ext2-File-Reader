package directorypackage;
public class Helper {
	/**
	 * Takes an array of bytes, converts it to hex and relevant characters, then prints them
	 * @param bytes Array of bytes to be printed
	 */
	public static void dumpHexBytes(byte[] bytes) 
	{
		int length = bytes.length + (16 - (bytes.length % 16)); 
        for(int i = 0; i < length; i++) {

            if(i < bytes.length) {
                System.out.printf("%02x", bytes[i]);
            }
            else {
                System.out.print("XX");
            }

            if((i + 1) % 2 == 0) System.out.print(" "); 
            if((i + 1) % 8 == 0) System.out.print("| ");

            if((i + 1) % 16 == 0) {
                for(int j = i - 15; j <= i; j++) {
                    if(j < bytes.length) {
                        // Checks ASCII range otherwise prints '-'
                        if (bytes[j] > 31 && bytes[j] < 127) {
                            System.out.printf("%c", bytes[j]);
                        } else {
                            System.out.print("-");
                        }
                    }
                    else {
                        System.out.print("X");
                    }

                    if((j + 1) % 8 == 0) System.out.print(" | ");
                }
                System.out.println();
            }
        }	
	}
/**
 * Prits the content of the groupDesciptor
 * @param descriptor	the given groupDescriptor
 */
	public static void printGroupDescriptor(GroupDescriptor descriptor) {
		System.out.println("Block Bitmap Pointer: " + descriptor.getBlockBitmapPointer());
		System.out.println("Inode Bitmap Pointer: " + descriptor.getInodeBitmapPointer());
		System.out.println("Inode Table Pointer: " + descriptor.getInodeTablePointer());
		System.out.println("Free Blocks: " + descriptor.getFreeBlocks());
		System.out.println("Free Inodes: " + descriptor.getFreeInodes());
		System.out.println("Used Directories: " + descriptor.getUsedDirectories());
	}
	/**
	 * Prints the superblock
	 * @param superBlock The SuperBlock
	 */
	
	public static void printSuperBlock(SuperBlock superBlock) {
		System.out.println("Magic Number: " + superBlock.getMagicNumber());
		System.out.println("Inodes: " + superBlock.getInodes());
		System.out.println("Blocks:  " + superBlock.getBlocks());
		System.out.println("Block Size:  " + superBlock.getBlockSize());
		System.out.println("Blocks per Group:  " + superBlock.getGroupBlocks());
		System.out.println("Inodes per Group:  " + superBlock.getGroupInodes());
		System.out.println("Inode Size: " + superBlock.getInodeSize());
		System.out.println("Volume Label: " + superBlock.getVolumeLabel());
	}
	/**
	 * Lists the files of a directory 
	 * @param files An array of FileInfo describing the files
	 */
	public static void list(FileInfo[] files) {
		System.out.println("---------------------------------------------------------------------------------");
		System.out.println("|"+" Permissions:"+"|"+" Number of links:"+"|"+" User ID:"+"|"+" Group ID:"+"|"+" Size:"+"|"+" Last modif:"+"|"+" name:"+"|");
		System.out.println("---------------------------------------------------------------------------------");
		for (int i = 0; i < files.length; i++)  {
			String listing = files[i].getMode() + " " + files[i].getLinks() + " " + files[i].getOwnerID() + " " + files[i].getGroupID() + " " + files[i].getSize(); 
			listing = listing + " " + files[i].getModified().toString() + " " + files[i].getName();
			System.out.println(listing);
		}
	}
}