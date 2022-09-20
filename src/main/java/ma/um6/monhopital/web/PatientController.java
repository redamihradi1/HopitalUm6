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
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

@Controller
@AllArgsConstructor
public class PatientController {

    private PatientRepository patientRepository;
    private MedecinRepository medecinRepository;


    @GetMapping(path = "/user/index")
    public String patients(Model model,
                           @RequestParam(name = "page",defaultValue = "0") int page ,
                           @RequestParam(name = "size",defaultValue = "5") int size,
                           @RequestParam(name = "keyword",defaultValue = "") String  keyword){
        Page<Patient> pagePatients = patientRepository.findByNomContains(keyword,PageRequest.of(page,size));
        model.addAttribute("listPatients" , pagePatients.getContent());
        model.addAttribute("pages",new int[pagePatients.getTotalPages()] );
        model.addAttribute("currentPage",page);
        model.addAttribute("keyword",keyword);
        return "patient/patients";
    }
    @GetMapping(path = "/admin/delete")
    public String delete(Long id , String keyword , int page){
        patientRepository.deleteById(id);
        return "redirect:/user/index?page="+page+"&keyword="+keyword;
    }
    @GetMapping(path = "/")
    public String home(){
        return "redirect:/user/index";
    }

    @GetMapping(path = "/user/patients")
    @ResponseBody
    public  List<Patient> listPatients(){
        return  patientRepository.findAll();
    }

    @GetMapping(path = "/admin/formPatient")
    public String formPatient(Model model,String nomMedecin){
        model.addAttribute("patient",new Patient());
        model.addAttribute("nomMedecin",nomMedecin);
        return "patient/formPatient";
    }
    @PostMapping(path = "/admin/save")
    public String save(Model model , @Valid Patient patient,String nomMedecin, BindingResult bindingResult,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "") String keyword){
        if (bindingResult.hasErrors()) return "formPatient";
        Medecin medecin = medecinRepository.findByNom(nomMedecin);

        if (medecin!=null){
            patient.setMedecin(medecin);
            patientRepository.save(patient);
            return "redirect:/user/index?page"+page+"keyword"+keyword;

        }
        else {
            patientRepository.save(patient);
            return "redirect:/user/index?page"+page+"keyword"+keyword;
        }


    }

    @GetMapping(path = "/admin/editPatient")
    public String edit(Model model,Long id ,String keyword ,String nomMedecin, int page){
        Patient patient = patientRepository.findById(id).orElse(null);
        if (patient==null) throw new RuntimeException("Patient introuvable");
        if(patient.getMedecin()==null){
            model.addAttribute("patient",patient);
            model.addAttribute("page",page);
            model.addAttribute("keyword",keyword);
            model.addAttribute("nomMedecin",nomMedecin);
            return "patient/editPatient1";
        }else{
            model.addAttribute("patient",patient);
            model.addAttribute("page",page);
            model.addAttribute("keyword",keyword);
            model.addAttribute("nomMedecin",nomMedecin);
            return "patient/editPatient";
        }

    }


    @GetMapping("admin/atribuerPatientMedecin")
    public String atribuerPatientMedecin(Model model,Long id,String nomMedecin ){
        Patient patient = patientRepository.findById(id).orElse(null);
        if (patient==null) throw new RuntimeException("Patient introuvable");
        model.addAttribute("patient",patient);
        model.addAttribute("nomMedecin",nomMedecin);
        return "patient/atribuerPatientMedecin";
    }
    @PostMapping(path = "/admin/savePatientMedecin")
    public String savePatientMedecin(Model model ,Long id, String nomMedecin){
        System.out.println(nomMedecin);

        Patient patient = patientRepository.findById(id).orElse(null);
        if (patient==null) throw new RuntimeException("Patient introuvable");
        Medecin medecin = medecinRepository.findByNom(nomMedecin);
        if (medecin==null) {
            return "redirect:/user/index";
        }else {
            patient.setMedecin(medecin);
            patientRepository.save(patient);
            return "redirect:/user/index";
        }

    }

}
