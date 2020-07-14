package com.google.autograder.servlets;

@WebServlet("/question")
public final class AssignmentServlet extends HttpServlet {

  private Database d = new Database();

  //deleted soon, will be adding questions thru outlines page later. for testing purposes. 
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the input from form
    String name = request.getParameter("name");
    String pointString = request.getParameter("points");
    String type = request.getParameter("type");
    int assignmentKey = request.getParameter("assignment-key");
    if (name != null && points != null && type != null && assignmentKey != null) {
        int points = Integer.parseInt(pointString);
        d.addQuestion(name, type, points, assignmentKey);
        response.sendRedirect("/pages/assignment.html");
    }
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String key = request.getParameter("assignment-key");
    if (key != null) {
        String json = d.getAllQuestionJSON(key);
        response.setContentType("application/json;");
        response.getWriter().println(json); }
  }
}