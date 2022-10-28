package mg.softlab.sirh.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/admins")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    private final AdminService adminService;
}
