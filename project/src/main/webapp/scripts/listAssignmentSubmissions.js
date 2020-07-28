async function listAssignmentSubmissions() {
    let urlParameters = new URLSearchParams(window.location.search);
    let assignmentID = urlParameters.get("assignmentID");
    let courseID = urlParameters.get("courseID");

    let servletURL = `/listAssignmentSubmissions?courseID=${courseID}&assignmentID=${assignmentID}`;

    let response = await fetch(servletURL, {
        method: "GET",
        mode: "no-cors"
    });

    console.log("\n\nBING\tBANG\tBONG\n\n");

}

listAssignmentSubmissions();