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
        function(link) {
            addSubmissionToTable(table, link);
        }
    );
}

function addSubmissionToTable(table, link) {
    let frame = document.createElement("iframe");
    let content = document.createElement("div");
    let space = document.createElement("p");

    frame.src = link;

    space.appendChild(frame);

    content.appendChild(document.createElement("br"));
    content.appendChild(space);
    content.appendChild(document.createElement("br"));

    table.appendChild(content);
}

listAssignmentSubmissions();