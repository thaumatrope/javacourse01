/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibb.taglib;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 *
 * @author schulung
 */
public class SimpleDate extends BodyTagSupport{
    
    private String attribut;
    
    public int doStartTag(){
        try {
            JspWriter out =pageContext.getOut();
            if(attribut !=null){
            out.print("<h1>"+new Date()+"</h1>");
            }
            else{
                out.print("Kein Attributwert");
            }
           
        } catch (IOException ex) {
            Logger.getLogger(SimpleDate.class.getName()).log(Level.SEVERE, null, ex);
        }
         return SKIP_BODY;
    }

    public String getAttribut() {
        return attribut;
    }

    public void setAttribut(String attribut) {
        this.attribut = attribut;
    }
    
    @Override
    public void release(){
        attribut=null;
    }
}
