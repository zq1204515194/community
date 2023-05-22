package life.majiang.community.provider;

import com.alibaba.fastjson.JSON;
import life.majiang.community.dto.AccessTokenDTO;
import life.majiang.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://gitee.com/oauth/token?grant_type=authorization_code&code=" + accessTokenDTO.getCode() + "&client_id=" + accessTokenDTO.getClient_id() + "&redirect_uri=" + accessTokenDTO.getRedirect_uri() + "&client_secret=" + accessTokenDTO.getClient_secret())
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            String token = string.split(":")[1].split("\"")[1];
            // System.out.println(token);
            return token;
            // System.out.println(string);
            // return string;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public GithubUser getUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                // .url("https://api.github.com/user")
                //在gitee接口文档里找
                .url("https://gitee.com/api/v5/user?access_token=" + accessToken)
                // .header("Authorization", "token" + accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
