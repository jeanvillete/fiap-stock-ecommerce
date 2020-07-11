package fiap.sample.login.user.domain;

import fiap.sample.login.common.exception.InvalidSuppliedDataException;
import fiap.sample.login.user.domain.exception.UserConflictException;
import fiap.sample.login.user.domain.exception.UserNotFoundException;

public interface UserService {
    void checkForConflictOnInsert(User user) throws UserConflictException;

    User insertUser(User user);

    void checkForNonNullFieldsOnInsert(String login, String type) throws InvalidSuppliedDataException;

    UserType parseUserType(String type) throws InvalidSuppliedDataException;

    User retrieveUser(User user) throws UserNotFoundException;

    void checkForValidLogin(String login) throws InvalidSuppliedDataException;
}
