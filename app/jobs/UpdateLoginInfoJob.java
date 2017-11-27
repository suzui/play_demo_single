package jobs;

import enums.ClientType;
import models.person.AccessToken;
import play.jobs.Job;

public class UpdateLoginInfoJob extends Job {
    
    private String accesstoken;
    
    private String appVersion;
    private String appType;
    private String osVersion;
    private String clientType;
    private String deviceToken;
    
    public UpdateLoginInfoJob(String accesstoken, String appVersion, String appType, String osVersion,
                              String clientType, String deviceToken) {
        this.accesstoken = accesstoken;
        this.appVersion = appVersion;
        this.appType = appType;
        this.osVersion = osVersion;
        this.clientType = clientType;
        this.deviceToken = deviceToken;
    }
    
    @Override
    public void doJob() throws Exception {
        AccessToken token = AccessToken.findByAccesstoken(accesstoken);
        if (token != null) {
            token.update(appVersion,appType, osVersion, clientType,deviceToken);
        }
    }
}
