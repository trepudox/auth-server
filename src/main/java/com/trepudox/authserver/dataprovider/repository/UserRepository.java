package com.trepudox.authserver.dataprovider.repository;

import com.trepudox.authserver.dataprovider.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, String> {
}
