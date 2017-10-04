package hp.ca_loop;

/**
 *
 * @author Chupryna-IV
 */
import java.text.ParseException;

public class AutoCreInc {

    /**
     * @param args the command line arguments
     */
    private String autocal_asc = "/apps/CA/WorkloadAutomationAE/autosys/bin/";
    private String dir_name = "";
    private String outFile = "/home/autosys/autoinc/tech/tech";
    private int num_incident;
    private String str_incident = " ";
    private String from = "mlist-it.autosys-support@ukrsibbank.com";
    private String to = "ihor.chupryna@ukrsibbank.com"; // anton.p.shevchenko@ukrsibbank.com";//добавить"anton.p.shevchenko@ukrsibbank.com";//
    private String toCopy = "sou@ukrsibbank.com,sc.isq@ukrsibbank.com,mlist-it.crm-reports@ukrsibbank.com,mlist-it.cle-support@ukrsibbank.com,mlist-it.datastage-support@ukrsibbank.com,mlist-it.trm-support@ukrsibbank.com,mlist-it.workstation-support@ukrsibbank.com";
    private String host = "mail.bnppua.net.intra";
    private Boolean debug = false;

    public static void main(String[] args) throws ParseException, Exception {

        AutoCreInc g = new AutoCreInc();
        MakeIncedent s = new MakeIncedent();
        GetDate d = new GetDate();
        //s.createInc(" AUTOSYS ", d.getDateStr()+" 09:30:00", "TRM-SCN1 aborted", "impact:\nreason:\naction:", "CLE Error");
        //s.updateInc("149305", "impact:\nreason:\naction: data");
        //s.DeleteInc("149323");
        //SendMail.sendMail(g.getTo(), g.getToCopy(), g.getFrom(), g.getHost(), g.getDebug(), FileToString.readFile("D:\\trm_new_20150918.txt"), "Test"+" "+d.getDateStr());
        PrepareInf m = new PrepareInf();
        m.prepareData();

        if (m.getConfigItem().equals("CLE")) {
            g.setTo("mlist-it.cle-support@ukrsibbank.com");
        }
        if (m.getConfigItem().equals("TRM")) {
            g.setTo("mlist-it.trm-support@ukrsibbank.com");
        }
        if (m.getConfigItem().equals("DATASTAGE")) {
            g.setTo("mlist-it.datastage-support@ukrsibbank.com");
        }
        if (m.getConfigItem().equals("DASHBOARD")) {
            g.setTo("mlist-it.workstation-support@ukrsibbank.com");
        }

        g.setToCopy(g.getToCopy().replaceAll("," + g.getTo(), ""));

        if (m.getIsTrouble().equals(true)) {

            m.setShortDisc(m.getNameScn() + " " + m.getStatScn());
            m.setFullDisc("impact: Worstation's data didn't update " + d.getDateStr() + " at 09:30"
                    + "\nreason: The scenario " + m.getNameScn() + " " + m.getStatScn() + "\naction: Worstation's data updated at __:__");
            g.setNum_incident(s.createInc(m.getConfigItem(), d.getDateStr() + " 09:30:00", m.getShortDisc(), m.getFullDisc(), m.getClassif()));
            g.setStr_incident(Integer.toString(g.getNum_incident()));

            TechFile.doModify(g.getStr_incident(), g.getOutFile());
            m.DataToMail(g.getStr_incident());

            SendMail.sendMail(g.getTo(), g.getToCopy(), g.getFrom(), g.getHost(), g.getDebug(), FileToString.readFile(m.getFileMail()), m.getShortDisc() + " " + d.getDateStr());

        } else {
            TechFile.doModify(g.getStr_incident(), g.getOutFile());
            System.out.println("Без инцидентов " + m.getIsTrouble());
        }

    }

    /**
     * @return the autocal_asc
     */
    public String getAutocal_asc() {
        return autocal_asc;
    }

    /**
     * @param autocal_asc the autocal_asc to set
     */
    public void setAutocal_asc(String autocal_asc) {
        this.autocal_asc = autocal_asc;
    }

    /**
     * @return the dir_name
     */
    public String getDir_name() {
        return dir_name;
    }

    /**
     * @param dir_name the dir_name to set
     */
    public void setDir_name(String dir_name) {
        this.dir_name = dir_name;
    }

    /**
     * @return the outFile
     */
    public String getOutFile() {
        return outFile;
    }

    /**
     * @param outFile the outFile to set
     */
    public void setOutFile(String outFile) {
        this.outFile = outFile;
    }

    /**
     * @return the num_incident
     */
    public int getNum_incident() {
        return num_incident;
    }

    /**
     * @param num_incident the num_incident to set
     */
    public void setNum_incident(int num_incident) {
        this.num_incident = num_incident;
    }

    /**
     * @return the str_incident
     */
    public String getStr_incident() {
        return str_incident;
    }

    /**
     * @param str_incident the str_incident to set
     */
    public void setStr_incident(String str_incident) {
        this.str_incident = str_incident;
    }

    /**
     * @return the from
     */
    public String getFrom() {
        return from;
    }

    /**
     * @return the to
     */
    public String getTo() {
        return to;
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @return the debug
     */
    public Boolean getDebug() {
        return debug;
    }

    /**
     * @return the toCopy
     */
    public String getToCopy() {
        return toCopy;
    }

    /**
     * @param to the to to set
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * @param toCopy the toCopy to set
     */
    public void setToCopy(String toCopy) {
        this.toCopy = toCopy;
    }
}
