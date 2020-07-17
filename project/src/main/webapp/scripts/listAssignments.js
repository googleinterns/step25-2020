async function getAssignments() {
    let servletURL = "/listAssignments";

    let response = await fetch(servletURL, {
        method: "GET",
        mode: "no-cors"
    });

    let redirect = response.headers.get("redirect");

    if (redirect != null) {
        window.location.replace(redirect);
    } else {

        getCourseDetails();

        let assignmentsJSON = await response.json();
        let assignmentList = assignmentsJSON["courseWork"];
        let assignmentTable = document.getElementById("assignments-table-body");

        assignmentList.forEach(assignment => {
            addAssignmentTableRow(assignmentTable, assignment.title, assignment.id, 3, 100);
        });
    }
}

function getCourseDetails() {
    let courseName = document.getElementById("course-name");
    let courseDescription = document.getElementById("course-description");

    let servletURL = "/courseDetails";

    // let response = await fetch(servletURL, {
    //     method: "GET",
    //     mode: "no-cors"
    // });
}

function addAssignmentTableRow(assignmentTable, assignmentName, assignmentID, numberOfSubmissions, percentageGraded) {
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

    assignmentLink.href = `/pages/assignment.html?assignmentID=${assignmentID}`;

    assignmentLink.appendChild(assignmentNameCell);

    row.appendChild(assignmentLink);
    row.appendChild(submissionsCell);
    row.appendChild(percentageGradedCell);

    assignmentTable.appendChild(row);
}

getAssignments();