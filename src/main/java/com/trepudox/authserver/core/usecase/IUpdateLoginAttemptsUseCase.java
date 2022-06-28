package com.trepudox.authserver.core.usecase;

import com.trepudox.authserver.dataprovider.model.UserModel;

public interface IUpdateLoginAttemptsUseCase {

    void update(UserModel user);

}
