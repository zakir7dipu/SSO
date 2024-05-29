package com.bizzsol.sso.sso.service;

import com.bizzsol.sso.sso.dao.RoleDao;
import com.bizzsol.sso.sso.dao.RoleDaoImpl;
import com.bizzsol.sso.sso.dao.UserDao;
import com.bizzsol.sso.sso.dao.UserDaoImpl;
import com.bizzsol.sso.sso.model.Application;
import com.bizzsol.sso.sso.model.Role;
import com.bizzsol.sso.sso.model.User;
import com.bizzsol.sso.sso.repository.ApplicationRepository;
import com.bizzsol.sso.sso.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserDao userDao;

    private RoleDao roleDao;

    @Autowired
    public UserServiceImpl(UserDao userDao, RoleDao roleDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
    }

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleDaoImpl rolesService;

    @Override
    public User findByUserName(String userName) {
        return userDao.findByUserName(userName);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUserName(username);

        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }

        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    public User save(User user) {
        LocalDateTime nowTime = LocalDateTime.now();
        user.setCreated_at(nowTime);
        user.setUpdated_at(nowTime);

        // Assuming rolesService.findRoleByName() returns a single Role object
        Role adminRole = rolesService.findRoleByName("ROLE_ADMIN");

        // Create a set to hold roles
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);

        // Set the roles to the user
        user.setRoles(roles);

        return userRepository.save(user);
    }

    public User genUserSave(User user) {
        LocalDateTime nowTime = LocalDateTime.now();
        user.setCreated_at(nowTime);
        user.setUpdated_at(nowTime);

        // Assuming rolesService.findRoleByName() returns a single Role object
        Role adminRole = rolesService.findRoleByName("ROLE_User");

        // Create a set to hold roles
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);

        // Set the roles to the user
        user.setRoles(roles);

        System.out.println(user);
        return userRepository.save(user);
    }

    public User createNewUserSave(User user) {
        LocalDateTime nowTime = LocalDateTime.now();
        user.setCreated_at(nowTime);
        user.setUpdated_at(nowTime);

        // Assuming rolesService.findRoleByName() returns a single Role object
        Role adminRole = rolesService.findRoleByName("ROLE_USER");

        // Create a set to hold roles
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);

        // Set the roles to the user
        user.setRoles(roles);

        return userRepository.save(user);
    }

    public User genUserUpdate(String username, User updatedUser) {
        LocalDateTime nowTime = LocalDateTime.now();
        User oldUser = this.findUserByUserName(username).orElse(null);
        if(oldUser != null) {
            oldUser.setEmail(updatedUser.getEmail() != null && !updatedUser.getEmail().equals("") ? updatedUser.getEmail() : oldUser.getEmail());

            oldUser.setPhone(updatedUser.getPhone() != null && !updatedUser.getPhone().equals("") ? updatedUser.getPhone() : oldUser.getPhone());

            oldUser.setUpdated_at(nowTime);
        }
        return userRepository.save(oldUser);
    }

    public List<User> findUsersExcludingCurrentAndAdmins() {
        String currentUsername = getCurrentUsername();
        return userRepository.findUsersExcludingCurrentAndAdmins(currentUsername);
    }

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    public Optional<User> findUserByUserName(String userName) {
        return userRepository.findUserByUserName(userName);
    }

    public void deleteUserByID(Long id) {
        userRepository.deleteById(id);
    }

    @Autowired
    private ApplicationRepository applicationRepository;
    public Set<Long> getApplicationIdsForUser(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return user.getApplications().stream()
                    .map(Application::getId)
                    .collect(Collectors.toSet());
        }

        throw new RuntimeException("User not found");
    }

    @Transactional
    public User syncUserApplications(Long userId, List<Long> applicationIds) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Set<Application> newApplications = new HashSet<>(applicationRepository.findAllById(applicationIds));

        // Remove roles not in the newRoles set
//        user.getApplications().removeIf(application -> !newApplications.contains(application));
        user.getApplications().removeAll(user.getApplications());

        // Add new roles
        user.getApplications().addAll(newApplications);

        return userRepository.save(user);
    }

}
