async function getCourseDetails() {
    let courseNameText = document.createElement("h2");
    let courseDescriptionText = document.createElement("h4");
    let courseName = document.getElementById("course-name-container");
    let courseDescription = document.getElementById("course-description-container");
    
    let servletURL = "/getCourseDetails";

    let response = await fetch(servletURL, {
        method: "GET",
        mode: "no-cors"
    });

    let courseJSON = await response.json();
    let course = courseJSON["course"];

    courseNameText.innerHTML = course.name;
    courseDescriptionText.innerHTML = course.description;

    courseName.appendChild(courseNameText);
    courseDescription.appendChild(courseDescriptionText);
}

getCourseDetails();