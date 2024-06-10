package zingmp3.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import zingmp3.service.user.UserService;
import zingmp3.web.dto.OnCreate;
import zingmp3.web.dto.UserRequest;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping(
            value = "/register",
            consumes = {
                    MediaType.APPLICATION_FORM_URLENCODED_VALUE
            }
    )
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@ModelAttribute @Validated(OnCreate.class) UserRequest newUser) {
        System.out.println(newUser);
        userService.register(newUser);
    }
}
