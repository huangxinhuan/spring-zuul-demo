package com.example.zuul.demo;

import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.handler.AbstractUrlHandlerMapping;

import javax.servlet.http.HttpServletRequest;

public class ZuulHandlerMapping extends AbstractUrlHandlerMapping {


    private ZuulController zuulController;

    public ZuulHandlerMapping(ZuulController zuulController) {
        this.zuulController = zuulController;
        setOrder(-200);
    }

    /**
     * Look up a handler instance for the given URL path.
     * <p>Supports direct matches, e.g. a registered "/test" matches "/test",
     * and various Ant-style pattern matches, e.g. a registered "/t*" matches
     * both "/test" and "/team". For details, see the AntPathMatcher class.
     * <p>Looks for the most exact pattern, where most exact is defined as
     * the longest path pattern.
     *
     * @param urlPath the URL the bean is mapped to
     * @param request current HTTP request (to expose the path within the mapping to)
     * @return the associated handler instance, or {@code null} if not found
     * @see #exposePathWithinMapping
     * @see AntPathMatcher
     */
    @Override
    protected Object lookupHandler(String urlPath, HttpServletRequest request) throws Exception {
        System.out.println("lookupHandler: " + urlPath);
        registerHandler("/zc/test", zuulController);
        return super.lookupHandler(urlPath, request);
    }
}
