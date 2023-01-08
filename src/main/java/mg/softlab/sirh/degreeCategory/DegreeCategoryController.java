package mg.softlab.sirh.degreeCategory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/degreecategories")
@RequiredArgsConstructor
@Slf4j
public class DegreeCategoryController {
    private final DegreeCategoryService degreeCategoryService;

    @GetMapping
    public Page<DegreeCategory> getAllDegreeCategories(@RequestParam int page,
                                                       @RequestParam int size) {
        return degreeCategoryService.findAllDegreeCategories(PageRequest.of(page, size));
    }

}
