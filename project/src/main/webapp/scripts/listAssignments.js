async function getAssignments() {
    let courseID = new URLSearchParams(window.location.search).get("courseID");
    let servletURL = `/listAssignments?courseID=${courseID}`;

    let response = await fetch(servletURL, {
        method: "GET",
        mode: "no-cors"
    });

    let redirect = response.headers.get("redirect");

    if (redirect != null) {
        window.location.replace(redirect);
    } else {
        let assignmentsJSON = await response.json();
        let assignmentList = assignmentsJSON["courseWork"];
        let assignmentTable = document.getElementById("assignments-table-body");

        let url = window.location.href;
        let courseID = url.substring(url.indexOf("?courseID=") + 10);

        assignmentList.forEach(assignment => {
            addAssignmentTableRow(courseID, assignmentTable, assignment.title, assignment.id, 3, 100);
        });
    }
}

function getCourseID() {
    let url = window.location.href;
    return url.substring(url.indexOf("?courseID=") + 10);
}

function addAssignmentTableRow(courseID, assignmentTable, assignmentName, assignmentID, numberOfSubmissions, percentageGraded) {
    let assignmentLink = document.createElement("a");
    let row = document.createElement("tr");

    let assignmentNameCell = document.createElement("td");
    let submissionsCell = document.createElement("td");
    let percentageGradedCell = document.createElement("td");

    let assignmentNameText = document.createElement("p");
    let submissionsText = document.createElement("p");
    let percentageGradedText = document.createElement("p");

    assignmentNameText.innerHTML = assignmentName;
    submissionsText.innerHTML = numberOfSubmissions;
    percentageGradedText.innerHTML = percentageGraded;

    assignmentNameCell.appendChild(assignmentNameText);
    submissionsCell.appendChild(submissionsText);
    percentageGradedCell.appendChild(percentageGradedText);

    assignmentLink.href = `/pages/assignment.html?assignmentID=${assignmentID}&courseID=${courseID}`;

    assignmentLink.appendChild(assignmentNameCell);

    row.appendChild(assignmentLink);
    row.appendChild(submissionsCell);
    row.appendChild(percentageGradedCell);

    assignmentTable.appendChild(row);
}

getAssignments();