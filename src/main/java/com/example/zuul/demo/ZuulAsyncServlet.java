package com.example.zuul.demo;

import com.netflix.zuul.http.ZuulServlet;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

public class ZuulAsyncServlet extends ZuulServlet {

    private ExecutorService executor;

    public ZuulAsyncServlet(ExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        AsyncContext asyncContext = servletRequest.startAsync();
        executor.execute(() -> {
            try {
                super.service(asyncContext.getRequest(), asyncContext.getResponse());
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            asyncContext.complete();
        });
        System.out.println("-------------------------------");
    }
}
