package rowing.authentication.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rowing.authentication.domain.user.CredentialRepository;
import rowing.authentication.domain.user.Username;

import java.util.ArrayList;

/**
 * User details service responsible for retrieving the user from the DB.
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final transient CredentialRepository credentialRepository;

    @Autowired
    public JwtUserDetailsService(CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
    }

    /**
     * Loads user information required for authentication from the DB.
     *
     * @param username The username of the user we want to authenticate
     * @return The authentication user information of that user
     * @throws UsernameNotFoundException Username was not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var optionalUser = credentialRepository.findByUsername(new Username(username));

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User does not exist");
        }

        var user = optionalUser.get();

        return new User(user.getUsername().toString(), user.getPassword().toString(),
                new ArrayList<>()); // no authorities/roles
    }
}
