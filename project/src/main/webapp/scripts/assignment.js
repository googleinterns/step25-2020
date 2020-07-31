async function getQuestionFunctions() {
    const urlParameters = new URLSearchParams(window.location.search);
    const assignmentID = urlParameters.get("assignmentID");
    const courseID = urlParameters.get("courseID");
    const assignmentKey = urlParameters.get("assignmentKey");
    
    const assignmentParameters = `?courseID=${courseID}&assignmentID=${assignmentID}&assignmentKey=${assignmentKey}`;

    var myDiv = document.getElementById('question-functions');

    var editOutline = document.createElement('a');
    editOutline.innerHTML = "Edit Question Outlines<br><br>";
    editOutline.href = `/pages/editOutline.html${assignmentParameters}`;

    var viewSubmissions = document.createElement('a');
    viewSubmissions.innerHTML = "View Submissions<br><br>";
    viewSubmissions.href = `/pages/viewSubmissions.html${assignmentParameters}`;

    var gradeSubmissions = document.createElement('a');
    gradeSubmissions.innerHTML = "Grade Submissions<br><br>";
    gradeSubmissions.href = `/pages/gradeSubmissions.html${assignmentParameters}`;

    var reviewGrades = document.createElement('a');
    reviewGrades.innerHTML = "Review Grades<br><br>";
    reviewGrades.href = `/pages/reviewGrades.html${assignmentParameters}`;

    myDiv.appendChild(editOutline);
    myDiv.appendChild(viewSubmissions);
    myDiv.appendChild(gradeSubmissions);
    myDiv.appendChild(reviewGrades);
}

getQuestionFunctions();