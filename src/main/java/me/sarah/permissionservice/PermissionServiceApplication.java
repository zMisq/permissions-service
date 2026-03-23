package me.sarah.permissionservice;

import me.sarah.permissionservice.adapter.out.mongo.UserRepositoryAdapter;
import me.sarah.permissionservice.port.out.UserRepositoryPort;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PermissionServiceApplication {

    public static void main(String[] args) {

        UserRepositoryPort port = new UserRepositoryAdapter(null, null);
        UserRepositoryAdapter adapter = new UserRepositoryAdapter(null, null);

        System.out.println(adapter instanceof UserRepositoryPort);

        SpringApplication.run(PermissionServiceApplication.class, args);
    }

}
