package me.sarah.permissionservice.adapter.in.rest.mapper;

import me.sarah.permissionservice.application.service.UserService;
import me.sarah.permissionservice.domain.model.Group;
import me.sarah.permissionservice.domain.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;


//RestController sagt Spring Klasse is REST-APi
@RestController
//Basis-URL
@RequestMapping("/users")
public class UserController {

    // wieso userservice -> Der UserController ist nur die Tür nach draussen
    // userService gehirn dahinter, kann man geiler testen,wartbarkeit und skalierbarkeit
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    //HTTP POSt -> erstellt einen User

    @PostMapping
    public User
    createUsers(@RequestParam String username) {
        return userService.createUser(username);
    }
    //alle User Abrufen

    @PostMapping("/users/{id}/groups")
    public void
    addGroupToUser(@PathVariable UUID id, @RequestBody Group group) {
        userService.addUserToGroup(id, group);
    }

    @GetMapping
    public Collection<User>getAllUsers(){
        return (userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable UUID id) {
        return userService.getUser(id);
    }

    @GetMapping("/users/{id}/permissions")
    public Set<String> getUserPermissions(@PathVariable UUID id) {
        return userService.getEffectivePermissions(id);
        }
    }
