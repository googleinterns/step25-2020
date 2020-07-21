async function getQuestionFunctions() {
    const assignmentDetails = getAssignmentDetails();
    const assignmentID = assignmentDetails[0];
    const courseID = assignmentDetails[1];
    
    const assignmentParameters = `assignmentID=${assignmentID}&courseID=${courseID}`

    var myDiv = document.getElementById('question-functions');

    var editOutline = document.createElement('a');
    editOutline.innerHTML = "Edit Question Outlines<br><br>";
    editOutline.href = `/pages/editOutline.html?${assignmentParameters}`;

    var viewSubmissions = document.createElement('a');
    viewSubmissions.innerHTML = "View Submissions<br><br>";
    viewSubmissions.href = `/pages/viewSubmissions.html?${assignmentParameters}`;

    var gradeSubmissions = document.createElement('a');
    gradeSubmissions.innerHTML = "Grade Submissions<br><br>";
    gradeSubmissions.href = `/pages/gradeSubmissions.html?${assignmentParameters}`;

    var reviewGrades = document.createElement('a');
    reviewGrades.innerHTML = "Review Grades<br><br>";
    reviewGrades.href = `/pages/reviewGrades.html?${assignmentParameters}`;

    myDiv.appendChild(editOutline);
    myDiv.appendChild(viewSubmissions);
    myDiv.appendChild(gradeSubmissions);
    myDiv.appendChild(reviewGrades);
}

function getAssignmentDetails() {
    let url = window.location.href;
    let assignmentIDIndex = url.indexOf("?assignmentID=") + 14;
    let courseIDIndex = url.indexOf("&courseID=");

    const assignmentID = url.substring(assignmentIDIndex, courseIDIndex);
    const courseID = url.substring(courseIDIndex + 10);

    return [assignmentID, courseID];
}

getQuestionFunctions();