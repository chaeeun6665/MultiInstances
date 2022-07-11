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

public class Client1 {
    public SmartFox smartFoxServer;
    public ConfigData config;
    //The main server => local
    public String localhost = "127.0.0.1";


    public void Start() {
        smartFoxServer = new SmartFox();

        smartFoxServer.addEventListener(SFSEvent.CONNECTION, new OnConnection());
        smartFoxServer.addEventListener(SFSEvent.CONNECTION_LOST, new OnConnectionLost());
        smartFoxServer.addEventListener(SFSEvent.LOGIN, new OnLogin());
        smartFoxServer.addEventListener(SFSEvent.LOGIN_ERROR, new OnLoginError());
        smartFoxServer.addEventListener(SFSEvent.EXTENSION_RESPONSE, new OnExtensionResponse());

        //ConfigData - Client configuration data loaded from an external xml file or passed directly to the deputy connect method
        config = new ConfigData();
        //Connection setting
        config.setHost(localhost);
        config.setPort(9933);
        config.setZone("MainControllerExtension");

        smartFoxServer.connect(config);
        System.out.println("Start working fine");
    }

    //Connection이 성공했을 때
    public class OnConnection implements IEventListener{

        @Override
        public void dispatch(BaseEvent baseEvent) throws SFSException {
            if((boolean)(baseEvent.getArguments().get("success"))){
                smartFoxServer.send(new LoginRequest("Oscar"));
            }
            System.out.println("Connection working fine");
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

            smartFoxServer.send(new ExtensionRequest("getUserName", params));
            System.out.println("login succeeded");
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
                smartFoxServer.send(new ExtensionRequest("getIpAddress", null));
            }
            else if(command.equals("IpAddress")) {
                String lobbyExtensionIp = wrapper.getUtfString("lobbyExtensionIp");
                Clinet1_2 client1_2 = new Clinet1_2();
                client1_2.SetIpAddress(lobbyExtensionIp);
                client1_2.Start();
            }
        }
    }

    public static void main(String[] args){
        Client1 client1 = new Client1();
        client1.Start();
    }
}
