package ch.visualboard.api.tictactoe;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties
@EnableScheduling
public class ApiTicTacToeApplication
{
    private static ConfigurableApplicationContext context;

    public static void main(String[] args)
    {
        context = SpringApplication.run(ApiTicTacToeApplication.class, args);
    }

    public static void restart() throws InterruptedException
    {
        ApplicationArguments args = context.getBean(ApplicationArguments.class);

        Thread thread = new Thread(() -> {
            context.close();
            context = SpringApplication.run(ApiTicTacToeApplication.class, args.getSourceArgs());
        });

        Thread.sleep(1000);

        thread.setDaemon(false);
        thread.start();
    }
}
