
package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.sun.corba.se.impl.encoding.MarshalInputStream;
import play.libs.WS;
import play.libs.WS.HttpResponse;

import java.io.IOException;
import java.util.List;

public class AmapUtils {
    
    public class AmapResult {
        public String status;
        public String info;
        public String infocode;
        public List<AreaResult> districts;
    }
    
    public class AreaResult {
        public String adcode;
        public String name;
        public String center;
        public String level;
        public List<AreaResult> districts;
    }
    
    public static AmapResult getAll() {
        HttpResponse response = WS.url("http://restapi.amap.com/v3/config/district?")
                .setParameter("key", "fcd8bef6e2a1afeb1b6de589241a6148").setParameter("subdistrict", 2).get();
        return new Gson().fromJson(response.getJson(), AmapResult.class);
    }
}
