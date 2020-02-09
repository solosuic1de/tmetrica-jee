package web.command.impl;

import model.domain.entity.User;
import model.factory.ServiceFactory;
import model.factory.ServiceType;
import model.service.UserService;
import web.command.GetPostCommand;
import web.constants.AttributeName;
import web.constants.ViewPathConstant;
import web.data.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * The type Login command.
 */
public class LoginCommand extends GetPostCommand {
    private UserService userService;

    /**
     * Instantiates a new Login command.
     */
    public LoginCommand() {
        userService = (UserService) ServiceFactory.getService(ServiceType.USERS);
    }

    @Override
    protected Page performGet(HttpServletRequest request) {
        return new Page(ViewPathConstant.LOGIN);
    }

    @Override
    protected Page performPost(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String email = request.getParameter("username");
        String password = request.getParameter("password");
        User user = userService.validateUser(email, password);

        if (user != null) {
            session.setAttribute("user", user);
            return successfulLogin(request);
        }
        return failedLogin(request);
    }

    private Page failedLogin(HttpServletRequest request) {
        request.setAttribute(AttributeName.LOGIN_ERROR, true);
        return new Page(ViewPathConstant.LOGIN, true);
    }

    private Page successfulLogin(HttpServletRequest request) {
        request.setAttribute(AttributeName.LOGIN_ERROR, false);
        return new Page(ViewPathConstant.HOME, true);
    }
}
