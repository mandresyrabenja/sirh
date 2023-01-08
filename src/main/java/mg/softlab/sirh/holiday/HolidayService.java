package mg.softlab.sirh.holiday;

import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class HolidayService {
    private final HolidayRepository holidayRepository;

    public Holiday createHoliday(Holiday holiday) {
        return holidayRepository.save(holiday);
    }

    public Page<Holiday> findAllHolidays(Pageable pageable) {
        return holidayRepository.findAll(pageable);
    }

    public List<Holiday> findAllHolidays() {
        return holidayRepository.findAll();
    }

    public Holiday findById(Long holidayId) {
        return holidayRepository.findById(holidayId).orElseThrow(() ->
                new IllegalStateException("Aucun jour ferié n'a " + holidayId + " comme ID"));
    }

    @Transactional
    public void updateHoliday(Long holidayId, LocalDate date, String name) {
        Holiday holiday = findById(holidayId);
        if(null != date) {
            holiday.setDate(date);
        }
        if (!Strings.isNullOrEmpty(name)) {
            holiday.setName(name);
        }
    }

    public void deleteHoliday(Long holidayId) {
        holidayRepository.delete(findById(holidayId));
    }

    public Page<Holiday> searchHoliday(String name, Pageable pageable) {
        return holidayRepository.searchHoliday(name, pageable);
    }
}
