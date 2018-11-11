package puk.team.maven.course;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<String> list = new ArrayList<>();
        list.add("this");
        list.add("is");
        list.add("a");
        list.add("test");
        String output = new Gson().toJson(list);

        resp.getWriter().println(output);
    }
}
