package me.sarah.permissionservice.application.service;

import me.sarah.permissionservice.domain.model.Group;
import me.sarah.permissionservice.domain.model.User;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    void testUserPermissions() {
        UserServiceImpl userService = new UserServiceImpl();

        Group adminGroup = new Group(UUID.randomUUID(), "Admin");

        adminGroup.addPermission("READ");
        adminGroup.addPermission("WRITE");
        adminGroup.addPermission("DELETE");

        Group userGroup = new Group(UUID.randomUUID(), "User");

        userGroup.addPermission("READ");
        // User erstellen
        User user = userService.createUser("Sarah");
        UUID userId = user.getId();
        // gruppe zuordnen
        userService.addUserToGroup(userId, adminGroup);

        userService.addUserToGroup(userId, adminGroup);

        //EFfektivr prtmissions holm
        Set<String> permissions = userService.getEffectivePermissions(userId);

        //test alle permissions vorhaneden

        assertTrue(permissions.contains("READ"));
        assertTrue(permissions.contains("WRITE"));
        assertTrue(permissions.contains("DELETE"));

        //KEINE DUBLIKATE

        assertEquals(3,permissions.size());
    }

}