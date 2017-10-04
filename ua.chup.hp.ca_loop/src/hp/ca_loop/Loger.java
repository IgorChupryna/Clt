package hp.ca_loop;

import java.io.File;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 *
 * @author Chupryna-IV
 */
public class Loger {
    public static void addLogData(String DataLog) throws Exception{
        AutoCreInc ca = new AutoCreInc();

        Date curTime = new Date();
        String curStringDate = new SimpleDateFormat("yyyyMMdd").format(curTime);

        File file = new File(ca.getDir_name()+"log_"+curStringDate+".txt");
        RandomAccessFile raf = new RandomAccessFile(file, "rw");

        String curLog = new SimpleDateFormat("[dd/MM/yyyy HH:mm:ss]").format(curTime);

        try {
            raf.skipBytes((int)raf.length());
            raf.writeBytes(curLog+"   "+DataLog+"\r\n");
            try {Thread.sleep(100); } catch(Exception e) {}


        } catch (Exception ex) {
            raf.skipBytes((int)raf.length());

            raf.writeBytes(ex.getLocalizedMessage());


        }

        raf.close();
    }
}
