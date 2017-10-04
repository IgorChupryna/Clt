package hp.ca_loop;

import com.hp.ifc.util.ApiDateUtils;
import com.hp.itsm.api.*;
import com.hp.itsm.api.interfaces.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Chupryna-IV
 */
public class MakeIncedent {

    private String servName = "911.usb";//"911-pre.bnppua.net.intra";//
    private String userName = "hp90554";
    private String userPass = "12345678";
    IIncident incident = null;
    IIncidentHome incidentHome = null;
    IIncidentWhere incidentWhere = null;
    IIncident[] incidents = null;
    Long templateID = null;
    ITemplate[] templates = null;
    Long impactID = null;
    IImpact[] impactes = null;
    IImpactHome impactHome = null;
    IImpact impact;
    ICICategory[] categories = null;
    Long serviceID = null;
    IService[] services = null;
    IServiceHome serviceHome = null;
    IService service;
    IStatusIncident[] statuses = null;
    IStatusIncident statusClose = null;
    IStatusIncident statusInWork = null;
    IStatusIncident statusNew = null;
    IConfigurationItem[] configurationItem = null;
    IConfigurationItemHome configurationItemHome = null;
    IConfigurationItemWhere configurationItemWhere = null;
    int ciID = 0;
    IWorkgroup workgroup[] = null;
    IWorkgroupHome workgroupHome = null;
    IWorkgroupWhere workgroupWhere = null;
    IAssignment assignment = null;
    String sPerson = "EMP90554";
    IPerson person[] = null;
    IPersonHome personHome = null;
    IPersonWhere personWhere = null;
    IPerson assignPerson = null;
    ApiSDSession session = null;
    IIncidentClassification classy[] = null;
    IIncidentClassificationHome classyHome = null;
    IIncidentClassificationWhere classyWhere = null;
    IIncidentCategory category[] = null;
    IIncidentCategoryHome categoryHome = null;
    IIncidentCategoryWhere categoryWhere = null;

    public String getServName() {
        return servName;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPass() {
        return userPass;
    }

    MakeIncedent() {
        try {
            session = ApiSDSession.openSession(getServName(), getUserName(), getUserPass());
            incidentHome = session.getIncidentHome();

            templates = session.getTemplateHome().searchOnName("*Инцидент (стандарт)");
            templateID = templates[0].getOID();

            impactes = session.getImpactHome().findAllImpact();
            impactHome = session.getImpactHome();

            categories = session.getCICategoryHome().findAllCICategory();

            services = session.getServiceHome().findAllService();
            serviceHome = session.getServiceHome();

            statuses = session.getStatusIncidentHome().findAllStatusIncident();
            statusClose = statuses[4];
            statusInWork = statuses[1];
            statusNew = statuses[0];

            configurationItemHome = session.getConfigurationItemHome();
            workgroupHome = session.getWorkgroupHome();
            classyHome = session.getIncidentClassificationHome();
            categoryHome = session.getIncidentCategoryHome();

            impactID = impactes[0].getOID();
            serviceID = services[4].getOID();

            workgroup = session.getWorkgroupHome().findAllWorkgroup();


            classy = classyHome.findAllIncidentClassification();
            category = categoryHome.findAllIncidentCategory();


//            for(int i =0; i<classy.length ;i++){
//               System.out.println(i+" "+classy[i].getText());
//            }
            for(int i =0; i<impactes.length ;i++){
                System.out.println(i+" "+impactes[i].getText());
            }


        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }


    }

    public Integer createInc(String confItem, String date, String short_description, String description, String classyf) throws ParseException {

        //impactID = impactes[4].getOID();
        for (int i = 0; i < impactes.length; i++) {
            if (impactes[i].getText().equals("Нет")) {
                impactID = impactes[i].getOID();//+
            }
        }

        //serviceID = services[3].getOID();

        for (int i = 0; i < services.length; i++) {
            if (services[i].getName().equals("Batch procedures")) {
                serviceID = services[i].getOID();//+
            }
        }

        configurationItemWhere = configurationItemHome.createConfigurationItemWhere();
        configurationItemWhere.addContainCriteriumOnName1(confItem);

        for (int i = 0; i < categories.length; i++) {
            if (categories[i].getText().equals("IT System")) {
                configurationItemWhere.addCriteriumOnCategory(categories[i]);
            }
        }
        configurationItem = configurationItemHome.findConfigurationItem(configurationItemWhere);

        for (int i = 0; i < configurationItem.length; i++) {
            if (configurationItem[i].getStatus().getText().equals("In operation")) {
                ciID = i;
            }
        }

        try {
            incident = incidentHome.openNewIncident(templateID);
        } catch (RuntimeException e) {
            // Problems making a problem: show error and exit.
            System.out.println(e.getMessage());
            return null;
        }

        impact = impactHome.openImpact(Long.valueOf(impactID));
        service = serviceHome.openService(Long.valueOf(serviceID));

        incident.setDescription(confItem + ": " + short_description);
        incident.setInformation(description);

        try {
            incident.setConfigurationItem(configurationItem[ciID]);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return null;
        }
        incident.setService(service);
        incident.setImpact(impact);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date data = dateFormat.parse(date);
        incident.setActualStart(ApiDateUtils.date2Double(data));

        assignment = incident.getAssignment();
        personHome = session.getPersonHome();
        personWhere = personHome.createPersonWhere();
        personWhere.addContainCriteriumOnSearchcode(sPerson);
        IPerson[] persons = personHome.findPerson(personWhere);
        assignPerson = null;

        for (int i = 0; i < persons.length; i++) {
            if (persons[i].getName().equals("Чуприна, Ігор Васильович")) {
                assignPerson = persons[i];//+
            }
        }

        if (assignPerson != null) {
            for (int i = 0; i < workgroup.length; i++) {
                if (workgroup[i].getName().equals("Регламентные операции")) {
                    assignment.setAssWorkgroup(workgroup[i]);//+
                }
            }

            assignment.setAssigneePerson(null);
            assignment.transfer();
        }

        try {
            for (int i = 0; i < category.length; i++) {
                if (category[i].getText().equals("Functional Problem")) {
                    incident.setCategory(category[i]);//+
                }
            }

            for (int i = 0; i < classy.length; i++) {
                if (classy[i].getText().equals(classyf)) {
                    incident.setClassification(classy[i]);//+
                }
            }


            //incident.setClassification(classy[classyf]);//+

        } catch (RuntimeException e) {
            System.out.println(e.getMessage());

        }
        incident.setSolution(description);

        incident.setStatus(statusNew);
        try {
            incident.save();
            System.out.println("Создан инцидент: " + incident.getID());
        } catch (RuntimeException e) {
            //	Something went wrong while saving,
            //	probably a required field was not set.
            System.out.println(e.getMessage());
        }
        Long id = incident.getID();
        incident = null;
        //chWorkorder(id);
        configurationItemWhere = null;
        return id.intValue();
    }

    public void updateInc(String id, String description) {
        try {
            incident = incidentHome.openIncident(Integer.parseInt(id));
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return;
        }
        incident.setStatus(statusInWork);
        incident.setInformation(description);

//        // Get the assignment entity instance for this incident.
//        assignment = incident.getAssignment();
//
//        // Get the person home
//        personHome = session.getPersonHome();
//
//        // Create a where instance for person
//        personWhere = personHome.createPersonWhere();
//
//        // Add a criterion : the searchcode of the person must contain
//        // the String sPerson
//        personWhere.addContainCriteriumOnSearchcode(sPerson);
//
//        // Perform the search for persons, using the self-made where clause
//        IPerson[] persons = personHome.findPerson(personWhere);
//
//        assignPerson = null;
//
//        if (persons == null) {
//            System.out.println("No persons found with searchcode containing " + sPerson);
//        } else if (persons.length > 1) {
//            System.out.println("More than one person found with searchcode containing " + sPerson);
//            System.out.println("Taking the first one");
//            assignPerson = persons[0];
//        } else if (persons.length == 1) {
//            assignPerson = persons[0];
//        }
//
//        if (assignPerson != null) {
//            assignment.setAssWorkgroup(workgroup[25]);
//            assignment.setAssigneePerson(null);
//            assignment.transfer();
//        }
//
//        try {
//            incident.setCategory(category[4]);
//            incident.setClassification(classy[77]);
//
//        } catch (RuntimeException e) {
//            System.out.println(e.getMessage());
//
//        }

        incident.setSolution(description);


        try {
            incident.save();
            System.out.println("Создан инцидент: " + incident.getID());
        } catch (RuntimeException e) {

            System.out.println(e.getMessage());
        }
        incident = null;
        workgroupWhere = null;
    }

    public void DeleteInc(String id) {
        try {
            incident = incidentHome.openIncident(Integer.parseInt(id));
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return;
        }
        incident.delete();

        try {

            System.out.println("Удален инцидент: " + incident.getID());
            incident.save();
        } catch (RuntimeException e) {

            System.out.println(e.getMessage());
        }
        incident = null;
        workgroupWhere = null;
    }
}
