package ma.um6.monhopital.security.service;


import ma.um6.monhopital.security.entities.AppRole;
import ma.um6.monhopital.security.entities.AppUser;

public interface SecurityService {
    AppUser saveNewUser(String username , String password , String password2);
    AppRole saveNewRole(String roleName , String description);
    void addRoleToUSer(String usernmae, String roleName);
    void removeRoleFromUSer(String usernmae, String roleName);
    AppUser loadUserByUserName(String username);
}
