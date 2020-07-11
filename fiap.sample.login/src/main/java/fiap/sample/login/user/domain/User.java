package fiap.sample.login.user.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "users")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private String login;
    private UserType type;

    public User() {
    }

    public User(String login, UserType type) {
        this.login = login;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public UserType getType() {
        return type;
    }
}
