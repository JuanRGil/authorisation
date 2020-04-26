package com.gil.arizon.juan.authorisation.repository;

import com.gil.arizon.juan.authorisation.domain.MyUser;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface MyUserRepository extends CrudRepository<MyUser, Long> {

  Optional<MyUser> findByUserNameOrEmail(String username, String email);
}
