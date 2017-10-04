package hp.ca_loop;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author Chupryna-IV
 */
public class PrepareInf {

    private Boolean isTrouble = false;
    private String configItem = " ";
    private String nameScn;
    private String[] allScn = {"NDST-BD-SCN1", "CLE-BD-SCN3", "CLE-BD-SCN4", "NDST-BD_ELT_LEADS", "TRM-BD-SCN1", "CLE-BD-SCN1", "WST-BD-SCN1"}; //,  "STAGE-test"
    private String statScn;
    private String classif;
    private String shortDisc;
    private String fullDisc;
    private String fileMail = "/home/autosys/autoinc/tech/techMail";

    public void prepareData() {
        AutoCreInc m = new AutoCreInc();

        for (int i = 0; i < getAllScn().length; i++) {
            System.out.println((i + 1) + "/" + getAllScn().length);

            ExeCommand.executeCommand(m.getAutocal_asc() + "autorep -J " + getAllScn()[i] + " | egrep \" FA | RU \" | head -1 | awk '{ print $1}'");
            ExeCommand.executeCommand(m.getAutocal_asc() + "autostatus -J " + ExeCommand.getpResult());

            if ("FAILURE".equals(ExeCommand.getpResult()) || "RUNNING".equals(ExeCommand.getpResult())) {

                setIsTrouble(true);
                if (ExeCommand.getpResult().equals("FAILURE")) {
                    setStatScn("aborted");
                } else {
                    setStatScn("didn't finish");
                }
                ExeCommand.executeCommand(m.getAutocal_asc() + "autorep -J " + getAllScn()[i] + " | egrep \" FA | RU \" | head -1 | awk '{ print $1}'");

                if (ExeCommand.getpResult().equals("CLE-BD-SCN3") || ExeCommand.getpResult().equals("CLE-BD-SCN4") || ExeCommand.getpResult().equals("CLE-BD-SCN1")) {
                    setNameScn(ExeCommand.getpResult());
                    setConfigItem("CLE");
                    setClassif("CLE Error");
                    break;
                }
                if (ExeCommand.getpResult().equals("NDST-BD_ELT_LEADS")||ExeCommand.getpResult().equals("NDST-BD-SCN1")) {
                    setNameScn(ExeCommand.getpResult());
                    setConfigItem("DATASTAGE");
                    setClassif("DATASTAGE Error");
                    break;
                }
                if (ExeCommand.getpResult().equals("TRM-BD-SCN1")) {
                    setNameScn(ExeCommand.getpResult());
                    setConfigItem("TRM");
                    setClassif("TRM Error");
                    break;
                }

                if (ExeCommand.getpResult().equals("WST-BD-SCN1")) {
                    setNameScn(ExeCommand.getpResult());
                    setConfigItem("DASHBOARD");
                    setClassif("DASHBOARD Error");
                    break;
                }

                if (ExeCommand.getpResult().equals("STAGE-test")) {
                    setNameScn(ExeCommand.getpResult());
                    setConfigItem("TRM");
                    setClassif("DASHBOARD Error");
                    break;
                }
            }
        }
    }

    public void DelFile(String path) {
        File file = new File(path);
        if (!file.delete()) {
        }
    }

    public void DataToMail(String num_inc) {
        DelFile(getFileMail());
        AutoCreInc m = new AutoCreInc();
        try {
            String what_problem = null;
            File file = new File(getFileMail());
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            raf.writeBytes("\nJob Name              Last Start            Last End             STATUS \n");
            raf.writeBytes("_____________________ ____________________  ____________________ _______\n");

            for (int i = 0; i < getAllScn().length; i++) {
                System.out.println((i + 1) + "/" + getAllScn().length);
                ExeCommand.executeCommand(m.getAutocal_asc() + "autostatus -J `" + m.getAutocal_asc()
                        + "autorep -J " + getAllScn()[i] + " -l 0 | egrep -v \"Job|__\" | tail -1 | awk '{ print $1}'`");
                String status = ExeCommand.getpResult();
                if (ExeCommand.getpResult().equals("FAILURE")) {
                    what_problem = getAllScn()[i] + " was aborted";
                }
                if (ExeCommand.getpResult().equals("RUNNING")) {
                    what_problem = getAllScn()[i] + " is running";
                }

                String tempScn = "";
                for (int j = 0; j < 20 - getAllScn()[i].length(); j++) {
                    tempScn = tempScn + " ";
                }

                ExeCommand.executeCommand(m.getAutocal_asc() + "autorep -J " + getAllScn()[i] + " -l 0 | egrep -v \"Job|__\"  | tail -1 | awk '{print $1,\"" + tempScn + "\",$2,$3,\" \",$4,$5}'");
                String rslt = ExeCommand.getpResult();

                rslt = rslt.replaceAll("----- RU", "---------- --------");

                raf.writeBytes(rslt + "  " + status + "\n");
            }

            raf.writeBytes("\n\nThe incident was registered automatically with number: " + num_inc + "\nBecause the scenario " + what_problem + "\n\n\n\n\n");
            raf.writeBytes("This is an automatically generated email, please do not reply....\n");
            raf.writeBytes("---------------------\n");
            raf.writeBytes("If you have any quastions,\nyou can contact:\nihor.chupryna@ukrsibbank.com or\nanton.p.shevchenko@ukrsibbank.com");
            raf.close();
        } catch (IOException e) {
            System.out.println("IOException:" + e);
            e.printStackTrace();

        }

    }

    /**
     * @return the isTrouble
     */
    public Boolean getIsTrouble() {
        return isTrouble;
    }

    /**
     * @param isTrouble the isTrouble to set
     */
    public void setIsTrouble(Boolean isTrouble) {
        this.isTrouble = isTrouble;
    }

    /**
     * @return the configItem
     */
    public String getConfigItem() {
        return configItem;
    }

    /**
     * @param configItem the configItem to set
     */
    public void setConfigItem(String configItem) {
        this.configItem = configItem;
    }

    /**
     * @return the nameScn
     */
    public String getNameScn() {
        return nameScn;
    }

    /**
     * @param nameScn the nameScn to set
     */
    public void setNameScn(String nameScn) {
        this.nameScn = nameScn;
    }

    /**
     * @return the allScn
     */
    public String[] getAllScn() {
        return allScn;
    }

    /**
     * @param allScn the allScn to set
     */
    public void setAllScn(String[] allScn) {
        this.allScn = allScn;
    }

    /**
     * @return the statScn
     */
    public String getStatScn() {
        return statScn;
    }

    /**
     * @param statScn the statScn to set
     */
    public void setStatScn(String statScn) {
        this.statScn = statScn;
    }

    /**
     * @return the classif
     */
    public String getClassif() {
        return classif;
    }

    /**
     * @param classif the classif to set
     */
    public void setClassif(String classif) {
        this.classif = classif;
    }

    /**
     * @return the shortDisc
     */
    public String getShortDisc() {
        return shortDisc;
    }

    /**
     * @param shortDisc the shortDisc to set
     */
    public void setShortDisc(String shortDisc) {
        this.shortDisc = shortDisc;
    }

    /**
     * @return the fullDisc
     */
    public String getFullDisc() {
        return fullDisc;
    }

    /**
     * @param fullDisc the fullDisc to set
     */
    public void setFullDisc(String fullDisc) {
        this.fullDisc = fullDisc;
    }

    /**
     * @return the fileMail
     */
    public String getFileMail() {
        return fileMail;
    }
}
