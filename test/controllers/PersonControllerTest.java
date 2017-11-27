package controllers;

import org.junit.Test;

import play.Play;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.test.FunctionalTest;

public class PersonControllerTest extends FunctionalTest {

	public static final String BASE_URL = Play.configuration.getProperty("application.baseUrl");

	@Test
	public void testCaptcha() {
		HttpResponse response = WS.url(BASE_URL + "/captcha").get();
	}

}