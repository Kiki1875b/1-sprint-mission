
package com.sprint.mission;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.sprint.mission.discodeit.*", "com.sprint.mission.discodeit.repository.jcf"})
public class DiscodeitApplication {

  public static void main(String[] args) {
    ApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);


  }
}
