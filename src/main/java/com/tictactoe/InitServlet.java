package com.tictactoe;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name="initServlet",value="/start")
public class InitServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(true);
        Field field = new Field();
        List<Sign> data = field.getFieldData();
        session.setAttribute("field",field);
        session.setAttribute("data",data);
        session.setAttribute("winner",Sign.EMPTY);
        try {
            getServletContext().getRequestDispatcher("/index.jsp").forward(req,resp);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }
}
