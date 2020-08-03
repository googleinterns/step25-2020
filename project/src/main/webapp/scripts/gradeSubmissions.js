// initialize horizonal fixed action button
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


async function getOptions() {
    const assignmentKey = getUrlVars()['assignment-key'];                                                                                                                                                                       
    var myDiv = document.getElementById('grading-options');

    var gradeIndivid = document.createElement('a');
    gradeIndivid.innerHTML = "Grade Individual Submissions<br><br>";
    gradeIndivid.href = "/pages/gradeIndividualSubmissions.html?assignment-key=" + assignmentKey;

    var questionGroups = document.createElement('a');
    questionGroups.innerHTML = "Grade Question Groups<br><br>";
    questionGroups.href = "/pages/questionPage.html?assignment-key=" + assignmentKey;

    var editOutline = document.createElement('a');
    editOutline.innerHTML = "Review Question Outlines<br><br>";
    editOutline.href = "/pages/editOutline.html?assignment-key=" + assignmentKey;

    myDiv.appendChild(gradeIndivid);
    myDiv.appendChild(questionGroups);
    myDiv.appendChild(editOutline);
}

// https://html-online.com/articles/get-url-parameters-javascript/
function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
        vars[key] = value;
    });
    return vars;
}