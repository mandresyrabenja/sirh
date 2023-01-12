package mg.softlab.sirh.candidate;


import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mg.softlab.sirh.jobOffer.JobOffer;
import mg.softlab.sirh.jobOffer.JobOfferService;
import mg.softlab.sirh.person.Person;
import mg.softlab.sirh.person.PersonService;
import mg.softlab.sirh.util.file.File;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

@RestController
@RequestMapping("api/v1/candidates")
@RequiredArgsConstructor
@Slf4j
public class CandidateController {
    private final CandidateService candidateService;
    private final JobOfferService jobOfferService;
    private final PersonService personService;
    @Value("${upload.location}")
    private String FILE_DIRECTORY;


    /**
     * Avoir le fichier PDF contenant le LM d'un candidat
     * @param id ID du candidat
     * @return le fichier PDF contenant le LM du candidat
     */
    @GetMapping(path = "/{id}/pdf-cover-letter", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] getCoverLetterPdf(@PathVariable("id") Long id) {
        try {
            return Files.readAllBytes(
                    Paths.get(FILE_DIRECTORY, "candidates", "lm",  id.toString() + ".pdf")
            );
        } catch (IOException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    @GetMapping(path = "/{id}/pdf-cover-letter-path")
    public String getCoverLetterPdfPath(@PathVariable("id") Long id) {
        try {
            return Paths.get(FILE_DIRECTORY, "candidates", "lm",  id.toString() + ".pdf").toAbsolutePath().toString();
        } catch (InvalidPathException | SecurityException e) {
            log.warn(e.getMessage());
            return e.getMessage();
        }
    }

    /**
     * Avoir le fichier PDF contenant le CV d'un candidat
     * @param id ID du candidat
     * @return le fichier PDF contenant le CV du candidat
     */
    @GetMapping(path = "/{id}/pdf-cv", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] getCvPdf(@PathVariable("id") Long id) {
        try {
            return Files.readAllBytes(
                    Paths.get(FILE_DIRECTORY, "candidates", "cv",  id.toString() + ".pdf")
            );
        } catch (IOException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    @GetMapping(path = "/{id}/pdf-cv-path")
    public String getCvPdfPath(@PathVariable("id") Long id) {
        try {
            return Paths.get(FILE_DIRECTORY, "candidates", "cv",  id.toString() + ".pdf").toAbsolutePath().toString();
        } catch (InvalidPathException | SecurityException e) {
            log.warn(e.getMessage());
            return e.getMessage();
        }
    }


    /**
     * Ajouter un fichier PDF contenant la lettre de motivation d'un candidat
     * @param candidateId ID du candidat
     * @param coverLetter Fichier PDF contenant la lettre de motivation
     * @return Reponse HTTP indiquant le succès ou l'echec de l'opération
     */
    @PutMapping(path = "{id}/pdf-cover-letter")
    public ResponseEntity<String> addCandidateCoverLetterPdf(@PathVariable("id") Long candidateId,
                                                             @RequestParam("coverLetter") MultipartFile coverLetter)
    {
        try {
            if(!candidateService.existsById(candidateId)) {
                throw new IllegalStateException("Aucun candidate n'a " + candidateId + " comme ID");
            }

            // Le fichier doit être un PDF
            if( !".pdf".equals(Strings.commonSuffix(".pdf", coverLetter.getOriginalFilename())) ) {
                throw new IllegalStateException("Le LM doit être un fichier PDF");
            }

            File.saveFile(coverLetter, FILE_DIRECTORY +"/candidates/lm", candidateId + ".pdf");

            return ResponseEntity.ok("LM PDF du candidat numero " + candidateId + " ajouté avec succès");
        } catch (IllegalStateException | IOException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * Ajouter un fichier PDF contenant le CV d'un candidat
     * @param candidateId ID du candidat
     * @param cv Fichier PDF contenant le CV
     * @return Reponse HTTP indiquant le succès ou l'echec de l'opération
     */
    @PutMapping(path = "{id}/pdf-cv")
    public ResponseEntity<String> addCandidatePdfCv(@PathVariable("id") Long candidateId,
                                                    @RequestParam("cv") MultipartFile cv) {
        try {
            if(!candidateService.existsById(candidateId)) {
                throw new IllegalStateException("Aucun candidate n'a " + candidateId + " comme ID");
            }

            // Le fichier doit être un PDF
            if( !".pdf".equals(Strings.commonSuffix(".pdf", cv.getOriginalFilename())) ) {
                throw new IllegalStateException("Le cv doit être un fichier PDF");
            }

            File.saveFile(cv, FILE_DIRECTORY +"/candidates/cv", candidateId + ".pdf");

            return ResponseEntity.ok("CV PDF du candidat numero " + candidateId + " ajouté avec succès");
        } catch (IllegalStateException | IOException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("/choosen")
    public Page<Candidate> getChoosenCandidates(@RequestParam Long offerId,
                                                @RequestParam int page,
                                                @RequestParam int size) {
        try {
            return candidateService.findChoosenCandidates(offerId, PageRequest.of(page, size));
        } catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return Page.empty();
        }
    }

    @PutMapping("/{id}/choose-candidate")
    public ResponseEntity<String> chooseCandidate(@PathVariable("id") Long candidateId) {
        try {
            candidateService.chooseCandidate(candidateId);
            return ResponseEntity.ok("Candidate numero " + candidateId + " choisi avec succès");
        } catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(candidateService.findById(id));
        } catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Trier les candidats d'une offre d'emploi par dimplôme, expérience ou age
     * @param jobOfferId ID de l'offre d'emploi<br>
     * @param criteria chaîne de caractère :<br>
     *                 <ul>
     *                  <li><b>"degree"</b> pour diplôme</li>
     *                  <li><b>"experience"</b> pour l'expérience</li>
     *                  <li><b>"age"</b> pour le plus jeune au plus âgé</li>
     *                 </ul>
     * @return Liste des candidats de l'offre d'emploi triée par dimplôme, expérience ou âge
     */
    @GetMapping("/sort")
    public Page<Candidate> sortCandidate(@RequestParam Long jobOfferId,
                                         @RequestParam String criteria,
                                         @RequestParam int page,
                                         @RequestParam int size) {
        return candidateService.sortCandidate(jobOfferId, criteria, PageRequest.of(page, size));
    }

    @GetMapping
    public Page<Candidate> getCandidatesByJobOffer(@RequestParam Long jobOfferId,
                                                   @RequestParam int page,
                                                   @RequestParam int size) {
        try {
            JobOffer jobOffer = jobOfferService.findById(jobOfferId);
            return candidateService.findCandidateByJobOffer(jobOffer, PageRequest.of(page, size));
        } catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return Page.empty();
        }
    }

    @PostMapping
    public ResponseEntity<Object> createCandidate(@RequestParam Long jobOfferId,
                                                  @RequestParam Long personId) {
        try {
            JobOffer jobOffer = jobOfferService.findById(jobOfferId);
            Person person = personService.findById(personId);

            Candidate candidate = candidateService.createCandidate(jobOffer, person);
            log.info("Candidature pour l'offre numero " + jobOffer.getId() + " crée avec succès");
            return ResponseEntity.ok(candidate);
        } catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
        
    }
}
