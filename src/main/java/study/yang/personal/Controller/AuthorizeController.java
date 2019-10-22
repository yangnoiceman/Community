package study.yang.personal.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import study.yang.personal.Provider.GithubProvider;
import study.yang.personal.dto.AccessTokenDTO;
import study.yang.personal.dto.GithubUser;
import study.yang.personal.mapper.UserMapper;
import study.yang.personal.model.User;

import javax.servlet.http.Cookie;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;


@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;
    //配置properties,提高代码安全性
    @Value("${github.client.id}")
    private  String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private  String redirectUri;

    @Autowired
    private UserMapper userMapper;


    @GetMapping("/callback")
    public String  callback(@RequestParam (name="code")String code,
                            @RequestParam (name="state") String state,
                            HttpServletResponse response
                           ){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setState(state);
        String accessToken =  githubProvider.GetAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUer(accessToken);
        //System.out.println(githubUser.getName());
        //System.out.println(user.getId());
        //System.out.println(user.getBio());
        if (githubUser !=null ){

            User user = new User();
            //数据库插入
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountID(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);

            response.addCookie(new Cookie("token",token));


            return "redirect:/";




        }else{
            //登录失败,重新登录
            return "redirect:index";


        }



    }
}
