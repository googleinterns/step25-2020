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
        let courseList = coursesJSON["courses"];
        let courseTable = document.getElementById("courses-table");

        courseList.forEach(course => {
            addCourseTableRow(courseTable, course.name, course.id);
        });   
    }
}

function addCourseTableRow(courseTable, courseName, courseID) {
    let content  = document.createElement("a");
    let cell = document.createElement("td");
    let row = document.createElement("tr");
    let text = document.createElement("p");

    content.href = `/pages/course.html?courseID=${courseID}`;
    text.innerHTML = courseName;

    cell.appendChild(text);
    content.appendChild(cell);
    row.appendChild(content);
    courseTable.appendChild(row);
}

getCourses();