package study.yang.personal.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import study.yang.personal.Provider.GithubProvider;
import study.yang.personal.dto.AccessTokenDTO;
import study.yang.personal.dto.GithubUser;


@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @GetMapping("/callback")
    public String  callback(@RequestParam (name="code")String code,
                            @RequestParam (name="state") String state) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri("http://localhost:8888/callback");
        accessTokenDTO.setClient_id("6981ee5e748021b85b08");
        accessTokenDTO.setClient_secret("e64294342c40cf18735b38f1a7ea64985b650da9");
        accessTokenDTO.setState(state);
        String accessToken =  githubProvider.GetAccessToken(accessTokenDTO);
        GithubUser user = githubProvider.getUer(accessToken);
        System.out.println(user.getName());
        return "index";
    }
}
