async function getOptions() {
    const assignmentKey = getUrlVars()['assignment-key'];                                                                                                                                                                       
    var myDiv = document.getElementById('grading-options');

    var gradeIndivid = document.createElement('a');
    gradeIndivid.innerHTML = "Grade Individual Submissions<br><br>";
    gradeIndivid.href = "/pages/gradeIndividualSubmissions.html?assignment-key=" + assignmentKey;

    var questionGroups = document.createElement('a');
    questionGroups.innerHTML = "Grade Question Groups<br><br>";
    questionGroups.href = "/pages/questionGroups.html?assignment-key=" + assignmentKey;

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