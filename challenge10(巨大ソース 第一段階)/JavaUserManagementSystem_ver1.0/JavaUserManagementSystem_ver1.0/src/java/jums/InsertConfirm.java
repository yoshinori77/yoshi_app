package jums;

import java.io.IOException;
import static java.lang.System.out;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * insertconfirm.jspと対応するサーブレット
 * フォーム入力された情報はここでセッションに格納し、以降持ちまわることになる
 * 直接アクセスした場合はerror.jspに振り分け
 * @author hayashi-s
 */
public class InsertConfirm extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try{
            HttpSession session = request.getSession();
            request.setCharacterEncoding("UTF-8");//セッションに格納する文字コードをUTF-8に変更
            String accesschk = request.getParameter("ac");
            if(accesschk ==null || (Integer)session.getAttribute("ac")!=Integer.parseInt(accesschk)){
                throw new Exception("不正なアクセスです");
            }
            
            //フォームからの入力を取得
            String name = request.getParameter("name");
            String year = request.getParameter("year");
            String month = request.getParameter("month");
            String day = request.getParameter("day");
            String type = request.getParameter("type");
            String tell = request.getParameter("tell");
            String comment = request.getParameter("comment");
            
            // UserDataBeansにデータを格納
            // セッターを呼ぶ
            UserDataBeans udb = new UserDataBeans();
            udb.setName(name);
            udb.setYear(year);
            udb.setMonth(month);
            udb.setDay(day);
            // 職業の値が空の場合、文字列で1とする
            // 空文字をintに変換するとnullエラーになるため
            if (type.equals("")) {
                type = "1";
                udb.setType(Integer.parseInt(type));
            } else {
                udb.setType(Integer.parseInt(type));
            }
            udb.setTell(tell);
            udb.setComment(comment);

//             setBirthday用
//             パース 
            if (year.equals("") || month.equals("") || day.equals("")) {
            } else {
                int p_month = Integer.parseInt(month);
                int p_day = Integer.parseInt(day);
                // 月と日が一桁の場合、先頭に0を挿入
                //　e.g. 2016年1月1日の場合 201611 → 20160101
                if (p_month <10) {
                    StringBuilder sb = new StringBuilder(month);
                    month = sb.insert(0, "0").toString();
                }
                if (p_day < 10) {
                    StringBuilder sb = new StringBuilder(day);
                    day = sb.insert(0, "0").toString();
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date p_birthday = sdf.parse(year + "-" + month + "-" + day);
                udb.setBirthday(p_birthday);
            }
            
            
            //セッションに格納
            session.setAttribute("udb", udb);
            System.out.println("Session updated!!");
            
            request.getRequestDispatcher("/insertconfirm.jsp").forward(request, response);
        }catch(Exception e){
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
            
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
