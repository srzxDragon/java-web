package sdu.cs.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;


//@WebFilter("*.do")
public class Demo01Filter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Hello A 1");

        // 放行
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("Hello A 2");
    }

    public void destroy() {

    }
}
