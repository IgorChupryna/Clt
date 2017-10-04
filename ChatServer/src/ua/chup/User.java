package ua.chup;


enum Status{OnLine,Absent,OffLine}

public class User {
    private String name;
    private String pass;
    private  Status  status=Status.OffLine;

    public User(String name, String pass) {
        this.name = name;
        this.pass = pass;
    }

    @Override
    public String toString() {
        String spaces = "                ";
        return
                "\n"+name + spaces.substring(name.length()) + status;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }


    public void setPass(String pass) {
        this.pass = pass;
    }
}
