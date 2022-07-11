import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.cluster.Member;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

public class HazelcastManager {
    static final HazelcastManager Instance = new HazelcastManager();
    public static HazelcastManager getInstance(){
        return Instance;
    }

    public HazelcastInstance newHazelcastConfig() {
        Config config = new Config();
        config.getNetworkConfig().setPort(5900).setPortAutoIncrement(false);
        config.setClusterName("HazelcastCluster");

        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName("HazelcastCluster");
        HazelcastInstance client = HazelcastClient.getHazelcastClientByName("MainClient");
        if(client == null) {
            clientConfig.setInstanceName("MainClient");
            HazelcastInstance newClient = HazelcastClient.newHazelcastClient(clientConfig);
            return newClient;
        }
        else{
            return client;
        }
    }

    public IMap<String, String> getDataFromMemory() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName("HazelcastCluster");

        HazelcastInstance client = HazelcastClient.getHazelcastClientByName("MainClient");
        if(client == null){
            clientConfig.setInstanceName("MainClient");
            HazelcastInstance newClient = HazelcastClient.newHazelcastClient(clientConfig);
        }

        //IMap<String,String> map = client.getMap("my-distributed-map");
        return null;
    }

    public void inputDataToCluster() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName("HazelcastCluster");

        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
        IMap <String,String> map = client.getMap("my-distributed-map");

        map.put("1","John");
        map.put("2","Mary");
        map.put("3","Jane");
    }

}
