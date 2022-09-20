package ma.um6.monhopital.web;

import lombok.AllArgsConstructor;
import ma.um6.monhopital.entities.Medecin;
import ma.um6.monhopital.entities.Patient;
import ma.um6.monhopital.repositories.MedecinRepository;
import ma.um6.monhopital.repositories.PatientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
public class MedecinController {
    private MedecinRepository medecinRepository;
    private PatientRepository patientRepository;

    @GetMapping(path = "/user/indexMedecin")
    public String patients(Model model,
                           @RequestParam(name = "page",defaultValue = "0") int page ,
                           @RequestParam(name = "size",defaultValue = "5") int size,
                           @RequestParam(name = "keyword",defaultValue = "") String  keyword){
        Page<Medecin> pageMedecins = medecinRepository.findByNomContains(keyword, PageRequest.of(page,size));
        model.addAttribute("listMedecins" , pageMedecins.getContent());
        model.addAttribute("pages",new int[pageMedecins.getTotalPages()] );
        model.addAttribute("currentPage",page);
        model.addAttribute("keyword",keyword);
        return "medecin/medecins";
    }

    @GetMapping(path = "/admin/deleteMedecin")
    public String delete(Long id , String keyword , int page){
        medecinRepository.deleteById(id);
        return "redirect:/user/indexMedecin?page="+page+"&keyword="+keyword;
    }

    @GetMapping(path = "/admin/formMedecin")
    public String formMedecin(Model model,String nomPatient){
        model.addAttribute("medecin",new Medecin());
        model.addAttribute("nomPatient",nomPatient);
        return "medecin/formMedecin";
    }

    @PostMapping(path = "/admin/saveMedecin")
    public String save(Model model , @Valid Medecin medecin,String nomPatient, BindingResult bindingResult,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "") String keyword){
        if (bindingResult.hasErrors()) return "formMedecin";
        Patient patient = patientRepository.findByNom(nomPatient);
        if (patient != null){
            medecin.ajouterPatient(patient);
            medecinRepository.save(medecin);
        }else {
            medecinRepository.save(medecin);
        }

        return "redirect:/user/indexMedecin?page"+page+"keyword"+keyword;
    }

    @GetMapping(path = "/admin/editMedecin")
    public String edit(Model model,Long id ,String keyword , int page){
        Medecin medecin = medecinRepository.findById(id).orElse(null);
        if (medecin==null) throw new RuntimeException("Medecin introuvable");
        model.addAttribute("medecin",medecin);
        model.addAttribute("page",page);
        model.addAttribute("keyword",keyword);

        return "medecin/editMedecin";
    }
    @PostMapping(path = "/admin/saveEditMedecin")
    public String saveEditMedecin(Model model , @Valid Medecin medecin, BindingResult bindingResult,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "") String keyword){
        if (bindingResult.hasErrors()) return "formMedecin";
        medecinRepository.save(medecin);
        return "redirect:/user/indexMedecin?page"+page+"keyword"+keyword;
    }
}
