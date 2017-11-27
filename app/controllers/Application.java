package controllers;

import play.db.jpa.NoTransaction;
import utils.AmapUtils;

public class Application extends BaseController {
    
    @NoTransaction
    public static void index() {
        renderHtml("start...");
    }
}