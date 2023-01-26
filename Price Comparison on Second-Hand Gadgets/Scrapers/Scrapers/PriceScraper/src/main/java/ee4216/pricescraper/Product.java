/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ee4216.pricescraper;

import java.io.UnsupportedEncodingException;

/**
 *
 * @author chunchun
 */
public class Product {
        
    private String source;
    private String id;   
    private String productName;
    private double secondHandPrice;
    private double originalPrice;
    private String description;
    private String warrantyDesc;
    private String lastUpdateDate;
    private String imageUrl;
    private String availability;
    private String productUrl;

    public String getSource() {
        return source;
    }

    public String getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public double getSecondHandPrice() {
        return secondHandPrice;
    }
    
    public double getOriginalPrice() {
        return originalPrice;
    }
    
    public String getDescription() {
        return description;
    }

    public String getWarrantyDesc() {
        return warrantyDesc;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getAvailability() {
        return availability;
    }

    public String getProductUrl() {
        return productUrl;
    }
    
    

    public Product(String source, String id, String productName, double secondHandPrice, double originalPrice, String description, String warrantyDesc, String lastUpdateDate, String imageUrl, String availability, String productUrl) throws UnsupportedEncodingException {
        this.source = source;
        this.id = id;
        this.productName = productName;
        this.secondHandPrice = secondHandPrice;
        this.originalPrice = originalPrice;        
        this.description = description;
        this.warrantyDesc = warrantyDesc;
        this.lastUpdateDate = lastUpdateDate;
        this.imageUrl = imageUrl;
        this.availability = availability;
        this.productUrl = productUrl;
    }
    
    public void insertIntoDatabase() {
        
    }
    
}
