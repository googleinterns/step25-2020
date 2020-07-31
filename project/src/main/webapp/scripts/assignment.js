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

    var editOutline = document.getElementById('editOutline');
    editOutline.href = `/pages/editOutline.html${assignmentParameters}`;

    var viewSubmissions = document.getElementById('viewSubmissions');
    viewSubmissions.href = `/pages/viewSubmissions.html${assignmentParameters}`;

    var gradeSubmissions = document.getElementById('gradeSubmissions');
    gradeSubmissions.href = `/pages/gradeSubmissions.html${assignmentParameters}`;

    var reviewGrades = document.getElementById('reviewGrades');
    reviewGrades.href = `/pages/reviewGrades.html${assignmentParameters}`;

}

getQuestionFunctions();
