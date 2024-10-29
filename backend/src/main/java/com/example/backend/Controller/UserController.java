//package com.example.backend.Controller;
//
//import com.example.backend.entity.User;
//import com.example.backend.repository.UserRepository;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/users")
//public class UserController {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    // CREATE
//    @PostMapping
//    public User createUser(@RequestBody User user) {
//        return userRepository.save(user);
//    }
//
//    // READ
//    @GetMapping("/{id}")
//    public User getUser(@PathVariable Long id) {
//        return userRepository.findById(id).orElse(null);
//    }
//
//    // UPDATE
//    @PutMapping("/{id}")
//    public User updateUser(@PathVariable Long id, @RequestBody User userDetails) {
//        User user = userRepository.findById(id).orElse(null);
//        if (user != null) {
//            user.setName(userDetails.getName());
//            user.setEmail(userDetails.getEmail());
//            return userRepository.save(user);
//        }
//        return null;
//    }
//
//    // DELETE
//    @DeleteMapping("/{id}")
//    public void deleteUser(@PathVariable Long id) {
//        userRepository.deleteById(id);
//    }
//
//    // GET ALL USERS
//    @GetMapping
//    public List<User> getAllUsers() {
//        return userRepository.findAll();
//    }
//}
//
