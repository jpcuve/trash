import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

public class EuipoTrial {
    private static class JsonHttpEntity extends BasicHttpEntity {
        private byte[] data;

        public JsonHttpEntity(ObjectMapper mapper, Object o) throws JsonProcessingException {
            this.data = mapper.writeValueAsBytes(o);
            setContentLength(data.length);
            setContentType("application/json");
        }

        @Override
        public InputStream getContent() throws IllegalStateException {
            return new ByteArrayInputStream(this.data);
        }
    }

    public static void main(String[] args) throws Exception {
        for (String filename: Arrays.asList("decision.txt", "non_decision.txt")){
            try (CloseableHttpClient client = HttpClients.createDefault()) {
                HttpPost post = new HttpPost("https://euipo-classifier-dot-dip-ml-test.appspot.com/api/pipeline");
                byte[] encodedAuth = Base64.encodeBase64("darts:darts01".getBytes(StandardCharsets.US_ASCII));
                post.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + new String(encodedAuth));
                ObjectMapper mapper = new ObjectMapper();
                ObjectNode objectNode = mapper.createObjectNode();
                Scanner scanner = new Scanner(
                        Thread.currentThread()
                                .getContextClassLoader()
                                .getResourceAsStream("euipo-german/" + filename),
                        StandardCharsets.UTF_8);
                String text = scanner
                        .useDelimiter("\\A")
                        .next();
                objectNode.put("text", text);
                post.setEntity(new JsonHttpEntity(mapper, objectNode));
                HttpResponse response = client.execute(post);
                objectNode = mapper.readValue(EntityUtils.toByteArray(response.getEntity()), ObjectNode.class);
                System.out.println(objectNode);
            }
        }
    }
}
