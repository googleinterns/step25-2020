// initialize horizonal fixed action button
document.addEventListener('DOMContentLoaded', function() {
    var elems = document.querySelectorAll('.fixed-action-btn');
    var instances = M.FloatingActionButton.init(elems, {
      direction: 'left',
      hoverEnabled: false
    });
});

getOptions();
async function getOptions() {
    const assignmentKey = getUrlVars()['assignment-key'];                                                                                                                                                                       
    var myDiv = document.getElementById('grading-options');

    var gradeIndivid = document.getElementById("gradeIndividualSubmissions");
    gradeIndivid.href = "/pages/gradeIndividualSubmissions.html?assignment-key=" + assignmentKey;

    var questionGroups = document.getElementById("questionGroups");
    questionGroups.href = "/pages/questionPage.html?assignment-key=" + assignmentKey;
}

// https://html-online.com/articles/get-url-parameters-javascript/
function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
        vars[key] = value;
    });
    return vars;
}