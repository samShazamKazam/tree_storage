package tree;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;

@SpringBootApplication
public class Application {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@PostConstruct
	private void initDb() {
		System.out.println(String.format("****** Creating table: %s ******", "node"));

		String sqlStatements[] = {
				"drop table node if exists",
				"create table node (id varchar(88) PRIMARY KEY, label varchar(255), parent_id varchar(88), level INT, " +
						" CONSTRAINT fk_parent FOREIGN KEY (parent_id) REFERENCES node(id)); "
		};

		Arrays.asList(sqlStatements).stream().forEach(sql -> {
			System.out.println(sql);
			jdbcTemplate.execute(sql);
		});
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
