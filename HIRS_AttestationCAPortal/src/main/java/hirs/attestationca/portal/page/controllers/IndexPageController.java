package hirs.attestationca.portal.page.controllers;

import hirs.attestationca.portal.page.PageController;
import static hirs.attestationca.portal.page.Page.INDEX;
import hirs.attestationca.portal.page.params.NoPageParams;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for the Index page.
 */
@Controller
@RequestMapping("/index")
public class IndexPageController extends PageController<NoPageParams> {

    /**
     * Constructor providing the Page's display and routing specification.
     */
    public IndexPageController() {
        super(INDEX);
    }

    /**
     * Returns the path for the view and the data model for the page.
     *
     * @param params The object to map url parameters into.
     * @param model The data model for the request. Can contain data from redirect.
     * @return the path for the view and data model for the page.
     */
    @Override
    @RequestMapping
    public ModelAndView initPage(final NoPageParams params, final Model model) {
        return getBaseModelAndView();
    }

}
