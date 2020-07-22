async function listAssignmentSubmissions() {

    let servletURL = "/listAssignmentSubmissions";

    let response = await fetch(servletURL, {
        method: "GET",
        mode: "no-cors"
    });

    console.log("\n\nBING\tBANG\tBONG\n\n");

}

listAssignmentSubmissions();