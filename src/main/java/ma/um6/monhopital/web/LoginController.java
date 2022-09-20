package ma.um6.monhopital.web;

import ma.um6.monhopital.entities.Patient;
import ma.um6.monhopital.security.entities.AppUser;
import ma.um6.monhopital.security.repositories.AppUserRepository;
import ma.um6.monhopital.security.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class LoginController {
    AppUserRepository appUserRepository;
    @Autowired
    SecurityService securityService;
    @GetMapping("/login")
    public String login(){
        return "connexion/login";
    }
    @GetMapping("/logout")
    public String logout(){
        return  "redirect:/login";
    }


    @GetMapping(path = "/register")
    public String formUser(Model model){
        model.addAttribute("user", new RegistrationClass());
        return "connexion/register";
    }
    @PostMapping(path = "/register/newUser")
    public String save(Model model , @Valid RegistrationClass registrationClass, BindingResult bindingResult){
        if (bindingResult.hasErrors()) return "formUser";
        String uname = registrationClass.getUsername();
        String pwd = registrationClass.getPassword();
        securityService.saveNewUser(uname,pwd,pwd);
        securityService.addRoleToUSer(uname,"USER");
        return "redirect:/user/index";
    }
    @GetMapping(path = "/admin/registerAdmin")
    public String formAdmin(Model model){
        model.addAttribute("user", new RegistrationClass());
        return "connexion/adminRegister";
    }
    @PostMapping(path = "/admin/newAdmin")
    public String saveAdmin(Model model , @Valid RegistrationClass registrationClass, BindingResult bindingResult){
        if (bindingResult.hasErrors()) return "formUser";
        String uname = registrationClass.getUsername();
        String pwd = registrationClass.getPassword();
        securityService.saveNewUser(uname,pwd,pwd);
        securityService.addRoleToUSer(uname,"ADMIN");
        return "redirect:/user/index";
    }



}
