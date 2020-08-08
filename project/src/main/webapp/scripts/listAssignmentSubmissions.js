async function listAssignmentSubmissions() {
    const urlParameters = new URLSearchParams(window.location.search);
    const assignmentKey = urlParameters.get("assignmentKey");
    const assignmentID = urlParameters.get("assignmentID");
    const courseID = urlParameters.get("courseID");

    const servletURL = `/listAssignmentSubmissions?courseID=${courseID}&assignmentID=${assignmentID}&assignmentKey=${assignmentKey}`;

    const response = await fetch(servletURL, {
        method: "GET",
        mode: "no-cors"
    });

    let redirect = response.headers.get("redirect");

    if (redirect != null) {
        window.location.replace(redirect);
    } else {
        const json = await response.json();

        const table = document.getElementById("submissions-table");
        json.forEach(
            async function(studentSubmission) {
                const student = await getStudent(courseID, studentSubmission.userID);

                const fullName = student.profile.name.fullName;
                const emailAddress = student.profile.emailAddress;
            
                addSubmissionToTable(table, studentSubmission, fullName, emailAddress);
            }
        );
    }
}

async function getStudent(courseID, studentID) {
    const servletURL = `/getStudent?courseID=${courseID}&studentID=${studentID}`;

    const response = await fetch(servletURL, {
        method: "GET",
        mode: "no-cors"
    });

    return await response.json();
}

function addSubmissionToTable(table, studentSubmission, name, email) {
    let frame = document.createElement("iframe");
    let content = document.createElement("div");
    let space = document.createElement("p");
    let info = document.createElement("h4");

    info.innerText = `${name} - ${email}`;
    frame.src = studentSubmission.driveFilePreviewLink;
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