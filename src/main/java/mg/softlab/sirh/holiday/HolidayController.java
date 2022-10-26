package mg.softlab.sirh.holiday;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/holidays")
@RequiredArgsConstructor
@Slf4j
public class HolidayController {
    private final HolidayService holidayService;

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
    public List<Holiday> getAllHolidays() {
        return holidayService.findAllHolidays();
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
