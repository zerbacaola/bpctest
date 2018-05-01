package ustinov.sergey.bpctest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static ustinov.sergey.bpctest.SafeGetter.EMPTY;

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
            log.error(format("Error occurred during search by person. %s", filter), e);
            return new JsonResult<>(
                JsonResult.Result.ERROR,
                Collections.emptyList(),
                e.getMessage()
            );
        }
    }

    @RequestMapping(
        method = RequestMethod.GET,
        value = "/getPhoto",
        produces = "application/json"
    )
    public JsonResult<String> getPhotoByPersonId(@RequestParam String id) {
        try {
            byte[] rawData = facade.getPhoto(id);
            String photo = ofNullable(rawData)
                .map(d -> Base64.getEncoder().encodeToString(d))
                .orElse(null);
            return new JsonResult<>(photo);
        } catch (Exception e) {
            log.error(format("Error occurred during fetching person photo. PersonId: %s", id), e);
            return new JsonResult<>(
                JsonResult.Result.ERROR,
                EMPTY,
                e.getMessage()
            );
        }
    }
}