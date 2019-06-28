import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class DittoClient {
    public static final String BASE_URI = "http://34.77.235.198";

    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<ObjectNode> nodes = Files
                .walk(Path.of("d:/application/images"))
                .filter(Files::isRegularFile)
                .map(path -> {
                    ObjectNode node = null;
                    try {
                        byte[] data =  Files.readAllBytes(path);
                        byte[] encoded = Base64.getEncoder().encode(data);
                        node = mapper.createObjectNode();
                        node.put("data", new String(encoded, StandardCharsets.US_ASCII));
                        node.put("image_identifier", path.getFileName().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return node;
                })
                .collect(Collectors.toList());
        ArrayNode a = mapper.createArrayNode();
        nodes.stream().filter(Objects::nonNull).forEach(a::add);
        HttpClient client = HttpClient.newHttpClient();
        byte[] data = mapper.writeValueAsBytes(a);
        System.out.println(data.length);
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URI + "/api/dispatcher/add-images"))
                .POST(HttpRequest.BodyPublishers.ofByteArray(data))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<byte[]> res = client.send(req, HttpResponse.BodyHandlers.ofByteArray());
        System.out.println(res.statusCode());
        if (res.statusCode() / 100 == 2){
            JsonNode node = mapper.readTree(res.body());
            System.out.println(node);
        } else { // most likely unique key violation
            ArrayNode identifiers = mapper.createArrayNode();
            Iterator<JsonNode> it = a.iterator();
            while (it.hasNext()){
                ObjectNode o = (ObjectNode) it.next();
                identifiers.add(o.get("image_identifier"));
            }
            req = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URI + "/api/dispatcher/check-images"))
                    .POST(HttpRequest.BodyPublishers.ofByteArray(mapper.writeValueAsBytes(identifiers)))
                    .header("Content-Type", "application/json")
                    .build();
            res = client.send(req, HttpResponse.BodyHandlers.ofByteArray());
            System.out.println(res.statusCode());
            if (res.statusCode() / 100 == 2) {
                JsonNode node = mapper.readTree(res.body());
                System.out.println(node);
            }
        }
    }
}
