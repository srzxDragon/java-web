package sdu.cs.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;


@WebFilter("/demo03.do")
public class Filter03 implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Hello Filter03 A");

        // 放行
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("Hello Filter03 B");
    }

    public void destroy() {

    }
}
