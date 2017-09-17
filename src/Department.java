import java.util.ArrayList;
import java.util.List;

public class Department {

    public String getDname() {
        return Dname;
    }

    public List<String> getDlocation() {
        return Dlocation;
    }

    public void setDlocation(List<String> dlocation) {
        Dlocation = dlocation;
    }

    public void setDname(String dname) {
        Dname = dname;
    }

    public String getLname() {
        return Lname;
    }

    public void setLname(String lname) {
        Lname = lname;
    }

    private String Dname;
    private List<String> Dlocation = new ArrayList();
    private String Lname;
}
