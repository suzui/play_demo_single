package jobs;

import com.qiniu.util.Auth;

import play.Play;
import play.cache.Cache;
import play.jobs.Every;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

@Every("30mn")
@OnApplicationStart
public class FreshQiniuTokenJob extends Job {

	@Override
	public void doJob() throws Exception {
		initQiniu();
	}

	private void initQiniu() {
		String accessKey = Play.configuration.getProperty("qiniu.accessKey");
		String secretKey = Play.configuration.getProperty("qiniu.secretKey");
		String bucket = Play.configuration.getProperty("qiniu.bucket");
		Auth auth = Auth.create(accessKey, secretKey);
		String upToken = auth.uploadToken(bucket, null, 86400, null);
		Cache.set("qiniuUptoken", upToken);
	}

}
