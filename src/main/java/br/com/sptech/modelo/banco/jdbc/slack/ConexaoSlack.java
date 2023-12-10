package br.com.sptech.modelo.banco.jdbc.slack;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.webhook.Payload;
import com.github.seratch.jslack.api.webhook.WebhookResponse;

public class ConexaoSlack {
    private static String webHookUrl = "https://hooks.slack.com/services/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";
//    private static String oAuthToken = "xoxb-5988094370241-6191801830372-uO6nizXTniJKXjW49mSbIWUP\n";
    private static String SlackChanell = "#alert-yotte";

    public static void main(String[] args) {
        System.out.println("Test Menssage");
        sendMenssageToSlack("yotte slack");
    }

    public static void sendMenssageToSlack(String mensagem) {
        try {
            StringBuilder msgBuilder = new StringBuilder();

            msgBuilder.append(mensagem);

            Payload payload = Payload.builder().channel(SlackChanell).text(msgBuilder.toString()).build();

            WebhookResponse wbResp = Slack.getInstance().send(webHookUrl, payload);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}