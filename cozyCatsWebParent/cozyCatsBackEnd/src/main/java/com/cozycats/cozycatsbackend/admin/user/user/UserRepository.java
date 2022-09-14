package com.cozycats.cozycatsbackend.admin.user.user;

import com.cozycats.cozycatscommon.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
}
