package ee4216.pricescraper;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author chunchun
 */
public class PriceAdditionalInfo {
    private String source;
    private String id;
    private double originalPrice;
    
    public String getSource() {
        return source;
    }

    public String getId() {
        return id;
    }
    
    public double getOriginalPrice(){
        return originalPrice;
    }

    public PriceAdditionalInfo(String source, String id, double originalPrice) {
        this.source = source;
        this.id = id;
        this.originalPrice = originalPrice;
    }
    
    
    
    
}
