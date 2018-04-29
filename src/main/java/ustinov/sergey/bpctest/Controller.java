package ustinov.sergey.bpctest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

import static java.lang.String.format;

@RestController
public class Controller {
    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private PersonFacade facade;

    @RequestMapping(
        method = RequestMethod.POST,
        value = "/search",
        produces = "application/json",
        consumes = "application/json"
    )
    public JsonResult<List<PersonTO>> searchByfilter(@RequestBody @Valid PersonFilterTO filter) {
        try {
            return new JsonResult<>(facade.search(filter));
        } catch (Exception e) {
            log.error(format("Error occured durring search by Person. %s", filter), e);
            return new JsonResult<>(
                JsonResult.Result.ERROR,
                Collections.emptyList(),
                e.getMessage()
            );
        }
    }
}