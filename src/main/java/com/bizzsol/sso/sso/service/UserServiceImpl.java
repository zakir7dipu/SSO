package com.bizzsol.sso.sso.service;

import com.bizzsol.sso.sso.dao.RoleDao;
import com.bizzsol.sso.sso.dao.RoleDaoImpl;
import com.bizzsol.sso.sso.dao.UserDao;
import com.bizzsol.sso.sso.dao.UserDaoImpl;
import com.bizzsol.sso.sso.model.Role;
import com.bizzsol.sso.sso.model.User;
import com.bizzsol.sso.sso.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
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
}
