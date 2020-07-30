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

    console.log(json);

}

listAssignmentSubmissions();