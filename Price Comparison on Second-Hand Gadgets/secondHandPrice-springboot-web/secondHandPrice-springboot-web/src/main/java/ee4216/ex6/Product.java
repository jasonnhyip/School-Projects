/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ee4216.ex6;

import java.sql.Timestamp;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Table;

/**
 *
 * @author vanting
 */

//@Entity
//@Table(name = "products")
public class Product{
    //@Column(name = "source")
    private String source;
    
    //@Id
    private String id;
    
    //@Column(name = "productName")
    private String productName;
    
    //@Column(name = "secondHandPrice")
    private double secondHandPrice;
    
    //@Column(name = "description")
    private String description;
    
    //@Column(name = "warrantyDesc")
    private String warrantyDesc;
    
    //@Column(name = "lastUpdateDate")
    private Timestamp lastUpdateDate;
    
    //@Column(name = "imageUrl")
    private String imageUrl;
    
    //@Column(name = "availability")
    private String availability;
    
    //@Column(name = "productUrl")
    private String productUrl;
    
    public Product(String source, String id, String productName, double secondHandPrice, String description, String warrantyDesc, Timestamp lastUpdateDate, String imageUrl, String availability, String productUrl) {
        this.source = source;
        this.id = id;
        this.productName = productName;
        this.secondHandPrice = secondHandPrice;
        this.description = description;
        this.warrantyDesc = warrantyDesc;
        this.lastUpdateDate = lastUpdateDate;
        this.imageUrl = imageUrl;
        this.availability = availability;
        this.productUrl = productUrl;
    }
    
    public String getSource() {
        return source;
    }
    
    public String getID() {
        return id;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public double getSecondHandPrice() {
        return secondHandPrice;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getWarrantyDesc() {
        return warrantyDesc;
    }
    
    public Timestamp getLastUpdateDate() {
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
    
    @Override
    public String toString() {
        return String.format(productName +", " + secondHandPrice);
    }
}