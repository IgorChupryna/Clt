/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hp.ca_loop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
/**
 *
 * @author Chupryna-IV
 */
public class ExeCommand {
    private static String pResult;

    public static void executeCommand(String command) {
        //UaChupCACallendars ca = new UaChupCACallendars();

        try {
            String[] finalCommand;
            if (System.getProperty("os.name").toLowerCase().indexOf("windows") != -1) {
                finalCommand = new String[4];
                finalCommand[0] = "cmd.exe";
                finalCommand[1] = "/y";
                finalCommand[2] = "/c";
                finalCommand[3] = command;
            } else {                                          //
                finalCommand = new String[3];
                finalCommand[0] = "/bin/sh";
                finalCommand[1] = "-c";
                finalCommand[2] = command;
            }
            // Execute the command...
            final Process pr = Runtime.getRuntime().exec(finalCommand);
            //Loger.addLogData("Process   " + command + " is starting ");
            // Capture output from STDOUT...
            BufferedReader br_in = null;
            String buff = null;

            try {
                br_in = new BufferedReader(new InputStreamReader(pr.getInputStream()));

                while ((buff = br_in.readLine()) != null) {
                    //System.out.println(buff);
                    pResult = buff;
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                    }
                }
                br_in.close();
            } catch (IOException ioe) {
                //System.out.println("Error printing process output.");
            } finally {
                try {
                    br_in.close();
                } catch (Exception ex) {
                }
            }
            buff = null;

            // Capture output from STDERR...
            BufferedReader br_err = null;
            try {
                br_err = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
                while ((buff = br_err.readLine()) != null) {

                    //System.out.println(buff);
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                    }
                }
                br_err.close();
            } catch (IOException ioe) {
                //System.out.println("Error printing process output.");
            } finally {
                try {
                    br_err.close();
                } catch (Exception ex) {
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }

    }

    /**
     * @return the pResult
     */
    public static String getpResult() {
        return pResult;
    }
}
