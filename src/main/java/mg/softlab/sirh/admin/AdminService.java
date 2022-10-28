package mg.softlab.sirh.admin;

import lombok.AllArgsConstructor;
import mg.softlab.sirh.util.Sha256;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@AllArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;

    public Admin createAdmin(@RequestBody Admin admin) {
        admin.setPassword(Sha256.hash(admin.getPassword()));
        return adminRepository.save(admin);
    }
}
