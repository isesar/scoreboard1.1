package org.scores.scoreboard1_1;

import cli.ScoreboardCLI;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import repository.InMemoryScoreboardRepository;
import service.ScoreboardService;

import java.util.Scanner;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            ScoreboardCLI cli = new ScoreboardCLI(new ScoreboardService(new InMemoryScoreboardRepository()), new Scanner(System.in));
            cli.run();
        };
    }
}
