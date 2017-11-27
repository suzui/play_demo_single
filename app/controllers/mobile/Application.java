package controllers.mobile;

import play.db.jpa.Transactional;

public class Application extends ApiController {
    
    @Transactional(readOnly = true)
    public static void index() {
        renderHtml("mobile api...");
    }
    
    
}
