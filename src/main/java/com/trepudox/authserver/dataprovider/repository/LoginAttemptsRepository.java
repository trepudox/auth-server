package com.trepudox.authserver.dataprovider.repository;

import com.trepudox.authserver.dataprovider.model.LoginAttemptsModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginAttemptsRepository extends CrudRepository<LoginAttemptsModel, String> {
}
