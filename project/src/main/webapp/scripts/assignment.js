document.addEventListener('DOMContentLoaded', function() {
    var elems = document.querySelectorAll('.fixed-action-btn');
    var instances = M.FloatingActionButton.init(elems, {
      direction: 'left',
      hoverEnabled: false
    });
});

async function getQuestionFunctions() {
    const urlParameters = new URLSearchParams(window.location.search);
    const assignmentID = urlParameters.get("assignmentID");
    const courseID = urlParameters.get("courseID");
    const assignmentKey = urlParameters.get("assignment-key");
    
    const assignmentParameters = `?courseID=${courseID}&assignmentID=${assignmentID}&assignment-key=${assignmentKey}`;

    var myDiv = document.getElementById('question-functions');

    // var editOutline = document.createElement('a');
    var editOutline = document.getElementById('editOutline');
    // editOutline.innerHTML = "Edit Question Outlines<br><br>";
    editOutline.href = `/pages/editOutline.html${assignmentParameters}`;

    // var viewSubmissions = document.createElement('a');
    var viewSubmissions = document.getElementById('viewSubmissions');
    // viewSubmissions.innerHTML = "View Submissions<br><br>";
    viewSubmissions.href = `/pages/viewSubmissions.html${assignmentParameters}`;

    // var gradeSubmissions = document.createElement('a');
    var gradeSubmissions = document.getElementById('gradeSubmissions');
    // gradeSubmissions.innerHTML = "Grade Submissions<br><br>";
    gradeSubmissions.href = `/pages/gradeSubmissions.html${assignmentParameters}`;

    // var reviewGrades = document.createElement('a');
    var reviewGrades = document.getElementById('reviewGrades');
    // reviewGrades.innerHTML = "Review Grades<br><br>";
    reviewGrades.href = `/pages/reviewGrades.html${assignmentParameters}`;

    // myDiv.appendChild(editOutline);
    // myDiv.appendChild(viewSubmissions);
    // myDiv.appendChild(gradeSubmissions);
    // myDiv.appendChild(reviewGrades);
}

getQuestionFunctions();
