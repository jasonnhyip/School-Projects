/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ee4216.ex6;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 * @author Jason
 */

@RestController
@RequestMapping(path="/api")
public class DataController {
    @Autowired
    JdbcTemplate jdbctemplate;
    
    Logger logger = LoggerFactory.getLogger(DataController.class);
    int display_no_per_page = 10;
    String last_sort = "lastUpdateDate";
    String last_order = "ASC";
    String sql;
    double min_price = 1000;
    double max_price = 31000;
     
    //@RequestParam("product_name") String product_name_input
    @GetMapping("/all_products")
    public Map<Integer, List<Product>> getProduct(
            @RequestParam(value = "product_name") String product_name,
            @RequestParam(value = "sort", defaultValue = "lastUpdateDate") String sort_Date_Price,
            @RequestParam(value = "order", defaultValue = "ASC") String sort_Order,
            @RequestParam(value = "page", defaultValue = "1") int page) {
   
       
        product_name = "%"+ product_name + "%";
        
        if ( !(sort_Date_Price.equals("lastUpdateDate") || sort_Date_Price.equals("secondHandPrice")) )
            sort_Date_Price = "lastUpdateDate";
        if ( !(sort_Order.equals("ASC")||sort_Order.equals("DESC")) )
            sort_Order = "ASC";
        
//        sql = "SELECT * FROM products WHERE productName LIKE \"%"+ product_name + "%\" AND secondHandPrice >= " + min_price + " AND lastUpdateDate >= DATE_ADD(NOW(), INTERVAL -1 MONTH)" + " ORDER BY " + sort_Date_Price + " " + sort_Order + " LIMIT " + display_no_per_page + " OFFSET " + (page-1)*10 ;
        sql = "SELECT * FROM products WHERE productName LIKE ? AND secondHandPrice BETWEEN " + min_price + " AND " + max_price + " AND lastUpdateDate >= DATE_ADD(NOW(), INTERVAL -1 MONTH) "
                + "ORDER BY " + sort_Date_Price + " " + sort_Order + " LIMIT " + display_no_per_page + " OFFSET ?" ;
        
        List<Product> products = jdbctemplate.query(sql, new Object[] {product_name, (page-1)*10},
            (row, rownum)-> new Product(row.getString("source"),row.getString("id"),row.getString("productName"),
                        row.getDouble("secondHandPrice"),row.getString("description"),row.getString("warrantyDesc"),
                        row.getTimestamp("lastUpdateDate"),row.getString("imageUrl"),row.getString("availability"), row.getString("productUrl")));
        
        int total_count = jdbctemplate.queryForObject("SELECT COUNT(*) FROM products WHERE productName LIKE ? AND secondHandPrice >= ? AND lastUpdateDate >= DATE_ADD(NOW(), INTERVAL -1 MONTH)", 
                new Object[] {product_name, min_price}, Integer.class);
        int total_page = (int) Math.ceil( (double)total_count / display_no_per_page );
        
        Map<Integer, List<Product>> map = new HashMap<>();
        map.put(total_page, products);
        
        return map;
    }
    
    @GetMapping("/overview")
    public Overview getOverview(@RequestParam(value = "product_name") String product_name) {
        
        
        product_name = "%"+ product_name + "%";
        
        //sql = "SELECT * FROM products WHERE productName LIKE \"%"+ product_name + "%\" AND secondHandPrice >= " + min_price + " AND lastUpdateDate >= DATE_ADD(NOW(), INTERVAL -1 WEEK)";
        sql = "SELECT * FROM products WHERE productName LIKE ? AND secondHandPrice BETWEEN "  + min_price + " AND " + max_price + " AND lastUpdateDate >= DATE_ADD(NOW(), INTERVAL -1 WEEK)";
        
        List<Product> products = jdbctemplate.query(sql, new Object[] {product_name},
            (row, rownum)-> new Product(row.getString("source"),row.getString("id"),row.getString("productName"),
                        row.getDouble("secondHandPrice"),row.getString("description"),row.getString("warrantyDesc"),
                        row.getTimestamp("lastUpdateDate"),row.getString("imageUrl"),row.getString("availability"), row.getString("productUrl")));
        
        double Min_price = 0;
        double Max_price = 0;
        double Avg_price = 0;
        double Last_transaction_price = 0;
        Timestamp latest_transacted_time = products.get(0).getLastUpdateDate();
        
        //obtain the first product's date with finished transaction
        for (Iterator<Product> iter = products.listIterator(); iter.hasNext();){ 
            Product current_product = iter.next();
            if (current_product.getAvailability().equals("已完成交易") || current_product.getAvailability().equals("已售出")) {
                latest_transacted_time = current_product.getLastUpdateDate();
                break;
            }
        }
        
        //sort the products list by second hand price in ascending order
        Collections.sort(products, new productPriceComparator());
        
        Min_price = products.get(0).getSecondHandPrice();
        Max_price = products.get(products.size()-1).getSecondHandPrice();
        
        //caculate the average by summing all up and obtain the latest transaction price
        for (Iterator<Product> iter = products.listIterator(); iter.hasNext(); ) {
            Product current_product = iter.next();
            Avg_price += current_product.getSecondHandPrice();
            if ( (current_product.getAvailability().equals("已完成交易") || current_product.getAvailability().equals("已售出")) && current_product.getLastUpdateDate().after(latest_transacted_time)) {
                Last_transaction_price = current_product.getSecondHandPrice();
                latest_transacted_time = current_product.getLastUpdateDate();
            }
        }
        
        Avg_price /= products.size();
        
//        Date date = new Date();
//        long current_time = date.getTime();
//        
//        System.out.println(date);
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
//        cal.add(Calendar.DATE, -7);
//
//        long one_week = cal.getTime().getTime();
//        System.out.println("One Week: "+ one_week);
//        
//        for (Iterator<Product> iter = products.listIterator(); iter.hasNext(); ) {
//            Timestamp product_lastUpdateTime = iter.next().getLastUpdateDate();
//            //System.out.println("Product date: " + product_lastUpdateTime);
//            if( product_lastUpdateTime.getTime() < one_week)
//            {
//                //System.out.println("Product date: " + product_lastUpdateTime);
//                System.out.println("Product date: " + product_lastUpdateTime + "    Product date(ms): " +product_lastUpdateTime.getTime() + "   One Week: " + one_week);
//                iter.remove();
//            }
//        }
//        Collections.sort(products, new productDateComparator());
        
        Overview overview = new Overview(Min_price, Max_price, Avg_price, Last_transaction_price, products);
        return overview;
    }
    
    @GetMapping("/low_price_per_date")
    public List<Product> getChart(@RequestParam(value = "product_name") String product_name) {
   
       
        product_name = "%"+ product_name + "%";
        
//        sql= "SELECT p1.* from products p1 "
//                + "JOIN (SELECT productName, MIN(secondHandPrice) AS min_value, Date(lastUpdateDate) DateOnly FROM products WHERE Date(lastUpdateDate) >= DATE(NOW()) - INTERVAL 7 DAY AND productName LIKE \"%"+ product_name + "%\" AND secondHandPrice >= " + min_price + " GROUP BY DateOnly, productName) "
//                + "AS p2 ON Date(p1.lastUpdateDate) = p2.DateOnly AND p1.secondHandPrice = p2.min_value AND p2.DateOnly >= DATE_ADD(NOW(), INTERVAL -1 WEEK) ORDER BY lastUpdateDate"                
//                ;
//        sql= "SELECT p1.* from products p1 "
//                + "JOIN (SELECT ANY_VALUE(productName), MIN(secondHandPrice) AS min_value, Date(lastUpdateDate) DateOnly FROM products WHERE Date(lastUpdateDate) >= DATE(NOW()) - INTERVAL 7 DAY AND productName LIKE \"%"+ product_name + "%\" AND secondHandPrice >= " + min_price + " GROUP BY DateOnly) "
//                + "AS p2 ON Date(p1.lastUpdateDate) = p2.DateOnly AND p1.secondHandPrice = p2.min_value AND p2.DateOnly >= DATE_ADD(NOW(), INTERVAL -1 WEEK) ORDER BY lastUpdateDate"                
//                ;
          sql= "SELECT p1.* from products p1 "
                + "JOIN (SELECT ANY_VALUE(productName) AS productName2, MIN(secondHandPrice) AS min_value, Date(lastUpdateDate) AS DateOnly FROM products WHERE Date(lastUpdateDate) >= DATE(NOW()) - INTERVAL 14 DAY AND productName LIKE ? AND secondHandPrice BETWEEN "  + min_price + " AND " + max_price + " GROUP BY DateOnly) "
                + "AS p2 ON Date(p1.lastUpdateDate) = p2.DateOnly AND p1.secondHandPrice = p2.min_value WHERE productName LIKE ? ORDER BY lastUpdateDate";                
                ;
        //logger.debug(product_name_input);
        List<Product> products = jdbctemplate.query(sql, new Object[] {product_name, product_name},
            (row, rownum)-> new Product(row.getString("source"),row.getString("id"),row.getString("productName"),
                        row.getDouble("secondHandPrice"),row.getString("description"),row.getString("warrantyDesc"),
                        row.getTimestamp("lastUpdateDate"),row.getString("imageUrl"),row.getString("availability"), row.getString("productUrl")));
        return products;
    }
    



}

class productPriceComparator implements Comparator<Product> {
            @Override
            public int compare(Product product1, Product product2) {
                return product1.getSecondHandPrice() > product2.getSecondHandPrice() ? 1 : -1;
            }       
}

class productDateComparator implements Comparator<Product> {
            @Override
            public int compare(Product product1, Product product2) {
                return product1.getLastUpdateDate().getTime() > product2.getLastUpdateDate().getTime() ? 1 : -1;
            }       
}