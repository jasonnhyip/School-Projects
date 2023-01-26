/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ee4216.ex6;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Jason
 */
public class MyErrorController implements ErrorController {
    

    
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        
//        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
//     
//        if (status != null) {
//            Integer statusCode = Integer.valueOf(status.toString());
//
//            if(statusCode == HttpStatus.BAD_REQUEST.value()) {
//                return "error-400";
//            }
//            else if(statusCode == HttpStatus.NOT_FOUND.value()) {
//                return "error-404";
//            }
//            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
//                return "error-500";
//            }
//        }
        return "error";
    }
    
    @Override
    public String getErrorPath() {
        return "/error";
    }
}
