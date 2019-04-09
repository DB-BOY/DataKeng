package cn.dbboy.flume;


import org.apache.commons.lang.StringUtils;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <br>
 * Created by DB_BOY on 2019/4/3.
 * <br>
 * 使用说明：
 * <br>
 * ======================================================
 * <br>
 * # 定义拦截器<br>
 * agent.sources.kafkaSource.interceptors = i1<br>
 * <p>
 * # 设置拦截器类型<br>
 * <p>
 * # key:value  会把日志中的key替换为value<br>
 * <p>
 * agent.sources.kafkaSource.interceptors.i1.type = cn.dbboy.flume.LogInterceptor$Builder
 * <br>
 * agent.sources.kafkaSource.interceptors.i1.pattern =before:after
 * ======================================================
 */
public class LogInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(LogInterceptor.class);

    /**
     * 需要替换的字符串信息
     * 格式："key:value,key:value"
     */
    private final String patternParm;
    private String[] splits;
    private String[] key_value;
    private String key;
    private String value;
    private HashMap<String, String> hashMap = new HashMap<String, String>();
    private Pattern compile = Pattern.compile("\"type\":\"(\\w+)\"");
    private Matcher matcher;
    private String group;

    private LogInterceptor(String patternkey) {
        logger.info("\n-------------LogInterceptor: " + patternkey);
        this.patternParm = patternkey;
    }

    /**
     * 初始化，执行一次
     */
    @Override
    public void initialize() {
        logger.info("\n-------------init");
        try {
            if (StringUtils.isNotBlank(patternParm)) {
                splits = patternParm.split(",");
                for (String key_value_pair : splits) {
                    key_value = key_value_pair.split(":");
                    key = key_value[0];
                    value = key_value[1];
                    hashMap.put(key, value);
                }
            }
        } catch (Exception e) {
            logger.error("数据格式错误，初始化失败。" + patternParm, e.getCause());
        }

    }

    @Override
    public void close() {
        logger.info("\n-------close");
    }


    /**
     * 具体的处理逻辑
     *
     * @param event
     * @return
     */
    public Event intercept(Event event) {
        logger.info("\n----------------keyset--------------------------");
        Map<String, String> headers = event.getHeaders();
        Iterator<Map.Entry<String, String>> iterator = headers.entrySet().iterator();
        Map.Entry<String, String> entry;
        while (iterator.hasNext()) {
            entry = iterator.next();
            logger.info(entry.getKey() + " " + entry.getValue());
        }
        logger.info("\n-------------------end-----------------------");


        //具体处理逻辑
        try {
            String origBody = new String(event.getBody());

            matcher = compile.matcher(origBody);
            if (matcher.find()) {
                group = matcher.group(1);
                if (StringUtils.isNotBlank(group)) {
                    String value = hashMap.get(group);
                    if (StringUtils.isNotBlank(value)) {
                        String newBody = origBody.replaceAll("\"type\":\"" + group + "\"", "\"type\":\"" + value + "\"");
                        event.setBody(newBody.getBytes());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("拦截器处理失败！", e.getCause());
        }
        return event;
    }


    @Override
    public List<Event> intercept(List<Event> events) {
        logger.info("-----------intercept----------------");
        for (Event event : events) {
            intercept(event);
        }
        return events;
    }

    public static class Builder implements Interceptor.Builder {
        private static final String PATTERN_KEY = "pattern";

        private String patternkey;

        @Override
        public void configure(Context context) {
            patternkey = context.getString(PATTERN_KEY);
            if (StringUtils.isEmpty(patternkey)) {
                throw new IllegalArgumentException("Must supply a valid search pattern " + PATTERN_KEY + " (may not be empty)");
            }
        }

        @Override
        public Interceptor build() {
            if (patternkey == null) {
                new NullPointerException("Regular expression patternkey required");
            }
            return new LogInterceptor(patternkey);
        }

    }
}