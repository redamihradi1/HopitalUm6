package ma.um6.monhopital.web;

import lombok.AllArgsConstructor;
import ma.um6.monhopital.entities.DossierMedical;
import ma.um6.monhopital.entities.Patient;
import ma.um6.monhopital.repositories.DossiermedicalRepository;
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
public class DossierController {
    private DossiermedicalRepository dossiermedicalRepository;
    private PatientRepository patientRepository;
    @GetMapping(path = "/user/indexDossier")
    public String dossiers(Model model,
                             @RequestParam(name = "page",defaultValue = "0") int page ,
                             @RequestParam(name = "size",defaultValue = "5") int size,
                             @RequestParam(name = "keyword",defaultValue = "") String  keyword){
        Page<DossierMedical> pageDossiers = dossiermedicalRepository.findByRapportContains(keyword, PageRequest.of(page,size));
        model.addAttribute("listDossiers" , pageDossiers.getContent());
        model.addAttribute("pages",new int[pageDossiers.getTotalPages()] );
        model.addAttribute("currentPage",page);
        model.addAttribute("keyword",keyword);
        return "dmedical/dossiers";
    }
    @GetMapping(path = "/admin/formDossier")
    public String formDossier(Model model,String nomPatient){

        model.addAttribute("dossier",new DossierMedical());
        model.addAttribute("nomPatient",nomPatient);
        return "dmedical/formDossier";
    }
    @PostMapping(path = "/admin/saveDossier")
    public String saveDossier(Model model , @Valid DossierMedical DossierMedical, String nomPatient,BindingResult bindingResult,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "") String keyword){
        if (bindingResult.hasErrors()) return "formDossier";
        Patient patient = patientRepository.findByNom(nomPatient);
        if(patient == null) return "redirect:/admin/formDossier";
        DossierMedical dossierMedical = dossiermedicalRepository.findByPatientNom(nomPatient);
        if (dossierMedical !=null) return "redirect:/admin/formDossier";
        if (patient != null && dossierMedical==null){
                DossierMedical.setPatient(patient);
                dossiermedicalRepository.save(DossierMedical);
                return "redirect:/user/indexDossier?page"+page+"keyword"+keyword;
        }
        return "redirect:/user/indexDossier";
    }
    @GetMapping(path = "/admin/deleteDossier")
    public String deleteDossier(Long id , String keyword , int page){
        dossiermedicalRepository.deleteById(id);
        return "redirect:/user/indexDossier?page="+page+"&keyword="+keyword;
    }
    @PostMapping(path = "/admin/saveEditDossier")
    public String saveEditDossier(Model model , @Valid DossierMedical DossierMedical, String nomPatient,BindingResult bindingResult,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "") String keyword){
        if (bindingResult.hasErrors()) return "formDossier";
        Patient patient = patientRepository.findByNom(nomPatient);
        DossierMedical dossierMedical = dossiermedicalRepository.findByPatientNom(nomPatient);
        if (patient != null ){
            DossierMedical.setPatient(patient);
            dossiermedicalRepository.save(DossierMedical);
            return "redirect:/user/indexDossier?page"+page+"keyword"+keyword;
        }
        else {
            return "redirect:/user/indexDossier";
        }
    }
    @GetMapping(path = "/admin/editDossier")
    public String editDossier(Model model,Long id ,String keyword , int page){
        DossierMedical dossierMedical = dossiermedicalRepository.findById(id).orElse(null);
        if (dossierMedical==null) throw new RuntimeException("Personnel introuvable");
        model.addAttribute("dossier",dossierMedical);
        model.addAttribute("page",page);
        model.addAttribute("keyword",keyword);

        return "dmedical/editDossier";
    }

    @GetMapping(path = "/user/detailDossier")
    public String detailDossier(Model model,Long id ){
        DossierMedical dossierMedical = dossiermedicalRepository.findById(id).orElse(null);
        if (dossierMedical==null) throw new RuntimeException("Personnel introuvable");
        model.addAttribute("dossier",dossierMedical);


        return "dmedical/detailDossier";
    }

}
