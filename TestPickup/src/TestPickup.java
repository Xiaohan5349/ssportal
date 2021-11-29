
import javax.net.ssl.HttpsURLConnection;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import org.json.simple.JSONObject;

public class TestPickup{
    public static <JSONParser> void main(String[] args) throws IOException {
        String pickupLocation = "https://localhost:9031/ext/ref/pickup?REF=" + referenceValue;
        System.out.println(pickupLocation);
        URL pickUrl = new URL(pickupLocation);
        URLConnection urlConn = pickUrl.openConnection();
        HttpsURLConnection httpsURLConn = (HttpsURLConnection)urlConn;
        urlConn.setRequestProperty("ping.uname", "changeme");
        urlConn.setRequestProperty("ping.pwd", "please change me before you go into production!");
        urlConn.setRequestProperty("ping.instanceId", "spadapter");

        String encoding = urlConn.getContentEncoding();
        InputStream is = urlConn.getInputStream();
        InputStreamReader streamReader = new InputStreamReader(is, encoding != null ? encoding : "UTF-8");

        JSONParser parser = new JSONParser();
        JSONObject spUserAttributes = (JSONObject)parser.parse(streamReader);
        System.out.println("User Attributes received = " + spUserAttributes.toString());
    }
}

