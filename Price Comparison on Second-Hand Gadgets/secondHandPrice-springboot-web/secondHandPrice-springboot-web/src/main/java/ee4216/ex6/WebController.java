/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ee4216.ex6;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Jason
 */

@Controller
public class WebController {
    
    @GetMapping("/overview")
    public String redirect_Overview(@RequestParam(value = "product_name") String product_name, HttpServletRequest request) {
        //String param1 = request.getParameter("product_name");
        return "overview" ;
    } 
    
    @GetMapping("/all_products")
    public String redirect_All_Products(@RequestParam(value = "product_name") String product_name, HttpServletRequest request) {
        return "all_products" ;
    } 
    
    @GetMapping("/trend")
    public String redirect_Trend(@RequestParam(value = "product_name") String product_name, HttpServletRequest request) {
        return "trend" ;
    } 
    
    @RequestMapping("/error_NoItem")
    public String handleError() {
        return "error_NoItem";
    }
}
