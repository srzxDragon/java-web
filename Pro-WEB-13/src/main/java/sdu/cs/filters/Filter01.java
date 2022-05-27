package sdu.cs.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;


@WebFilter("/demo03.do")
public class Filter01 implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Hello Filter01 A");

        // 放行
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("Hello Filter01 B");
    }

    public void destroy() {

    }
}
