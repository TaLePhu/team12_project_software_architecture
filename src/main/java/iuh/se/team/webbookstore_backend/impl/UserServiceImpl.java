package iuh.se.team.webbookstore_backend.impl;

import iuh.se.team.webbookstore_backend.dao.RoleRepository;
import iuh.se.team.webbookstore_backend.dao.UserRepository;
import iuh.se.team.webbookstore_backend.entities.Role;
import iuh.se.team.webbookstore_backend.entities.User;
import iuh.se.team.webbookstore_backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(int id) {
        Optional<User> userOpt = userRepository.findById(id);
        return userOpt.orElse(null);
    }

    @Override
    public User createUser(User user) {
        // Mã hóa password trước khi lưu
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Lấy danh sách roleId từ user.getRoles()
        List<Role> rolesFromClient = user.getRoles();
        List<Role> roles = null;

        if (rolesFromClient != null && !rolesFromClient.isEmpty()) {
            // Lấy role từ DB theo roleId
            roles = rolesFromClient.stream()
                    .map(r -> roleRepository.findById(r.getRoleId())
                            .orElseThrow(() -> new RuntimeException("Role not found: " + r.getRoleId())))
                    .collect(Collectors.toList());
        } else {
            // Nếu không có role, gán mặc định USER
            Role userRole = roleRepository.findByRoleName("USER");
            roles = List.of(userRole);
        }
        user.setRoles(roles);

        return userRepository.save(user);
    }

    @Override
    public User updateUser(int id, User user) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) return null;
        User existing = userOpt.get();

        // Cập nhật các trường cần thiết (trừ password nếu không thay đổi)
        existing.setFirstName(user.getFirstName());
        existing.setLastName(user.getLastName());
        existing.setUsername(user.getUsername());
        existing.setEmail(user.getEmail());
        existing.setPhoneNumber(user.getPhoneNumber());
        existing.setBillingAddress(user.getBillingAddress());
        existing.setShippingAddress(user.getShippingAddress());
        existing.setActivated(user.isActivated());
        existing.setGender(user.getGender());
        existing.setRoles(user.getRoles());

        // Nếu password thay đổi, mã hóa lại
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return userRepository.save(existing);
    }

    @Override
    public boolean deleteUser(int id) {
        if (!userRepository.existsById(id)) return false;
        userRepository.deleteById(id);
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                rolesToAuthorities(user.getRoles())
        );
    }

    private Collection<? extends GrantedAuthority> rolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean changePassword(String username, String currentPassword, String newPassword, String confirmPassword) {
        User user = userRepository.findByUsername(username);
        if (user == null) return false;

        // Kiểm tra mật khẩu hiện tại
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return false;
        }

        // Kiểm tra mật khẩu mới và xác nhận
        if (!newPassword.equals(confirmPassword)) {
            return false;
        }

        // Kiểm tra mật khẩu mới khác mật khẩu cũ
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            return false;
        }

        // Có thể thêm kiểm tra độ mạnh mật khẩu ở đây nếu muốn

        // Cập nhật mật khẩu mới
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }


}
