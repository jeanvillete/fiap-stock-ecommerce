package fiap.sample.login.user.domain;

import fiap.sample.login.common.exception.InvalidSuppliedDataException;
import fiap.sample.login.user.domain.exception.UserConflictException;
import fiap.sample.login.user.domain.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final String CACHE_KEY = "USER_LOGIN_CACHE";

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void checkForConflictOnInsert(User user) throws UserConflictException {
        Integer count = repository.countByLoginAndType(
                user.getLogin(),
                user.getType()
        )
        .orElse(0);

        if (count > 0) {
            LOGGER.debug(
                    "Impossible record new user, there is already a user recorded with the provided login [{}}]" +
                            " and type [{}}]",
                    user.getLogin(),
                    user.getType()
            );
            throw new UserConflictException(
                    "There is already a user recorded with the provided login [" + user.getLogin() +  "]" +
                            " and type [" +  user.getType() + "]"
            );
        }
    }

    @Override
    @Caching(put = {@CachePut(value = CACHE_KEY, key = "{#user.login, #user.type}")})
    public User insertUser(User user) {
        LOGGER.debug(
                "Recording a new user instance with fields login [{}] and type [{}]",
                user.getLogin(),
                user.getType()
        );

        User newUser = repository.save(user);

        if (Objects.isNull(newUser.getId())) {
            throw new IllegalStateException(
                    "The new user just recorded should have its id property filled, but it is null."
            );
        }

        return newUser;
    }

    @Override
    public void checkForNonNullFieldsOnInsert(String login, String type) throws InvalidSuppliedDataException {
        if (Objects.isNull(login)) {
            throw new InvalidSuppliedDataException("Field 'login' is mandatory");
        }
        if (Objects.isNull(type)) {
            throw new InvalidSuppliedDataException("Field 'type' is mandatory");
        }
    }

    @Override
    public UserType parseUserType(String type) throws InvalidSuppliedDataException {
        try {
            return UserType.valueOf(type);
        } catch (IllegalArgumentException exception) {
            throw new InvalidSuppliedDataException(
                    "Field 'type' must be either " + UserType.customer + " or " + UserType.stock
            );
        }
    }

    @Override
    @Cacheable(value = CACHE_KEY, key = "{#user.login, #user.type}")
    public User retrieveUser(User user) throws UserNotFoundException {
        LOGGER.debug(
                "Retrieving user for login, with fields login [{}] and type [{}]",
                user.getLogin(),
                user.getType()
        );

        return repository.findByLoginAndType(
                user.getLogin(),
                user.getType()
        )
        .orElseThrow(() ->
            new UserNotFoundException(
                    "No user could be found for the provided login [" + user.getLogin() +"]" +
                            " and type [" + user.getType() + "]"
            )
        );
    }

    @Override
    public void checkForValidLogin(String login) throws InvalidSuppliedDataException {
        if (!Pattern.compile("^([a-z]){4}$").matcher(login).matches()) {
            throw new InvalidSuppliedDataException("Login must be 4 characters alphabetic.");
        }
    }

}
