
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.mongodb.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;


public class Main {
    //Main method retrieves the join results from MySQL DB, converts them into JSON and insert them into MongoDB.
    public static void main(String[] args){
        try {
            HashMap<String,Department> deptHashMap = new HashMap<>();            //Department Hashmap for storing each Department project,
            HashMap<String,Project> projectHashMap = new HashMap<>();            //Project Hashmap for storing each Project object
            ObjectMapper mapper = new ObjectMapper();

            ResultSet resultSetProj = execQuery(Constants.projectDocQuery);     //Resultset of Project Join query is stored.
            while(resultSetProj.next()){
                Project project;
                if(resultSetProj.getString("Pnumber")==null){
                    continue;
                }
                if(projectHashMap.containsKey(resultSetProj.getString("Pnumber"))) {
                    project = projectHashMap.get(resultSetProj.getString("Pnumber"));
                }
                else
                {
                    project = new Project();
                    project.setPnumber(resultSetProj.getString("Pnumber"));
                    project.setPname(resultSetProj.getString("Pname"));
                    project.setDname(resultSetProj.getString("Dname"));
                }
                Employee employee = new Employee();                                 //A Porject object can contain many employees. So each employee object is added
                employee.setLname(resultSetProj.getString("Lname"));        //to Employee list in project object
                employee.setFname(resultSetProj.getString("Fname"));
                if(resultSetProj.getString("Hours")!=null)
                    employee.setHours(Double.parseDouble(resultSetProj.getString("Hours")));
                else
                    employee.setHours(0.0);
                List<Employee> empList =project.getEmployee();
                empList.add(employee);
                project.setEmployee(empList);
                projectHashMap.put(project.getPnumber(),project);               //The hashmap keeps on updating with new project object and/or employee object.
            }

            Iterator iterator = projectHashMap.keySet().iterator();             //When the Project resultset is added to hashmap,
            while (iterator.hasNext()) {                                            //each Project object is looped using Iterator
                String key = iterator.next().toString();                                  //and then converted to JSON
                Project project1 = projectHashMap.get(key);                                 //using mapper class of JACKSON library
                String jsonProj = mapper.writeValueAsString(project1);                          //and inserted into MongoDB
                System.out.println(jsonProj);
                insertDocument(jsonProj, Constants.projectCollection);
            }
            getDocuments(Constants.projectCollection);


            //Similarly the same procedure as above, each Department object in department hashmap is converted into JSON and inserted into MongoDB
            ResultSet resultSetDept = execQuery(Constants.departmentDocQery);
            while (resultSetDept.next()) {
                Department department;
                if(deptHashMap.containsKey(resultSetDept.getString("Dname")))
                {
                    department=deptHashMap.get(resultSetDept.getString("Dname"));
                }
                else
                {
                    department = new Department();
                    department.setDname(resultSetDept.getString("Dname"));
                    department.setLname(resultSetDept.getString("Lname"));
                }
                List<String> dlocation = department.getDlocation();
                dlocation.add(resultSetDept.getString("Dlocations"));
                department.setDlocation(dlocation);
                deptHashMap.put(department.getDname(),department);
            }
            Iterator iterator1 = deptHashMap.keySet().iterator();
            while (iterator1.hasNext()) {
                String key = iterator1.next().toString();
                Department dept1 = deptHashMap.get(key);
                String jsonDept = mapper.writeValueAsString(dept1);
                System.out.println(jsonDept);
                insertDocument(jsonDept, Constants.deppartmentCollection);
            }
            getDocuments(Constants.deppartmentCollection);

            getDocumentFilter(Constants.deppartmentCollection);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    //This method connects to MySQL DB and retrieves the query resultset
    public static ResultSet execQuery(String query)
    {
        ResultSet resultSet=null;
        try {
            System.out.println("\nConnecting to Mysql database!!");
            Connection con;
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(Constants.url, Constants.userName, Constants.password);
            PreparedStatement ps = con.prepareStatement(query);
            resultSet = ps.executeQuery();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return resultSet;
    }

    //This method connects to MongoDB and inserts the JSON into the required collection
    public static void insertDocument(String json,String collName)
    {
        try{
            MongoClient mongoClient = new MongoClient(new MongoClientURI(Constants.mongoUrl));
            MongoDatabase db = mongoClient.getDatabase(Constants.mongoDb);
            MongoCollection collection = db.getCollection(collName);
            Document dbObject = Document.parse(json);
            collection.insertOne(dbObject);
            System.out.println("Document inserted in collection : "+collName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //This method dispplays all the documents in the specified collection
    public static void getDocuments(String collName)
    {
        try {
            MongoClient mongoClient = new MongoClient(new MongoClientURI(Constants.mongoUrl));
            MongoDatabase db = mongoClient.getDatabase(Constants.mongoDb);
            MongoCollection<Document> collection = db.getCollection(collName);
            System.out.println("\nAll the documents for the collection "+collName+" : ");
            MongoCursor<Document> cursor = collection.find().iterator();
            while (cursor.hasNext()) {
                System.out.println(cursor.next());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //This method displays the documents that match the criteria.
    public static void getDocumentFilter(String collName)
    {
        try {
            MongoClient mongoClient = new MongoClient(new MongoClientURI(Constants.mongoUrl));
            MongoDatabase db = mongoClient.getDatabase(Constants.mongoDb);
            MongoCollection<Document> collection = db.getCollection(collName);
            System.out.println("\nDocument with required filter : ");
            //The required criteria is specified in the find method.
            MongoCursor<Document> cursor = collection.find(eq("dname", "Research" )).iterator();          //Filter Syntax collection.find(gt("i", 50)) or collection.find(and(gt("i", 50), lte("i", 100)))
            while (cursor.hasNext()) {
                System.out.println(cursor.next());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}


