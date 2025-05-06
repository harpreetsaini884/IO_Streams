package in.sp.configfiles;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class SpringConfigFiles {


    @Bean
    public DriverManagerDataSource myDataSource(){
        DriverManagerDataSource dataSource=new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/spring_jdbc_db");
        dataSource.setUsername("root");
        dataSource.setPassword("H@rpreet2002");

        return  dataSource;
    }

    @Bean
    public JdbcTemplate myJDBCTemplate(){
        JdbcTemplate jdbcTemplate=new JdbcTemplate();
        jdbcTemplate.setDataSource(myDataSource());
        return  jdbcTemplate;
    }
}
