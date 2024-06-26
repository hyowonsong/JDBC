package hello.jdbc.connection;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;

//JDBC 를 사용해서 실제 데이터베이스 에 연결

@Slf4j
public class DBConnectionUtil {
    public static Connection getConnection(){
        try{
            Connection connection = DriverManager.getConnection(URL,USERNAME, PASSWORD);
            log.info("get connection={}, class={}", connection, connection.getClass());
            return connection;
        } catch (SQLException e){
            throw new IllegalStateException(e);
        }

    }
}
