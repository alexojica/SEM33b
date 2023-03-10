package rowing.notification.controllers;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import rowing.commons.models.NotificationRequestModel;
import rowing.notification.authentication.AuthManager;
import rowing.notification.domain.notification.NotifyUserService;

/**
 * Hello World example controller.
 * <p>
 * This controller shows how you can extract information from the JWT token.
 * </p>
 */
@Data
@RestController
public class DefaultController {
    @Autowired
    private transient NotifyUserService notifyUserService;

    private final transient AuthManager authManager;

    /**
     * Instantiates a new controller.
     *
     * @param authManager Spring Security component used to authenticate and authorize the user
     */
    @Autowired
    public DefaultController(AuthManager authManager) {
        this.authManager = authManager;
    }

    /**
     * Notifies the user.
     *
     * @return if notifying the user was successful
     */
    @PostMapping("/notify")
    public ResponseEntity notifyUser(@RequestBody NotificationRequestModel request) {
        try {
            notifyUserService.notifyUser(request);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.toString());
        }

        return ResponseEntity.ok().build();
    }
}
