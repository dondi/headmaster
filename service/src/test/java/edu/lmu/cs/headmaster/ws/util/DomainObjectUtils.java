package edu.lmu.cs.headmaster.ws.util;

import java.util.ArrayList;
import java.util.List;

import edu.lmu.cs.headmaster.ws.domain.Role;
import edu.lmu.cs.headmaster.ws.domain.User;
import edu.lmu.cs.headmaster.ws.domain.UserRole;

/**
 * Holder for utility methods of use to multiple unit tests.
 */
public class DomainObjectUtils {

    /**
     * Helper factory method for creating new user objects.
     */
    public static User createUserObject(String login, String email, String challenge, Role... roles) {
        User user = new User();
        user.setLogin(login);
        user.setEmail(email);
        user.setChallengeRequest(challenge);

        List<UserRole> userRoles = new ArrayList<UserRole>();
        for (Role role: roles) {
            UserRole userRole = new UserRole();
            userRole.setUser(user);
            userRole.setRole(role);
            userRoles.add(userRole);
        }
        
        user.setRoles(userRoles);
        return user;
    }

}
