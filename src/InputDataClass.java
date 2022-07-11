import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

public class InputDataClass {
    /*public static void main(String[] args) {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName("HazelcastCluster");

        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
        IMap <String,String> map = client.getMap("my-distributed-map");

        map.put("1","John");
        map.put("2","Mary");
        map.put("3","Jane");
    }*/
}
