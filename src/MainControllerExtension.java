import com.hazelcast.map.IMap;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import com.smartfoxserver.v2.extensions.SFSExtension;
import java.util.Map;

import static com.sun.jndi.ldap.LdapPoolManager.trace;

public class MainControllerExtension extends SFSExtension {
    static final String lobbyExtensionIpAddress = "192.168.0.134";

    @Override
    public void init() {

        trace("MainControllerExtension is working!");

        addRequestHandler("getUserName", userNameReqHandler.class);
        addRequestHandler("getIpAddress", ipAddressReqHandler.class);
        HazelcastManager.getInstance().inputDataToCluster();
        /*IMap<String,String> map = HazelcastManager.getInstance().getDataFromMemory();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            trace(entry.getKey() + " " + entry.getValue());
        }*/
    }

    @Override
    public void destroy() {
        trace("destroy");
        super.destroy();
    }

    public static class userNameReqHandler extends BaseClientRequestHandler{

        @Override
        public void handleClientRequest(User sender, ISFSObject params) {
            String userName = params.getUtfString("userName");

            ISFSObject loginResObj = new SFSObject();
            loginResObj.putUtfString("loginSuccessMessage", userName + ": login succeeded");

            send("loginResult", loginResObj, sender);
        }
    }

    public static class ipAddressReqHandler extends BaseClientRequestHandler{

        @Override
        public void handleClientRequest(User sender, ISFSObject params) {
            ISFSObject ipAddress = new SFSObject();
            ipAddress.putUtfString("lobbyExtensionIp", lobbyExtensionIpAddress);

            send("IpAddress", ipAddress, sender);
        }
    }
}
