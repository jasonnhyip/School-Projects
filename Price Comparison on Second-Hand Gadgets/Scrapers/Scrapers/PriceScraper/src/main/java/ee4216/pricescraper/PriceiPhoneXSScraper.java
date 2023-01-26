/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
* PROBLEMS
* 1. Database cannot store Chinese
* 2. Unrelated cases
* 3. HTTP 410 Error (HttpStatusException)
*/
package ee4216.pricescraper;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

/**
 *
 * @author chunchun
 */
public class PriceiPhoneXSScraper {

    public static final String PRODUCT_BASE_URL = "https://www.price.com.hk/";
    public static final String IPHONE_XS_URL = "https://www.price.com.hk/search.php?m=T&q=iphone+xs";
    public static Sql2o sql2o = Scraper.sql2o;
    public static boolean xsSeries_valid = false;

    public static void main(String[] args) {

        print("Scraping iPhone XS from price.com");
        int totalPage = getTotalPageNum(IPHONE_XS_URL);
        
        //for (int i = totalPage; i >= 1; i--) {
        for (int i = 1; i <= totalPage; i++) {
            Elements links = getProductsByPage(i);
            if (links != null) {
                for (Element link : links) {
                    try {
                        xsSeries_valid = false;
                        Product product = getProductInfo(link);
                        if (xsSeries_valid == true) {
                            updateOrCreate(product);
                            print("Source: " + product.getSource() + ", ID: " + product.getId() + ", Name: " + product.getProductName());
                        }
                    } catch (HttpStatusException ex) {
                        Logger.getLogger(PriceiPhoneXSScraper.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

    }

    public static Product getProductInfo(Element link) throws HttpStatusException{
        
        String productUrl = link.attr("href");
        
        try {

            String source = "Price.com";
            Document product = Jsoup.connect(PRODUCT_BASE_URL + productUrl).get();

            String[] splitUrl = productUrl.split("i=");
            String id = splitUrl[1]; //product.select("#itemID").attr("value");

            String productName = product.select("table tbody tr:first-child td:last-child").text();
            String regex = "[^\\p{L}\\p{N}\\p{P}\\p{Z}]";
            productName = productName.replaceAll(regex, "");
            productName = productName.substring(0, Math.min(productName.length(), 100));
            
            if (productName.toLowerCase().contains("iphone") && productName.toLowerCase().contains("xs") 
                    && !productName.toLowerCase().contains("protect") && !productName.toLowerCase().contains("case") && !productName.toLowerCase().contains("sim")
                    && !productName.toLowerCase().contains("skin") && !productName.toLowerCase().contains("bumper") && !productName.toLowerCase().contains("otterbox")
                    && !productName.toLowerCase().contains("uag") && !productName.toLowerCase().contains("rhinoshield") && !productName.toLowerCase().contains("charge")
                    && !productName.toLowerCase().contains("lens") && !productName.contains("膜") && !productName.contains("殼")
                    && !productName.contains("充電") && !productName.contains("線") && !productName.contains("磁力") && !productName.contains("喇叭") 
                    && !productName.contains("盒")  && !productName.contains("換") && !productName.contains("修理") 
                    && !productName.contains("回收") && !productName.contains("收購") && !productName.contains("援交"))
                xsSeries_valid = true;

            String price = product.select(".trade_price span:first-child").text() + product.select(".trade_price span:last-child").text();
            double secondHandPrice = price.equals("") ? 0 : Double.parseDouble(price.replaceAll("[^0-9]", ""));

            String oriPrice = product.select("td.trade_price_1st span.text-price-unit").text() + product.select("td.trade_price_1st span.text-price-number").text();
            double originalPrice = oriPrice.equals("") ? 0 : Double.parseDouble(oriPrice.replaceAll("[^0-9]", ""));

            String warrantyDesc = product.select("table tbody tr td:first-child:contains(保養期至:) + td").text();

            String lastUpdateDate = product.select("table tbody tr td:first-child:contains(最後更新:) + td").text();

            String description = product.select(".description").text();
            description = description.substring(0, Math.min(description.length(), 200));
            description = description.replaceAll(regex, "");
            
            String availability = product.select("table tbody tr td:first-child:contains(刊登狀態:) + td").text();

            String imageUrl = product.select(".full-image a").attr("href");

//            print(productUrl);
//            print("product id: " + id);
//            print("Product Name: " + productName);
//            print("Second-hand price to be stored: " + price);
//            print("Description: " + description);
//            print("Warranty Info: " + warrantyDesc);
//            print("Last Update Date: " + lastUpdateDate);
//            print("Image URL: " + PRODUCT_BASE_URL + imageUrl);
//            print("Availability: " + availability);
//
//            print("Original price to be stored: " + oriPrice);

            return new Product(source, id, productName, secondHandPrice, originalPrice, description, warrantyDesc, lastUpdateDate, PRODUCT_BASE_URL + imageUrl, availability, PRODUCT_BASE_URL + productUrl);

        } catch (IOException ex) {
            Logger.getLogger(PriceiPhoneXSScraper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static Elements getProductsByPage(int page) {
        try {
            Document document = Jsoup.connect(IPHONE_XS_URL + "&page=" + page).get();
            Elements links = document.select("div.line > a");
            return links;
        } catch (IOException ex) {
            Logger.getLogger(PriceiPhoneXSScraper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static int getTotalPageNum(String url) {
        try {
            Document document = Jsoup.connect(url).get();
            Elements anchors = document.select("ul.pagination a");
            int lastPage = 1;
            for (Element anchor : anchors) {

                String urlString = anchor.attr("href");
                int i = urlString.indexOf("page=");
                if (i >= 0) {
                    urlString = urlString.substring(i, urlString.length());
                    urlString = urlString.replaceAll("[^0-9]", "");
                    int pageNum = Integer.parseInt(urlString);
                    if (pageNum > lastPage) {
                        lastPage = pageNum;
                    }
                }
            }
            return lastPage;
        } catch (IOException ex) {
            Logger.getLogger(PriceiPhoneXSScraper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return 1;
    }

    public static void print(String string) {
        System.out.println(string);
    }

//    public static List<Product> updateOrCreate(Product product) {
    public static void updateOrCreate(Product product) {
        String fetchSQL = "SELECT * FROM products WHERE source = :source AND id = :id";
        try (Connection con = sql2o.open()) {
            List<Product> products = con
                    .createQuery(fetchSQL)
                    .addParameter("source", product.getSource())
                    .addParameter("id", product.getId())
                    .executeAndFetch(Product.class);

            if (products.size() > 0) {
                String updateSQL
                        = "UPDATE products SET "
                        + "productName = :productName, "
                        + "secondHandPrice = :secondHandPrice, "
                        + "description = :description, "
                        + "warrantyDesc = :warrantyDesc, "
                        + "lastUpdateDate = :lastUpdateDate, "
                        + "imageUrl = :imageUrl, "
                        + "availability = :availability, "
                        + "productUrl = :productUrl "
                        + "WHERE source = :source AND "
                        + "id = :id";

                con
                        .createQuery(updateSQL)
                        .addParameter("productName", product.getProductName())
                        .addParameter("secondHandPrice", product.getSecondHandPrice())
                        .addParameter("description", product.getDescription())
                        .addParameter("warrantyDesc", product.getWarrantyDesc())
                        .addParameter("lastUpdateDate", product.getLastUpdateDate())
                        .addParameter("imageUrl", product.getImageUrl())
                        .addParameter("availability", product.getAvailability())
                        .addParameter("productUrl", product.getProductUrl())
                        .addParameter("source", product.getSource())
                        .addParameter("id", product.getId())
                        .executeUpdate();

                String updateMoreInfoSQL
                        = "UPDATE addinfos SET "
                        + "originalPrice = :originalPrice "
                        + "WHERE source = :source AND "
                        + "id = :id";
                con
                        .createQuery(updateMoreInfoSQL)
                        .addParameter("originalPrice", product.getOriginalPrice())
                        .addParameter("source", product.getSource())
                        .addParameter("id", product.getId())
                        .executeUpdate();
            } else {
                String insertSQL
                        = "INSERT INTO products(source, id, productName, secondHandPrice, description, warrantyDesc, lastUpdateDate, imageUrl, availability, productUrl) "
                        + "VALUES (:source, :id, :productName, :secondHandPrice, :description, :warrantyDesc, :lastUpdateDate, :imageUrl, :availability, :productUrl)";
                con.createQuery(insertSQL).bind(product).executeUpdate();
                
                String insertMoreInfoSQL
                        = "INSERT INTO addinfos(source, id, originalPrice) "
                        + "VALUES (:source, :id, :originalPrice)";
                con.createQuery(insertMoreInfoSQL).bind(product).executeUpdate();
            }
        }
    }

}
