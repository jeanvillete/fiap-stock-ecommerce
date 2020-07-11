package fiap.sample.login.user.domain.usecase;

import fiap.sample.login.common.exception.InvalidSuppliedDataException;
import fiap.sample.login.user.domain.User;
import fiap.sample.login.user.domain.UserService;
import fiap.sample.login.user.domain.UserType;
import fiap.sample.login.user.domain.exception.UserConflictException;
import fiap.sample.login.user.domain.exception.UserNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserUseCase {

    public static class UserPayload {
        private String login;
        private String type;

        public UserPayload() {
        }

        public UserPayload(String login, String type) {
            this.login = login;
            this.type = type;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    private final UserService userService;

    public UserUseCase(UserService userService) {
        this.userService = userService;
    }

    public String createNewLogin(UserPayload userPayload) throws UserConflictException, InvalidSuppliedDataException {
        userService.checkForNonNullFieldsOnInsert(
                userPayload.login,
                userPayload.type
        );

        userService.checkForValidLogin(userPayload.login);
        UserType userType = userService.parseUserType(userPayload.type);

        User user = new User(
                userPayload.login,
                userType
        );

        userService.checkForConflictOnInsert(user);

        User newUser = userService.insertUser(user);

        return newUser.getId();
    }

    public String login(UserPayload userPayload) throws InvalidSuppliedDataException, UserNotFoundException {
        userService.checkForNonNullFieldsOnInsert(
                userPayload.login,
                userPayload.type
        );

        UserType userType = userService.parseUserType(userPayload.type);

        User user = new User(
                userPayload.login,
                userType
        );

        User savedUser = userService.retrieveUser(user);

        return savedUser.getId();
    }

}
