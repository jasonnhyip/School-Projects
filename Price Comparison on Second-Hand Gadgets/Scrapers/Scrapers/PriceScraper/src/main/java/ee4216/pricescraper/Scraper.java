/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ee4216.pricescraper;

import org.sql2o.Sql2o;

/**
 *
 * @author chunchun
 */
public class Scraper {
    public static Sql2o sql2o;
    
    static {
        sql2o = new Sql2o("jdbc:mysql://68.183.118.44:3306/GadgetsCombined?useUnicode=yes&characterEncoding=UTF-8", "emiltsang", "eE4216gROUP2!");
//        sql2o = new Sql2o("jdbc:mysql://localhost:3306/PriceDotCom?useUnicode=yes&characterEncoding=UTF-8", "root", "Emil1997");
    }
}
