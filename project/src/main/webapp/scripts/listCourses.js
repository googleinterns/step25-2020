async function getCourses() {

    let servletURL = "/listCourses";

    let response = await fetch(servletURL, {
        method: "GET",
        mode: "no-cors"
    });

    
}

getCourses();