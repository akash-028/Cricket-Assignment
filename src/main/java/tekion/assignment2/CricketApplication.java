package tekion.assignment2;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tekion.assignment2.connection.ConnectMongo;

@SpringBootApplication
public class CricketApplication {

    public static void main(String[] args) {

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = loggerContext.getLogger("org.mongodb.driver");
        rootLogger.setLevel(Level.WARN);
        ConnectMongo.createIndexes();
        SpringApplication.run(CricketApplication.class, args);
    }

}
