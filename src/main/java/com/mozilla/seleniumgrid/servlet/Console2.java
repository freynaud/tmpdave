package com.mozilla.seleniumgrid.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openqa.grid.internal.Registry;
import org.openqa.grid.web.servlet.RegistryBasedServlet;

import com.google.common.io.ByteStreams;

public class Console2 extends RegistryBasedServlet {

  public Console2(){
    super(null);
  }
    public Console2(Registry registry) {
        super(registry);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    protected void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(200);

        StringBuilder builder = new StringBuilder();

        builder.append("<html>");
        builder.append("<head>");
        builder.append("<title>Selenium Grid Console2</title>");
        builder.append("</body>");
        builder.append("<h1>Hello World</h1>");
        builder.append("</body>");
        builder.append("</html>");

        InputStream in = new ByteArrayInputStream(builder.toString().getBytes("UTF-8"));
        try {
            ByteStreams.copy(in, response.getOutputStream());
        } finally {
            in.close();
            response.getOutputStream().close();
        }
    }

}
