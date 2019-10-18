package study.yang.personal.Provider;

import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.springframework.stereotype.Component;
import study.yang.personal.dto.AccessTokenDTO;
import study.yang.personal.dto.GithubUser;

import javax.imageio.IIOException;
import java.io.IOException;

/**
 * @yangnooiceman
 * 获取accesstoken
 */

@Component
public class GithubProvider {
    public String GetAccessToken(AccessTokenDTO accessTokenDTO){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType,JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
           String string = response.body().string();
           String token = string.split("&")[0].split("=")[1];//获取accesstoken需要进行获取地址拆分,accesstoken在等号后&号前
            return  token;
        } catch (Exception e) {
            e.printStackTrace();
        }return null;


    }

    //在github获取用户信息
    public GithubUser getUer(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();

            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return  githubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
