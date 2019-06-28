package com.example.zuul.demo;

import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.ZuulServlet;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ServletWrappingController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ZuulController extends ServletWrappingController {

    public ZuulController() {
        setServletClass(ZuulServlet.class);
        setServletName("zuul");
        setSupportedMethods((String[]) null); // Allow all
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            // We don't care about the other features of the base class, just want to
            // handle the request
            System.out.println("-----------------------------");
            return super.handleRequestInternal(request, response);
        }
        finally {
            // @see com.netflix.zuul.context.ContextLifecycleFilter.doFilter
            RequestContext.getCurrentContext().unset();
        }
    }

}
