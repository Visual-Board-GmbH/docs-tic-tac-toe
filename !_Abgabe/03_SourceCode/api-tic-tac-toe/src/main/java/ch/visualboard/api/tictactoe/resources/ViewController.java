package ch.visualboard.api.tictactoe.resources;

import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("3bdfb159-2774-4139-a7bb-35f1bcac28da")
public class ViewController
{
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public void redirect(HttpServletResponse httpResponse) throws Exception
    {
        httpResponse.sendRedirect("/index.html");
    }
}
