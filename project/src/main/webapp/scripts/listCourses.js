async function getCourses() {

    let servletURL = "/listCourses";

    let response = await fetch(servletURL, {
        method: "GET",
        mode: "no-cors"
    });

    let redirect = response.headers.get("redirect");

    if (redirect != null) {
        window.location.replace(redirect);
    } else {

        let coursesJSON = await response.json();

        console.log(coursesJSON);
        
    }


}

getCourses();