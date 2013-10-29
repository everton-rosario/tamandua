/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.tamandua.ws;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


/**
 * @author Everton Rosario (erosario@gmail.com)
 */
public class UTF8Filter implements Filter {

    public void destroy()
    {
        
    }
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
//        byte[] byteArray = new byte[60000];
//        request.getInputStream().read(byteArray);
//        String s = new String(byteArray);
//        System.out.println(s);
        request.setCharacterEncoding("UTF8");
        response.setCharacterEncoding("UTF8");
        chain.doFilter(request, response);
    }
    public void init(FilterConfig filterConfig) throws ServletException
    {
        
    }
}
