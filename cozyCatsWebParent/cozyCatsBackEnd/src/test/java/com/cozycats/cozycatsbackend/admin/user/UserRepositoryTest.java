package com.cozycats.cozycatsbackend.admin.user;

import com.cozycats.cozycatscommon.entity.Role;
import com.cozycats.cozycatscommon.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)

public class UserRepositoryTest {


    @Autowired
    private UserRepository repo;

//    @Autowired
//    private TestEntityManager entityManager;

//
//    @Test
//    public void testCreateNewUserWithOneRole(){
////        Role roleAdmin = entityManager.find(Role.class, 1);
//        Role roleAdmin = new Role(1);
//        User userTest = new User("usertest@gmail.com", "user123", "user", "username");
//        userTest.addRole(roleAdmin);
//        User savedUser = repo.save(userTest);
//        assertThat(savedUser.getId()).isGreaterThan(0);
//
//
//    }

//    @Test
//    public void testCreateNewUserWithTwoRoles(){
//        User uservini = new User("viniTest@gmail.com", "vini123", "vini", "mark");
//        Role roleEditor = new Role(4);
//        Role roleAssistant = new Role(3);
//        uservini.addRole(roleEditor);
//        uservini.addRole(roleAssistant);
//
//        User savedUser = repo.save(uservini);
//        assertThat(savedUser.getId()).isGreaterThan(0);
//
//    }

    @Test
    public void testListAllUsers(){
        Iterable<User> userList = repo.findAll();
        userList.forEach(user -> System.out.println(user));
    }

    @Test
    public void testGetById(){
        User username = repo.findById(1).get();
        System.out.println(username);
        assertThat(username).isNotNull();
    }

    @Test
    public  void testUpdateUserDetails(){
        User user = repo.findById(1).get();
        user.setEnabled(true);
        user.setEmail("email@gmail.ocom");
        repo.save(user);
    }

    @Test
    public void testUpdateUserRoles(){
        User user = repo.findById(12).get();
        Role roleAssistant = new Role(3);
        Role roleSales = new Role(2);
        user.getRoles().remove(roleAssistant);
        user.addRole(roleSales);
        repo.save(user);
    }

    @Test
    public void testDeleteUser(){
        Integer userId = 1;
        repo.deleteById(userId);
    }

    @Test
    public void testGetUserByEmail(){
        String email = "viniTest@gmail.com";
        User user = repo.getUserByEmail(email);
        assertThat(user).isNotNull();
    }

    @Test
    public void testCountById(){
        Integer id = 12;
        Long countById = repo.countById(id);
        assertThat(countById).isNotNull().isGreaterThan(0);
    }

    @Test
    public void testDisabledUser(){
        Integer id = 28;
        repo.updateEnableStatus(id, false);
    }

    @Test
    public void testListFirstPage(){
        int pageNumber = 1;
        int pageSize = 4;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = repo.findAll(pageable);
        List<User> listUsers = page.getContent();
        listUsers.forEach(user -> System.out.println(user));
        assertThat(listUsers.size()).isEqualTo(pageSize);
    }

    @Test
    public void testSearchUser(){
        String keyword = "hana";
        int pageNumber = 1;
        int pageSize = 4;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = repo.findAll(keyword, pageable);
        List<User> listUsers = page.getContent();
        listUsers.forEach(user -> System.out.println(user));

        assertThat(listUsers.size()).isGreaterThan(0);
    }

}
