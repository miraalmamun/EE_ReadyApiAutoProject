package Utility.JIRA;

import com.atlassian.connect.play.java.AcHost;
import com.atlassian.fugue.Option;
import com.google.common.collect.ImmutableList;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.util.List;
import java.util.Map;

public class ZephyrConfig extends PropertiesConfiguration {

    public Option<String> USER_NAME;
    public String JIRA_HOST_KEY;
    public String JIRA_BASE_URL;
    public String JIRA_SHARED_SECRET;

    public String ZEPHYR_BASE_URL;
    public String APP_KEY;
    public String SECRET_KEY;
    public String ACCESS_KEY;
    public AcHost host;

    final List<String> reqdConfigKeys = ImmutableList.<String>builder().add("userName").add("jiraHostKey").add("jiraBaseURL").add("sharedSecret").add("zephyrBaseURL").add("accessKey").add("secretKey").add("appKey").build();

    private ZephyrConfig(){
    }

    public  ZephyrConfig(String accessKey, String secretKey, String userName, String zephyrBaseUrl) {
        JIRA_HOST_KEY = accessKey;
        JIRA_SHARED_SECRET = secretKey;

        ZEPHYR_BASE_URL = zephyrBaseUrl;
        ACCESS_KEY = accessKey;
        USER_NAME = Option.option(userName);

        host = new AcHost();
        host.setKey(JIRA_HOST_KEY);
        host.setBaseUrl(JIRA_BASE_URL);
        host.setSharedSecret(JIRA_SHARED_SECRET);

    }

    public ZephyrConfig(String fileName) throws ConfigurationException {
        super(fileName);
        configure();
    }

    public ZephyrConfig(Map<String, String> props) throws ConfigurationException {
        super();
        for(Map.Entry<String, String> prop : props.entrySet()) {
            this.addProperty(prop.getKey(), prop.getValue());
        }
        configure();
    }

    private void configure() throws ConfigurationException{
        checkMandatoryPropertiesSet();
        setLocalPropertyValues();
    }

    private void checkMandatoryPropertiesSet() throws ConfigurationException {
        for (String key : reqdConfigKeys){
            if (!this.containsKey(key)) {
                getLogger().fatal(key + "is required in ZFJ Cloud configration");
                throw new ConfigurationException(key + "is required in ZFJ Cloud configration");
            }
        }
    }

    public static class ZConfigBuilder{

        private ZephyrConfig zconfig;

        public ZConfigBuilder() {
            zconfig = new ZephyrConfig();
        }

        public ZConfigBuilder withJiraHostKey(String hostKey) {
            zconfig.addProperty("jiraHostKey", hostKey);
            return this;
        }

        public ZConfigBuilder withJIRABaseUrl(String baseUrl) {
            zconfig.addProperty("jiraBaseURL", baseUrl);
            return this;
        }

        public ZConfigBuilder withJIRASharedSecret(String sharedSecret) {
            zconfig.addProperty("sharedSecret", sharedSecret);
            return this;
        }

        public ZConfigBuilder withZephyrBaseUrl(String zephyrBaseUrl) {
            zconfig.addProperty("zephyrBaseURL", zephyrBaseUrl);
            return this;
        }

        public ZConfigBuilder withZephyrAppKey(String appKey) {
            zconfig.addProperty("appKey", appKey);
            return this;
        }

        public ZConfigBuilder withZephyrAccessKey(String accessKey) {
            zconfig.addProperty("accessKey", accessKey);
            return this;
        }

        public ZConfigBuilder withZephyrSecretKey(String secretKey) {
            zconfig.addProperty("secretKey", secretKey);
            return this;
        }

        public ZConfigBuilder withJiraUserName(String userName) {
            zconfig.addProperty("userName", userName);
            return this;
        }

        public ZephyrConfig build() throws ConfigurationException{
            zconfig.configure();
            return zconfig;
        }
    }


    private void setLocalPropertyValues() {
        JIRA_HOST_KEY = this.getString("accessKey");
        //JIRA_BASE_URL = this.getString("jiraBaseURL");
        JIRA_SHARED_SECRET = this.getString("secretKey");

        ZEPHYR_BASE_URL = this.getString("zephyrBaseURL");
        //SECRET_KEY = this.getString("secretKey");
        ACCESS_KEY = this.getString("accessKey");
        //APP_KEY = this.getString("appKey");

        USER_NAME =  Option.some(this.getString("userName"));

        host = new AcHost();
        host.setKey(JIRA_HOST_KEY);
        host.setBaseUrl(JIRA_BASE_URL);
        host.setSharedSecret(JIRA_SHARED_SECRET);

        //host.setPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCQriXlBXjuybF3+jPDcYIbRfvZFlnH0Ci8gbINGFyx/I5bGupEyXpzbB6crlFNsY3Q+c7qT7rKOR7DJVLZX2APPAzTQhe06wjfEe1RJHdIjqCOio/VxipZAZWdmae4OWNMfo5zZ+NHb69z5qJjcYDv1EY+f2t7n323UAlwmZro9QIDAQAB");
//        host.setSharedSecret("5bdcdc66-ded6-4fc7-ab38-82cb92a3986e");

    }
}
