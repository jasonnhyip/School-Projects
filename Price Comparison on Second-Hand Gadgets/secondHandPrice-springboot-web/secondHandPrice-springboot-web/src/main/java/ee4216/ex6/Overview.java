/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ee4216.ex6;

import java.util.List;

/**
 *
 * @author Jason
 */
public class Overview {
    double Min_price;
    double Max_price;
    double Avg_price;
    double Last_transaction_price;
    List<Product> Products;
    
    public Overview(double Min_price, double Max_price, double Avg_price, double Last_transaction_price, List<Product> Products) {
        this.Min_price = Min_price;
        this.Max_price = Max_price;
        this.Avg_price = Avg_price;
        this.Last_transaction_price = Last_transaction_price;
        this.Products = Products;
    }
    
    public double getMin_prince() {
        return Min_price;
    }
    
    public double getMax_price() {
        return Max_price;
    }
    
    public double getAvg_price() {
        return Avg_price;
    }
    
    public double getLast_transaction_price() {
        return Last_transaction_price;
    }
    
    public List<Product> getProducts() {
        return Products;
    }
}
