async function listAssignmentSubmissions() {
    const urlParameters = new URLSearchParams(window.location.search);
    const assignmentID = urlParameters.get("assignmentID");
    const courseID = urlParameters.get("courseID");

    const servletURL = `/listAssignmentSubmissions?courseID=${courseID}&assignmentID=${assignmentID}`;

    const response = await fetch(servletURL, {
        method: "GET",
        mode: "no-cors"
    });

    const json = await response.json();

    const table = document.getElementById("submissions-table");
    json.forEach(
        function(studentSubmission) {
            addSubmissionToTable(table, studentSubmission);
        }
    );
}

function addSubmissionToTable(table, studentSubmission) {
    let frame = document.createElement("iframe");
    let content = document.createElement("div");
    let space = document.createElement("p");
    let info = document.createElement("h4");

    info.innerText = `${studentSubmission.name} - ${studentSubmission.email}`;
    frame.src = studentSubmission.link;
    frame.height= "400px";
    frame.width="100%";

    space.appendChild(frame);

    content.appendChild(document.createElement("br"));
    content.appendChild(info);
    content.appendChild(document.createElement("br"));
    content.appendChild(space);
    content.appendChild(document.createElement("br"));

    table.appendChild(content);
}

listAssignmentSubmissions();