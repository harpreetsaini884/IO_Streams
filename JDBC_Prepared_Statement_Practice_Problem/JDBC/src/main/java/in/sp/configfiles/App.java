package in.sp.configfiles;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class App {
    public static void main(String[] args) {


        ApplicationContext context=new AnnotationConfigApplicationContext(SpringConfigFiles.class);
        JdbcTemplate jdbcTemplate=context.getBean(JdbcTemplate.class);

// INSERT OPERATION -----------------------

//        int rollno=1711;
//        String name="Harry";
//        int marks=884;

//        String insert_into_sql="INSERT INTO  STUDENT values(?,?,?)";
//        int count=jdbcTemplate.update(insert_into_sql,rollno,name,marks);
//
//        if(count>0){
//            System.out.println("Insertion Success");
//        }else
//        {
//            System.out.println("INsertion Failed");
//        }

   //  UPDATE OPEARTION IN SPRING JDBC
//        int marks=87;
//        int rollno=101;
//        String update_sql_query="UPDATE student SET marks=? where rollno=?";
//        int count=jdbcTemplate.update(update_sql_query,marks,rollno);
//
//        if(count >0){
//            System.out.println("Successfully Updated");
//        }
//        else
//        {
//            System.out.println("Updation Failed");
//        }


//        // DELET OPEARTION
//
//        int rollno=101;
//        String delete_sql_query="DELETE from student WHERE rollno=?";
//        int count =jdbcTemplate.update(delete_sql_query,rollno);
//        if(count>0){
//            System.out.println("deleted Successfully");
//        }
//        else {
//            System.out.println("Programme Failed");
//        }

    }
}
