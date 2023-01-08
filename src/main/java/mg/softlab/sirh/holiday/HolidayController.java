package mg.softlab.sirh.holiday;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("api/v1/holidays")
@RequiredArgsConstructor
@Slf4j
public class HolidayController {
    private final HolidayService holidayService;

    @GetMapping(path = "/search")
    public Page<Holiday> searchHoliday(@RequestParam String name,
                                       @RequestParam int page,
                                       @RequestParam int size) {
        return holidayService.searchHoliday(name, PageRequest.of(page, size));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> searchHoliday(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(holidayService.findById(id));
        } catch(IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping
    public ResponseEntity<String> updateHoliday(@RequestParam Long holidayId,
                                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                    LocalDate date,
                                                @RequestParam(required = false) String name)
    {
        try {
            holidayService.updateHoliday(holidayId, date, name);
            return ResponseEntity.ok("Jour ferié numero " + holidayId + " mis à jour avec succès");
        }catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping
    public Page<Holiday> getAllHolidays(@RequestParam int page,
                                        @RequestParam int size) {
        return holidayService.findAllHolidays(PageRequest.of(page, size));
    }

    @PostMapping
    public Holiday createHoliday(@RequestBody Holiday holiday) {
        return holidayService.createHoliday(holiday);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteHoliday(@RequestParam Long holidayId) {
        try {
            holidayService.deleteHoliday(holidayId);
            return ResponseEntity.ok("Jour ferié numero " + holidayId + " effacé avec succès");
        } catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
