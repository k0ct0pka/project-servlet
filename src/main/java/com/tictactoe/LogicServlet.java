package com.tictactoe;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static javax.swing.UIManager.put;

@WebServlet(name = "logic",value = "/logic")
public class LogicServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        int index = getSelectedIndex(req);
        Field field = extractField(session);
        Map<Integer, Sign> field1 = field.getField();
        if(checkWin(session,resp,field)){
            return;
        };
        if (field1.get(index).equals(Sign.EMPTY)) {
            field1.put(index,Sign.CROSS);
            if(checkWin(session,resp,field)){
                return;
            };
            int emptyFieldIndex = field.getEmptyFieldIndex();
            if(emptyFieldIndex >= 0){
                field1.put(emptyFieldIndex,Sign.NOUGHT);
                if(checkWin(session,resp,field)){
                    return;
                };
            } else{
                session.setAttribute("draw",true);
                List<Sign> data = field.getFieldData();
                session.setAttribute("data",data);
                resp.sendRedirect("/index.jsp");
                return;
            }
            List<Sign> data = field.getFieldData();
            session.setAttribute("data", data);
            session.setAttribute("field", field);

        }
        resp.sendRedirect("/index.jsp");
    }

    private boolean checkWin(HttpSession session, HttpServletResponse resp,Field field) throws IOException {
        Sign winner = field.checkWin();
        if(winner == Sign.CROSS || winner == Sign.NOUGHT){
            session.setAttribute("winner", winner);
            List<Sign> data = field.getFieldData();
            session.setAttribute("data", data);
            resp.sendRedirect("/index.jsp");
            return true;
        }
        return false;
    }

    private Field extractField(HttpSession currentSession) {
        Object fieldAttribute = currentSession.getAttribute("field");
        if (Field.class != fieldAttribute.getClass()) {
            currentSession.invalidate();
            throw new RuntimeException("Session is broken, try one more time");
        }
        return (Field) fieldAttribute;
    }


    private int getSelectedIndex(HttpServletRequest request) {
        String click = request.getParameter("click");
        boolean isNumeric = click.chars().allMatch(Character::isDigit);
        return isNumeric ? Integer.parseInt(click) : 0;
    }
}
