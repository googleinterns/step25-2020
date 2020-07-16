async function getQuestionFunctions() {
    const assignmentKey = getUrlVars()['assignment-key'];
    var myDiv = document.getElementById('question-functions');

    var editOutline = document.createElement('a');
    editOutline.innerHTML = "Edit Question Outlines<br><br>";
    editOutline.href = "/pages/editOutline.html?assignment-key=" + assignmentKey;

    var viewSubmissions = document.createElement('a');
    viewSubmissions.innerHTML = "View Submissions<br><br>";
    viewSubmissions.href = "/pages/viewSubmissions.html?assignment-key=" + assignmentKey;

    var gradeSubmissions = document.createElement('a');
    gradeSubmissions.innerHTML = "Grade Submissions<br><br>";
    gradeSubmissions.href = "/pages/gradeSubmissions.html?assignment-key=" + assignmentKey;

    var reviewGrades = document.createElement('a');
    reviewGrades.innerHTML = "Review Grades<br><br>";
    reviewGrades.href = "/pages/reviewGrades.html?assignment-key=" + assignmentKey;

    myDiv.appendChild(editOutline);
    myDiv.appendChild(viewSubmissions);
    myDiv.appendChild(gradeSubmissions);
    myDiv.appendChild(reviewGrades);
}

// https://html-online.com/articles/get-url-parameters-javascript/
function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
        vars[key] = value;
    });
    return vars;
}