package univ.exam.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {
    private String name;
    private boolean isSuspicious;

    public User(String user, boolean b) {
        this.name = user;
        this.isSuspicious = b;
    }
}
