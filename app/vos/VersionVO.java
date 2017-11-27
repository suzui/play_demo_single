package vos;

import java.io.IOException;

import enums.AppType;
import org.apache.commons.io.FileUtils;

import annotations.DataField;
import enums.ClientType;
import play.Logger;
import play.Play;

public class VersionVO extends OneData {
    
    @DataField(name = "版本号")
    public String version = "";
    @DataField(name = "下载地址")
    public String downloadUrl = "";
    @DataField(name = "更新说明")
    public String updateIntro = "";
    @DataField(name = "是否强制更新")
    public Integer isForcedUpdate = 0;
    
    public VersionVO() {
    }
    
    public VersionVO(AppType app, ClientType client) {
        try {
            for (String line : FileUtils.readLines(
                    Play.getFile("/documentation/version/" + "VERSION_" + app.name() + "_" + client.name()), "utf8")) {
                String key = line.substring(0, line.indexOf(":"));
                String value = line.substring(line.indexOf(":") + 1, line.length());
                if ("version".equals(key)) {
                    this.version = value;
                }
                if ("downloadUrl".equals(key)) {
                    this.downloadUrl = value;
                }
                if ("updateIntro".equals(key)) {
                    this.updateIntro = value;
                }
                if ("isForcedUpdate".equals(key)) {
                    this.isForcedUpdate = Integer.parseInt(value);
                }
            }
        } catch (IOException e) {
            Logger.error("versionerror:%s", e.getMessage());
        }
    }
}