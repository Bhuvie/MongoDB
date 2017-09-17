
public class Constants {

    public static String mongoUrl = "mongodb://127.0.0.1:27017";
    public static String mongoDb = "MongoDb2";
    public static String deppartmentCollection = "departmentCollection";
    public static String projectCollection = "projectCollection";
    public static String projectDocQuery = "select  Pnumber,Pname,Dname,Lname,Fname,Hours from (select Fname,Lname,ssn,Pnumber,Pname,Dname,Dnum from bhuviedb.employee e LEFT OUTER JOIN (select Pnumber,Pname,Dname,Dnum from project p LEFT OUTER JOIN department d on p.Dnum = d.Dnumber) as temp  on e.Dno = temp.Dnum) as temp2 LEFT OUTER JOIN works_on w on temp2.ssn = w.Essn and temp2.Pnumber = w.Pno;";
    public static String departmentDocQery = "select d.Dname, dl.Dlocations, e.Lname from department as d LEFT OUTER JOIN dept_locations as dl on d.Dnumber = dl.Dnumber JOIN employee as e on d.Mgr_ssn=e.Ssn;";
    public static String url = "jdbc:mysql://bhuviedbi.cwyiughlbpf0.us-west-2.rds.amazonaws.com:3306/bhuviedb";
    public static String userName = "bhuvie93";
    public static String password = "**********";


}
