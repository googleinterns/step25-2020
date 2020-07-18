async function getAssignmentDetails() {
    let assignmentTitleText = document.createElement("h2");
    let assignmentDescriptionText = document.createElement("h4");
    let assignmentTitle = document.getElementById("assignment-title-container");
    let assignmentDescription = document.getElementById("assignment-description-container");
    
    let servletURL = "/getAssignmentDetails";

    let response = await fetch(servletURL, {
        method: "GET",
        mode: "no-cors"
    });

    let assignmentJSON = await response.json();
    let assignment = assignmentJSON["assignment"];

    assignmentTitleText.innerHTML = assignment.title;
    assignmentDescriptionText.innerHTML = assignment.description;

    assignmentTitle.appendChild(assignmentTitleText);
    assignmentDescription.appendChild(assignmentDescriptionText);
}

getAssignmentDetails();