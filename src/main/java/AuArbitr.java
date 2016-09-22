import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

/**
 * Created by jpc on 8/26/16.
 */
public class AuArbitr {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuArbitr.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static JsonNode postJson(final String host, final String endPoint, final JsonNode request) throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            final HttpPost httpPost = new HttpPost(host + endPoint);
            StringEntity req = new StringEntity(MAPPER.writeValueAsString(request), ContentType.create("application/json", "UTF-8"));
            httpPost.setEntity(req);
            httpPost.addHeader("Accept", "application/json");
            return client.execute(httpPost, (res) -> {
                int status = res.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    final HttpEntity entity = res.getEntity();
                    if (entity != null){
                        byte[] content = EntityUtils.toByteArray(entity);
                        System.out.println(new String(content));
                        final JsonNode node = MAPPER.readTree(content);
                        return node;
                    }
                    System.out.printf("No response from server%n");
                    return null;
                } else if (status >= 300 && status < 400){
                    final String redirection = res.getFirstHeader("location").getValue();
                    System.out.println("Redirection: " + redirection);
                    return postJson(host, redirection, request);
                }
                System.out.printf("Cannot get response, http response status: %s%n", status);
                return null;
            });
        }

    }

    public static void main(String[] args) throws Exception {
        final WebClient webClient = new WebClient();
        webClient.getOptions().setRedirectEnabled(true);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        HtmlPage loginPage = webClient.getPage("https://my.arbitr.ru/#index");
        webClient.waitForBackgroundJavaScript(10000);
//        HtmlAnchor aLogin = loginPage.getFirstByXPath("//a[@class='b-main-menu-item-link']");
//        System.out.println(aLogin);
        for (HtmlAnchor anchor: loginPage.getAnchors()){
            System.out.println(anchor);
        };
/*
        HtmlTextInput iUsername = loginPage.getFirstByXPath("//input[@class='ui-textbox-input js-textbox js-textbox--username']");
        System.out.println(iUsername);
*/
/*
        HtmlDivision loginForm = loginPage.getFirstByXPath("//div[@class='b-main-form b-main-form--logon js-main-form js-main-form--logon b-main-form--esia']");
        HtmlTextInput usernameInput = loginForm.getFirstByXPath(".//input[@class='ui-textbox-input js-textbox js-textbox--username']");
        usernameInput.setText("eandjieva@darts-ip.com");
        HtmlPasswordInput passwordInput = loginForm.getFirstByXPath(".//input[@class='ui-textbox-input js-textbox js-textbox--password']");
        passwordInput.setText("123456");
        HtmlCheckBoxInput rememberMe = loginForm.getFirstByXPath(".//input[@type='checkbox' and @name='RememberMe']");
        rememberMe.setChecked(true);
        HtmlAnchor loginLink = loginForm.getFirstByXPath("//a[@class='ui-button ui-button--darkform js-button js-button--logon' and contains(@href,'#logon')]");
        loginLink.click();
*/

/*
        final HtmlPage page = webClient.getPage("http://ras.arbitr.ru/");
        System.out.println(page.getWebResponse().getContentAsString());
*/
    }
}
