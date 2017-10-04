package hp.ca_loop;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author Chupryna-IV
 */
public class TechFile {

    public static void doModify(String add, String fileBck) throws Exception {
        try {

            File file = new File(fileBck);
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            raf.skipBytes((int) raf.length());
            raf.writeBytes(add+"\n");

            raf.close();


        } catch (IOException e) {
            System.out.println("IOException:" + e);
            e.printStackTrace();
        }

    }
}
