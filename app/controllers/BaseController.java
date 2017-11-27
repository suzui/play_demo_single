package controllers;

import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import jobs.UpdateLoginInfoJob;
import models.person.AccessToken;
import models.person.Person;
import play.Logger;
import play.Play;
import play.cache.Cache;
import play.mvc.After;
import play.mvc.Before;
import play.mvc.Catch;
import play.mvc.Controller;
import play.mvc.Finally;
import play.mvc.Http.Cookie;
import play.mvc.Http.Header;
import play.mvc.Http.Request;
import play.mvc.Http.Response;
import play.mvc.Scope.Session;
import play.mvc.Util;
import play.mvc.With;
import vos.Result;
import vos.Result.StatusCode;

@With(DocController.class)
public class BaseController extends Controller {
    
    private static final String VO = "vo";
    private static final String DOC = "doc";
    private static final String CURRENT_PERSON_ID = "currentPersonId";
    private static final String KEEP_PERSON_ID = "keepPersonId";
    private static final String BASE_URL = Play.configuration.getProperty("application.baseUrl");
    
    @Before(priority = 0)
    static void requestInfo() {
        Logger.info("[requestInfo start]:================");
        final Request request = Request.current();
        Logger.info("[requestInfo header]:%s", request.headers.entrySet().stream()
                .map(e -> e.getKey() + ":" + e.getValue()).collect(Collectors.toList()));
        Logger.info("[requestInfo action]:%s,%s,%s,%s", request.isAjax(), request.method, request.url, request.action);
        Logger.info("[requestInfo end]:================");
    }
    
    @Before(priority = 1)
    static void randomseed() {
        Logger.info("[randomseed start]:================");
        final Request request = Request.current();
        if (request != null && "post".equalsIgnoreCase(request.method)) {
            final Header randomseed = request.headers.get("randomseed");
            if (randomseed != null) {
                Logger.info("[randomseed]:%s", randomseed);
                final String key = request.action + randomseed.value();
                if (Cache.get(key) != null) {
                    renderJSON(Result.failed(StatusCode.SYSTEM_POST_REPEAT));
                }
                Cache.add(key, true, "10mn");
            }
        }
        Logger.info("[randomseed end]:================");
    }
    
    @Before(priority = 2)
    static void params() {
        Logger.info("[params start]:================");
        final Request request = Request.current();
        request.params.put(VO, "");
        Logger.info("[params]:%s", request.params.allSimple().entrySet().stream()
                .map(e -> e.getKey() + ":" + e.getValue()).collect(Collectors.toList()));
        Logger.info("[params end]:================");
    }
    
    @Catch
    static void exception(Throwable throwable) {
        Logger.info("[exception start]:================");
        Logger.info("[exception]:%s", throwable);
        Logger.info("[exception end]:================");
        if (Play.mode.isProd()) {
            renderJSON(Result.failed());
        }
    }
    
    @After
    static void status() {
        if (!request.params._contains(DOC)) {
            Logger.info("[status start]:================");
            Logger.info("[status]:%s", response.status);
            Logger.info("[status end]:================");
        }
    }
    
    @Finally
    static void finish() {
        if (!request.params._contains(DOC)) {
            Logger.info("[finish start]:================");
            Logger.info("[finish]:%s", response.out);
            Logger.info("[finish end]:================");
        }
    }
    
    @Util
    protected static void accesstoken() {
        Logger.info("[accesstoken start]:================");
        final Request request = Request.current();
        if (request.params._contains(DOC)) {
            return;
        }
        final String accesstoken = getToken();
        if (StringUtils.isBlank(accesstoken) || AccessToken.findByAccesstoken(accesstoken) == null) {
            renderJSON(Result.failed(StatusCode.SYSTEM_TOKEN_UNVALID));
        }
        final AccessToken token = getAccessTokenByToken();
        if (token == null) {
            renderJSON(Result.failed(StatusCode.SYSTEM_TOKEN_UNVALID));
        }
        Logger.info("[accesstoken]:%s,%s,%s", token.person.id, token.person.name, token.person.username);
        final Map<String, Header> headers = request.headers;
        final String appVersion = headers.get("appversion") == null ? null : headers.get("appversion").value();
        final String appType = headers.get("apptype") == null ? null : headers.get("apptype").value();
        final String osVersion = headers.get("osversion") == null ? null : headers.get("osVersion").value();
        final String clientType = headers.get("clienttype") == null ? null : headers.get("clienttype").value();
        final String deviceToken = headers.get("devicetoken") == null ? null : headers.get("devicetoken").value();
        if (token.person.lastLoginTime == null || System.currentTimeMillis() - token.person.lastLoginTime > 10 * 60 * 1000) {
            new UpdateLoginInfoJob(accesstoken, appVersion, appType, osVersion, clientType, deviceToken);
        }
        Logger.info("[accesstoken end]:================");
    }
    
    @Util
    protected static void setPersonIdToSession(Long personId) {
        Session.current().put(CURRENT_PERSON_ID, personId + "");
    }
    
    @Util
    protected static void setPersonIdToCookie(Long personId) {
        Response.current().setCookie(KEEP_PERSON_ID, personId + "", "365d");
    }
    
    @Util
    protected static void removePersonIdToSession() {
        Session.current().remove(CURRENT_PERSON_ID);
    }
    
    @Util
    protected static void removePersonIdToCookie() {
        Response.current().removeCookie(KEEP_PERSON_ID);
    }
    
    @Util
    protected static String getPersonIdFromSession() {
        Session session = Session.current();
        return null != session && session.contains(CURRENT_PERSON_ID) ? session.get(CURRENT_PERSON_ID) : null;
    }
    
    @Util
    protected static String getPersonIdFromCookie() {
        Map<String, Cookie> cookies = Request.current().cookies;
        Cookie cookie = cookies.get(KEEP_PERSON_ID);
        if (null != cookie) {
            return cookie.value;
        }
        return null;
    }
    
    @Util
    protected static Person getCurrPerson() {
        String personId = getPersonIdFromSession();
        if (personId == null) {
            personId = getPersonIdFromCookie();
        }
        if (personId == null) {
            return null;
        }
        return Person.findByID(Long.parseLong(personId));
    }
    
    @Util
    protected static String getToken() {
        final Header accessToken = Request.current().headers.get("accesstoken");
        if (accessToken == null || StringUtils.isBlank(accessToken.value())) {
            return null;
        } else {
            return accessToken.value();
        }
    }
    
    @Util
    protected static AccessToken getAccessTokenByToken() {
        String token = getToken();
        return token == null ? null : AccessToken.findByAccesstoken(token);
    }
    
    @Util
    protected static Person getPersonByToken() {
        String token = getToken();
        return token == null ? null : AccessToken.findPersonByAccesstoken(token);
    }
    
}