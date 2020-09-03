package demo;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

//1
@SpringBootApplication
public class Application {
    //0
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    //2
    @Bean
    CommandLineRunner init(CustomerRepository r) {
     return args -> Arrays
      .stream(
       ("Mark,Fisher;Scott,Frederick;Brian,Dussault;"
        + "Josh,Long;Kenny,Bastani;Dave,Syer;Spencer,Gibb").split(";"))
      .map(n -> n.split(",")).map(tpl -> r.save(new Customer(tpl[0], tpl[1])))
      .forEach(System.out::println);
    }
}
