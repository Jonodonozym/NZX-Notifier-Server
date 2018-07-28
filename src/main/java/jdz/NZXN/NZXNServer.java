
package jdz.NZXN;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NZXNServer {	
    public static void main(String[] args) {
        SpringApplication.run(NZXNServer.class, args);
    }
}