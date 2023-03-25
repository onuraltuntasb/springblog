package com.onuraltuntas.springblog.setup;

import com.onuraltuntas.springblog.entity.Privilege;
import com.onuraltuntas.springblog.entity.Role;
import com.onuraltuntas.springblog.repository.PrivilegeRepository;
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


    private final PrivilegeRepository privilegeRepository;
    private final RoleRepository roleRepository;

    boolean alreadySetup = false;
    //TODO learn how to active hear on ci/cd

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

//        admin set
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

      Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
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
