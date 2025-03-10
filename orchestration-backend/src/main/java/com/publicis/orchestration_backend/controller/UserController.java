package com.publicis.orchestration_backend.controller;

import com.publicis.orchestration_backend.entity.User;
import com.publicis.orchestration_backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "User Controller", description = "APIs for Orchestration")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/load")
    @Operation(summary = "Load Users from External API", description = "Fetch users from an external API and save them to the database.")
    public ResponseEntity<String> loadUsers() {
        userService.loadUsersFromExternalAPI();
        return ResponseEntity.ok("Users loaded successfully");
    }

    @GetMapping("/search")
    @Operation(summary = "Search Users", description = "Search users by first name, last name, or SSN.")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String query) {
        return ResponseEntity.ok(userService.searchUsers(query));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get User by ID", description = "Retrieve a user by their unique ID.")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get User by Email", description = "Retrieve a user by their email address.")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }
}

