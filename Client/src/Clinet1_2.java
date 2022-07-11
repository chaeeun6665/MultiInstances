import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;
import sfs2x.client.SmartFox;
import sfs2x.client.core.BaseEvent;
import sfs2x.client.core.IEventListener;
import sfs2x.client.core.SFSEvent;
import sfs2x.client.entities.User;
import sfs2x.client.requests.ExtensionRequest;
import sfs2x.client.requests.LoginRequest;
import sfs2x.client.util.ConfigData;

public class Clinet1_2 {
    public SmartFox smartFoxServer2;
    public ConfigData config;
    public String serverIp = "";


    public void Start() {
        smartFoxServer2 = new SmartFox();

        smartFoxServer2.addEventListener(SFSEvent.CONNECTION, new OnConnection());
        smartFoxServer2.addEventListener(SFSEvent.CONNECTION_LOST, new OnConnectionLost());
        smartFoxServer2.addEventListener(SFSEvent.LOGIN, new OnLogin());
        smartFoxServer2.addEventListener(SFSEvent.LOGIN_ERROR, new OnLoginError());
        smartFoxServer2.addEventListener(SFSEvent.EXTENSION_RESPONSE, new OnExtensionResponse());

        //ConfigData - Client configuration data loaded from an external xml file or passed directly to the deputy connect method
        config = new ConfigData();
        //Connection setting
        config.setHost(serverIp);
        config.setPort(9933);
        config.setZone("TestZone1");

        smartFoxServer2.connect(config);
    }

    public class OnConnection implements IEventListener {

        @Override
        public void dispatch(BaseEvent baseEvent) throws SFSException {
            if((boolean)(baseEvent.getArguments().get("success"))){
                smartFoxServer2.send(new LoginRequest("Oscar"));
            }
        }
    }

    //Connection이 제대로 되지 않았을 때
    public class OnConnectionLost implements IEventListener{

        @Override
        public void dispatch(BaseEvent baseEvent) throws SFSException {
            System.out.println("Connection Lost");
        }
    }

    public class OnLogin implements IEventListener{

        @Override
        public void dispatch(BaseEvent baseEvent) throws SFSException {
            ISFSObject params = SFSObject.newInstance();
            User user = (User)baseEvent.getArguments().get("user");
            params.putUtfString("userName", user.getName());

            smartFoxServer2.send(new ExtensionRequest("getUserName", params));
        }
    }

    public class OnLoginError implements IEventListener{

        @Override
        public void dispatch(BaseEvent baseEvent) throws SFSException {
            System.out.println("Login error: " + (baseEvent.getArguments().get("errorMessage")));
        }
    }

    public class OnExtensionResponse implements IEventListener{

        @Override
        public void dispatch(BaseEvent baseEvent) throws SFSException {
            ISFSObject wrapper = (ISFSObject) baseEvent.getArguments().get("params");
            String command = (String) baseEvent.getArguments().get("cmd");
            if(command.equals("loginResult")) {
                String loginSuccessResult = wrapper.getUtfString("loginSuccessMessage");
                System.out.println("Result: " + loginSuccessResult);
                //smartFoxServer2.send(new ExtensionRequest("getIpAddress", null));
            }
        }
    }

    public void SetIpAddress(String ip) {
        serverIp = ip;
    }
}
