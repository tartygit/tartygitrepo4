package com.cth.sdm.service;

import com.cth.sdm.entity.User;
import com.cth.sdm.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initDefaultUsers() {
        if (userRepository.count() == 0) {
            createUser("admin", "admin123", "System Administrator", "admin@organization.com", "+123456789", "ROLE_ADMIN");
            createUser("maker", "maker123", "Maker User", "maker@organization.com", "+123456780", "ROLE_MAKER");
            createUser("checker", "checker123", "Checker Approver", "checker@organization.com", "+123456781", "ROLE_CHECKER");
        }
    }

    @Transactional
    public User createUser(String username, String rawPassword, String fullName, String email, String phone, String role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setRole(role);
        user.setEnabled(true);
        user.setAccountLocked(false);
        user.setFailedAttempts(0);
        user.setPasswordExpiryDate(LocalDateTime.now().plusDays(90));
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public void lockUser(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            user.setAccountLocked(true);
            userRepository.save(user);
        });
    }

    @Transactional
    public void unlockUser(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            user.setAccountLocked(false);
            user.setFailedAttempts(0);
            userRepository.save(user);
        });
    }

    @Transactional
    public void resetPassword(String username, String newPassword) {
        userRepository.findByUsername(username).ifPresent(user -> {
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setPasswordExpiryDate(LocalDateTime.now().plusDays(90));
            userRepository.save(user);
        });
    }
}
