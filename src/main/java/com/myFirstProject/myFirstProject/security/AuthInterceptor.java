package com.myFirstProject.myFirstProject.security;

import com.myFirstProject.myFirstProject.dto.TodosReq;
import com.myFirstProject.myFirstProject.dto.TodosResp;
import com.myFirstProject.myFirstProject.exception.UserNotAuthorisedException;
import com.myFirstProject.myFirstProject.repository.UserRepository;
import com.myFirstProject.myFirstProject.service.PasswordMD5Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.security.NoSuchAlgorithmException;

//після створення його обовязково треба зареєструвати в конфігах class AuthMVCConfig
//сама в хедерах пишу шо хочу а клієнту кажу щоб він це обовязково вказав
//ловить вхідний запит
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {
    @Value("${security.enable}")
    private boolean securityEnable;

    @Value("${goes.to.another.server}")
    private boolean goesToAnotherServer;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordMD5Service passwordMD5Service;

    //щоб послати REST запрос на чужий сервер адресу якого знаємо
    @Autowired
    private RestTemplate restTemplate;

    private Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);
    private String format;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //REST - GET
        connectionWithAnotherServer();
        return checkSecurity(request);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        try {
            URI uri = restTemplate.postForLocation("https://jsonplaceholder.typicode.com/posts/", new TodosReq());
        /*TodosReq і TodosResp ніяк не повязані з HttpServletRequest request, HttpServletResponse response  - то мої запити які я посилаю
         i отримую з сторонньго сервера
         гет - я отримую респонс
         пост - я надсилаю реквест */
            if (uri != null) {
                logger.info(String.format("Get URI from Todos service - %s", uri.toString()));
            } else {
                logger.info("Don't get uri from Todos service ");
            }
        }catch (RestClientException ex){
            logger.info("Todos service not available");
        }

    }

    //витягаю з реквеста дані які мені були потрібні і перевіряю їх
    private boolean checkUserCredentials(HttpServletRequest request) throws NoSuchAlgorithmException {
        String login = request.getHeader("login");
        String password = request.getHeader("password");
        loginAndPasswordValidation(login, password);
        String result = passwordMD5Service.codePassword(password);
        if (userRepository.getUserByLoginAndPassword(login, result).isPresent()) {
            return true;
        } else {
            throw new UserNotAuthorisedException("User with this password and login is unauthorised");
        }
    }

    public boolean checkSecurity(HttpServletRequest request) throws NoSuchAlgorithmException {
        if (securityEnable) {
            return checkUserCredentials(request);
        } else {
            return true;
        }
    }

    private void loginAndPasswordValidation(String login, String password) {
        if (login == null || password == null) {
            throw new UserNotAuthorisedException("Enter login and password for authorisation");
        }
    }

    public void connectionWithAnotherServer() {
        if (goesToAnotherServer) {
            try {
                TodosResp todosResp = restTemplate.getForObject("https://jsonplaceholder.typicode.com/todos/1", TodosResp.class);
                if (todosResp ==null){
                    logger.info("Object from Todos service is empty");
                }else {
                    logger.info(format);
                }
            }catch (RestClientException ex){
                logger.info("Todos service not available");
            }
        }
    }



}

