package ma.um6.monhopital;


import ma.um6.monhopital.security.service.SecurityService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class MonHopitalApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonHopitalApplication.class, args);
    }

    //@Bean
    CommandLineRunner saveUsers(SecurityService securityService ){
        return args -> {
            securityService.saveNewUser("admin","admin","admin");

            securityService.saveNewRole("USER","");
            securityService.saveNewRole("ADMIN","hada ana xD");

            securityService.addRoleToUSer("admin","ADMIN");
            securityService.addRoleToUSer("admin","USER");
        };
    }


    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
