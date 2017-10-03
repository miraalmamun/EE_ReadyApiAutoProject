package Utility.JIRA;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZephyrCloudRestClient {

    private JwtGenerator jwtGenerator;

    private Logger log = LoggerFactory.getLogger(ZephyrCloudRestClient.class);

    private ZephyrCloudRestClient() {
    }

    public static Builder  restBuilder(String zephyrBaseUrl, String accessKey, String secretKey, String userName){
        return new ZephyrCloudRestClient().new Builder(zephyrBaseUrl, accessKey, secretKey, userName);
    }

    public JwtGenerator getJwtGenerator(){
        return jwtGenerator;
    }

    public class Builder {

        private String accessKey;
        private String secretKey;
        private String userName;
        private String zephyrBaseUrl;

        private Builder(String zephyrBaseUrl, String accessKey, String secretKey, String userName) {
            this.zephyrBaseUrl = zephyrBaseUrl;
            this.accessKey = accessKey;
            this.secretKey = secretKey;
            this.userName = userName;
        }

        public ZephyrCloudRestClient build() {
            ZephyrConfig zConfig = new ZephyrConfig(accessKey, secretKey, userName, zephyrBaseUrl);
            jwtGenerator = new JwtGeneratorImpl(zConfig);

            return ZephyrCloudRestClient.this;
        }
    }
}
