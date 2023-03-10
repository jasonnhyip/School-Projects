/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ee4216.pricescraper;

/**
 *
 * @author Hinson
 */

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
public class PriceMacBookProScraper {

    public static final String PRODUCT_BASE_URL = "https://www.price.com.hk/";
    public static final String MACBOOK_PRO_URL = "https://www.price.com.hk/search.php?m=T&q=macbook+pro";
    public static Sql2o sql2o = Scraper.sql2o;
    public static boolean proSeries_valid = false;

    public static void main(String[] args) {

        print("Scraping Macbook Pro from price.com");
        int totalPage = getTotalPageNum(MACBOOK_PRO_URL);

        for (int i = 1; i <= totalPage; i++) {
            Elements links = getProductsByPage(i);
            if (links != null) {
                for (Element link : links) {
                    try {
                        proSeries_valid = false;
                        Product product = getProductInfo(link);
                        if (proSeries_valid == true) {
                            updateOrCreate(product);
                            print("Source: " + product.getSource() + ", ID: " + product.getId() + ", Name: " + product.getProductName());
                        }
                    } catch (HttpStatusException ex) {
                        Logger.getLogger(PriceMacBookProScraper.class.getName()).log(Level.SEVERE, null, ex);
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
            
            if (productName.toLowerCase().contains("macbook") && productName.toLowerCase().contains("pro") 
                    && !productName.toLowerCase().contains("protect") && !productName.toLowerCase().contains("case") && !productName.toLowerCase().contains("sim")
                    && !productName.toLowerCase().contains("skin") && !productName.toLowerCase().contains("bumper") && !productName.toLowerCase().contains("otterbox")
                    && !productName.toLowerCase().contains("uag") && !productName.toLowerCase().contains("rhinoshield") && !productName.toLowerCase().contains("charge")
                    && !productName.toLowerCase().contains("lens") && !productName.contains("???") && !productName.contains("???")
                    && !productName.contains("??????") && !productName.contains("???") && !productName.contains("??????") && !productName.contains("??????") 
                    && !productName.contains("???") && !productName.contains("???") && !productName.contains("??????") 
                    && !productName.contains("??????") && !productName.contains("??????") && !productName.contains("??????"))
                proSeries_valid = true;

            String price = product.select(".trade_price span:first-child").text() + product.select(".trade_price span:last-child").text();
            double secondHandPrice = price.equals("") ? 0 : Double.parseDouble(price.replaceAll("[^0-9]", ""));

            String oriPrice = product.select("td.trade_price_1st span.text-price-unit").text() + product.select("td.trade_price_1st span.text-price-number").text();
            double originalPrice = oriPrice.equals("") ? 0 : Double.parseDouble(oriPrice.replaceAll("[^0-9]", ""));

            String warrantyDesc = product.select("table tbody tr td:first-child:contains(????????????:) + td").text();

            String lastUpdateDate = product.select("table tbody tr td:first-child:contains(????????????:) + td").text();

            String description = product.select(".description").text();
            description = description.substring(0, Math.min(description.length(), 200));
            description = description.replaceAll(regex, "");

            String availability = product.select("table tbody tr td:first-child:contains(????????????:) + td").text();

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
            Document document = Jsoup.connect(MACBOOK_PRO_URL + "&page=" + page).get();
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

