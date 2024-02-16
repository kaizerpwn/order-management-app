package com.ibrahimokic.ordermanagement.repositories;

import com.ibrahimokic.ordermanagement.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
