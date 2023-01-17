package com.onuraltuntas.springblog.setup;

import com.onuraltuntas.springblog.entity.Privilege;
import com.onuraltuntas.springblog.entity.Role;
import com.onuraltuntas.springblog.repository.PriviligeRepository;
import com.onuraltuntas.springblog.repository.RoleRepository;
import com.onuraltuntas.springblog.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SetupDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {


    private final PriviligeRepository priviligeRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    boolean alreadySetup = false;


    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;
        Privilege readPrivilege
                = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege
                = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        List<Privilege> adminPrivileges = Arrays.asList(
                readPrivilege, writePrivilege);
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege));

        Role adminRole = roleRepository.findByName("ROLE_ADMIN");

        //admin set
//        User user = new User();
//        user.setName("Admin");
//        user.setPassword(passwordEncoder.encode("test"));
//        user.setEmail("test@test.com");
//        user.setRoles(Arrays.asList(adminRole));
//        userRepository.save(user);

        alreadySetup = true;
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {

      Privilege privilege = priviligeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            priviligeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(
            String name, List<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {

            role = Role.builder().name(name).privileges(privileges).build();

            roleRepository.save(role);
        }
        return role;
    }
}
