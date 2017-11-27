package models;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import jpaListeners.BaseModelListener;
import play.db.jpa.Model;

@MappedSuperclass
@EntityListeners(BaseModelListener.class)
public class BaseModel extends Model {
    
    @Version
    public long version;
    
    public boolean deleted = false;
    
    public long createTime = System.currentTimeMillis();
    
    public long updateTime = System.currentTimeMillis();
    
    private static final String AND = " and ";
    private static final String FROM = " from ";
    private static final String WHERE = " where ";
    private static final String FROM_WHERE_PATTERN = "from\\s([\\S].*?)\\swhere\\s";
    
    private static String defaultCondition() {
        return "deleted=false";
    }
    
    public static String defaultSql() {
        return defaultCondition();
    }
    
    public static String defaultSql(String originStr) {
        String originSql = originStr;
        if (StringUtils.containsIgnoreCase(originSql, FROM)) {
            if (StringUtils.containsIgnoreCase(originSql, WHERE)) {
                Pattern pattern = Pattern.compile(FROM_WHERE_PATTERN, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(originSql);
                while (matcher.find()) {
                    String tableName = matcher.group(1);
                    String string = tableName.contains(" ") ? tableName.substring(tableName.lastIndexOf(' ') + 1) + '.'
                            : "";
                    String newSqlString = string + defaultCondition() + AND;
                    String originString = matcher.group();
                    originSql = originSql.replace(originString, originString + newSqlString);
                }
            } else {
                originSql = originSql + WHERE + defaultCondition();
            }
        } else {
            originSql = defaultCondition() + (StringUtils.isBlank(originSql) ? "" : AND) + originSql;
        }
        return originSql;
    }
    
    public void logicDelete() {
        this.deleted = true;
        this.save();
    }
    
    public Date createTime() {
        return new Date(this.createTime);
    }
    
    public Date updateTime() {
        return new Date(this.updateTime);
    }
    
    public static Boolean convert(int b) {
        return BooleanUtils.toBooleanObject(b, 1, 0, -1);
    }
}
