package ma.um6.monhopital.web;

import lombok.AllArgsConstructor;
import ma.um6.monhopital.entities.Patient;
import ma.um6.monhopital.entities.Personnel;
import ma.um6.monhopital.repositories.PatientRepository;
import ma.um6.monhopital.repositories.PersonnelRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

@Controller
@AllArgsConstructor
public class PersonnelController {
    private PersonnelRepository personnelRepository;

    @GetMapping(path = "/user/indexPersonnel")
    public String personnels(Model model,
                           @RequestParam(name = "page",defaultValue = "0") int page ,
                           @RequestParam(name = "size",defaultValue = "5") int size,
                           @RequestParam(name = "keyword",defaultValue = "") String  keyword){
        Page<Personnel> pagePersonnels = personnelRepository.findByNomContains(keyword, PageRequest.of(page,size));
        model.addAttribute("listPersonnels" , pagePersonnels.getContent());
        model.addAttribute("pages",new int[pagePersonnels.getTotalPages()] );
        model.addAttribute("currentPage",page);
        model.addAttribute("keyword",keyword);
        return "personnel/personnels";
    }
    @GetMapping(path = "/admin/formPersonnel")
    public String formPersonnel(Model model){
        model.addAttribute("personnel",new Personnel());
        return "personnel/formPersonnel";
    }
    @PostMapping(path = "/admin/savePersonnel")
    public String savePersonnel(Model model , @Valid Personnel personnel, BindingResult bindingResult,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "") String keyword){
        if (bindingResult.hasErrors()) return "formPersonnel";
        personnelRepository.save(personnel);
        return "redirect:/user/indexPersonnel?page"+page+"keyword"+keyword;
    }
    @GetMapping(path = "/admin/deletePersonnel")
    public String deletePersonnel(Long id , String keyword , int page){
        personnelRepository.deleteById(id);
        return "redirect:/user/indexPersonnel?page="+page+"&keyword="+keyword;
    }
    @GetMapping(path = "/admin/editPersonnel")
    public String editPersonnel(Model model,Long id ,String keyword , int page){
        Personnel personnel = personnelRepository.findById(id).orElse(null);
        if (personnel==null) throw new RuntimeException("Personnel introuvable");
        model.addAttribute("personnel",personnel);
        model.addAttribute("page",page);
        model.addAttribute("keyword",keyword);

        return "personnel/editPersonnel";
    }
}
