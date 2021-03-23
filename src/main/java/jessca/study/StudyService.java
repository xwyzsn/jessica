package jessca.study;

import com.alibaba.druid.sql.visitor.functions.Char;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
@Service
public class StudyService {

    public StudyService() throws Exception {
    }
    public List<Study> getStudy() throws Exception{
        Connection conn = DruidFactory.getConnection();
        List<Study> l = new ArrayList<Study>();
        Statement statement = conn.createStatement();
        String sql = "select * from study ";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            String date = resultSet.getString(2);
            int score = resultSet.getInt(3);
            int id = resultSet.getInt(1);
            l.add(new Study(date,score,id));
        }
        statement.close();
        conn.close();
        return l;
    }

    public void postToDb(Study study) throws Exception {
        Connection conn = DruidFactory.getConnection();
        System.out.println(study.date);
        String date = study.date;
        Integer score = study.score;
        String sql = "insert into study(date,score) values("+" \""+date+"\" ,"+score+")";
        System.out.println(sql);
        Statement statement =conn.createStatement();
        int f = statement.executeUpdate(sql);
        if(f!=0){
            System.out.println("success");
        }
        else {
            System.out.println("fail");
        }

        statement.close();
        conn.close();
    }
    public List<ToDo> getTodo() throws Exception {
        Connection conn = DruidFactory.getConnection();
        Statement statement=conn.createStatement();
        String sql = "select * from todo";
        ResultSet resultSet =statement.executeQuery(sql);
        List<ToDo>list = new ArrayList<>();
        while(resultSet.next()){
            String context = resultSet.getString(2);
            int id = resultSet.getInt(1);
            String date = resultSet.getString(3);
            list.add(new ToDo(context,id,date));
        }
        statement.close();
        conn.close();

        return list;
    }

    public void deleteTo( int itemId) throws Exception {
        Connection conn = DruidFactory.getConnection();
        Statement statement=conn.createStatement();
        String sql ="delete  from todo where id = "+itemId;
        int f = statement.executeUpdate(sql);
        if(f!=0){
            System.out.println("delete success");
        }
        else {
            System.out.println("delete fail");
        }
        statement.close();
        conn.close();
    }

    public void addNewTodo(ToDo toDo) throws Exception {
        Connection conn = DruidFactory.getConnection();
        Statement statement = conn.createStatement();
        String date = toDo.date;
        String context=toDo.text;
        String sql = "insert into todo(LimitTime,content) values("+"\""+date+"\" ,\""+context+"\")";
        int f = statement.executeUpdate(sql);
        if(f!=0){
            System.out.println("success");
        }
        else{
            System.out.println("fail");
        }
        statement.close();
        conn.close();

    }

    public List<Chart> getChar() throws Exception {
        Connection conn = DruidFactory.getConnection();
        Statement statement = conn.createStatement();
        String sql ="SELECT date,sum(score) FROM study GROUP BY date ORDER BY date ";
        List<Chart>l=new ArrayList<>();
        ResultSet resultSet = statement.executeQuery(sql);
        while(resultSet.next()){
            String date = resultSet.getString(1);
            int score = resultSet.getInt(2);
            l.add(new Chart(date,score));
        }
        statement.close();
        conn.close();
        return l;

    }

    public List<Chart> getCharGreatZero() throws Exception {
        Connection conn = DruidFactory.getConnection();
        Statement statement = conn.createStatement();
        String sql ="SELECT date,sum(score) FROM study WHERE score>=0 GROUP BY date ORDER BY date ";
        List<Chart> l = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery(sql);
        while(resultSet.next()){
            String date = resultSet.getString(1);
            int score = resultSet.getInt(2);
            if(score>=24){
                continue;
            }
            l.add(new Chart(date,score));
        }
        statement.close();
        conn.close();
        return l;

    }

    public List<Word> getWord() throws Exception {
        Connection connection=DruidFactory.getConnection();
        Statement statement = connection.createStatement();
        String sql ="SELECT name,sum(number) as total ,date FROM word GROUP BY name,date  ORDER BY date ";
        ResultSet resultSet =statement.executeQuery(sql);
        List<Word>list=new ArrayList<>();
        while (resultSet.next()){
            String name=resultSet.getString(1);
            int number = resultSet.getInt(2);
            String date = resultSet.getString(3);

            Word word=new Word(name,number,date);

            list.add(word);
        }
        statement.close();
        connection.close();
        return list;

    }

    public void addWord(Word word) throws Exception {
        Connection connection = DruidFactory.getConnection();
        Statement statement = connection.createStatement();
        String sql = "INSERT into word(name,number,date) values( \""+word.name+"\","+word.number+",\""+word.date+"\" )";
        System.out.println(sql);
        int f =statement.executeUpdate(sql);

        if(f!=0){
            System.out.println("insert word success");
        }
        else{
            System.out.println("insert word fail");
        }
        statement.close();
        connection.close();

    }

    public List<TotalWord> getTotalWord() throws Exception {
        Connection connection=DruidFactory.getConnection();
        Statement statement=connection.createStatement();
        String sql="SELECT sum(number) as total,name FROM word  GROUP BY name";
        ResultSet resultSet =statement.executeQuery(sql);
        List<TotalWord>list=new ArrayList<>();
        while (resultSet.next()){
            String name = resultSet.getString(2);
            int total = resultSet.getInt(1);
            TotalWord totalWord=new TotalWord(name,total);
            list.add(totalWord);
        }
        statement.close();
        connection.close();
        return list;
    }
}
