package iuh.se.team.webbookstore_backend.impl;

import iuh.se.team.webbookstore_backend.dao.RoleRepository;
import iuh.se.team.webbookstore_backend.dao.UserRepository;
import iuh.se.team.webbookstore_backend.entities.Role;
import iuh.se.team.webbookstore_backend.entities.User;
import iuh.se.team.webbookstore_backend.services.UserSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserSevice {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), rolesToAuthorities(user.getRoles()));
    }



    //    Chuyển đổi danh sách quyền (Quyen) của người dùng
 //    thành danh sách các đối tượng GrantedAuthority mà Spring Security sử dụng.
    private Collection<? extends GrantedAuthority> rolesToAuthorities(Collection<Role> roles) {
      return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toList());
    };


}
