package fiap.sample.login.user.application;

import fiap.sample.login.common.exception.InvalidSuppliedDataException;
import fiap.sample.login.user.domain.exception.UserConflictException;
import fiap.sample.login.user.domain.exception.UserNotFoundException;
import fiap.sample.login.user.domain.usecase.UserUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("login")
public class UserController {

    private final UserUseCase userUseCase;

    public UserController(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createNewUserLogin(@RequestBody UserUseCase.UserPayload userPayload) throws UserConflictException, InvalidSuppliedDataException {
        return userUseCase.createNewLogin(userPayload);
    }

    @GetMapping("{type}/{login}")
    public String login(@PathVariable("type") String type, @PathVariable("login") String login) throws UserNotFoundException, InvalidSuppliedDataException {
        return userUseCase.login(
                new UserUseCase.UserPayload(
                        login,
                        type
                )
        );
    }

}
